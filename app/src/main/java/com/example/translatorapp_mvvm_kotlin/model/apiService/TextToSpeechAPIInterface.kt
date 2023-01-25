package com.example.translatorapp_mvvm_kotlin.model.apiService

import com.example.translatorapp_mvvm_kotlin.model.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface TextToSpeechAPIInterface {

    @GET(".")
    suspend fun getTextToSpeech(
        @Query("key") key: String,
        @Query("rapidapi-key") rapidApiKey: String,
        @Query("b64") b64: Boolean,
        @Query("src") text: String,
        @Query("hl") language_code: String,
        @Query("c") fileType: String,
        @Query("r") speed: Int,
    ): Response<ResponseBody>

//    @FormUrlEncoded
//    @POST(".")
//    suspend fun getTextToSpeech(
//        @Query("key") key: String,
//        @Query("rapidapi-key") rapidApiKey: String,
//        @Query("b64") b64: Boolean,
//        @Field("src") text: String,
//        @Field("hl") language_code: String,
//        @Field("c") fileType: String,
//        @Field("r") speed: Int,
//    ): Response<ResponseBody>

    companion object {
        private val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        private val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()


        var gson: Gson = GsonBuilder().setLenient().create()

        var retrofitService: TextToSpeechAPIInterface? = null
        fun getInstance(): TextToSpeechAPIInterface {
            if (retrofitService == null) {
                val retrofit = Retrofit
                    .Builder()
                    .baseUrl(Constants.TEXT_TO_SPEECH_API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitService = retrofit.create(TextToSpeechAPIInterface::class.java)
            }
            return retrofitService!!
        }
    }
}