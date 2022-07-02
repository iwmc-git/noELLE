package noelle.encryptor.utils;

import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public final class ArgonEncoderUtil {
    private static final Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();
    private static final Base64.Decoder b64decoder = Base64.getDecoder();

    public static @NotNull String encode(byte[] hash, @NotNull Argon2Parameters parameters) throws IllegalArgumentException {
        var stringBuilder = new StringBuilder();
        switch (parameters.getType()) {
            case Argon2Parameters.ARGON2_d -> stringBuilder.append("$argon2d");
            case Argon2Parameters.ARGON2_i -> stringBuilder.append("$argon2i");
            case Argon2Parameters.ARGON2_id -> stringBuilder.append("$argon2id");
            default -> throw new IllegalArgumentException("Invalid algorithm type: " + parameters.getType());
        }

        stringBuilder.append("$v=").append(parameters.getVersion())
                .append("$m=").append(parameters.getMemory())
                .append(",t=").append(parameters.getIterations())
                .append(",p=").append(parameters.getLanes());

        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(b64encoder.encodeToString(parameters.getSalt()));
        }

        stringBuilder.append("$").append(b64encoder.encodeToString(hash));
        return stringBuilder.toString();
    }

    @Contract("_ -> new")
    public static @NotNull Argon2Hash decode(@NotNull String encodedHash) throws IllegalArgumentException {
        var parts = encodedHash.split("\\$");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid encoded Argon2-hash");
        }

        int currentPart = 1;
        var paramsBuilder = switch (parts[currentPart++]) {
            case "argon2d" -> new Argon2Parameters.Builder(Argon2Parameters.ARGON2_d);
            case "argon2i" -> new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i);
            case "argon2id" -> new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id);
            default -> throw new IllegalArgumentException("Invalid algorithm type: " + parts[0]);
        };

        if (parts[currentPart].startsWith("v=")) {
            paramsBuilder.withVersion(Integer.parseInt(parts[currentPart].substring(2)));
            currentPart++;
        }

        var performanceParams = parts[currentPart++].split(",");
        if (performanceParams.length != 3) {
            throw new IllegalArgumentException("Amount of performance parameters invalid");
        }

        if (!performanceParams[0].startsWith("m=")) {
            throw new IllegalArgumentException("Invalid memory parameter");
        }

        paramsBuilder.withMemoryAsKB(Integer.parseInt(performanceParams[0].substring(2)));
        if (!performanceParams[1].startsWith("t=")) {
            throw new IllegalArgumentException("Invalid iterations parameter");
        }

        paramsBuilder.withIterations(Integer.parseInt(performanceParams[1].substring(2)));
        if (!performanceParams[2].startsWith("p=")) {
            throw new IllegalArgumentException("Invalid parallelity parameter");
        }

        paramsBuilder.withParallelism(Integer.parseInt(performanceParams[2].substring(2)));
        paramsBuilder.withSalt(b64decoder.decode(parts[currentPart++]));

        return new Argon2Hash(b64decoder.decode(parts[currentPart]), paramsBuilder.build());
    }

    public static class Argon2Hash {
        private byte[] hash;
        private Argon2Parameters parameters;

        Argon2Hash(byte[] hash, Argon2Parameters parameters) {
            this.hash = Arrays.clone(hash);
            this.parameters = parameters;
        }

        public byte[] getHash() {
            return Arrays.clone(this.hash);
        }

        public void setHash(byte[] hash) {
            this.hash = Arrays.clone(hash);
        }

        public Argon2Parameters getParameters() {
            return this.parameters;
        }

        public void setParameters(Argon2Parameters parameters) {
            this.parameters = parameters;
        }
    }
}
