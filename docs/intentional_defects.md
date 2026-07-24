# Intentional UX & Accessibility Defects Specification

This document details every intentional UX and accessibility flaw engineered into `demo-website` for automated audit testing.

| Defect Category | Defect Type | Location | Architectural Reason / Impact | WCAG / UX Remedy |
|-----------------|-------------|----------|-------------------------------|------------------|
| Accessibility | `MISSING_ALT_TEXT` | Home `/`, About `/about` | Images lack `alt` tags (`<img src="...">`). Screen readers fail to describe visual context to visually impaired users. | Add descriptive `alt` attribute (e.g. `alt="Globe icon"`). |
| Accessibility | `MISSING_FORM_LABEL` | Contact `/contact` | Form fields `<input name="username">` lack `<label for="...">` or `aria-label`. Screen reader users do not know input purpose. | Wrap input in `<label>` or bind using `id` and `<label for="id">`. |
| Accessibility | `HEADING_HIERARCHY_SKIP` | Home `/`, About `/about` | Heading structures skip levels (jumps directly from `<h1>` to `<h4>` or `<h5>`). Breaks screen reader document outline navigation. | Maintain sequential structure: `<h1>` -> `<h2>` -> `<h3>`. |
| Accessibility | `POOR_COLOR_CONTRAST` | About `/about` | Low contrast text (`#a0a0a0` light gray on `#ffffff` white). Fails WCAG AA minimum 4.5:1 contrast threshold. | Increase contrast ratio to at least 4.5:1 (e.g., text color `#333333`). |
| UX / Mobile | `SMALL_CLICK_TARGET` | Home `/`, Contact `/contact` | Tiny click targets (7px links and 15px submit buttons). Causes misclicks and mobile touch navigation failures. | Ensure minimum interactive touch target of 24x24px (preferably 44x44px). |
| UX / Layout | `UNFORMATTED_WALL_OF_TEXT` | About `/about` | Long single paragraph block without visual hierarchy, line breaks, or callouts. Degrades readability and user focus. | Break content into short paragraphs (2-3 sentences), add bullet points and subheadings. |
| UX / Navigation | `CONFUSING_LINK_LABELS` | Sidebar Navigation | Vague link texts like "Click Here" or "Secret Portal". Users cannot predict destination before clicking. | Use clear, self-descriptive action labels (e.g. "View About Us", "Contact Support"). |
