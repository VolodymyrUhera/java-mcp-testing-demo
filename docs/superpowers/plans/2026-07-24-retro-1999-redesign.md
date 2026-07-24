# Retro 1999 Web Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Completely redesign the Java web server application into an authentic 1997-2001 Windows 98 / GeoCities web experience with full screen-reader and keyboard accessibility.

**Architecture:** Enhance HTML structure in Java `HttpServer` handlers (`NavigationHelper`, `HomeHandler`, `AboutHandler`, `ContactHandler`, `LinksHandler`), build a modular late-90s CSS design system in `/static/style.css`, and create retro SVG graphic assets in `/static/images/`.

**Tech Stack:** Java 21 (`com.sun.net.httpserver`), HTML5 with ARIA accessibility landmarks, CSS3 retro design system, SVG pixel graphics.

## Global Constraints

- Authentic 1997-2001 visual aesthetic (Win98 window chrome, beveled buttons, navy/cyan/magenta palette, marquee, visitor counter).
- Full accessibility (screen reader skip links, WCAG AA contrast, ARIA landmarks, `aria-hidden` on decorative elements, keyboard focus rings).
- Responsive layout (collapses gracefully on screens < 768px).
- Zero modern minimalist design (no Tailwind, Bootstrap, glassmorphism, or flat design).

---

### Task 1: Retro Design System & Static CSS (`style.css`)

**Files:**
- Modify: `demo-website/src/main/resources/static/style.css`

**Interfaces:**
- Consumes: Static asset request from `StaticAssetHandler`
- Produces: Visual styling for Win98 window containers, bevel borders, scrolling marquees, counter box, retro badges, accessibility focus rings, responsive breakpoints, screen reader utility `.sr-only`.

- [ ] **Step 1: Write retro CSS rules in `style.css`**

