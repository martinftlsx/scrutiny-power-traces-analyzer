package muni.scrutiny.module.configurations.input.reference;

import com.google.gson.annotations.SerializedName;
import muni.scrutiny.module.pipelines.base.CustomPipelineParameters;

import java.util.List;

public class CreateReferenceProfileOperation {
    @SerializedName("operation_code")
    public String operationCode;

    @SerializedName("custom_parameters")
    public CustomPipelineParameters customParameters;

    @SerializedName("file_paths")
    public List<String> filePaths;

    public boolean arePathsSpecified() {
        return filePaths == null || filePaths.size() == 0;
    }
}