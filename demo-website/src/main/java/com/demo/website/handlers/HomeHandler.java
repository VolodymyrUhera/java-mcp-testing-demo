package com.demo.website.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sidebar = NavigationHelper.renderSidebar("/");

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Welcome to CyberSpace 1999 Ultimate Web Home!</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <div class="banner-marquee">
                    <marquee behavior="scroll" direction="left">
                        *** WELCOME TO MY CYBERSPACE HOME PAGE! BEST VIEWED IN NETSCAPE NAVIGATOR 4.0 AT 800x600 RESOLUTION! ***
                    </marquee>
                </div>
                
                <br>
                
                <table class="main-layout">
                    <tr>
                        """ + sidebar + """
                        <!-- Main Content Area -->
                        <td class="content">
                            <!-- Heading Hierarchy: H1 -->
                            <h1>Welcome to CyberSpace 1999!</h1>
                            
                            <!-- Heading Hierarchy: H1 -> H2 -->
                            <h2>Latest News &amp; Updates (Updated July 1999)</h2>
                            
                            <p>
                                Thank you for visiting my personal corner of the World Wide Web! This site is currently 
                                under heavy construction as I learn HTML and Web Design.
                            </p>
                            
                            <!-- Under construction banner -->
                            <center>
                                <img src="/static/images/under_construction.svg" width="250" height="40" alt="Under Construction Banner">
                            </center>
                            
                            <h2>Featured Highlights</h2>
                            <p>
                                Check out our awesome guestbook or send us an electronic mail via our form!
                            </p>
                            
                            <!-- Fake Awards Section -->
                            <div class="award-badge">
                                🏆 WINNER OF THE 1999 GOLDEN WEB AWARD 🏆
                            </div>
                            
                            <p>
                                Need more info? <a href="/about" class="info-link">Click here for more information</a>
                            </p>
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
