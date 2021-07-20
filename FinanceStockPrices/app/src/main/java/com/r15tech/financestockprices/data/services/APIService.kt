package com.r15tech.financestockprices.data.services

import com.r15tech.financestockprices.data.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun searchCompany(@Url url: String): Response<SearchCompany>

    @GET
    suspend fun getCompanyOverview(@Url url: String): Response<CompanyOverview>

    @GET
    suspend fun getCompanyQuote(@Url url: String): Response<GlobalQuote>

    companion object {

        var apiService: APIService? = null

        fun getInstance(): APIService {

            if (apiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://www.alphavantage.co/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiService = retrofit.create(APIService::class.java)
            }
            return apiService!!
        }

    }
}