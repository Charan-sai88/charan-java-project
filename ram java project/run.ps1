param(
    [string]$Password = $env:DB_PASSWORD,
    [string]$Username = "root",
    [string]$Port = "8080"
)

if (-not $Password) {
    $Password = Read-Host -Prompt "Enter MySQL root password (press Enter if empty)"
}

$env:DB_USERNAME = $Username
$env:DB_PASSWORD = $Password

Write-Host "Starting Product & Billing Application on http://localhost:$Port..." -ForegroundColor Green
java -jar "target\product-billing-0.0.1-SNAPSHOT.jar" --server.port=$Port
