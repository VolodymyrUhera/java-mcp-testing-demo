# UX Journey Test Report

- **Journey:** Home -> About -> Home -> Contact -> Submit Form -> Screenshot
- **Total Duration:** 6883 ms
- **Total Interactions:** 8
- **Failures:** 0

## Journey Steps Execution Log

| Step | Target / Artifact | Status | Latency (ms) |
|------|-------------------|--------|--------------|
| 1. Open Home Page | http://localhost:8080/ | Success | 870 |
| 2. Click About Us Link | http://localhost:8080/about | Success | 868 |
| 3. Return to Home Page | http://localhost:8080/ | Success | 873 |
| 4. Click Secret Portal (Contact) | http://localhost:8080/contact | Success | 869 |
| 5. Fill Contact Form Fields | http://localhost:8080/contact | Success | 2442 |
| 6. Submit Form | http://localhost:8080/contact | Success | 851 |
| 7. Capture Proof Screenshot | reports/ux_journey_screenshot.png | Saved | 0 |

![UX Journey Proof Screenshot](ux_journey_screenshot.png)
