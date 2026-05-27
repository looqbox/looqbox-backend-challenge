#!/usr/bin/env bash
set -euo pipefail

# Runs Gradle tests inside a Java 21 container (no local JDK required).
# Opens the HTML test report in the default browser when tests finish.
# Usage: ./run-tests.sh

export MSYS_NO_PATHCONV=1

docker run --rm -v "$(pwd):/app" -w /app eclipse-temurin:21-jdk bash -lc "chmod +x gradlew && ./gradlew test --console=plain"
exit_code=$?

report="build/reports/tests/test/index.html"
open_test_report() {
  if [[ ! -f "$report" ]]; then
    echo "Test report not found: $report" >&2
    return
  fi
  if [[ -n "${CI:-}" ]]; then
    echo "Test report: $(pwd)/$report"
    return
  fi
  case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*)
      win_pwd="$(pwd -W 2>/dev/null || pwd)"
      cmd.exe /c start "" "${win_pwd}\\build\\reports\\tests\\test\\index.html"
      ;;
    Darwin)
      open "$report"
      ;;
    *)
      xdg-open "$report" 2>/dev/null || echo "Open manually: $(pwd)/$report"
      ;;
  esac
}

open_test_report
exit "$exit_code"
