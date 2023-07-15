package com.sovchilar.made.domain.models.remote.payment

data class PaymentConfirmResponseModel(
    val transactionId: String,
    val utrno: String,
    val responseCode: Int,
    val terminalId: String,
    val merchantId: String,
    val cardNumber: String,
    val date: String
)