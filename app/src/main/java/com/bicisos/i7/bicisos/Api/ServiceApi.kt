package com.bicisos.i7.bicisos.Api

import com.bicisos.i7.bicisos.model.*
import com.bicisos.i7.bicisos.model.polizas.Login
import com.bicisos.i7.bicisos.model.reportes.Reporte
import com.bicisos.i7.bicisos.model.reportes.Reportes
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface ServiceApi {

    @Multipart
    @POST("/gttapi/contratacion")
    suspend fun sendEmailContract(
        @Header("sos-token") authorization: String?,
        @Part("data") data: RequestBody,
        @Part lateral: MultipartBody.Part,
        @Part pedal: MultipartBody.Part,
        @Part sillin: MultipartBody.Part,
        @Part manubrio: MultipartBody.Part,
        @Part pago: MultipartBody.Part,
    ): Response<ContractResp>

    @GET("/gttapi/polizas/{phone}")
    suspend fun loginGeneralContract(
        @Path("phone") phone: String,
    ): Response<PolizasResponse>

    @GET("/gttapi/talleres/readFile")
    suspend fun getTalleres(): Response<TallerResponse>

    @GET("/gttapi/reportes")
    suspend fun getReportes(): Response<Reportes>

    @POST("/gttapi/polizas/login")
    suspend fun loginContract(
        @Header("sos-token") authorization: String?,
        @Body body: Login,
    ): Response<ContractResponse>

    @POST("/gttapi/auth/login")
    suspend fun loginBicis(
        @Body body: LoginBicis,
    ): Response<UserResponse>

    @POST("/gttapi/auth/register")
    suspend fun registerBicis(
        @Body body: RegisterBicis,
    ): Response<UserResponse>

    @POST("/gttapi/auth/login/google")
    suspend fun loginGoogle(
        @Body body: RegisterBicis,
    ): Response<UserResponse>

    @POST("/gttapi/reportes/agregar")
    suspend fun reporteBici(
        @Body body: Reporte,
    ): Response<ContractResp>

    companion object {

        operator fun invoke(): ServiceApi {

            val builder = OkHttpClient().newBuilder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)

            builder.addInterceptor { chain ->
                val request: Request = chain.request().newBuilder().addHeader(
                    "sos-token",
                    "1bWv6Mpi85ZDkwK4AvJVZH2zA6hcdk0BecEoP4upNOLi2hLM4fzzcPmmSe8UUI5EcJoT4dySqIStZaqvCnlo4dLCASmhjZInXhRlcdc"
                ).build()
                chain.proceed(request)
            }

            return Retrofit.Builder()
                .baseUrl(ApiUrls.urlApi)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceApi::class.java)
        }
    }
}