```css
/* Retro late-1990s Design System & Accessibility */
:root {
    --win-silver: #c0c0c0;
    --win-navy: #000080;
    --win-light-blue: #1084d0;
    --win-dark-gray: #808080;
    --win-white: #ffffff;
    --win-black: #000000;
    --neon-yellow: #ffff00;
    --neon-cyan: #00ffff;
    --neon-pink: #ff00ff;
    --neon-green: #00ff00;
}

body {
    background-color: var(--win-navy);
    background-image: radial-gradient(#1084d0 1px, transparent 0);
    background-size: 16px 16px;
    color: var(--neon-yellow);
    font-family: "Comic Sans MS", "Verdana", "MS Sans Serif", sans-serif;
    margin: 0;
    padding: 12px;
}

/* Accessibility: Hidden Skip Link for Screen Readers */
.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
}
.sr-only:focus {
    position: static;
    width: auto;
    height: auto;
    clip: auto;
    white-space: normal;
    background: #ffff00;
    color: #000000;
    padding: 10px;
    font-weight: bold;
    z-index: 9999;
}

/* Keyboard Focus Ring */
a:focus, button:focus, input:focus, textarea:focus {
    outline: 3px solid var(--neon-cyan) !important;
    outline-offset: 2px;
}

/* Win98 Window Container */
.win98-window {
    background-color: var(--win-silver);
    border: 3px outset var(--win-white);
    box-shadow: 4px 4px 0px var(--win-black);
    color: var(--win-black);
    margin: 10px auto;
    max-width: 960px;
}

.win98-titlebar {
    background: linear-gradient(90deg, var(--win-navy), var(--win-light-blue));
    color: var(--win-white);
    padding: 4px 8px;
    font-weight: bold;
    font-size: 14px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.win98-controls span {
    display: inline-block;
    width: 16px;
    height: 14px;
    background: var(--win-silver);
    color: var(--win-black);
    border: 1px outset var(--win-white);
    font-size: 10px;
    text-align: center;
    line-height: 12px;
    margin-left: 2px;
    font-weight: bold;
    cursor: default;
}

/* Main Layout Grid & Table Fallback */
.main-layout {
    width: 100%;
    border-collapse: collapse;
}

.sidebar {
    width: 220px;
    background-color: #008080;
    color: #ffffff;
    vertical-align: top;
    padding: 12px;
    border-right: 3px inset var(--win-white);
}

.content {
    background-color: #ffffff;
    color: #000000;
    padding: 20px;
    vertical-align: top;
}

/* Vintage Flaming Header & Marquee */
.flaming-header {
    color: var(--neon-yellow);
    text-shadow: 2px 2px #ff0000, 4px 4px #ff00ff;
    font-family: "Impact", "Comic Sans MS", fantasy;
}

.banner-marquee {
    background-color: #ff0000;
    color: #ffffff;
    font-weight: bold;
    font-size: 16px;
    padding: 6px;
    border: 3px ridge var(--neon-yellow);
}

/* Retro Buttons & Counter */
.nav-btn {
    background-color: var(--win-silver);
    color: var(--win-black);
    padding: 6px 10px;
    text-decoration: none;
    border: 2px outset var(--win-white);
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
}
.nav-btn.active, .nav-btn:hover {
    background-color: var(--neon-yellow);
    color: var(--win-black);
    border-style: inset;
}

.counter-box {
    background-color: var(--win-black);
    color: var(--neon-green);
    font-family: "Courier New", monospace;
    font-size: 22px;
    letter-spacing: 3px;
    padding: 6px 12px;
    border: 2px inset var(--win-white);
    display: inline-block;
}

.award-badge {
    border: 3px double #ffd700;
    background-color: #ffffcc;
    color: #000000;
    padding: 10px;
    margin: 15px 0;
    text-align: center;
    font-weight: bold;
}

.webring-box {
    border: 2px inset var(--win-white);
    background-color: var(--win-silver);
    color: var(--win-black);
    padding: 8px;
    font-size: 12px;
    text-align: center;
    margin-top: 10px;
}

.badge-row img {
    margin: 2px;
}

/* Form Styles */
.retro-form table {
    background-color: var(--win-silver);
    border: 2px outset var(--win-white);
}

.submit-btn {
    background-color: var(--win-silver);
    border: 2px outset var(--win-white);
    font-weight: bold;
    padding: 6px 16px;
    cursor: pointer;
}
.submit-btn:hover {
    background-color: var(--neon-cyan);
}

/* Reduced Motion Support */
@media (prefers-reduced-motion: reduce) {
    marquee {
        animation: none;
        behavior: static;
    }
}

/* Mobile Responsiveness */
@media (max-width: 768px) {
    .main-layout, .main-layout tr, .main-layout td {
        display: block;
        width: 100% !important;
    }
    .sidebar {
        border-right: none;
        border-bottom: 3px inset var(--win-white);
    }
}
```

- [ ] **Step 2: Commit CSS changes**

```bash
git add demo-website/src/main/resources/static/style.css
git commit -m "style: add late-90s Win98 retro design system with accessibility support"
```

---

### Task 2: Enhanced Retro SVG Graphics (`/static/images/`)

**Files:**
- Create/Modify: `demo-website/src/main/resources/static/images/globe.svg`
- Create/Modify: `demo-website/src/main/resources/static/images/under_construction.svg`
- Create/Modify: `demo-website/src/main/resources/static/images/netscape_badge.svg`
- Create/Modify: `demo-website/src/main/resources/static/images/ie_badge.svg`

**Interfaces:**
- Consumes: Handlers referencing `/static/images/*.svg`
- Produces: Nostalgic late-90s pixel graphics.

- [ ] **Step 1: Create retro SVG icons**

Create/update SVGs with 90s pixel aesthetics and explicit accessibility tags.

- [ ] **Step 2: Commit SVG assets**

```bash
git add demo-website/src/main/resources/static/images/
git commit -m "assets: add 90s retro SVG badges and animated icons"
```

---

### Task 3: Sidebar Navigation Helper & Shared Chrome (`NavigationHelper.java`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java`

**Interfaces:**
- Consumes: `activePath` (`/`, `/about`, `/contact`, `/links`)
- Produces: HTML string for sidebar navigation with ARIA `<nav>`, 3D buttons, visitor counter, webring widget, and 80x15 retro badges.

- [ ] **Step 1: Update `NavigationHelper.java` HTML generation**

