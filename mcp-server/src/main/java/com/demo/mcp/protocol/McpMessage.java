package com.demo.mcp.protocol;

import com.demo.mcp.json.JsonValue;
import java.util.*;

public class McpMessage {
    public static String createResponse(JsonValue id, JsonValue result) {
        Map<String, JsonValue> map = new LinkedHashMap<>();
        map.put("jsonrpc", JsonValue.of("2.0"));
        map.put("id", id != null ? id : JsonValue.nullValue());
        map.put("result", result != null ? result : JsonValue.of(new LinkedHashMap<>()));
        return JsonValue.of(map).toJsonString();
    }

    public static String createError(JsonValue id, int code, String message) {
        Map<String, JsonValue> map = new LinkedHashMap<>();
        map.put("jsonrpc", JsonValue.of("2.0"));
        map.put("id", id != null ? id : JsonValue.nullValue());

        Map<String, JsonValue> errMap = new LinkedHashMap<>();
        errMap.put("code", JsonValue.of(code));
        errMap.put("message", JsonValue.of(message));
        map.put("error", JsonValue.of(errMap));

        return JsonValue.of(map).toJsonString();
    }

    public static String createToolResult(JsonValue id, String textContent, boolean isError) {
        Map<String, JsonValue> contentItem = new LinkedHashMap<>();
        contentItem.put("type", JsonValue.of("text"));
        contentItem.put("text", JsonValue.of(textContent));

        List<JsonValue> contentList = new ArrayList<>();
        contentList.add(JsonValue.of(contentItem));

        Map<String, JsonValue> resultMap = new LinkedHashMap<>();
        resultMap.put("content", JsonValue.of(contentList));
        if (isError) {
            resultMap.put("isError", JsonValue.of(true));
        }

        return createResponse(id, JsonValue.of(resultMap));
    }
}
