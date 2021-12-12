const GENERATE_OTP_URL="http://localhost:5050/electronic-address/generate-otp"
const VERIFY_OTP_URL="http://localhost:5050/electronic-address/verify-otp"
const IS_EMAIL_VERIFIED="http://localhost:5050/electronic-address/isEmailVerified"
const IS_PHONE_VERIFIED="http://localhost:5050/electronic-address/isPhoneVerified"
const IS_BANK_VERIFIED="http://localhost:5050/user-account/isBankVerified"
const SEND_MONEY_TO_USER="http://localhost:5050/send-transaction"
const REQUEST_MONEY_TO_USER="http://localhost:5050/request-transaction"
const FETCH_WALLET_AMOUNT="http://localhost:5050/user-account/get-current-amount"
const DEDUCT_MONEY_FROM_BANK_TO_VERIFY="http://localhost:5050/user-account/deduct-money-from-bank-to-verify"
const VERIFY_BANK="http://localhost:5050/user-account/verify-bank"
const HAS_ADDITIONAL="http://localhost:5050/has-additional"
const BANK_ACCOUNT="http://localhost:5050/bank-account"
const USER_ACCOUNT = "http://localhost:5050/user-account"
const METRICS_URL = "http://localhost:5050/metrics"

window.onload = function() {
  loadVariableValueInMainPage()
  isEmailVerified()
  isPhoneVerified()
  isBankVerified()
  document.getElementById("main").style.display="block";
}

function isEmailVerified(){
  const data=JSON.parse(document.cookie).ssn;
  postData(IS_EMAIL_VERIFIED, data)
  .then(data => data.text())
  .then(data => {
      if (data==="Verified") {
        document.getElementById("verifyEmail").style.backgroundColor="#05C342";
        document.getElementById("verifyEmail").style.color="white";
        document.getElementById("verifyEmail").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
        document.getElementById("verifyEmail").disabled = true;

      }
  })
}
function isPhoneVerified(){
  const data=JSON.parse(document.cookie).ssn;
  postData(IS_PHONE_VERIFIED, data)
  .then(data => data.text())
  .then(data => {
      if (data==="Verified") {
        document.getElementById("verifyPhone").style.backgroundColor="#05C342";
        document.getElementById("verifyPhone").style.color="white";
        document.getElementById("verifyPhone").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
        document.getElementById("verifyPhone").disabled = true;
      }
  })
}

function isBankVerified(){
  const data=JSON.parse(document.cookie).ssn;
  postData(IS_BANK_VERIFIED, data)
  .then(data => data.text())
  .then(data => {
      if (data==="Verified") {
        document.getElementById("verifyBank").style.backgroundColor="#05C342";
        document.getElementById("verifyBank").style.color="white";
        document.getElementById("verifyBank").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
        document.getElementById("verifyBank").disabled = true;
      }
  })
}
function sendMoneyToUser(){
  const identifier = document.getElementById("email").value.length > 0 ?document.getElementById("email").value : document.getElementById("telephone").value
  const data={
    "identifier":identifier,
    "ssn":JSON.parse(document.cookie).ssn,
    "amount":document.getElementById("amount").value,
    "memo":document.getElementById("memo").value
}
  postData(SEND_MONEY_TO_USER, data)
  .then(data => {
    if (data.ok) {
        alert('successfully sent money')
    } else {
        alert('oops something went wrong...', data.json())
    }
})
}
function requestMoneyFromUser(){
  const identifier = document.getElementById("requestEmail").value.length > 0 ?document.getElementById("requestEmail").value : document.getElementById("requestTelephone").value
  const data={
    "identifier":identifier,
    "ssn":JSON.parse(document.cookie).ssn,
    "amount":document.getElementById("requestAmount").value,
    "memo":document.getElementById("requestMemo").value
}
  postData(REQUEST_MONEY_TO_USER, data)
  .then(data => {
    if (data.ok) {
        alert('successfully requested money')
    } else {
        alert('oops something went wrong...', data.json())
    }
})
}
function loadVariableValueInMainPage(){
  let dataArray=JSON.parse(document.cookie)
  let firstName=dataArray.firstname;
  document.getElementById("firstNameSpan").innerHTML=firstName;
  let ssn=dataArray.ssn;
  document.getElementById("ssnSpan").innerHTML=ssn;
  data=JSON.parse(document.cookie).ssn;
  postData(FETCH_WALLET_AMOUNT,data)
  .then(data => data.json())
  .then(data => {
      if (data!=null) {
          document.getElementById("walletBalanceSpan").innerHTML=data;
      } else {
          alert("Something went wrong");
      }
  })
  loadPersonalInfoToAccountPage()
}

