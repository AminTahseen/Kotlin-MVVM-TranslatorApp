package com.example.translatorapp_mvvm_kotlin.model.apiService

import com.example.translatorapp_mvvm_kotlin.model.Constants
import com.example.translatorapp_mvvm_kotlin.model.dto.TranslatedTextDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("get")
    suspend fun getTranslatedText(
        @Query("q") query: String,
        @Query("langpair") langPair:String
    ) : Response<TranslatedTextDTO>
    companion object {
        var retrofitService: APIInterface? = null
        fun getInstance() : APIInterface {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(APIInterface::class.java)
            }
            return retrofitService!!
        }
    }
}