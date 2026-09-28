    // Port of Node/Buffer/Encoding.js.
    public static Object byteLengthImpl = (java.util.function.Function<Object, Object>) (str) ->
        (java.util.function.Function<Object, Object>) (encoding) ->
            __M$Node_Buffer.__decode((String) str, (String) encoding).length;