function loadPersonalInfoToAccountPage() {
  const personalData = JSON.parse(document.cookie)
  document.getElementById('firstname').value = personalData.firstname
  document.getElementById('lastname').value = personalData.lastname
  document.getElementById('tel').value = personalData.phoneno
  document.getElementById('mailId').value = personalData.email
  document.getElementById('password').value = '********************'
  const bank_details = getDataFromNetwork(`${HAS_ADDITIONAL}/${personalData.ssn}`)
  bank_details
  .then(data => data.json())
  .then(data => {
    let html = ``
    for (let index = 0 ; index < data.length; index++) {
      const item = data[index]
      html += `
        <div class=${index}>
          <label id=accountNumberLabel>A/c Number: </label><input type="text" placeholder="account number" id="accountnumber" value=${item.baNumber}>
        </div>
        <div class=${index}>
          <label id=branchLabel>Bank Id: </label><input type="text" placeholder="branch id" id="branch" value=${item.bankId}>
        </div>
        <div class=${index}>
          <label id=branchLabel>Balance: </label><input type="text" placeholder="balance" id="balance" value=${item.bank_balance}>
        </div>
        ${item.bankId != personalData.bankId ? 
          `<div class=${index}>
            <button id='removeBankBtn' onclick="removeBankRow(${index})">Remove Bank</button>
          </div>` :
          `<div></div>`
        }
        
      `
    }
    document.getElementById('saved_bank_list').insertAdjacentHTML("beforeend", html)
  })
}

function addHadAdditionalBanks() {
  const personalData = JSON.parse(document.cookie)
  const bank_details = document.getElementById('saved_bank_list').getElementsByTagName("input")
  const had_additional_attributes = []
  for (let i = 0; i < bank_details.length; i = i + 3) {
    had_additional_attributes.push({
      ssn: personalData.ssn,
      baNumber: bank_details[i].value,
      bankId: bank_details[i + 1].value,
      bank_balance: bank_details[i + 2].value,
      verified: false
    })
  }
  for(let i = 0; i < had_additional_attributes.length; i++) {
    postData(BANK_ACCOUNT, {bankID: had_additional_attributes[i].bankId, baNumber: had_additional_attributes[i].baNumber})
    .then(data => data.json())
    .then(data => {
      if (data) {
        postData(HAS_ADDITIONAL, had_additional_attributes[i])
        .then(data => data.json)
        .then(data => {
          if(data != null) {
            console.log('bank added successfully')
          } else {
            console.error('something happened.')
          }
        })
      }
    })
  }
}

async function getDataFromNetwork(url) {
  return await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    }
  })
}

