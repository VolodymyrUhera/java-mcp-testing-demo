# UX Journey Test Report

- **Journey:** Home -> About -> Home -> Contact -> Submit Form -> Screenshot
- **Total Duration:** 666 ms
- **Total Interactions:** 8
- **Failures:** 0

## Journey Steps Execution Log

| Step | Target / Artifact | Status | Latency (ms) |
|------|-------------------|--------|--------------|
| 1. Open Home Page | http://localhost:8080/ | Success | 103 |
| 2. Click About Us Link | http://localhost:8080/about | Success | 72 |
| 3. Return to Home Page | http://localhost:8080/ | Success | 75 |
| 4. Click Secret Portal (Contact) | http://localhost:8080/contact | Success | 115 |
| 5. Fill Contact Form Fields | http://localhost:8080/contact | Success | 116 |
| 6. Submit Form | http://localhost:8080/contact | Success | 94 |
| 7. Capture Proof Screenshot | reports/ux_journey_screenshot.png | Saved | 0 |

![UX Journey Proof Screenshot](ux_journey_screenshot.png)
