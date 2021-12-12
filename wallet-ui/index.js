const CREATE_ACCOUNT_URL = "http://localhost:5050/user-account"
const USER_ACCOUNT_LOGIN_URL="http://localhost:5050/user-account/login"

function signIn(ele){
    document.getElementById("signInBackground").style.display="block";
}

function closePage(){
    document.getElementById("signInBackground").style.display="none";
}

function login(){
    const data={
        "ssn":document.getElementById("sign-in-ssn").value,
        "password": document.getElementById("sign-in-password").value
    }
    postData(USER_ACCOUNT_LOGIN_URL, data)
    .then(data => data.json())
    .then(data => {
        if ('ssn' in data) {
            document.cookie=JSON.stringify(data)
            window.location.href = 'http://127.0.0.1:5500/menu.html';
        } else {
            alert("Incorrect Id or password");
        }
    })
}

function createAccount() {
    const data = {
        "ssn": document.getElementById("ssn").value,
        "firstname": document.getElementById("firstname").value,
        "lastname": document.getElementById("lastname").value,
        "phoneno": document.getElementById("tel").value,
        "email": document.getElementById("email").value,
        "balance": 0.00,
        "bankId": document.getElementById("branch").value,
        "baNumber": document.getElementById("accountnumber").value,
        "bank_balance":document.getElementById("bankname").value,
        "password": document.getElementById("password").value
    }
    postData(CREATE_ACCOUNT_URL, data)
    .then(data => {
        if (data.ok) {
            alert('successfully created account')
        } else {
            alert('oops something went wrong...', data.json())
        }
    })
}

async function postData(url, data) {
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        body: JSON.stringify(data)

    })
    return response
}