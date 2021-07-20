package com.r15tech.financestockprices.data.repositories

import com.r15tech.financestockprices.data.model.CompanyOverview
import com.r15tech.financestockprices.data.model.GlobalQuote
import com.r15tech.financestockprices.data.model.SearchCompany
import com.r15tech.financestockprices.data.services.APIService
import retrofit2.Response


class AddCompanyRepository(private val apiService: APIService) {

    suspend fun searchCompany(url: String): Response<SearchCompany> {
        return apiService.searchCompany(url)
    }

    suspend fun getCompanyQuote(url: String): Response<GlobalQuote> {
        return apiService.getCompanyQuote(url)
    }

    suspend fun getCompanyOverview(url: String): Response<CompanyOverview> {
        return apiService.getCompanyOverview(url)
    }
}