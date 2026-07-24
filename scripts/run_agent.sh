#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "==> Running Test Runner Sub-Agent..."
chmod +x "$SCRIPT_DIR/test_runner_agent.py"
python3 "$SCRIPT_DIR/test_runner_agent.py" "$@"
