    // Port of Node/Buffer.js. A Buffer is a window over a shared byte array,
    // so slices keep Node's view semantics; the encoding and numeric helpers
    // are shared with Node.Buffer.Immutable.
    public static final class NodeBuffer {
        public final byte[] array;
        public final int offset;
        public final int length;
        public NodeBuffer(byte[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }
    }

    public static NodeBuffer __wrap(byte[] array) { return new NodeBuffer(array, 0, array.length); }

    public static java.nio.charset.Charset __charset(String encoding) {
        switch (encoding.toLowerCase()) {
            case "utf16le":
            case "utf-16le":
            case "ucs2":
            case "ucs-2":
                return java.nio.charset.StandardCharsets.UTF_16LE;
            case "latin1":
            case "binary":
                return java.nio.charset.StandardCharsets.ISO_8859_1;
            case "ascii":
                return java.nio.charset.StandardCharsets.US_ASCII;
            default:
                return java.nio.charset.StandardCharsets.UTF_8;
        }
    }

    public static byte[] __decode(String value, String encoding) {
        String enc = encoding.toLowerCase();
        if (enc.equals("hex")) {
            String hex = value.length() % 2 == 0 ? value : value + "0";
            byte[] out = new byte[hex.length() / 2];
            for (int i = 0; i < out.length; i++) {
                out[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
            }
            return out;
        }
        if (enc.equals("base64")) return java.util.Base64.getDecoder().decode(value);
        if (enc.equals("base64url")) return java.util.Base64.getUrlDecoder().decode(value);
        return value.getBytes(__charset(encoding));
    }

    public static String __encode(byte[] bytes, String encoding) {
        String enc = encoding.toLowerCase();
        if (enc.equals("hex")) {
            StringBuilder builder = new StringBuilder();
            for (byte value : bytes) builder.append(String.format("%02x", value & 0xff));
            return builder.toString();
        }
        if (enc.equals("base64")) return java.util.Base64.getEncoder().encodeToString(bytes);
        if (enc.equals("base64url")) return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return new String(bytes, __charset(encoding));
    }

    public static byte[] __window(NodeBuffer buffer) {
        return java.util.Arrays.copyOfRange(buffer.array, buffer.offset, buffer.offset + buffer.length);
    }

    public static double __readNumber(String type, NodeBuffer buffer, int index) {
        int absolute = buffer.offset + index;
        java.nio.ByteBuffer view = java.nio.ByteBuffer.wrap(buffer.array).order(
            type.endsWith("LE") ? java.nio.ByteOrder.LITTLE_ENDIAN : java.nio.ByteOrder.BIG_ENDIAN);
        switch (type) {
            case "UInt8": return view.get(absolute) & 0xff;
            case "Int8": return view.get(absolute);
            case "UInt16LE":
            case "UInt16BE": return view.getShort(absolute) & 0xffff;
            case "Int16LE":
            case "Int16BE": return view.getShort(absolute);
            case "UInt32LE":
            case "UInt32BE": return view.getInt(absolute) & 0xffffffffL;
            case "Int32LE":
            case "Int32BE": return view.getInt(absolute);
            case "FloatLE":
            case "FloatBE": return view.getFloat(absolute);
            case "DoubleLE":
            case "DoubleBE": return view.getDouble(absolute);
            default: throw new RuntimeException("Unknown buffer value type " + type);
        }
    }

    public static void __writeNumber(String type, double value, int index, NodeBuffer buffer) {
        int absolute = buffer.offset + index;
        java.nio.ByteBuffer view = java.nio.ByteBuffer.wrap(buffer.array).order(
            type.endsWith("LE") ? java.nio.ByteOrder.LITTLE_ENDIAN : java.nio.ByteOrder.BIG_ENDIAN);
        switch (type) {
            case "UInt8":
            case "Int8": view.put(absolute, (byte) ((int) value)); break;
            case "UInt16LE":
            case "UInt16BE": view.putShort(absolute, (short) ((int) value)); break;
            case "UInt32LE":
            case "UInt32BE":
            case "Int32LE":
            case "Int32BE": view.putInt(absolute, (int) ((long) value)); break;
            case "FloatLE":
            case "FloatBE": view.putFloat(absolute, (float) value); break;
            case "DoubleLE":
            case "DoubleBE": view.putDouble(absolute, value); break;
            default: throw new RuntimeException("Unknown buffer value type " + type);
        }
    }

    public static Object allocUnsafeImpl = (java.util.function.Function<Object, Object>) (size) ->
        (java.util.function.Supplier<Object>) () -> __wrap(new byte[((Number) size).intValue()]);

    public static Object allocUnsafeSlowImpl = (java.util.function.Function<Object, Object>) (size) ->
        (java.util.function.Supplier<Object>) () -> __wrap(new byte[((Number) size).intValue()]);

    public static Object freezeImpl = (java.util.function.Function<Object, Object>) (buffer) ->
        (java.util.function.Supplier<Object>) () -> __wrap(__window((NodeBuffer) buffer));

    public static Object thawImpl = (java.util.function.Function<Object, Object>) (buffer) ->
        (java.util.function.Supplier<Object>) () -> __wrap(__window((NodeBuffer) buffer));

    public static Object writeInternal = (java.util.function.Function<Object, Object>) (typeObj) ->
        (java.util.function.Function<Object, Object>) (value) ->
        (java.util.function.Function<Object, Object>) (offset) ->
        (java.util.function.Function<Object, Object>) (buffer) ->
            (java.util.function.Supplier<Object>) () -> {
                __writeNumber((String) typeObj, ((Number) value).doubleValue(), ((Number) offset).intValue(), (NodeBuffer) buffer);
                return null;
            };

    public static Object writeStringInternal = (java.util.function.Function<Object, Object>) (encoding) ->
        (java.util.function.Function<Object, Object>) (offset) ->
        (java.util.function.Function<Object, Object>) (length) ->
        (java.util.function.Function<Object, Object>) (value) ->
        (java.util.function.Function<Object, Object>) (bufferObj) ->
            (java.util.function.Supplier<Object>) () -> {
                NodeBuffer buffer = (NodeBuffer) bufferObj;
                byte[] encoded = __decode((String) value, (String) encoding);
                int start = ((Number) offset).intValue();
                int limit = Math.min(((Number) length).intValue(), encoded.length);
                limit = Math.min(limit, Math.max(0, buffer.length - start));
                System.arraycopy(encoded, 0, buffer.array, buffer.offset + start, limit);
                return limit;
            };

    public static Object setAtOffsetImpl = (java.util.function.Function<Object, Object>) (value) ->
        (java.util.function.Function<Object, Object>) (offset) ->
        (java.util.function.Function<Object, Object>) (bufferObj) ->
            (java.util.function.Supplier<Object>) () -> {
                NodeBuffer buffer = (NodeBuffer) bufferObj;
                int index = ((Number) offset).intValue();
                if (index >= 0 && index < buffer.length) {
                    buffer.array[buffer.offset + index] = (byte) ((Number) value).intValue();
                }
                return null;
            };

    public static Object copyImpl = (java.util.function.Function<Object, Object>) (sourceStart) ->
        (java.util.function.Function<Object, Object>) (sourceEnd) ->
        (java.util.function.Function<Object, Object>) (sourceObj) ->
        (java.util.function.Function<Object, Object>) (targetStart) ->
        (java.util.function.Function<Object, Object>) (targetObj) ->
            (java.util.function.Supplier<Object>) () -> {
                NodeBuffer source = (NodeBuffer) sourceObj;
                NodeBuffer target = (NodeBuffer) targetObj;
                int start = Math.max(0, Math.min(((Number) sourceStart).intValue(), source.length));
                int end = Math.max(start, Math.min(((Number) sourceEnd).intValue(), source.length));
                int target0 = Math.max(0, Math.min(((Number) targetStart).intValue(), target.length));
                int count = Math.min(end - start, target.length - target0);
                System.arraycopy(source.array, source.offset + start, target.array, target.offset + target0, count);
                return count;
            };

    public static Object fillImpl = (java.util.function.Function<Object, Object>) (octet) ->
        (java.util.function.Function<Object, Object>) (start) ->
        (java.util.function.Function<Object, Object>) (end) ->
        (java.util.function.Function<Object, Object>) (bufferObj) ->
            (java.util.function.Supplier<Object>) () -> {
                NodeBuffer buffer = (NodeBuffer) bufferObj;
                int from = Math.max(0, Math.min(((Number) start).intValue(), buffer.length));
                int to = Math.max(from, Math.min(((Number) end).intValue(), buffer.length));
                java.util.Arrays.fill(buffer.array, buffer.offset + from, buffer.offset + to, (byte) ((Number) octet).intValue());
                return null;
            };

    public static Object poolSize = (java.util.function.Supplier<Object>) () -> 8192;

    public static Object setPoolSizeImpl = (java.util.function.Function<Object, Object>) (size) ->
        (java.util.function.Supplier<Object>) () -> null;

    public static Object swap16Impl = (java.util.function.Function<Object, Object>) (bufferObj) ->
        (java.util.function.Supplier<Object>) () -> {
            NodeBuffer buffer = (NodeBuffer) bufferObj;
            if (buffer.length % 2 != 0) throw new RuntimeException("Buffer size must be a multiple of 16-bits");
            for (int i = 0; i < buffer.length; i += 2) {
                int a = buffer.offset + i;
                byte tmp = buffer.array[a];
                buffer.array[a] = buffer.array[a + 1];
                buffer.array[a + 1] = tmp;
            }
            return buffer;
        };

    public static Object swap32Impl = (java.util.function.Function<Object, Object>) (bufferObj) ->
        (java.util.function.Supplier<Object>) () -> {
            NodeBuffer buffer = (NodeBuffer) bufferObj;
            if (buffer.length % 4 != 0) throw new RuntimeException("Buffer size must be a multiple of 32-bits");
            for (int i = 0; i < buffer.length; i += 4) {
                int base = buffer.offset + i;
                byte b0 = buffer.array[base];
                byte b1 = buffer.array[base + 1];
                buffer.array[base] = buffer.array[base + 3];
                buffer.array[base + 1] = buffer.array[base + 2];
                buffer.array[base + 2] = b1;
                buffer.array[base + 3] = b0;
            }
            return buffer;
        };

    public static Object swap64Impl = (java.util.function.Function<Object, Object>) (bufferObj) ->
        (java.util.function.Supplier<Object>) () -> {
            NodeBuffer buffer = (NodeBuffer) bufferObj;
            if (buffer.length % 8 != 0) throw new RuntimeException("Buffer size must be a multiple of 64-bits");
            for (int i = 0; i < buffer.length; i += 8) {
                for (int j = 0; j < 4; j++) {
                    int a = buffer.offset + i + j;
                    int b = buffer.offset + i + 7 - j;
                    byte tmp = buffer.array[a];
                    buffer.array[a] = buffer.array[b];
                    buffer.array[b] = tmp;
                }
            }
            return buffer;
        };

    public static Object transcodeImpl = (java.util.function.Function<Object, Object>) (buffer) ->
        (java.util.function.Function<Object, Object>) (from) ->
        (java.util.function.Function<Object, Object>) (to) ->
            (java.util.function.Supplier<Object>) () -> {
                String decoded = __encode(__window((NodeBuffer) buffer), (String) from);
                return __wrap(__decode(decoded, (String) to));
            };