function generate(ele,id){
  loadOTPPage(id);
  let data="";
  if(id === "phoneSubmitOTP"){
    data=JSON.parse(document.cookie).phoneno;
  }
  else{
    data=JSON.parse(document.cookie).email;
  }
  
  postData(GENERATE_OTP_URL, data)
  .then(data => data.json())
  .then(data => {
      if (data!=null) {
          alert("Your otp is "+data)
      } else {
          alert("Something went wrong");
      }
  })
}
function generateMoneyToVerifyFromBank(ele,id){
  loadOTPPage(id);
  const data=JSON.parse(document.cookie).ssn;
  postData(DEDUCT_MONEY_FROM_BANK_TO_VERIFY, data)
  .then(data => data.json())
  .then(data => {
      if (data!=null) {
          alert("$"+data+" is deducted from your bank. Please enter the same amount to verify bank.")
      } else {
          alert("Something went wrong");
      }
  })
}
function verifyBank(ele,id){
  document.getElementById(id).style.display="none";
  const data={
    "ssn":JSON.parse(document.cookie).ssn,
    "verificationAmount":document.getElementById("amountDeductedToVerify").value
}
postData(VERIFY_BANK, data)
.then(data => {
  if (data.ok) {
      alert('Bank has been verified');
      document.getElementById("verifyBank").style.backgroundColor="#05C342";
      document.getElementById("verifyBank").style.color="white";
      document.getElementById("verifyBank").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
      document.getElementById("verifyBank").disabled = true;
  } else {
      alert('oops something went wrong...', data.json())
  }
})
}
function verifyEmail(ele,id){
  document.getElementById(id).style.display="none";
  const data={
    "identifier":document.getElementById("emailOTP").value,
    "otp": parseInt(document.getElementById("oneTimePasswordEmail").value),
    "ssn":JSON.parse(document.cookie).ssn
}
postData(VERIFY_OTP_URL, data)
.then(data => {
  if (data.ok) {
      alert('Email has been verified');
      document.getElementById("verifyEmail").style.backgroundColor="#05C342";
      document.getElementById("verifyEmail").style.color="white";
      document.getElementById("verifyEmail").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
  } else {
      alert('oops something went wrong...', data.json())
  }
})

}

