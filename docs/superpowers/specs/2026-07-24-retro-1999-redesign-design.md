# Design Specification: Late-1990s / Early-2000s Retro Web Redesign

## 1. Overview
Redesign the Java-based demo website into an authentic 1997–2001 internet experience (Win98 Desktop + GeoCities CyberSpace aesthetic) while preserving standard Java `HttpServer` architecture, semantic HTML5, responsive layout, clean CSS, and accessibility.

## 2. Visual Architecture & Design System

### 2.1 Color Palette & Variables
- **Background**: Classic Navy Blue (`#000080`) with tiled starry/pixel background pattern.
- **Window Frame**: Windows 95/98 Silver (`#c0c0c0`) with 3D outset/inset borders.
- **Title Bar**: Active Win98 navy-to-cyan gradient (`linear-gradient(90deg, #000080, #1084d0)`), white bold text (`#ffffff`).
- **Accents**: Neon Yellow (`#ffff00`), Hot Pink (`#ff00ff`), Cyan (`#00ffff`), Lime Green (`#00ff00`), Gold (`#ffd700`).

### 2.2 Typography
- Body font: Comic Sans MS, Verdana, Arial, MS Sans Serif, sans-serif.
- Headings: Pixel/Retro fonts, flaming text shadow effects (`text-shadow: 2px 2px #ff0000, 4px 4px #ffff00`).
- Monospace element font: Courier New (for counters, code/form output).

### 2.3 Retro Components & UI Elements
1. **Marquee Top Header**: Scrolling yellow/cyan text on red/purple background banner.
2. **Win98 Desktop Window Container**:
   - Header with Win98 titlebar, title text, and `[ _ ] [ █ ] [ X ]` window controls.
   - 3D Beveled borders (`border: 3px outset #ffffff` and `border: 2px inset #808080`).
3. **Sidebar Navigation (`NavigationHelper.java`)**:
   - Animated 3D Spinning Globe SVG (`globe.svg`).
   - Beveled navigation buttons with yellow highlight for active page.
   - Visitor Counter: Green LED on black box (`004291`).
   - WebRing Navigation Widget (`CyberRing #1337`).
   - 80x15 Nostalgic 90s Badges: "Best Viewed in Netscape 4.0", "IE 5.0", "Made with Notepad", "Y2K OK", "Valid HTML 4.01".
4. **Page Specific Features**:
   - **Home (`HomeHandler.java`)**: Flaming headline, animated Under Construction banner, News update panel, Golden Web award badge.
   - **About (`AboutHandler.java`)**: Win98 dialog styling, team profile card, CRT scanline overlay toggle/effect, Y2K readiness notice, legal disclaimer.
   - **Contact (`ContactHandler.java`)**: Beveled form inputs, glossy "Send E-Mail" submit button, POST response page formatted like a retro terminal transmission.
   - **Links (`LinksHandler.java`)**: Yahoo! 1999 style directory list with folder icons, retro search banner, web ring navigation.

## 3. Technical Implementation Plan

### 3.1 Backend Java Components
- `DemoWebServer.java`: Keep existing port & context routes (`/`, `/about`, `/contact`, `/links`, `/static/*`).
- `NavigationHelper.java`: Update HTML template for sidebar including WebRing and 80x15 badges.
- `HomeHandler.java`, `AboutHandler.java`, `ContactHandler.java`, `LinksHandler.java`: Update HTML structures to use Win98 window markup while keeping all page content and forms intact.
- `StaticAssetHandler.java`: Continue serving static assets with proper MIME types.

### 3.2 Frontend Assets (`/static/`)
- `style.css`: Comprehensive retro stylesheet implementing responsive Win98 window styles, 3D buttons, bevels, marquee, counter, marquee animations, and mobile layout rules.
- SVGs (`/static/images/`): Upgrade/add SVG assets for retro globe, mailbox, under construction banner, team photo, folder icons, netscape badge, IE badge, webring logo.

## 4. Accessibility & Mobile Responsiveness
- High text-to-background contrast (> 4.5:1 ratio for text content).
- Full keyboard focus indicators using bright neon outlines.
- Responsive CSS media queries `@media (max-width: 768px)` so table/window layouts collapse cleanly on mobile screens while maintaining 90s visual look.

## 5. Verification & Testing Criteria
1. Server compiles (`mvn test-compile` or `mvn compile`).
2. Server runs on `http://localhost:8080`.
3. All pages (`/`, `/about`, `/contact`, `/links`) render with 1999 Win98/GeoCities look.
4. Contact POST form submits correctly and renders response page.
5. Code maintains clean separation between Java backend handlers and CSS/SVG static assets.