Implement ARIA `<nav aria-label="Main Navigation">`, active page states, alt texts on badges, `aria-hidden` on decorative webring elements.

- [ ] **Step 2: Test compile**

Run: `mvn -f demo-website/pom.xml compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit NavigationHelper changes**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java
git commit -m "feat: enhance NavigationHelper with Win98 controls, ARIA landmarks, and retro badges"
```

---

### Task 4: Home Page Redesign (`HomeHandler.java`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/HomeHandler.java`

**Interfaces:**
- Consumes: GET `/`
- Produces: Win98 window HTML with skip link, `<header>`, `<main>`, `<aside>`, Under Construction banner, News log table, fake Golden Web Award.

- [ ] **Step 1: Update `HomeHandler.java` with Win98 window wrapper & accessibility**

Include skip link `<a href="#main-content" class="sr-only">Skip to main content</a>`, Win98 header, semantic landmarks, and flaming retro title.

- [ ] **Step 2: Test compile**

Run: `mvn -f demo-website/pom.xml compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit HomeHandler**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/HomeHandler.java
git commit -m "feat: redesign HomeHandler to Win98 window layout with screen reader support"
```

---

### Task 5: About Page Redesign (`AboutHandler.java`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/AboutHandler.java`

**Interfaces:**
- Consumes: GET `/about`
- Produces: Retro About page wrapped in Win98 Dialog window with team photo frame, Y2K notice, disclaimer.

- [ ] **Step 1: Update `AboutHandler.java`**

Implement accessible layout, skip links, semantic text structure, team photo alt text, and Y2K readiness badge.

- [ ] **Step 2: Test compile**

Run: `mvn -f demo-website/pom.xml compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit AboutHandler**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/AboutHandler.java
git commit -m "feat: redesign AboutHandler with Win98 dialog chrome and accessible Y2K notice"
```

---

### Task 6: Contact & E-Mail Transmission Form (`ContactHandler.java`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/ContactHandler.java`

**Interfaces:**
- Consumes: GET `/contact`, POST `/contact`
- Produces: Accessible 90s form with explicit `<label for="...">` elements and retro terminal response page.

- [ ] **Step 1: Update `ContactHandler.java` GET and POST responses**

Ensure explicit labels for `username`, `email`, and `comments`, ARIA attributes, retro button styling, and cyber transmission confirmation page.

- [ ] **Step 2: Test compile & POST functionality**

Run: `mvn -f demo-website/pom.xml compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit ContactHandler**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/ContactHandler.java
git commit -m "feat: redesign ContactHandler with accessible retro form controls and transmission response"
```

---

### Task 7: Cool Links Web Directory (`LinksHandler.java`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/LinksHandler.java`

**Interfaces:**
- Consumes: GET `/links`
- Produces: Yahoo 1999 style directory with folder icons, clear descriptive link text, and web ring links.

- [ ] **Step 1: Update `LinksHandler.java`**

Structure directory into accessible `<ul>` lists with descriptive link labels and retro folder icons.

- [ ] **Step 2: Test compile**

Run: `mvn -f demo-website/pom.xml compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Commit LinksHandler**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/LinksHandler.java
git commit -m "feat: redesign LinksHandler into Yahoo 1999 retro directory with accessible links"
```

---

### Task 8: Build Verification & End-to-End Test

**Files:**
- Test: All endpoints (`/`, `/about`, `/contact`, `/links`, `/static/*`)

- [ ] **Step 1: Compile entire project**

Run: `mvn -f demo-website/pom.xml clean package`
Expected: `BUILD SUCCESS`

- [ ] **Step 2: Start server and test HTTP responses**

Run: `java -jar demo-website/target/demo-website-1.0-SNAPSHOT.jar 8085 &`
Test GET `/`, `/about`, `/contact`, `/links`, `/static/style.css` using `curl -I http://localhost:8085/`.
Expected: `HTTP/1.1 200 OK` for all endpoints.

- [ ] **Step 3: Verify POST transmission**

Run: `curl -X POST -d "username=RetroCoder&email=coder@geocities.com&comments=Hello1999" http://localhost:8085/contact`
Expected: `HTTP/1.1 200 OK` with "Electronic Mail Sent!" content.
