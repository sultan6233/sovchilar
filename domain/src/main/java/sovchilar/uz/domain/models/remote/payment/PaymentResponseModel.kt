package sovchilar.uz.domain.models.remote.payment

data class PaymentResponseModel(
    val transactionId: Int,
    val session: Int,
    val otpSentPhone: String
)