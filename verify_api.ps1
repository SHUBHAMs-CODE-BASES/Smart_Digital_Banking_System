$baseUrl = "http://localhost:8080/api"

function Login($username, $password) {
    $body = @{
        username = $username
        password = $password
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/auth/signin" -Method Post -Body $body -ContentType "application/json"
        return $response.token
    } catch {
        Write-Host "Login failed for $username : $($_.Exception.Message)"
        return $null
    }
}

function Get-Balance($token, $accountNum) {
    $headers = @{ Authorization = "Bearer $token" }
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/accounts/$accountNum" -Method Get -Headers $headers
        return $response.balance
    } catch {
        Write-Host "Get-Balance failed via Account Endpoint. Trying History..."
        # Fallback or error
        return 0
    }
}

function Transfer($token, $from, $to, $amount) {
    $headers = @{ Authorization = "Bearer $token" }
    $body = @{
        sourceAccountNumber = $from
        targetAccountNumber = $to
        amount = $amount
        type = "TRANSFER"
        description = "Test Transfer"
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/transactions/process" -Method Post -Body $body -ContentType "application/json" -Headers $headers
        Write-Host "Transfer Success: $($response.id)"
        return $true
    } catch {
        Write-Host "Transfer Failed: $($_.Exception.Message)"
        Write-Host $_.ErrorDetails.Message
        return $false
    }
}

function Update-Profile($token, $name) {
    $headers = @{ Authorization = "Bearer $token" }
    $body = @{
        fullName = $name
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/user/profile" -Method Put -Body $body -ContentType "application/json" -Headers $headers
        return $response
    } catch {
        Write-Host "Update Profile Failed: $($_.Exception.Message)"
        return $null
    }
}

# 1. Login
Write-Host "Logging in..."
$adminToken = Login "admin" "admin123"
$userToken = Login "user1" "user123"

if (-not $adminToken -or -not $userToken) {
    Write-Host "Tokens missing. Aborting."
    exit
}

# 2. Check Initial Balances
Write-Host "`nChecking Initial Balances..."
$adminBal = Get-Balance $adminToken "1000001234"
$userBal = Get-Balance $userToken "1000005678"
Write-Host "Admin Balance: $adminBal"
Write-Host "User1 Balance: $userBal"

# 3. Transfer 1000 Admin -> User1
Write-Host "`nPerforming Transfer 1000.00..."
Transfer $adminToken "1000001234" "1000005678" 1000.00

# 4. Check Final Balances
Write-Host "`nChecking Final Balances..."
$adminBalFinal = Get-Balance $adminToken "1000001234"
$userBalFinal = Get-Balance $userToken "1000005678"
Write-Host "Admin Balance: $adminBalFinal"
Write-Host "User1 Balance: $userBalFinal"

if ($adminBalFinal -lt $adminBal -and $userBalFinal -gt $userBal) {
    Write-Host "SUCCESS: Transfer verified."
} else {
    Write-Host "FAILURE: Transfer balances incorrect."
}

# 5. Connect Profile
Write-Host "`nUpdating Profile for User1..."
$updatedUser = Update-Profile $userToken "Test User Updated"
if ($updatedUser.fullName -eq "Test User Updated") {
    Write-Host "SUCCESS: Profile name updated to 'Test User Updated'"
} else {
    Write-Host "FAILURE: Profile update mismatch."
}