function verifyPhone(ele,id){
  document.getElementById(id).style.display="none";
  const data={
    "identifier":document.getElementById("phoneOTP").value,
    "otp": parseInt(document.getElementById("oneTimePasswordPhone").value),
    "ssn":JSON.parse(document.cookie).ssn
}
postData(VERIFY_OTP_URL, data)
.then(data => {
  if (data.ok) {
      alert('Phone has been verified');
      document.getElementById("verifyPhone").style.backgroundColor="#05C342";
      document.getElementById("verifyPhone").style.color="white";
      document.getElementById("verifyPhone").innerHTML='<b><i class="fas fa-check-circle"></i> Verified</b>';
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

function loadOTPPage(id){
  document.getElementById(id).style.display="block";
}
function changeColor(ele,id){
    let i;
    let j;
    let btnId=document.getElementsByClassName("tabContainer");
    for(j=0;j<btnId.length;j++){
        btnId[j].style.display="none";
    }
    let button_num=document.getElementsByClassName("tablink");
    for(i=0;i<button_num.length;i++){
      button_num[i].style.color="white";
      button_num[i].style.backgroundColor="#54ABDC";
    }
    ele.style="Color:white";
    ele.style.backgroundColor="#02428E";
    document.getElementById(id).style.display="block";
    if (id === 'transactions-tab') {
      getTransactions("send-transactions-table", 0)
    }
}

  function PageDisplay(ele,id){document.getElementById(id).style.display="block";}


  // Close Send Page
  function closeSendPage(){document.getElementById("sendMoney").style.display="none";}
    // Close Request Page
  function closeRequestPage(){document.getElementById("requestMoney").style.display="none";}
  // Close Page
  function closePage(ele,id){document.getElementById(id).style.display="none";}

  function logoutMenu(ele){
    document.cookie = `${document.cookie};expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    window.location.href = 'http://127.0.0.1:5500/index.html'; 
  }

  function editPersonalDetails() {
    setVisibilityById('personalDetailSave', 'flex')
    setInputStatus('personalDetailInput', false)
  }
  
  function editPersonalDetailsSave() {
    const body = {
      ssn: JSON.parse(document.cookie).ssn,
      firstname: document.getElementById('firstname').value,
      lastname: document.getElementById('lastname').value,
      email: document.getElementById('mailId').value,
      phonenumber: document.getElementById('tel').value,
      password: document.getElementById('password').value
    }
    postData(`${USER_ACCOUNT}/update`, body)
    .then(data => data.json())
    .then(data => {
      if ("ssn" in data) {
        document.cookie = JSON.stringify(data)
        alert('details are updated')
      }
      setVisibilityById('personalDetailSave', 'none')
      setInputStatus('personalDetailInput', true)
    })
    .catch(error => {
      alert('oops, something went wrong')
      setVisibilityById('personalDetailSave', 'none')
      setInputStatus('personalDetailInput', true)
    })
  }
  
  function setInputStatus(classname, status) {
    const inputs = document.getElementsByClassName(classname)[0].getElementsByTagName('input')
    for(let i =0; i < inputs.length; i++) {
      inputs[i].disabled = status
      inputs[i].style.border = status ? 'none' : '1px solid #54ABDC'
    }
  } 
  
  function setVisibilityById(id, status) {
    document.getElementById(id).style.display = status
  } 
  
  function editBankDetail() {
    setVisibilityById('bankDetailSave', 'flex')
    setInputStatus('bankDetailInput', false)
  }
  
  function saveBankDetail() {
    setVisibilityById('bankDetailSave', 'none')
    setInputStatus('bankDetailInput', true)
    addHadAdditionalBanks()
  }
  
  function addBankRow() {
    const id = Date.now()
    const html = `
      <div class=${id}>
        <label id=accountNumberLabel>A/c Number: </label><input type="text" placeholder="account number" id="accountnumber">
      </div>
      <div class=${id}>
        <label id=branchLabel>Bank Id: </label><input type="text" placeholder="branch id" id="branch">
      </div>
      <div class=${id}>
        <label id=branchLabel>Balance: </label><input type="text" placeholder="balance" id="balance">
      </div>
      <div class=${id}>
        <button id='removeBankBtn' onclick="removeBankRow(${id})">Remove Bank</button>
      </div>
    `
    document.getElementById('saved_bank_list').insertAdjacentHTML("beforeend", html)
    setVisibilityById('bankDetailSave', 'flex')
  }
  
  function removeBankRow(className) {
    const elems = document.getElementsByClassName(className)
    while(elems.length > 0) {
      elems[0].remove()
    }
  }
  
  function getTransactions(table_class, index) {
    const urls = ['http://localhost:5050/send-transaction', 'http://localhost:5050/request-transaction', `http://localhost:5050/transaction/received`]
    const actionKey = index === 0 ? 'cancel' : index === 1? 'approve/reject' : ''
    const grid = new gridjs.Grid({
      search: true,
      width: '105%',
      resizable: true,
      columns: [
        {
          name: "ID"
        },
        {
          name: "Date/Time"
        },
        {
          name: "SSN"
        },
        {
          name: 'memo'
        },
        {
          name: 'Identifier'
        },
        {
          name: 'Transaction Status'
        },
        {
          name: 'Cancel Reason'
        },
        {
          name: 'Amount'
        },
        {
          name: 'Action',
          formatter: (cell, row) => {
            if (row.cells[5].data.toLowerCase() === 'pending' && index < 2) {
              if (index === 1 && row.cells[2].data === JSON.parse(document.cookie).ssn) {
                return ''
              }
              return gridjs.h('button', {
                className: 'py-2 mb-4 px-4 border rounded-md text-white bg-blue-600',
                onClick: () => tableAction(row, index, grid)
              }, actionKey)
            }
          }
        }],
        server: {
          url: `${urls[index]}?ssn=${JSON.parse(document.cookie).ssn}`,
          then: data => data.map(item => 
            [item.stid ? item.stid : item.rtid, moment.unix(item.dateortime / 1000).format('dddd, MMMM Do, YYYY h:mm:ss A'), item.ssn, item.memo, item.identifier, item.status, item.cancel_reason, item.amount]
          )
        } 
    }).render(document.getElementById(table_class));
  }

  function tableAction(row, index, grid) {
    const urls = ['http://localhost:5050/send-transaction/cancel', 'http://localhost:5050/request-transaction']
    const id = parseInt(row.cells[0].data)
    if (index === 0) {
      const cancelReason = prompt("Enter reason to cancel", 'transaction cancelled')
      getDataFromNetwork(`${urls[index]}?id=${id}&cancelReason=${cancelReason}`)
      .then(response => {
        if (response.ok) {
          grid.forceRender()
        } else {
          response.json()
          .then(data => alert('something went wrong...', data.message))
        }
      })
    } else if (index == 1) {
      const action = prompt('Approve/Reject', 'approve')
      if (action.toLocaleLowerCase() === 'approve') {
        getDataFromNetwork(`${urls[index]}/approve?id=${id}&ssn=${JSON.parse(document.cookie).ssn}`)
        .then(response => {
          if (response.ok) {
            grid.forceRender()
          } else {
            response.json()
            .then(data => alert('something went wrong...', data.message))
          }
        })
      } else {
        getDataFromNetwork(`${urls[index]}/cancel?id=${id}`)
        .then(response => {
          if (response.ok) {
            grid.forceRender()
          } else {
            response.json()
            .then(data => alert('something went wrong...', data.message))
          }
        })
      }
    }
  }
  
  function toggleTransactionsView(event) {
    const id = event.target.id
    const index = parseInt(id.replaceAll('tab_', ''))
    const tabs = document.getElementsByClassName('transactions_tab')
    const tables = document.getElementsByClassName('transaction_table')
    const table_classes = ["send-transactions-table", "request-transactions-table", "receive-transactions-table"]
    for (let i = 0 ; i < tables.length; i++) {
      if (i == index) {
        tables[i].classList.add('active')
        tabs[i].classList.add('active')
      } else {
        tables[i].classList.remove('active')
        tabs[i].classList.remove('active')
      }
    }
    getTransactions(table_classes[index], index)
  }
  
  function addMoneyToWallet() {
    const url = `${USER_ACCOUNT}/add-money-to-wallet`
    const body = {
      ssn: JSON.parse(document.cookie).ssn,
      accountnumber: document.getElementById('addMoneyAccountNumber').value,
      bankid: document.getElementById('addMoneyBankId').value,
      amount: document.getElementById('addMoneyAmount').value
    }
    postData(url, body)
    .then(data => data.text())
    .then(data => {
      if (data != null) {
        alert('add money to wallet')
      } else {
        alert('oops.. something went wrong')
      }
    })
  }

  function toggleMetricView(event) {
    const id = event.target.id
    const index = parseInt(id.replaceAll('tab_', ''))
    const tabs = document.getElementsByClassName('metrics_view')
    const views = document.getElementsByClassName('metrics_tab')
    for (let i = 0 ; i < tabs.length; i++) {
      if (i == index) {
        tabs[i].classList.add('active')
        views[i].classList.add('active')
      } else {
        tabs[i].classList.remove('active')
        views[i].classList.remove('active')
      }
    }
    if (index === 3) {
      getBestUser()
    }
  }

  function getMetricsForRange() {
    const from = document.getElementById('from-date').value
    const to = document.getElementById('to-date').value
    getDataFromNetwork(`${METRICS_URL}/by_range?ssn=${JSON.parse(document.cookie).ssn}&from=${from}&to=${to}`)
    .then(data => data.json())
    .then(data => {
      if ("send-amount" in data) {
        document.getElementById('metric_send_label').textContent = `$${data['send-amount']}`
        document.getElementById('metric_receive_label').textContent = `$${data['receive-amount']}`
      }
    })
  }

  function getMetricsForMonth() {
    const month = document.getElementById('metric-month').value
    const args = month.split("-")
    getDataFromNetwork(`${METRICS_URL}/by_month?ssn=${JSON.parse(document.cookie).ssn}&month=${args[1]}&year=${args[0]}`)
    .then(data => data.json())
    .then(data => {
      if('send-amount' in data) {
        document.getElementById('metric_send_label_month').textContent = `$${data['send-amount']}`
        document.getElementById('metric_avg_send_label_month').textContent = `$${data['avg-send-amount']}`
        document.getElementById('metric_receive_label_month').textContent = `$${data['receive-amount']}`
        document.getElementById('metric_avg_receive_label_month').textContent = `$${data['avg-receive-amount']}`
      }
    })
  }

  function getMaxMetricsForMonth() {
    const month = document.getElementById('max-metric-month').value
    const args = month.split("-")
    getDataFromNetwork(`${METRICS_URL}/max_by_month?ssn=${JSON.parse(document.cookie).ssn}&month=${args[1]}&year=${args[0]}`)
    .then(data => data.json())
    .then(data => {
      if('received_transactions' in data) {
        const sendAmount = data['send_transactions'].length > 0 ? data['send_transactions'][0].amount : 0
        const receiveAmount = data['received_transactions'].length > 0 ? data['received_transactions'][0].amount : 0
        document.getElementById('max-transactions-table').innerHTML = ''
        document.getElementById('max-send-amount').textContent = `$${sendAmount}`
        document.getElementById('max-receive-amount').textContent = `$${receiveAmount}`
        let tableData = data['received_transactions'].map(item => {
          if (item !== null && Object.keys(item).length > 0) {
            return [
              'Received',
              item['rtid'] ? item['rtid'] : item['stid'],
              moment.unix(item['dateortime'] / 1000).format('dddd, MMMM Do, YYYY h:mm:ss A'),
              item['ssn'],
              item['memo'],
              item['identifier'],
              item['status'],
              item['cancel_reason'],
              item['amount']
            ]
          }
        })
        tableData = [...tableData, ...data['send_transactions'].map(item => {
          if (item !== null && Object.keys(item).length > 0) {
            return [
              'Send',
              item['rtid'] ? item['rtid'] : item['stid'],
              moment.unix(item['dateortime'] / 1000).format('dddd, MMMM Do, YYYY h:mm:ss A'),
              item['ssn'],
              item['memo'],
              item['identifier'],
              item['status'],
              item['cancel_reason'],
              item['amount']
            ]
          }
        })]
        tableData = tableData.filter(data => data != undefined)
        const grid = new gridjs.Grid({
          search: true,
          width: '105%',
          resizable: true,
          columns: [
            {
              name: 'Type'
            },
            {
              name: "ID"
            },
            {
              name: "Date/Time"
            },
            {
              name: "SSN"
            },
            {
              name: 'memo'
            },
            {
              name: 'Identifier'
            },
            {
              name: 'Status'
            },
            {
              name: 'Cancel Reason'
            },
            {
              name: 'Amount'
            }],
            data: tableData
        }).render(document.getElementById('max-transactions-table'));
        grid.forceRender()
      }
    })
  }

  function getBestUser() {
    getDataFromNetwork(`${METRICS_URL}/best_users?ssn=${JSON.parse(document.cookie).ssn}`)
    .then(data => data.json())
    .then(data => {
      if(data && data.length > 0) {
        const tableData = data.map(item => [`${item.firstname} ${item.lastname}`, item.emailid, item.phoneno])
        new gridjs.Grid({
          search: true,
          columns: [
            {
              name: 'Name'
            },
            {
              name: "Email"
            },
            {
              name: "Phone Number"
            }],
            data: tableData
        }).render(document.getElementById('best-users-table'));
      }
    })
  }

  function sendMoneyToBank() {
    const body = {
      ssn: JSON.parse(document.cookie).ssn,
      ac_number: document.getElementById('depositMoneyAccountNumber').value,
      amount: document.getElementById('depositMoneyAmount').value
    }
    postData(`${USER_ACCOUNT}/deposit_money`, body)
    .then(data => {
      if (data.ok) {
        alert("deposit successful")
      } else if (data.status === 500) {
        alert("oops..." + data.statusText)
      } else {
        alert('oops... something went wrong')
      }
    })
  }
  