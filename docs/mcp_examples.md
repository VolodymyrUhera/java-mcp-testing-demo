# MCP Server Protocol Request & Response Examples

The Java Playwright MCP Server uses JSON-RPC 2.0 over Standard I/O (Stdio). Below are request and response examples for all 12 supported tools.

---

### 1. `launch_browser`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "launch_browser",
    "arguments": {
      "headless": true
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Browser launched successfully."
      }
    ]
  }
}
```

---

### 2. `open_url` / `navigate`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "open_url",
    "arguments": {
      "url": "http://localhost:8080"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Navigated to http://localhost:8080 [Status: 200]"
      }
    ]
  }
}
```

---

### 3. `click`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "click",
    "arguments": {
      "selector": "a[href='/contact']"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Clicked element matching selector: a[href='/contact']"
      }
    ]
  }
}
```

---

### 4. `fill_form`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 4,
  "method": "tools/call",
  "params": {
    "name": "fill_form",
    "arguments": {
      "selector": "input[name='username']",
      "value": "CyberSurfer99"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 4,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Filled selector 'input[name='username']' with value 'CyberSurfer99'"
      }
    ]
  }
}
```

---

### 5. `wait_for_selector`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 5,
  "method": "tools/call",
  "params": {
    "name": "wait_for_selector",
    "arguments": {
      "selector": "#contactForm",
      "timeout": 5000
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 5,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Element matching '#contactForm' is ready."
      }
    ]
  }
}
```

---

### 6. `evaluate_javascript`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 6,
  "method": "tools/call",
  "params": {
    "name": "evaluate_javascript",
    "arguments": {
      "script": "document.title"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 6,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Evaluation Result: Contact Us - CyberSpace 1999"
      }
    ]
  }
}
```

---

### 7. `take_screenshot`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 7,
  "method": "tools/call",
  "params": {
    "name": "take_screenshot",
    "arguments": {
      "filePath": "reports/screenshot.png"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 7,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Screenshot saved to reports/screenshot.png"
      }
    ]
  }
}
```

---

### 8. `save_pdf`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 8,
  "method": "tools/call",
  "params": {
    "name": "save_pdf",
    "arguments": {
      "filePath": "reports/document.pdf"
    }
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 8,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "PDF saved to reports/document.pdf"
      }
    ]
  }
}
```

---

### 9. `extract_content`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 9,
  "method": "tools/call",
  "params": {
    "name": "extract_content",
    "arguments": {}
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 9,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Extracted Content: {title=Welcome to CyberSpace 1999, url=http://localhost:8080/, links=[{text=Home Base, href=/}, {text=Click Here, href=/about}]}"
      }
    ]
  }
}
```

---

### 10. `get_performance_metrics`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 10,
  "method": "tools/call",
  "params": {
    "name": "get_performance_metrics",
    "arguments": {}
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 10,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Performance Metrics: {loadTimeMs=120, domContentLoadedMs=45, firstContentfulPaintMs=60, resourceCount=3}"
      }
    ]
  }
}
```

---

### 11. `analyze_accessibility`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 11,
  "method": "tools/call",
  "params": {
    "name": "analyze_accessibility",
    "arguments": {}
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 11,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Accessibility Analysis: {totalImages=2, missingAltCount=2, totalFormInputs=3, unlabelledInputCount=3, headingJumps=1}"
      }
    ]
  }
}
```

---

### 12. `close_browser`

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 12,
  "method": "tools/call",
  "params": {
    "name": "close_browser",
    "arguments": {}
  }
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 12,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Browser closed successfully."
      }
    ]
  }
}
```
