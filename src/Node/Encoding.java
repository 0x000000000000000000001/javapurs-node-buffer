    // Port of Node/Encoding.js over java.nio: byte length per Node encoding.
    private static int __encodingByteLength(String value, String encoding) {
        String name = encoding == null ? "utf8" : encoding.toLowerCase(java.util.Locale.ROOT);
        switch (name) {
            case "utf8":
            case "utf-8":
                return value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
            case "utf16le":
            case "utf-16le":
            case "ucs2":
            case "ucs-2":
                return value.getBytes(java.nio.charset.StandardCharsets.UTF_16LE).length;
            case "ascii":
            case "latin1":
            case "binary":
                return value.length();
            case "base64":
            case "base64url": {
                String cleaned = value.replaceAll("[^A-Za-z0-9+/=_-]", "");
                int padding = 0;
                for (int index = cleaned.length() - 1; index >= 0 && cleaned.charAt(index) == '='; index--) padding++;
                return Math.max(0, (cleaned.length() * 3) / 4 - padding);
            }
            case "hex":
                return value.length() / 2;
            default:
                return value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
        }
    }

    public static Object byteLengthImpl = (java.util.function.Function<Object, Object>) (value) ->
        (java.util.function.Function<Object, Object>) (encoding) ->
            (double) __encodingByteLength((String) value, (String) encoding);
