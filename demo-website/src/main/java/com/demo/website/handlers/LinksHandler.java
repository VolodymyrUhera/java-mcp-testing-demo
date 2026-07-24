package com.demo.website.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class LinksHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sidebar = NavigationHelper.renderSidebar("/links");

        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Cool Links &amp; Cyber Directory - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <a href="#main-content" class="sr-only">Skip to main content</a>

                <div class="banner-marquee" role="region" aria-label="Announcement Marquee">
                    <marquee behavior="scroll" direction="left">
                        *** EXPLORE THE BEST WEBSITES ON THE INFORMATION SUPERHIGHWAY! ***
                    </marquee>
                </div>

                <br>

                <div class="win98-window">
                    <div class="win98-titlebar"><span>🌐 CyberSpace 1999 - Cool Links &amp; Cyber Directory</span><div class="win98-controls" aria-hidden="true"><span>_</span><span>&#9633;</span><span>X</span></div></div>
                    
                    <table class="main-layout">
                        <tr>
                            """ + sidebar + """
                            <td class="content" id="main-content" tabindex="-1">
                                <main>
                                    <h1 class="flaming-header">🌐 CyberSpace 1999 Web Directory &amp; Cool Links</h1>
                                    
                                    <p>
                                        Welcome to our hand-curated directory of awesome Web sites, search engines, 
                                        and cyber resources on the Internet!
                                    </p>
                                    
                                    <h2 class="flaming-header">🔍 Search Engines &amp; Portals</h2>
                                    <ul>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://archive.org" target="_blank" rel="noopener noreferrer">Internet Archive Wayback Machine</a> - Travel back to the 90s Web!</li>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://www.geocities.ws" target="_blank" rel="noopener noreferrer">GeoCities Archive</a> - Classic homepages and GIFs.</li>
                                    </ul>
                                    
                                    <h2 class="flaming-header">💾 Technology &amp; Standards</h2>
                                    <ul>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://www.w3.org" target="_blank" rel="noopener noreferrer">W3C (World Wide Web Consortium)</a> - Official HTML &amp; CSS Standards.</li>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://openjdk.org" target="_blank" rel="noopener noreferrer">OpenJDK Project</a> - Java 21 Open Source JDK.</li>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://slashdot.org" target="_blank" rel="noopener noreferrer">Slashdot</a> - News for Nerds, Stuff that Matters.</li>
                                    </ul>
                                    
                                    <h2 class="flaming-header">🎨 Graphics &amp; Retro Art</h2>
                                    <ul>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://www.w3.org/History.html" target="_blank" rel="noopener noreferrer">World Wide Web History</a> - Tim Berners-Lee's original web history.</li>
                                        <li><img src="/static/images/folder.svg" width="16" height="16" alt="Folder Icon"> <a href="https://www.w3.org/Style/CSS/specs" target="_blank" rel="noopener noreferrer">CSS Specifications</a> - Cascading Style Sheets standard.</li>
                                    </ul>

                                    <br>
                                    <center>
                                        <div class="award-badge" role="region" aria-label="Directory Awards">⭐ FEATURED ON THE 1999 COOL SITE OF THE DAY ⭐</div>
                                    </center>
                                </main>
                            </td>
                        </tr>
                    </table>
                </div>
            </body>
            </html>
            """;

        byte[] responseBytes = html.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
