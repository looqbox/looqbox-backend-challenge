param()

# Runs Gradle tests inside a Java 21 container (no local JDK required).
# Opens the HTML test report in the default browser when tests finish.
# Usage (PowerShell): .\run-tests.ps1

$ErrorActionPreference = "Stop"

docker run --rm -v "${PWD}:/app" -w /app eclipse-temurin:21-jdk bash -lc "chmod +x gradlew && ./gradlew test --console=plain"
$exitCode = $LASTEXITCODE

$report = Join-Path $PWD "build\reports\tests\test\index.html"
if (Test-Path $report) {
    if (-not $env:CI) {
        Start-Process $report
    } else {
        Write-Host "Test report: $report"
    }
} else {
    Write-Warning "Test report not found at $report"
}

exit $exitCode
