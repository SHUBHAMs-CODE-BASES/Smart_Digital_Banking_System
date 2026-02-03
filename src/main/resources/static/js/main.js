const API_URL = 'http://localhost:8080/api';

function getToken() {
    return localStorage.getItem('token');
}

function isLoggedIn() {
    return !!getToken();
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

function getAuthHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getToken()
    };
}

async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method: method,
        headers: getAuthHeaders()
    };
    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(API_URL + endpoint, options);
        if (response.status === 401) {
            logout();
            return null;
        }
        return response;
    } catch (error) {
        console.error('API Error:', error);
        return null;
    }
}

function showMessage(elementId, message, type) {
    const el = document.getElementById(elementId);
    if(el) {
        el.textContent = message;
        el.className = 'alert alert-' + type;
        el.style.display = 'block';
    }
}
