# Bank App API Documentation

## Base URL
`http://localhost:8080`

## Endpoints

### Create Account
- **URL:** `http://localhost:8080/api/accounts`
- **Method:** `POST`
- **Request:**
  ```json
  {
    "accountNumber": "44004300",
    "accountHolderName": "Asyl Omarov",
    "balance": 50000.00
  }
  
- **Response:**
  ```json
  {
    "id": 6,
    "accountNumber": "44004300",
    "accountHolderName": "Asyl Omarov",
    "balance": 50000.0
  }

### Get Account By ID
- **URL:** `http://localhost:8080/api/accounts/{id}`
- **Method:** `GET`

- **Response:**
  ```json
  {
    "id": 2,
    "accountNumber": "4400",
    "accountHolderName": "Adil Ahmetov",
    "balance": 150000.00
  }

### Transfer
- **URL:** `http://localhost:8080/api/accounts/transfer`
- **Method:** `POST`
- **Request:**
  ```json
  {
    "fromAccountId": 6,
    "toAccountId": 2,
    "amount": 20000.00
}

- **Response:**
  ```json
  {
    "message": "Transfer successful"
  }

### Deposit
- **URL:** `http://localhost:8080/api/accounts/{id}/deposit`
- **Method:** `PUT`
- **Request:**
  ```json
  {
    "amount": 50000.00
  }

- **Response:**
  ```json
  {
    "id": 3,
    "accountNumber": "4401",
    "accountHolderName": "Askhat Akpaev",
    "balance": 250000.0
  }

### Withdraw
- **URL:** `http://localhost:8080/api/accounts/{id}/withdraw`
- **Method:** `PUT`
- **Request:**
  ```json
  {
    "amount": 50000.00
  }

- **Response:**
  ```json
  {
    "id": 3,
    "accountNumber": "4401",
    "accountHolderName": "Askhat Akpaev",
    "balance": 200000.0
  }