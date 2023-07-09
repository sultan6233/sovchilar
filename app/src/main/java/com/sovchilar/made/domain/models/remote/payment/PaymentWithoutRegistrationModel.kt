package com.sovchilar.made.domain.models.remote.payment

data class PaymentWithoutRegistrationModel(
    val amount: Double,
    val cardNumber: String,
    val expireDate: String,
    val extraId: String,
    val transactionData: String = ""
)