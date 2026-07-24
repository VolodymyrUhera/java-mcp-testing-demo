package com.demo.website.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AboutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String sidebar = NavigationHelper.renderSidebar("/about");

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>About Us - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <div class="banner-marquee">
                    <marquee behavior="scroll" direction="left">
                        *** LEARN MORE ABOUT THE TEAM BEHIND CYBERSPACE 1999 ***
                    </marquee>
                </div>
                <br>
                <table class="main-layout">
                    <tr>
                        """ + sidebar + """
                        <td class="content">
                            <h1>About Our Cyber Endeavor</h1>
                            
                            <!-- Intentional Accessibility Defect: Heading jump H1 to H5 -->
                            <h5>Our Story &amp; Vision</h5>
                            
                            <!-- Intentional UX Defect: Unformatted long wall of text without structure -->
                            <p>
                                Founded in December 1998, our cyber web studio began as a passion project to bring 
                                interactive multimedia web documents to the emerging information superhighway. 
                                We believe in using the power of HTML table architecture, Netscape extensions, 
                                and dynamic JavaScript applets to craft truly unforgettable web pages that load in 
                                under 45 seconds on standard 56k dial-up modems. Our team works tirelessly day and 
                                night to craft custom graphics, animated icons, midis, and guestbook scripts. 
                                As we approach the new millennium, Y2K readiness is our top priority, ensuring all 
                                of our internal systems can seamlessly handle the transition from 1999 to 2000 
                                without losing a single guestbook entry or visitor counter tally.
                            </p>
                            
                            <!-- Intentional Accessibility Defect: Poor contrast text (Light Gray on White background) -->
                            <div class="low-contrast-text">
                                LEGAL DISCLAIMER: All content presented on this site is copyright 1999 CyberSpace Web Studio. 
                                Unauthorized duplication or downloading of our animated GIFs is strictly prohibited under federal 
                                copyright guidelines. This text is intentionally rendered in low contrast for accessibility testing.
                            </div>
                            
                            <br>
                            <!-- Team Photo -->
                            <center>
                                <img src="/static/images/team_photo.svg" width="250" height="120" alt="CyberSpace 1999 Team Photo">
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
