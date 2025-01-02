![](https://merchant.payway.com.kh/images/aba-logo.svg)
# ABA POS Integration Specification v1.1
![ABA POS](https://www.payway.com.kh/image/product/how_it_works/pos_terminal/1.webp)
## Sale Transaction (QR & Card)
Send a sale transaction request and wait for the response from the ABA POS terminal.
### Request
| Field        | Data Type   |  Mandatory | Data/Format    | Description                        | 
| :----------- | :-------    | :---------| :-----------    | :--------------------------------- |
| CMD          | String      | Yes      | SALE            | Command ABA POS to do a sale transaction.                  |
| TYPE         | String      | Yes      | QR, CARD, ALL   | Type of the payment. ALL: Accept QR and card transactions. QR: Accept only QR. CARD: Accept only card transactions. |
| QRTYPE       | String      | Yes      | KHQR, WECHAT, ALIPAY, ALL, NONE | Payment method of the QR code. The merchant accounts must be configured to support those payment methods before you can use them.         |
| AMT          | String      | Yes      | #.## | Transaction amount. Eg: 0.01 or 10000.00. Do not use the format like this: 0.001 or 0.00001. |
| CURRCODE     | String      | Yes      | USD, KHR        | Transaction currency. The merchant account should be configured to support dual currency. |
| TIMESTAMP    | String      | Yes    | yyyy-MM-dd HH:mm:ss | Timestamp of the request. Eg: 2021-05-06 16:00:35 |
| ECRREF       | String      | Yes    |  | A unique transaction ID or invoice ID is generated from the cashier's POS system or Kiosk. Eg: 1234567890, or any random text + Date/Time (INV_2024_12_30_16_00_59) |

### Response
| Field        | Data Type   |  Mandatory | Value/Format    | Description                        | 
| :----------- | :-------    | :---------| :-----------    | :--------------------------------- |
| CMD          | String      | Yes      | SALE            | Sale transaction command.                  |
| TYPE         | String      | Yes      | QR, CARD, ALL   | Type of the payment. ALL: Accept QR and card transactions. QR: Accept only QR. CARD: Accept only card transactions. |
| AMT          | String      | Yes      | #.## | Transaction amount. |
| CURRCODE     | String      | Yes      | USD, KHR        | Transaction currency. |
| ECRREF       | String      | Yes    |  | ECRREF from the request. |
| STATUS       | String      | Yes    | Approved, Declined, Pending, Invalid Hash, Invalid CMD, Duplicate ECRREF, POS Busy, Error | Status of the request or transaction. **Approved**: transaction approved. **Declined**: transaction declined. **Pending**: ABA POS is waiting for payment to be completed. **Invalid CMD**: you sent an invalid command (CMD). **Invalid Hash**: wrong encryption key or data corrupted. **POS Busy**: ABA POS doesn't stand on the home screen. **Error**: connection error between ABA POS and the server or any unexpected error. |
| TRACE       | String     | Yes      | 6-digit number left-padded by zero | Invoice number of the card transaction. Eg: 000001, 012345, 100001. You can use its value to do VOID card transactions. |
| TRXN_ID    | String     | Yes      |   | QR transaction ID or card reference number (RRN). You can use TRXN_ID to do a REFUND on QR transactions. | 
| DATE       | String     | Yes      | YYYYMMDD format | Transaction date in YYYYMMDD format. Eg: 20210506 |
| TIME       | String     | Yes      | HHMMSS format   | Transaction time in HHMMSS format. Eg: 160035 |
| PAYER      | String     | Yes      | Value depends on payer account | A formatted QR payer account or a masked card number. E.g.: MOTHEARA (*498) or 428609*****5001 |
| PAN (Deprecated) | String    | No     |      | Please use the field "PAYER".
| TERMINALID | String     | Yes      | 8-digit value     | Terminal ID of ABA POS |
| MERCHANTID | String     | Yes      | 15-digit value      | Merchant ID of ABA POS |
| CARD       | String     | No       | VISA, MASTERCARD, JCB, UNIONPAY, CSS, FLEET CARD | The card brand that the user uses for payment. |
### Use cases
1. Display all the payment methods on the screen (QR & card). This is the most commonly used by merchants.
```json
Request:
{
  "CMD":"SALE",
  "TYPE":"ALL",
  "QRTYPE":"ALL",
  "AMT":"0.01",
  "CURRCODE":"USD",
  "ECRREF":"INV0001",
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
Response:
{
  "CMD":"SALE",
  "TYPE":"ALL",
  "AMT":"0.01",
  "CURRCODE":"USD",
  "APV":"411195",
  "ECRREF":"INV0001",
  "DATE":"20241231",
  "TIME":"153723",
  "STATUS":"Approved",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288",
  "TRXN_ID":"173563424399318",
  "PAYER":"MOTHEARA (*998)"
}
```

2. Display only KHQR, WeChat, or AliPay on the screen (QR only). WeChat and AliPay only support USD currency.
```json
Request:
{
  "CMD":"SALE",
  "TYPE":"QR",
  "QRTYPE":"KHQR", // You can change to "WECHAT" or "ALIPAY" here.
  "AMT":"0.01",
  "CURRCODE":"USD",
  "ECRREF":"INV0002",
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
Response:
{
  "CMD":"SALE",
  "TYPE":"QR",
  "AMT":"O.01",
  "CURRCODE":"USD",
  "APV":"810331",
  "ECRREF":"INVO002",
  "DATE":"20241231",
  "TIME":"155204",
  "STATUS":"Approved",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288",
  "TRXN_ID":"173563512416913",
  "PAYER":"MOTHEARA (*998)"
}
```
4. Display only card payment on the screen (card only).
```json
Request:
{
  "CMD":"SALE",
  "TYPE":"CARD",
  "QRTYPE":"NONE", // Please note that we use "NONE" here.
  "AMT":"0.01",
  "CURRCODE":"USD",
  "ECRREF":"INV0004",
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
Response:
{
  "CMD":"SALE",
  "TYPE":"CARD",
  "AMT":"0.01",
  "CURRCODE":"USD",
  "APV":"990877",
  "ECRREF":"INVO004",
  "DATE":"20241231",
  "TIME":"160016",
  "STATUS":"Approved",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288",
  "TRACE":"000028",
  "TRXN_ID":"000098922280",
  "CARD":"VISA",
  "PAYER":"VISA 428609*****6960"
}
```

## Back to the Home Screen (Cancel waiting)
While ABA POS is standing on the payment screen and **the user hasn't paid yet**, you can request that ABA POS go back to the home screen. Some SDKs might not support this yet. But if you are using Java, Android, Flutter, JavaScript, or C++ SDK, you can call **.cancelWaitingPosTerminal()**. Then you can send this command:
```json
Request:
{
  "CMD":"CANCEL",
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
The response will contain many empty values. You can ignore the response. 
Here is the sample:
{
  "CMD":"SALE",
  "TYPE":"ALL",
  "AMT":"0.01",
  "CURRCODE":"USD",
  "APV":"",
  "ECRREF":"INVO005",
  "DATE":"",
  "TIME":"",
  "STATUS":"Cancel", // Please note that we use "STATUS":"Cancel" here.
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288",
  "TRACE":"000000",
  "TRXN_ID":"",
  "PAYER":"",
}
```
## Inquiry Transaction
If the connection has been lost between your POS system and ABA POS, or you got the timeout from the SDKs, you can inquire about the transaction to see if the payment has been completed or not.
```json
Request:
{
  "CMD":"MEMENQ",
  "ECRREF":"INVO004",
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
Response:
{
  "CMD":"SALE",
  "TYPE":"CARD",
  "AMT":"0.01",
  "CURRCODE":"USD",
  "APV":"990877",
  "ECRREF":"INVO004",
  "DATE":"20241231",
  "TIME":"160016",
  "STATUS":"Approved",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288",
  "TRACE":"000028",
  "TRXN_ID":"000098922280",
  "CARD":"VISA",
  "PAYER":"VISA 428609*****6960"
}
```
or
```json
Response:
{
  "CMD":"MEMENQ",
  "ECRREF":"INVO004",
  "DATE":"20241231",
  "TIME":"160016",
  "MESSAGE":"No transaction found",
  "STATUS":"Not Found"
}
```

## Void Card Transaction
You can void the card transaction if the payment hasn't been settled from the bank or it's still under 24 hours after the payment is completed.
```json
Request:
{
  "CMD":"VOID",
  "TRACE":"000028", // You can also find its value from the receipt printed by ABA POS (TRACE#).
  "TIMESTAMP":"2024-12-31 11:58:29"
}
```
```json
Response:
{
  "CMD":"VOID",
  "TYPE":"CARD",
  "DATE":"20241231",
  "TIME":"170450",
  "APV":"990877",
  "TRACE":"000028",
  "STATUS":"Approved",
  "TRXN_ID":"000098922280"
}
```
The STATUS value can be:
- "Approved": the transaction is successfully voided.
- "Declined": the transaction is declined.
- "Already Void": the transaction is already voided.
- "Not Found": the transaction is not found in ABA POS.

## Refund Transaction
The refund policy depends on the payment service provider (ABA Bank, WeChat, Alipay, and card schemes). For example, you can still refund an ABA QR and Alipay transaction before 30 days.
```json
Request:
{
  "CMD":"REFUND",
  "TYPE":"QR", // Change to "CARD" if you want to refund the card transaction.
  "TIMESTAMP":"2024-12-31 11:58:29",
  "TRXN_ID":"173563512416913"
}
```
```json
Response:
{
  "CMD":"REFUND",
  "TYPE":"QR",
  "APV":"810331",
  "DATE":"20241231",
  "TIME":"182551",
  "STATUS":"Approved",
  "TRXN_ID":"173563512416913"
}
```
## Generate QR for Secondary Screen
If your POS system or Kiosk can connect to the Internet, you should instead use the [ABA QR API](https://www.payway.com.kh/km/aba-qr-api) as it provides more robust integration. Just head to https://www.payway.com.kh/aba-qr-api or https://www.payway.com.kh for more info.

1.  To request ABA POS to generate a QR string to display on your secondary screen:
```json
Request:
{
  "CMD":"REQR",
  "ECRREF":"INV00000000001", // Must be 14 characters and unique.
  "AMT":"0.02",
  "CURRCODE":"USD",
  "QRTYPE":"KHQR", // Can be KHQR, WECHAT, ALIPAY
  "TIMESTAMP":"2021-05-06 16:00:35",
  "LIFETIME":"300" // Default 120 seconds, max 300 seconds
}
```
- QRTYPE value can be KHQR, WECHAT, or ALIPAY.
- LIFETIME and ECRREF are embedded in the QR string for ABA Pay, KHQR, WeChat, and Alipay. ABA will reject the transaction if the payment happens after the LIFETIME.

```json
Success response:
{
  "CMD":"REQR",
  "AMT":"0.02",
  "CURRCODE":"USD",
  "ECRREF":"INV00000000001",
  "OPTIONS":"ABA Pay,KHQR",
  "LIFETIME":300,
  "QRSTRING":"000201010212021641277888800030330416518352888000042.....",
  "STATUS":"Success",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288"
}

Error response:
{
  "CMD":"REQR",
  "AMT":"0.02",
  "CURRCODE":"USD",
  "ECRREF":"INV1",
  "MESSAGE":"'ECRREF' must be 14 characters",
  "STATUS":"Error",
  "TERMINALID":"56990288",
  "MERCHANTID":"222071556990288"
}
```
2.  Check Transaction Status
To retrieve the transaction status for a specific transaction ID, merchants should execute the following command. It is required to initiate this command only once and wait for the duration of the LIFETIME period. Merchants are advised to issue the command again for final payment status verification before considering the transaction as unsuccessful.
```json
Request:
{
  "CMD":"CHECK_TRXN",
  "ECRREF":"INV00000000001",
  "TIMESTAMP":"2024-12-31 11:58:29",
  "WAIT_TIME":"300"
}
```
- WAIT_TIME: ABA POS will keep calling the server to check the status of the QR transaction belonging to the ECRREF until it reaches the WAIT_TIME duration.
```json
Success response:
{
  "AMT":"0.01",
  "APV":"464548",
  "ECRREF":"INV00000000001",
  "CMD":"CHECK_TRXN",
  "CURRCODE":"USD",
  "MERCHANTID":"222071556990288",
  "PAYER":"DARA (*169)",
  "TYPE":"SHOWQR",
  "STATUS":"Success",
  "TERMINALID":"56990288",
  "TRXN_ID":"INV00000000001"
}
 
Error response:
{
  "ECRREF":"INV00000000100",
  "CMD":"CHECK_TRXN",
  "MERCHANTID":"222071556990288",
  "MESSAGE":"No transaction found",
  "STATUS":"Error",
  "TERMINALID":"56990288"
}
```
3.  Abort
To halt or cancel a command or operation currently in progress on the ABA Merchant POS, merchants must issue an Abort command. This action will terminate the ongoing task on the ABA POS terminal, allowing for the initiation of a new command.
```json
Request:
{
  "CMD":"ABORT",
  "TIMESTAMP":"2024-03-05 16:57:34"
}
```
```json
Success response:
{
  "CMD":"ABORT",
  "STATUS":"Success",
}

Error response:
{
  "CMD":"ABORT",
  "STATUS":"Error",
  "MESSAGE": "Something went wrong"
}
```
