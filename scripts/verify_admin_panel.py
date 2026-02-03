import requests
import json
import time

BASE_URL = "http://localhost:8080/api"
AUTH_URL = "http://localhost:8080/api/auth"

def run_test():
    print("--- Starting Admin Panel Verification ---")

    # 1. Login as Admin
    # Note: Assuming 'admin' user exists or we use the test user if they have admin role.
    # If no admin user, this might fail. But usually system has default admin or we register one.
    # For now, let's try to register a new admin if possible, or use a known one.
    # Since I don't have the explicit admin credentials from recent creation, I will try a default 'admin' / 'admin123' 
    # OR register a new user and try to hit admin endpoints (expecting 403 if not admin).

    # Actually, let's just try to login as the test user from before. 
    # If they are not admin, we might need to manually set role in DB or assume there is an admin.
    # Let's try to register a user with role 'admin' directly.
    
    timestamp = int(time.time())
    admin_username = f"admin_{timestamp}"
    admin_password = "password123"
    
    try:
        reg_payload = {
            "username": admin_username,
            "email": f"admin_{timestamp}@example.com",
            "password": admin_password,
            "fullName": "Admin User",
            "role": ["item", "admin"] # Passing 'admin' role if allowed by SignUpRequest
        }
        # Note: 'item' is likely wrong for role, usually it's ["admin"]. 
        # But SignUpRequest usually takes a set of strings. 
        # Let's try sending ["admin"]
        reg_payload["role"] = ["admin"]
        
        session = requests.Session()
        res = session.post(f"{AUTH_URL}/signup", json=reg_payload)
        
        if res.status_code != 200:
             print(f"WARN: Could not register new admin. Trying login with default 'admin'. Status: {res.status_code}")
             # Try default admin
             login_payload = {"username": "admin", "password": "password"} # Guessing default
        else:
             print("Registered new admin successfully.")
             login_payload = {"username": admin_username, "password": admin_password}

        # 2. Login
        res = session.post(f"{AUTH_URL}/signin", json=login_payload)
        if res.status_code == 200:
            token = res.json()['token']
            print("PASS - Admin Login")
        else:
            print(f"FAIL - Admin Login: {res.text}")
            return

        headers = {"Authorization": f"Bearer {token}"}

        # 3. Verify Dashboard Stats
        res = session.get(f"http://localhost:8080/api/admin/dashboard-stats", headers=headers)
        if res.status_code == 200:
            stats = res.json()
            print(f"PASS - Dashboard Stats: Users={stats['totalUsers']}, Accounts={stats['totalAccounts']}")
        else:
             print(f"FAIL - Dashboard Stats: {res.status_code} {res.text}")

        # 4. Verify Users List
        res = session.get(f"http://localhost:8080/api/admin/users", headers=headers)
        if res.status_code == 200:
            users = res.json()
            print(f"PASS - Users List: Parsed {len(users)} users")
        else:
             print(f"FAIL - Users List: {res.status_code}")

        # 5. Verify Transactions List
        res = session.get(f"http://localhost:8080/api/admin/transactions", headers=headers)
        if res.status_code == 200:
            txns = res.json()
            print(f"PASS - Transactions List: Parsed {len(txns)} transactions")
        else:
             print(f"FAIL - Transactions List: {res.status_code}")

    except Exception as e:
        print(f"ERROR: {str(e)}")

if __name__ == "__main__":
    run_test()
