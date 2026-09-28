    // Port of Node/Buffer/Constants.js. The JVM has no pool, so the constants
    // are the practical maximum sizes of this backend.
    public static Object inspectMaxBytes = (java.util.function.Supplier<Object>) () -> 50;
    public static Object maxLength = Integer.MAX_VALUE;
    public static Object maxStringLength = Integer.MAX_VALUE;
