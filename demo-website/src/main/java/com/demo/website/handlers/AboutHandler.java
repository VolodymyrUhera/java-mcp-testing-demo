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
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>About Us - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
            </head>
            <body>
                <a href="#main-content" class="sr-only">Skip to main content</a>
                
                <div class="banner-marquee" role="region" aria-label="Announcement Marquee">
                    <marquee behavior="scroll" direction="left">
                        *** LEARN MORE ABOUT THE TEAM BEHIND CYBERSPACE 1999 ***
                    </marquee>
                </div>
                
                <br>
                
                <div class="win98-window">
                    <div class="win98-titlebar"><span>ℹ️ CyberSpace 1999 - About Us (Properties)</span><div class="win98-controls" aria-hidden="true"><span>_</span><span>&#9633;</span><span>X</span></div></div>
                    
                    <table class="main-layout">
                        <tr>
                            """ + sidebar + """
                            <td class="content" id="main-content" tabindex="-1">
                                <main>
                                    <h1 class="flaming-header">About Our Cyber Endeavor</h1>
                                    
                                    <h2 class="flaming-header">Our Story &amp; Vision</h2>
                                    
                                    <p>
                                        Founded in December 1998, our cyber web studio began as a passion project to bring 
                                        interactive multimedia web documents to the emerging information superhighway. 
                                        We believe in using the power of HTML table architecture, Netscape extensions, 
                                        and dynamic JavaScript applets to craft truly unforgettable web pages that load in 
                                        under 45 seconds on standard 56k dial-up modems. Our team works tirelessly day and 
                                        night to craft custom graphics, animated icons, midis, and guestbook scripts.
                                    </p>
                                    
                                    <p>
                                        <img src="/static/images/y2k_badge.svg" width="80" height="15" alt="Y2K Ready Official Badge">
                                        <strong>Y2K Readiness Statement:</strong> As we approach the new millennium, Y2K readiness is our top priority, ensuring all 
                                        of our internal systems can seamlessly handle the transition from 1999 to 2000 
                                        without losing a single guestbook entry or visitor counter tally.
                                    </p>
                                    
                                    <center>
                                        <img src="/static/images/team_photo.svg" width="250" height="120" alt="CyberSpace 1999 Cyber Team Photo in VR Headsets">
                                    </center>
                                    <br>
                                    
                                    <div class="disclaimer-text" role="note">
                                        LEGAL DISCLAIMER: All content presented on this site is copyright 1999 CyberSpace Web Studio. 
                                        Unauthorized duplication or downloading of our animated GIFs is strictly prohibited under federal 
                                        copyright guidelines.
                                    </div>
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

