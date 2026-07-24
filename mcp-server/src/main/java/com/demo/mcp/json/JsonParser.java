package com.demo.mcp.json;

import java.util.*;

public class JsonParser {
    private final String src;
    private int idx = 0;

    public JsonParser(String src) {
        this.src = src != null ? src.trim() : "";
    }

    public static JsonValue parse(String json) {
        return new JsonParser(json).parseValue();
    }

    public JsonValue parseValue() {
        skipWhitespace();
        if (idx >= src.length()) return JsonValue.nullValue();

        char c = src.charAt(idx);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return JsonValue.of(parseString());
        if (c == 't' || c == 'f') return parseBoolean();
        if (c == 'n') return parseNull();
        if (c == '-' || Character.isDigit(c)) return parseNumber();

        throw new IllegalArgumentException("Unexpected character at position " + idx + ": " + c);
    }

    private JsonValue parseObject() {
        idx++; // skip '{'
        Map<String, JsonValue> map = new LinkedHashMap<>();
        skipWhitespace();

        if (peek() == '}') {
            idx++;
            return JsonValue.of(map);
        }

        while (idx < src.length()) {
            skipWhitespace();
            if (peek() != '"') throw new IllegalArgumentException("Expected string key in object at position " + idx);
            String key = parseString();
            skipWhitespace();
            if (peek() != ':') throw new IllegalArgumentException("Expected ':' after key at position " + idx);
            idx++; // skip ':'
            JsonValue val = parseValue();
            map.put(key, val);
            skipWhitespace();

            char next = peek();
            if (next == '}') {
                idx++;
                break;
            } else if (next == ',') {
                idx++;
            } else {
                throw new IllegalArgumentException("Expected ',' or '}' in object at position " + idx);
            }
        }
        return JsonValue.of(map);
    }

    private JsonValue parseArray() {
        idx++; // skip '['
        List<JsonValue> list = new ArrayList<>();
        skipWhitespace();

        if (peek() == ']') {
            idx++;
            return JsonValue.of(list);
        }

        while (idx < src.length()) {
            JsonValue val = parseValue();
            list.add(val);
            skipWhitespace();

            char next = peek();
            if (next == ']') {
                idx++;
                break;
            } else if (next == ',') {
                idx++;
            } else {
                throw new IllegalArgumentException("Expected ',' or ']' in array at position " + idx);
            }
        }
        return JsonValue.of(list);
    }

    private String parseString() {
        idx++; // skip opening '"'
        StringBuilder sb = new StringBuilder();
        while (idx < src.length()) {
            char c = src.charAt(idx++);
            if (c == '"') return sb.toString();
            if (c == '\\') {
                if (idx >= src.length()) break;
                char esc = src.charAt(idx++);
                switch (esc) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (idx + 4 <= src.length()) {
                            String hex = src.substring(idx, idx + 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            idx += 4;
                        }
                    }
                    default -> sb.append(esc);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private JsonValue parseBoolean() {
        if (src.startsWith("true", idx)) {
            idx += 4;
            return JsonValue.of(true);
        } else if (src.startsWith("false", idx)) {
            idx += 5;
            return JsonValue.of(false);
        }
        throw new IllegalArgumentException("Invalid boolean at position " + idx);
    }

    private JsonValue parseNull() {
        if (src.startsWith("null", idx)) {
            idx += 4;
            return JsonValue.nullValue();
        }
        throw new IllegalArgumentException("Invalid null at position " + idx);
    }

    private JsonValue parseNumber() {
        int start = idx;
        if (peek() == '-') idx++;
        while (idx < src.length() && (Character.isDigit(src.charAt(idx)) || src.charAt(idx) == '.' || src.charAt(idx) == 'e' || src.charAt(idx) == 'E' || src.charAt(idx) == '+' || src.charAt(idx) == '-')) {
            idx++;
        }
        String numStr = src.substring(start, idx);
        if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
            return JsonValue.of(Double.parseDouble(numStr));
        } else {
            return JsonValue.of(Long.parseLong(numStr));
        }
    }

    private void skipWhitespace() {
        while (idx < src.length() && Character.isWhitespace(src.charAt(idx))) {
            idx++;
        }
    }

    private char peek() {
        if (idx >= src.length()) return '\0';
        return src.charAt(idx);
    }
}
