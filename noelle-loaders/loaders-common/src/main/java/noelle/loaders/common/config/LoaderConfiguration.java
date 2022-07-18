package noelle.loaders.common.config;

import com.google.gson.annotations.SerializedName;

public class LoaderConfiguration {

    @SerializedName("debug-downloads")
    protected boolean debug;

    @SerializedName("check-file-hash")
    protected boolean checkFileHash;

    @SerializedName("use-remapper")
    protected boolean useRemapper;

    public boolean debug() {
        return debug;
    }

    public boolean checkFileHash() {
        return checkFileHash;
    }

    public boolean useRemapper() {
        return useRemapper;
    }
}
