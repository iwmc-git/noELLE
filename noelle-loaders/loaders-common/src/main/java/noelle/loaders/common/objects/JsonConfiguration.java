package noelle.loaders.common.objects;

import com.google.gson.annotations.SerializedName;

public class JsonConfiguration {

    @SerializedName("debug-downloads")
    protected boolean debug;

    @SerializedName("check-file-hash")
    protected boolean checkFileHash;

    public boolean isDebug() {
        return debug;
    }

    public boolean isCheckFileHash() {
        return checkFileHash;
    }
}