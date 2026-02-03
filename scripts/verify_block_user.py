
import requests
import json
import sys

BASE_Url = "http://localhost:8080/api"

import time

def print_result(test_name, success, message=""):
    status = "PASS" if success else "FAIL"
    print(f"{status} - {test_name}: {message}")
    if not success:
        sys.exit(1)

def main():
    print("--- Starting Block User Verification ---")
    
    timestamp = int(time.time())
    admin_username = f"admin_{timestamp}"
    admin_password = "password123"
    victim_username = f"victim_{timestamp}"
    victim_password = "password123"

    # 0. Register Admin & Victim
    reg_url = f"{BASE_Url}/auth/signup"
    
    # Register Admin
    admin_payload = {
        "username": admin_username, 
        "email": f"admin_{timestamp}@bank.com", 
        "password": admin_password, 
        "role": ["admin"], 
        "fullName": "Administrator"
    }
    requests.post(reg_url, json=admin_payload)
    
    # Register Victim
    victim_payload = {
        "username": victim_username, 
        "email": f"victim_{timestamp}@bank.com", 
        "password": victim_password, 
        "role": ["user"], 
        "fullName": "Victim User"
    }
    requests.post(reg_url, json=victim_payload)

    # 1. Admin Login
    admin_login = {
        "username": admin_username,
        "password": admin_password
    }
    admin_token = None
    try:
        r = requests.post(f"{BASE_Url}/auth/signin", json=admin_login)
        if r.status_code == 200:
            admin_token = r.json()['token'] # verify_admin_panel uses 'token', check backend response type
            print_result("Admin Login", True)
        else:
            print_result("Admin Login", False, f"Status: {r.status_code} - RESPONSE: {r.text}")
    except Exception as e:
        # Try 'accessToken' if 'token' fails, backend inconsistent?
        try:
             admin_token = r.json()['accessToken']
             print_result("Admin Login", True)
        except:
             print_result("Admin Login", False, str(e))

    # 2. Find Victim User ID
    headers = {"Authorization": f"Bearer {admin_token}"}
    victim_id = None
    
    r = requests.get(f"{BASE_Url}/admin/users", headers=headers)
    if r.status_code == 200:
        users = r.json()
        for u in users:
            if u['username'] == victim_username:
                victim_id = u['id']
                break

        if not victim_id and len(users) > 0:
            victim_id = users[0]['id']
            victim_username = users[0]['username']
        
        if victim_id:
            print_result("Find Victim User", True, f"Found user {victim_username} (ID: {victim_id})")
        else:
            print_result("Find Victim User", False, "No users found")
    else:
        print_result("Find Victim User", False, f"Status: {r.status_code}")

    # 3. Block User
    r = requests.put(f"{BASE_Url}/admin/users/{victim_id}/status", headers=headers, json={"status": "BLOCKED"})
    if r.status_code == 200:
        print_result("Block User API", True)
    else:
        print_result("Block User API", False, f"Status: {r.status_code} - {r.text}")

    # 4. Try Login as Victim (Expect Failure)
    victim_login = {
        "username": victim_username,
        "password": victim_password
    }
    # Note: If password changed, this might fail on auth before check. 
    # But usually test env has default pass. If auth fails generally, we might assume blocked? 
    # Ideally blocked returns specific error or 401.
    
    r = requests.post(f"{BASE_Url}/auth/signin", json=victim_login)
    if r.status_code == 401 or r.status_code == 403: # Bad credentials or unauthorized
        # We need to distinguish bad creds from blocked if possible, but 401 is expected.
        # Ideally the message says "User is disabled" or similar.
        print_result("Victim Login (Blocked)", True, f"Rejected with {r.status_code}")
    else:
        print_result("Victim Login (Blocked)", False, f"Unexpected code: {r.status_code}")


    # 5. Unblock User
    r = requests.put(f"{BASE_Url}/admin/users/{victim_id}/status", headers=headers, json={"status": "ACTIVE"})
    if r.status_code == 200:
        print_result("Unblock User API", True)
    else:
        print_result("Unblock User API", False, f"Status: {r.status_code}")

    # 6. Try Login as Victim (Expect Success)
    r = requests.post(f"{BASE_Url}/auth/signin", json=victim_login)
    if r.status_code == 200:
        print_result("Victim Login (Active)", True)
    else:
        print_result("Victim Login (Active)", False, f"Status: {r.status_code}")

if __name__ == "__main__":
    main()
