package sovchilar.arch.featureremoteapi

import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import sovchilar.uz.domain.models.PostResponse
import sovchilar.uz.domain.models.UserModel
import sovchilar.uz.domain.models.remote.AdvertisementsModel
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthResponseModel


interface ApiService {

    @POST("api/personals")
    suspend fun postAdvertisement(
        @Header("Authorization") authToken: String,
        @Body postAdvertisement: AdvertisementsModel
    ): Response<PostResponse>

    @GET("api/personals/all")
    suspend fun getAdvertisements(@Query("page") page: Int, @Query("limit") limit: Int, @Query("order") order: String = "desc"): Response<UserModel>

    @GET("api/personals")
    fun getOwnAdvertisements(@Header("Authorization") authToken: String): Call<UserModel>

    @POST("api/auth")
    suspend fun loginOrRegister(@Body authModel: AuthModel): Response<AuthResponseModel>

    @POST("api/payment/make")
    fun pay(
        @Header("Authorization") authToken: String,
        @Body paymentModel: sovchilar.uz.domain.models.remote.payment.PaymentModel
    ): Call<sovchilar.uz.domain.models.remote.payment.PaymentResultModel>

    @POST("api/payment/confirm")
    fun confirmPayment(
        @Header("Authorization") authToken: String, @Body
        paymentConfirmModel: sovchilar.uz.domain.models.remote.payment.PaymentConfirmModel
    ): Call<sovchilar.uz.domain.models.remote.payment.PaymentConfirmResponseModel>

    @GET("api/payment/price")
    fun getPrice(@Header("Authorization") authToken: String): Call<sovchilar.uz.domain.models.remote.payment.PaymentPriceResponseModel>

    companion object {
        var BASE_URL = "http://176.96.241.238:3333/"
        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .protocols(listOf(Protocol.HTTP_1_1)).build()
            val retrofit =
                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
                    .baseUrl(BASE_URL).build()
            return retrofit.create(ApiService::class.java)
        }
    }

}
