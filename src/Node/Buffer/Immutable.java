    // Port of Node/Buffer/Immutable.js over the Node.Buffer window type.
    public static Object showImpl = (java.util.function.Function<Object, Object>) (bufferObj) -> {
        __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
        StringBuilder builder = new StringBuilder("<Buffer");
        int shown = Math.min(buffer.length, 50);
        for (int i = 0; i < shown; i++) {
            builder.append(String.format(" %02x", buffer.array[buffer.offset + i] & 0xff));
        }
        if (buffer.length > shown) builder.append(" ... ").append(buffer.length - shown).append(" more bytes");
        return builder.append('>').toString();
    };

    private static int __compare(__M$Node_Buffer.NodeBuffer left, __M$Node_Buffer.NodeBuffer right) {
        int length = Math.min(left.length, right.length);
        for (int i = 0; i < length; i++) {
            int l = left.array[left.offset + i] & 0xff;
            int r = right.array[right.offset + i] & 0xff;
            if (l != r) return l < r ? -1 : 1;
        }
        if (left.length == right.length) return 0;
        return left.length < right.length ? -1 : 1;
    }

    public static Object eqImpl = (java.util.function.Function<Object, Object>) (a) ->
        (java.util.function.Function<Object, Object>) (b) ->
            __compare((__M$Node_Buffer.NodeBuffer) a, (__M$Node_Buffer.NodeBuffer) b) == 0;

    public static Object compareImpl = (java.util.function.Function<Object, Object>) (a) ->
        (java.util.function.Function<Object, Object>) (b) ->
            __compare((__M$Node_Buffer.NodeBuffer) a, (__M$Node_Buffer.NodeBuffer) b);

    public static Object alloc = (java.util.function.Function<Object, Object>) (size) ->
        __M$Node_Buffer.__wrap(new byte[((Number) size).intValue()]);

    public static Object fromArray = (java.util.function.Function<Object, Object>) (octets) -> {
        Object[] values = (Object[]) octets;
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) bytes[i] = (byte) ((Number) values[i]).intValue();
        return __M$Node_Buffer.__wrap(bytes);
    };

    public static Object fromArrayBuffer = (java.util.function.Function<Object, Object>) (arrayBuffer) -> {
        byte[] bytes = (byte[]) arrayBuffer;
        return new __M$Node_Buffer.NodeBuffer(bytes, 0, bytes.length);
    };

    public static Object fromStringImpl = (java.util.function.Function<Object, Object>) (str) ->
        (java.util.function.Function<Object, Object>) (encoding) ->
            __M$Node_Buffer.__wrap(__M$Node_Buffer.__decode((String) str, (String) encoding));

    public static Object comparePartsImpl = (java.util.function.Function<Object, Object>) (src) ->
        (java.util.function.Function<Object, Object>) (target) ->
        (java.util.function.Function<Object, Object>) (targetStart) ->
        (java.util.function.Function<Object, Object>) (targetEnd) ->
        (java.util.function.Function<Object, Object>) (sourceStart) ->
        (java.util.function.Function<Object, Object>) (sourceEnd) ->
            (java.util.function.Supplier<Object>) () -> {
                __M$Node_Buffer.NodeBuffer source = (__M$Node_Buffer.NodeBuffer) src;
                __M$Node_Buffer.NodeBuffer other = (__M$Node_Buffer.NodeBuffer) target;
                int ts = Math.max(0, Math.min(((Number) targetStart).intValue(), other.length));
                int te = Math.max(ts, Math.min(((Number) targetEnd).intValue(), other.length));
                int ss = Math.max(0, Math.min(((Number) sourceStart).intValue(), source.length));
                int se = Math.max(ss, Math.min(((Number) sourceEnd).intValue(), source.length));
                return __compare(
                    new __M$Node_Buffer.NodeBuffer(source.array, source.offset + ss, se - ss),
                    new __M$Node_Buffer.NodeBuffer(other.array, other.offset + ts, te - ts));
            };

    public static Object readImpl = (java.util.function.Function<Object, Object>) (type) ->
        (java.util.function.Function<Object, Object>) (offset) ->
        (java.util.function.Function<Object, Object>) (buffer) ->
            __M$Node_Buffer.__readNumber((String) type, (__M$Node_Buffer.NodeBuffer) buffer, ((Number) offset).intValue());

    private static String __stringSlice(Object bufferObj, int start, int end, String encoding) {
        __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
        int from = Math.max(0, Math.min(start, buffer.length));
        int to = Math.max(from, Math.min(end, buffer.length));
        byte[] bytes = java.util.Arrays.copyOfRange(buffer.array, buffer.offset + from, buffer.offset + to);
        return __M$Node_Buffer.__encode(bytes, encoding);
    }

    public static Object readStringImpl = (java.util.function.Function<Object, Object>) (encoding) ->
        (java.util.function.Function<Object, Object>) (start) ->
        (java.util.function.Function<Object, Object>) (end) ->
        (java.util.function.Function<Object, Object>) (buffer) ->
            __stringSlice(buffer, ((Number) start).intValue(), ((Number) end).intValue(), (String) encoding);

    public static Object toStringImpl = (java.util.function.Function<Object, Object>) (encoding) ->
        (java.util.function.Function<Object, Object>) (bufferObj) -> {
            __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
            return __M$Node_Buffer.__encode(__M$Node_Buffer.__window(buffer), (String) encoding);
        };

    public static Object toStringSubImpl = (java.util.function.Function<Object, Object>) (encoding) ->
        (java.util.function.Function<Object, Object>) (start) ->
        (java.util.function.Function<Object, Object>) (end) ->
        (java.util.function.Function<Object, Object>) (buffer) ->
            __stringSlice(buffer, ((Number) start).intValue(), ((Number) end).intValue(), (String) encoding);

    public static Object toArray = (java.util.function.Function<Object, Object>) (bufferObj) -> {
        byte[] bytes = __M$Node_Buffer.__window((__M$Node_Buffer.NodeBuffer) bufferObj);
        Object[] octets = new Object[bytes.length];
        for (int i = 0; i < bytes.length; i++) octets[i] = (int) (bytes[i] & 0xff);
        return octets;
    };

    public static Object toArrayBuffer = (java.util.function.Function<Object, Object>) (bufferObj) ->
        __M$Node_Buffer.__window((__M$Node_Buffer.NodeBuffer) bufferObj);

    public static Object getAtOffsetImpl = (java.util.function.Function<Object, Object>) (offset) ->
        (java.util.function.Function<Object, Object>) (bufferObj) -> {
            __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
            int index = ((Number) offset).intValue();
            return index >= 0 && index < buffer.length
                ? (int) (buffer.array[buffer.offset + index] & 0xff)
                : null;
        };

    public static byte[] __concatToLength(Object[] buffers, int totalLength) {
        int total = 0;
        for (Object buffer : buffers) total += ((__M$Node_Buffer.NodeBuffer) buffer).length;
        int length = totalLength < 0 ? total : totalLength;
        byte[] out = new byte[length];
        int offset = 0;
        for (Object bufferObj : buffers) {
            __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
            int count = Math.min(buffer.length, length - offset);
            if (count <= 0) break;
            System.arraycopy(buffer.array, buffer.offset, out, offset, count);
            offset += count;
        }
        return out;
    }

    public static Object concat = (java.util.function.Function<Object, Object>) (buffers) ->
        __M$Node_Buffer.__wrap(__concatToLength((Object[]) buffers, -1));

    public static Object concatToLength = (java.util.function.Function<Object, Object>) (buffers) ->
        (java.util.function.Function<Object, Object>) (totalLength) ->
            __M$Node_Buffer.__wrap(__concatToLength((Object[]) buffers, ((Number) totalLength).intValue()));

    public static Object sliceImpl = (java.util.function.Function<Object, Object>) (start) ->
        (java.util.function.Function<Object, Object>) (end) ->
        (java.util.function.Function<Object, Object>) (bufferObj) -> {
            __M$Node_Buffer.NodeBuffer buffer = (__M$Node_Buffer.NodeBuffer) bufferObj;
            int from = Math.max(0, Math.min(((Number) start).intValue(), buffer.length));
            int to = Math.max(from, Math.min(((Number) end).intValue(), buffer.length));
            return new __M$Node_Buffer.NodeBuffer(buffer.array, buffer.offset + from, to - from);
        };

    public static Object size = (java.util.function.Function<Object, Object>) (buffer) ->
        ((__M$Node_Buffer.NodeBuffer) buffer).length;
