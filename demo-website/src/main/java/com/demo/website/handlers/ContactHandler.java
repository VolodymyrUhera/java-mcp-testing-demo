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
                <html>
                <head>
                    <title>Message Received! - CyberSpace 1999</title>
                    <link rel="stylesheet" href="/static/style.css">
                </head>
                <body>
                    <table class="main-layout">
                        <tr>
                            """ + sidebar + """
                            <td class="content">
                                <h1>Electronic Mail Sent!</h1>
                                <p>Thank you for reaching out! We received your data transmission:</p>
                                <pre style="background:#eee; padding:10px;">""" + formData + """
                                </pre>
                                <a href="/">Return to Home Base</a>
                            </td>
                        </tr>
                    </table>
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
            <html>
            <head>
                <title>Contact Us - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <div class="banner-marquee">
                    <marquee behavior="scroll" direction="left">
                        *** SEND US AN ELECTRONIC MAIL MESSAGE OR GUESTBOOK NOTE! ***
                    </marquee>
                </div>
                <br>
                <table class="main-layout">
                    <tr>
                        """ + sidebar + """
                        <td class="content">
                            <h1>Electronic Mail Transmission Form</h1>
                            
                            <h4>Fill out all fields below:</h4>
                            
                            <form action="/contact" method="POST" id="contactForm">
                                <table border="1" cellpadding="5">
                                    <tr>
                                        <td>Name:</td>
                                        <td>
                                            <!-- Intentional Accessibility Defect: Missing <label> element and id/name mapping -->
                                            <input type="text" name="username" placeholder="Your Cyber Alias">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Email:</td>
                                        <td>
                                            <!-- Intentional Accessibility Defect: Missing <label> element -->
                                            <input type="text" name="email" placeholder="alias@provider.com">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Comments:</td>
                                        <td>
                                            <!-- Intentional Accessibility Defect: Missing <label> element -->
                                            <textarea name="comments" rows="4" cols="30"></textarea>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" align="center">
                                            <!-- Intentional UX Defect: Extremely tiny submit button target (15px) -->
                                            <input type="submit" id="submitBtn" value="Send E-Mail" class="tiny-submit-btn">
                                            <br>
                                            <span style="font-size:8px;">(Click tiny button above to submit)</span>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </tr>
                </table>
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
