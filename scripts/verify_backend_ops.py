import requests
import json
import time

BASE_URL = "http://localhost:8080/api"
AUTH_URL = "http://localhost:8080/api/auth"

session = requests.Session()
token = None

def print_result(test_name, success, message=""):
    status = "PASS" if success else "FAIL"
    print(f"{status} - {test_name}: {message}")

def run_test():
    global token
    timestamp = int(time.time())
    username = f"testuser_{timestamp}"
    email = f"test_{timestamp}@example.com"
    password = "password123"

    print(f"--- Starting Full System Verification for user: {username} ---")

    # 1. Register
    try:
        reg_payload = {
            "username": username,
            "email": email,
            "password": password,
            "fullName": "Test User",
            "role": ["user"]
        }
        res = session.post(f"{AUTH_URL}/signup", json=reg_payload)
        if res.status_code == 200:
            print_result("Registration", True)
        else:
            print_result("Registration", False, res.text)
            return
    except Exception as e:
        print_result("Registration", False, str(e))
        return

    # 2. Login
    try:
        login_payload = {
            "username": username,
            "password": password
        }
        res = session.post(f"{AUTH_URL}/signin", json=login_payload)
        if res.status_code == 200:
            data = res.json()
            token = data['token']
            print_result("Login", True)
        else:
            print_result("Login", False, res.text)
            return
    except Exception as e:
        print_result("Login", False, str(e))
        return

    headers = {"Authorization": f"Bearer {token}"}

    # 3. Create Account (Simulate Store)
    try:
        acc_payload = {"initialBalance": 5000.00}
        res = session.post(f"{BASE_URL}/accounts/create", json=acc_payload, headers=headers)
        if res.status_code == 200:
            print_result("Create Account", True)
        else:
            print_result("Create Account", False, res.text)
    except Exception as e:
        print_result("Create Account", False, str(e))

    # 4. Fetch Accounts (Simulate Retrieve)
    try:
        res = session.get(f"{BASE_URL}/accounts/my-accounts", headers=headers)
        if res.status_code == 200 and len(res.json()) > 0:
            acc = res.json()[0]
            print_result("Retrieve Account", True, f"Balance: {acc['balance']}")
        else:
            print_result("Retrieve Account", False, res.text)
    except Exception as e:
        print_result("Retrieve Account", False, str(e))

    # 5. Add Beneficiary (Store)
    try:
        ben_payload = {
            "name": "Friend One",
            "accountNumber": "9876543210",
            "bankName": "Other Bank",
            "ifscCode": "OTH001"
        }
        res = session.post(f"{BASE_URL}/beneficiaries", json=ben_payload, headers=headers)
        if res.status_code == 200:
            print_result("Add Beneficiary", True)
        else:
            print_result("Add Beneficiary", False, res.text)
    except Exception as e:
        print_result("Add Beneficiary", False, str(e))

    # 6. Get Beneficiaries (Retrieve)
    try:
        res = session.get(f"{BASE_URL}/beneficiaries", headers=headers)
        if res.status_code == 200:
            bens = res.json()
            found = any(b['name'] == "Friend One" for b in bens)
            if found:
                 print_result("Retrieve Beneficiary", True, "Data persisted in DB")
            else:
                 print_result("Retrieve Beneficiary", False, "Beneficiary not found")
        else:
            print_result("Retrieve Beneficiary", False, res.text)
    except Exception as e:
        print_result("Retrieve Beneficiary", False, str(e))

    # 7. Create Deposit (Store)
    try:
        dep_payload = {"type": "FD", "amount": 1000, "tenureMonths": 12}
        res = session.post(f"{BASE_URL}/deposits/create", json=dep_payload, headers=headers)
        if res.status_code == 200:
            print_result("Create Deposit", True)
        else:
            print_result("Create Deposit", False, res.text)
    except Exception as e:
        print_result("Create Deposit", False, str(e))

    # 8. Apply Loan (Store)
    try:
        loan_payload = {"type": "Personal", "amount": 50000}
        res = session.post(f"{BASE_URL}/loans/apply", json=loan_payload, headers=headers)
        if res.status_code == 200:
            print_result("Apply Loan", True)
        else:
            print_result("Apply Loan", False, res.text)
    except Exception as e:
        print_result("Apply Loan", False, str(e))

if __name__ == "__main__":
    run_test()
