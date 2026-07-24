package com.demo.website.handlers;

public class NavigationHelper {

    public static String renderSidebar(String activePath) {
        String homeActive = "/".equals(activePath) ? "style=\"background-color:#ffff00; color:#000000; font-weight:bold;\"" : "";
        String aboutActive = "/about".equals(activePath) ? "style=\"background-color:#ffff00; color:#000000; font-weight:bold;\"" : "";
        String contactActive = "/contact".equals(activePath) ? "style=\"background-color:#ffff00; color:#000000; font-weight:bold;\"" : "";
        String linksActive = "/links".equals(activePath) ? "style=\"background-color:#ffff00; color:#000000; font-weight:bold;\"" : "";

        StringBuilder sb = new StringBuilder();
        sb.append("<td class=\"sidebar\">\n");
        sb.append("    <center>\n");
        sb.append("        <img src=\"/static/images/globe.svg\" width=\"80\" height=\"80\" alt=\"CyberSpace Globe\">\n");
        sb.append("        <h3>NAVIGATE</h3>\n");
        sb.append("    </center>\n");
        sb.append("    <hr>\n");
        sb.append("    <a href=\"/\" class=\"nav-btn\" ").append(homeActive).append(">Home Base</a>\n");
        sb.append("    <a href=\"/about\" class=\"nav-btn\" ").append(aboutActive).append(">About Us</a>\n");
        sb.append("    <a href=\"/contact\" class=\"nav-btn\" ").append(contactActive).append(">Secret Portal</a>\n");
        sb.append("    <a href=\"/links\" class=\"nav-btn\" ").append(linksActive).append(">Cool Links</a>\n");
        sb.append("    <br><br>\n");
        sb.append("    <center>\n");
        sb.append("        <div class=\"counter-box\">004291</div>\n");
        sb.append("        <div style=\"font-size:10px; color:#ffffff;\">VISITORS SINCE JAN 1999</div>\n");
        sb.append("    </center>\n");
        sb.append("</td>\n");

        return sb.toString();
    }
}
