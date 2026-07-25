package com.demo.website.handlers;

public class NavigationHelper {

    public static String renderSidebar(String activePath) {
        boolean isHome = "/".equals(activePath);
        boolean isAbout = "/about".equals(activePath);
        boolean isContact = "/contact".equals(activePath);
        boolean isLinks = "/links".equals(activePath);
        boolean isChat = "/chat".equals(activePath);

        String homeClass = "nav-btn" + (isHome ? " active" : "");
        String homeAria = isHome ? " aria-current=\"page\"" : "";

        String aboutClass = "nav-btn" + (isAbout ? " active" : "");
        String aboutAria = isAbout ? " aria-current=\"page\"" : "";

        String contactClass = "nav-btn" + (isContact ? " active" : "");
        String contactAria = isContact ? " aria-current=\"page\"" : "";

        String linksClass = "nav-btn" + (isLinks ? " active" : "");
        String linksAria = isLinks ? " aria-current=\"page\"" : "";

        String chatClass = "nav-btn" + (isChat ? " active" : "");
        String chatAria = isChat ? " aria-current=\"page\"" : "";

        StringBuilder sb = new StringBuilder();
        sb.append("<td class=\"sidebar\" role=\"region\" aria-label=\"Sidebar Navigation\">\n");
        sb.append("    <nav aria-label=\"Main Navigation\">\n");
        sb.append("        <center>\n");
        sb.append("            <img src=\"/static/images/globe.svg\" width=\"80\" height=\"80\" alt=\"CyberSpace 1999 Globe Logo\">\n");
        sb.append("            <h3>NAVIGATE</h3>\n");
        sb.append("        </center>\n");
        sb.append("        <hr>\n");
        sb.append("        <a href=\"/\" class=\"").append(homeClass).append("\"").append(homeAria).append(">Home Base</a>\n");
        sb.append("        <a href=\"/about\" class=\"").append(aboutClass).append("\"").append(aboutAria).append(">About Us</a>\n");
        sb.append("        <a href=\"/contact\" class=\"").append(contactClass).append("\"").append(contactAria).append(">Secret Portal</a>\n");
        sb.append("        <a href=\"/links\" class=\"").append(linksClass).append("\"").append(linksAria).append(">Cool Links</a>\n");
        sb.append("        <a href=\"/chat\" class=\"").append(chatClass).append("\"").append(chatAria).append(">Cyber Chat 90s</a>\n");
        sb.append("        <br><br>\n");
        sb.append("        <center>\n");
        sb.append("            <div class=\"counter-box\" aria-label=\"Visitor Counter: 004291\">004291</div>\n");
        sb.append("            <div style=\"font-size:10px; color:#ffffff;\">VISITORS SINCE JAN 1999</div>\n");
        sb.append("        </center>\n");
        sb.append("        <div class=\"webring-box\">\n");
        sb.append("            <div>CyberSpace Ring #1337</div>\n");
        sb.append("            <a href=\"#\" aria-label=\"Previous WebRing site\">&lt; Prev</a> | \n");
        sb.append("            <a href=\"#\" aria-label=\"Random WebRing site\">Random</a> | \n");
        sb.append("            <a href=\"#\" aria-label=\"Next WebRing site\">Next &gt;</a>\n");
        sb.append("        </div>\n");
        sb.append("        <div class=\"badge-row\" style=\"margin-top: 15px; text-align: center;\">\n");
        sb.append("            <img src=\"/static/images/netscape_badge.svg\" width=\"80\" height=\"15\" alt=\"Netscape Navigator 4.0 Badge\">\n");
        sb.append("            <img src=\"/static/images/ie_badge.svg\" width=\"80\" height=\"15\" alt=\"Internet Explorer 5.0 Badge\">\n");
        sb.append("            <img src=\"/static/images/y2k_badge.svg\" width=\"80\" height=\"15\" alt=\"Y2K Ready Badge\">\n");
        sb.append("        </div>\n");
        sb.append("    </nav>\n");
        sb.append("</td>\n");

        return sb.toString();
    }
}

