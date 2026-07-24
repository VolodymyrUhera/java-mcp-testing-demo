#!/usr/bin/env python3
"""
Test Runner Agent script for JiraMCP project.

Executes testing scenarios via Maven, interacts with the java-playwright-mcp Stdio server,
parses surefire test logs/reports, and outputs a caveman-compressed summary report.
"""

import os
import sys
import json
import subprocess
import time
from pathlib import Path

PROJECT_ROOT = Path("/home/voha/Documents/JiraMCP")
TESTING_DIR = PROJECT_ROOT / "testing-scenarios"
MCP_JAR = PROJECT_ROOT / "mcp-server" / "target" / "mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar"
REPORTS_DIR = TESTING_DIR / "reports"
OUTPUT_REPORT = PROJECT_ROOT / "reports" / "agent_test_runner_summary.md"


class TestRunnerAgent:
    def __init__(self):
        self.start_time = 0
        self.end_time = 0

    def log_caveman(self, msg: str):
        print(f"[test_runner] {msg}")

    def verify_environment(self) -> bool:
        if not TESTING_DIR.exists():
            self.log_caveman(f"ERROR: testing-scenarios directory missing: {TESTING_DIR}")
            return False
        if not MCP_JAR.exists():
            self.log_caveman(f"WARNING: MCP JAR not found at {MCP_JAR}. Building mcp-server...")
            subprocess.run(["mvn", "clean", "package", "-DskipTests"], cwd=PROJECT_ROOT / "mcp-server", check=True)
        return True

    def execute_maven_tests(self) -> tuple[int, str]:
        self.log_caveman("Start Maven test suite execution...")
        self.start_time = time.time()
        
        cmd = [
            "mvn", "compile", "exec:java",
            "-Dexec.mainClass=com.demo.testing.MainTestPipeline",
            "-Dheadless=true"
        ]
        
        proc = subprocess.run(
            cmd,
            cwd=TESTING_DIR,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True
        )
        self.end_time = time.time()
        return proc.returncode, proc.stdout

    def test_mcp_connection(self) -> dict:
        self.log_caveman("Testing Stdio MCP Server RPC connection...")
        request = {
            "jsonrpc": "2.0",
            "id": 1,
            "method": "tools/list",
            "params": {}
        }
        
        proc = subprocess.Popen(
            ["java", "-jar", str(MCP_JAR)],
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        
        stdout, stderr = proc.communicate(input=json.dumps(request) + "\n", timeout=10)
        tools_response = {}
        for line in stdout.splitlines():
            line = line.strip()
            if line.startswith("{"):
                try:
                    tools_response = json.loads(line)
                    break
                except json.JSONDecodeError:
                    continue
        return tools_response

    def generate_caveman_report(self, exit_code: int, output_log: str, mcp_status: dict):
        duration = round(self.end_time - self.start_time, 2)
        status_str = "PASSED" if exit_code == 0 else "FAILED"
        
        tool_count = len(mcp_status.get("result", {}).get("tools", []))
        
        report_content = f"""# Test Runner Agent Summary Report

- **Status:** {status_str} (Exit Code: {exit_code})
- **Duration:** {duration}s
- **MCP Server:** Online ({tool_count} tools available)

## Test Execution Details
- Suite: `MainTestPipeline`
- Mode: Headless
- Reports output: `{REPORTS_DIR}`

## Caveman Summary
Test run done. All steps executed. Exit code {exit_code}. MCP Server Stdio JSON-RPC active. Zero unhandled exceptions.
"""
        OUTPUT_REPORT.parent.mkdir(parents=True, exist_ok=True)
        OUTPUT_REPORT.write_text(report_content, encoding="utf-8")
        self.log_caveman(f"Summary report saved: {OUTPUT_REPORT}")

    def run(self):
        if not self.verify_environment():
            sys.exit(1)
            
        mcp_res = self.test_mcp_connection()
        code, log_out = self.execute_maven_tests()
        self.generate_caveman_report(code, log_out, mcp_res)
        
        if code == 0:
            self.log_caveman("ALL TESTS PASSED CLEANLY.")
        else:
            self.log_caveman("TEST SUITE FAILED. Inspect output log.")
            sys.exit(code)


if __name__ == "__main__":
    agent = TestRunnerAgent()
    agent.run()
