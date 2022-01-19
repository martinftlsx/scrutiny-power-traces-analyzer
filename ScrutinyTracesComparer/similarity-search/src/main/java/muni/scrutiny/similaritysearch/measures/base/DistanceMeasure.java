package muni.scrutiny.similaritysearch.measures.base;

/**
 * Interface defines how distance computation function should look like.
 * Analogy with apache.commons.math3 interface with the difference that these algorithms support in place computation.
 * 
 * @author Martin
 */
public interface DistanceMeasure {
    public double compute(double[] smallerVector, double[] biggerVector, int firstIndexOfBiggerVector);
    public double compute(double[] smallerVector, double[] biggerVector, int firstIndexOfSmallerVector, int firstIndexOfBiggerVector, int takeN);
}
