package com.sovchilar.made.domain.models.remote.payment

data class PaymentWithoutRegistrationResponseModel(
    val session: Long,
    val error: String,
    val optSentPhone: String,
    val transactionId: Long
)