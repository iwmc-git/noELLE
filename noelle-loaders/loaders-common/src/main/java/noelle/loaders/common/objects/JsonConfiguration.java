package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;

public class JsonConfiguration {

    @SerializedName("debug-downloads")
    protected boolean debug;

    @SerializedName("check-file-hash")
    protected boolean checkFileHash;

    @SerializedName("use-remapper")
    protected boolean useRemapper;

    public boolean isDebug() {
        return debug;
    }

    public boolean isCheckFileHash() {
        return checkFileHash;
    }

    public boolean useRemapper() {
        return useRemapper;
    }
}
