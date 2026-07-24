package com.demo.mcp.json;

import java.util.*;

public class JsonValue {
    public enum Type { OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL }

    private final Type type;
    private final Object value;

    public JsonValue(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static JsonValue of(String str) { return str == null ? nullValue() : new JsonValue(Type.STRING, str); }
    public static JsonValue of(Number num) { return num == null ? nullValue() : new JsonValue(Type.NUMBER, num); }
    public static JsonValue of(Boolean bool) { return bool == null ? nullValue() : new JsonValue(Type.BOOLEAN, bool); }
    public static JsonValue of(Map<String, JsonValue> obj) { return obj == null ? nullValue() : new JsonValue(Type.OBJECT, obj); }
    public static JsonValue of(List<JsonValue> arr) { return arr == null ? nullValue() : new JsonValue(Type.ARRAY, arr); }
    public static JsonValue nullValue() { return new JsonValue(Type.NULL, null); }

    public Type getType() { return type; }
    public Object getValue() { return value; }

    public boolean isObject() { return type == Type.OBJECT; }
    public boolean isArray() { return type == Type.ARRAY; }
    public boolean isString() { return type == Type.STRING; }
    public boolean isNumber() { return type == Type.NUMBER; }
    public boolean isBoolean() { return type == Type.BOOLEAN; }
    public boolean isNull() { return type == Type.NULL; }

    @SuppressWarnings("unchecked")
    public Map<String, JsonValue> asObject() { return (Map<String, JsonValue>) value; }
    
    @SuppressWarnings("unchecked")
    public List<JsonValue> asArray() { return (List<JsonValue>) value; }
    
    public String asString() { return value == null ? "" : value.toString(); }
    public double asDouble() { return value instanceof Number n ? n.doubleValue() : 0.0; }
    public long asLong() { return value instanceof Number n ? n.longValue() : 0L; }
    public boolean asBoolean() { return Boolean.TRUE.equals(value); }

    public JsonValue get(String key) {
        if (isObject() && asObject().containsKey(key)) {
            return asObject().get(key);
        }
        return null;
    }

    public String toJsonString() {
        if (type == Type.NULL || value == null) return "null";
        if (type == Type.STRING) return "\"" + escapeString(value.toString()) + "\"";
        if (type == Type.NUMBER || type == Type.BOOLEAN) return value.toString();
        if (type == Type.ARRAY) {
            StringBuilder sb = new StringBuilder("[");
            List<JsonValue> list = asArray();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i) == null ? "null" : list.get(i).toJsonString());
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        }
        if (type == Type.OBJECT) {
            StringBuilder sb = new StringBuilder("{");
            Map<String, JsonValue> map = asObject();
            int i = 0;
            for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
                sb.append("\"").append(escapeString(entry.getKey())).append("\":");
                sb.append(entry.getValue() == null ? "null" : entry.getValue().toJsonString());
                if (++i < map.size()) sb.append(",");
            }
            sb.append("}");
            return sb.toString();
        }
        return "null";
    }

    private static String escapeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
