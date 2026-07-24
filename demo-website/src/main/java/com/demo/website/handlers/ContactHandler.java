package com.demo.website.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ContactHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String sidebar = NavigationHelper.renderSidebar("/contact");

        if ("POST".equalsIgnoreCase(method)) {
            // Read form data
            InputStream is = exchange.getRequestBody();
            String formData = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String responseHtml = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Message Received! - CyberSpace 1999</title>
                    <link rel="stylesheet" href="/static/style.css">
                </head>
                <body>
                    <a href="#main-content" class="sr-only">Skip to main content</a>
                    
                    <div class="banner-marquee" role="region" aria-label="Announcement Marquee">
                        <marquee behavior="scroll" direction="left">
                            *** ELECTRONIC MAIL TRANSMISSION RECEIVED! THANK YOU FOR CONTACTING US! ***
                        </marquee>
                    </div>
                    
                    <br>
                    
                    <div class="win98-window">
                        <div class="win98-titlebar"><span>✉️ CyberSpace 1999 - Electronic Mail &amp; Guestbook</span><div class="win98-controls" aria-hidden="true"><span>_</span><span>&#9633;</span><span>X</span></div></div>
                        
                        <table class="main-layout">
                            <tr>
                                """ + sidebar + """
                                <td class="content" id="main-content" tabindex="-1">
                                    <main>
                                        <h1 class="flaming-header">Electronic Mail Transmitted!</h1>
                                        
                                        <div role="status" aria-live="polite">
                                            <p>Thank you for reaching out! We received your data transmission:</p>
                                            <pre style="background: #000000; color: #00ff00; padding: 10px; font-family: 'Courier New', monospace; border: 2px inset #c0c0c0;">""" + formData + """
                                            </pre>
                                        </div>
                                        <br>
                                        <a href="/" class="info-link">&lt;&lt; Return to Home Base</a>
                                    </main>
                                </td>
                            </tr>
                        </table>
                    </div>
                </body>
                </html>
                """;

            byte[] bytes = responseHtml.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        // GET request - render contact form
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Contact Us - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <a href="#main-content" class="sr-only">Skip to main content</a>
                
                <div class="banner-marquee" role="region" aria-label="Announcement Marquee">
                    <marquee behavior="scroll" direction="left">
                        *** SEND US AN ELECTRONIC MAIL MESSAGE OR GUESTBOOK NOTE! ***
                    </marquee>
                </div>
                
                <br>
                
                <div class="win98-window">
                    <div class="win98-titlebar"><span>✉️ CyberSpace 1999 - Electronic Mail &amp; Guestbook</span><div class="win98-controls" aria-hidden="true"><span>_</span><span>&#9633;</span><span>X</span></div></div>
                    
                    <table class="main-layout">
                        <tr>
                            """ + sidebar + """
                            <td class="content" id="main-content" tabindex="-1">
                                <main>
                                    <h1 class="flaming-header">Electronic Mail Transmission Form</h1>
                                    
                                    <h2>Fill out all fields below:</h2>
                                    
                                    <form action="/contact" method="POST" id="contactForm" class="retro-form">
                                        <table border="1" cellpadding="5">
                                            <tr>
                                                <td><label for="username">Name:</label></td>
                                                <td>
                                                    <input type="text" id="username" name="username" placeholder="Your Cyber Alias" aria-required="true">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><label for="email">Email:</label></td>
                                                <td>
                                                    <input type="text" id="email" name="email" placeholder="alias@provider.com" aria-required="true">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><label for="comments">Comments:</label></td>
                                                <td>
                                                    <textarea id="comments" name="comments" rows="4" cols="30" placeholder="Type your message here..." aria-required="true"></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="center">
                                                    <input type="submit" id="submitBtn" value="Send E-Mail" class="submit-btn">
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                </main>
                            </td>
                        </tr>
                    </table>
                </div>
            </body>
            </html>
            """;

        byte[] responseBytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}

