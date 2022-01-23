package muni.cotemplate.module;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorrelationComputer implements Runnable {
    private final double[] voltage;
    private final HashMap<Character, double[]> correlations;
    private final Map.Entry<Character, List<Pair<Integer, Integer>>> characterIntervals;
    private final int characterCount;
    private final int segmentWidth;
    private final int windowIndexFrom;
    private final int windowIndexTo;

    public CorrelationComputer(
            double[] voltage,
            HashMap<Character, double[]> correlations,
            Map.Entry<Character, List<Pair<Integer, Integer>>> characterIntervals,
            int characterCount,
            int segmentWidth,
            int windowIndexFrom,
            int windowIndexTo) {
        this.voltage = voltage;
        this.correlations = correlations;
        this.characterIntervals = characterIntervals;
        this.characterCount = characterCount;
        this.segmentWidth = segmentWidth;
        this.windowIndexFrom = windowIndexFrom;
        this.windowIndexTo = windowIndexTo;
    }

    @Override
    public void run() {
        for (int windowIndex = windowIndexFrom; windowIndex < windowIndexTo; windowIndex++) {
            computeCorrelationForCharacterAndWindowIndex(
                    voltage,
                    correlations,
                    characterIntervals,
                    characterCount,
                    segmentWidth,
                    windowIndex);
        }
    }

    private void computeCorrelationForCharacterAndWindowIndex(
            double[] voltage,
            HashMap<Character, double[]> correlations,
            Map.Entry<Character, List<Pair<Integer, Integer>>> characterIntervals,
            int characterCount,
            int segmentWidth,
            int windowIndex) {
        double[] averageSegment = computeAverageSegment(
                voltage,
                characterIntervals,
                characterCount,
                segmentWidth,
                windowIndex);

        double averageCorrelation = computeAverageCorrelation(
                voltage,
                characterIntervals,
                characterCount,
                segmentWidth,
                windowIndex,
                averageSegment);

        correlations.get(characterIntervals.getKey())[windowIndex] = averageCorrelation;
    }

    private double computeAverageCorrelation(double[] voltage, Map.Entry<Character, List<Pair<Integer, Integer>>> characterIntervals, int characterCount, int segmentWidth, int windowIndex, double[] averageSegment) {
        double averageCorrelation = 0;
        for (Pair<Integer, Integer> interval : characterIntervals.getValue()) {
            double segmentCorrelation = correlationCoefficientStable(voltage, averageSegment, windowIndex + interval.getKey(), windowIndex + interval.getValue(), segmentWidth);
            averageCorrelation += segmentCorrelation/ characterCount;
        }
        return averageCorrelation;
    }

    private double[] computeAverageSegment(double[] voltage, Map.Entry<Character, List<Pair<Integer, Integer>>> characterIntervals, int characterCount, int segmentWidth, int windowIndex) {
        double[] averageSegment = new double[segmentWidth];
        for (Pair<Integer, Integer> interval : characterIntervals.getValue()) {
            int segmentIndex = 0;
            for (int traceIndex = interval.getKey(); traceIndex < interval.getValue(); traceIndex++) {
                averageSegment[segmentIndex] += voltage[windowIndex + traceIndex]/ characterCount;
                segmentIndex++;
            }
        }
        return averageSegment;
    }

    private double correlationCoefficientStable(double[] voltage, double[] averageSegment, int intervalFrom, int intervalTo, int n) {
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double squareSumX = 0;
        double squareSumY = 0;
        int segmentIndex = 0;
        for (int intervalIndex = intervalFrom; intervalIndex < intervalTo; intervalIndex++) {
            sumX = sumX + voltage[intervalIndex];
            sumY = sumY + averageSegment[segmentIndex];
            sumXY = sumXY + voltage[intervalIndex] * averageSegment[segmentIndex];
            squareSumX = squareSumX + voltage[intervalIndex] * voltage[intervalIndex];
            squareSumY = squareSumY + averageSegment[segmentIndex] * averageSegment[segmentIndex];
            segmentIndex++;
        }

        double corr = (n * sumXY - sumX * sumY) / Math.sqrt(((n * squareSumX - sumX * sumX)*(n * squareSumY - sumY * sumY))+0.00001);
        return corr;
    }
}
