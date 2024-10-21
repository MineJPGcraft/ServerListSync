package top.alazeprt.sls.util;

public enum ServerOrder {
    DEFAULT,
    REVERSE,
    RANDOM,
    ALPHABETICAL;

    public static ServerOrder parse(String order) {
        return switch (order.toLowerCase()) {
            case "reverse" -> REVERSE;
            case "random" -> RANDOM;
            case "alphabetical" -> ALPHABETICAL;
            default -> DEFAULT;
        };
    }
}
