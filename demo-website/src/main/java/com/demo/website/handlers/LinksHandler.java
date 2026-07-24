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
            <html>
            <head>
                <title>Cool Links &amp; Cyber Directory - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <div class="banner-marquee">
                    <marquee behavior="scroll" direction="left">
                        *** EXPLORE THE BEST WEBSITES ON THE INFORMATION SUPERHIGHWAY! ***
                    </marquee>
                </div>
                <br>
                <table class="main-layout">
                    <tr>
                        """ + sidebar + """
                        <td class="content">
                            <h1>🌐 CyberSpace 1999 Web Directory &amp; Cool Links</h1>
                            
                            <p>
                                Welcome to our hand-curated directory of awesome Web sites, search engines, 
                                and cyber resources on the Internet!
                            </p>
                            
                            <h4>🔍 Search Engines &amp; Portals</h4>
                            <ul>
                                <li><a href="https://archive.org" target="_blank">Internet Archive Wayback Machine</a> - Travel back to the 90s Web!</li>
                                <li><a href="https://www.geocities.ws" target="_blank">GeoCities Archive</a> - Classic homepages and GIFs.</li>
                            </ul>
                            
                            <h4>💾 Technology &amp; Standards</h4>
                            <ul>
                                <li><a href="https://www.w3.org" target="_blank">W3C (World Wide Web Consortium)</a> - Official HTML &amp; CSS Standards.</li>
                                <li><a href="https://openjdk.org" target="_blank">OpenJDK Project</a> - Java 21 Open Source JDK.</li>
                                <li><a href="https://slashdot.org" target="_blank">Slashdot</a> - News for Nerds, Stuff that Matters.</li>
                            </ul>
                            
                            <h4>🎨 Graphics &amp; Retro Art</h4>
                            <ul>
                                <li><a href="https://www.w3.org/History.html" target="_blank">World Wide Web History</a> - Tim Berners-Lee's original web history.</li>
                                <li><a href="https://www.w3.org/Style/CSS/specs" target="_blank">CSS Specifications</a> - Cascading Style Sheets standard.</li>
                            </ul>

                            <br>
                            <center>
                                <div class="award-badge">
                                    ⭐ FEATURED ON THE 1999 COOL SITE OF THE DAY ⭐
                                </div>
                            </center>
                        </td>
                    </tr>
                </table>
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
