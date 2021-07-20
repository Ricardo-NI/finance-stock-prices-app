package com.r15tech.financestockprices.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.r15tech.financestockprices.data.model.*
import com.r15tech.financestockprices.data.repositories.AddCompanyRepository


class AddCompanyViewModel(private val repository: AddCompanyRepository) : ViewModel() {

    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State> = _repos

    suspend fun searchCompany(company: String) {

        _repos.postValue(State.Loading)

        val call =
            repository.searchCompany("query?function=SYMBOL_SEARCH&keywords=$company&apikey=1GJ6P2OCZHVDQTYN")
        val result: SearchCompany? = call.body()

        if (call.isSuccessful) {
            val companies: List<Company>? = result?.bestMatches
            if (companies != null) {
                _repos.postValue(State.SuccessCompany(companies))
            }

        } else {
            val error = call.errorBody()?.charStream().toString()
            _repos.postValue(State.Error(error))
        }
    }

    suspend fun getCompanyQuote(company: String) {

        _repos.postValue(State.Loading)

        val call =
            repository.getCompanyQuote("query?function=GLOBAL_QUOTE&symbol=$company&apikey=1GJ6P2OCZHVDQTYN")
        val result: GlobalQuote? = call.body()

        if (call.isSuccessful) {

            val companyQ: CompanyQuote? = result?.globalQuote

            if (companyQ != null) {
                _repos.postValue(State.SuccessCompanyQuote(companyQ))
            }
        } else {
            val error = call.errorBody()?.charStream().toString()
            _repos.postValue(State.Error(error))
        }

    }

    suspend fun getCompanyOverview(company: String) {

        _repos.postValue(State.Loading)

        val call =
            repository.getCompanyOverview("query?function=OVERVIEW&symbol=$company&apikey=1GJ6P2OCZHVDQTYN")
        val result: CompanyOverview? = call.body()

        if (call.isSuccessful) {
            if (result != null) {
                val companyO: CompanyOverview = result

                val test: String? = companyO.symbol
                if (!test.isNullOrEmpty()) {
                    _repos.postValue(State.SuccessCompanyOverview(companyO))
                } else {
                    val error = "null data."
                    _repos.postValue(State.Error(error))
                }
            }
        } else {
            val error = call.errorBody()?.charStream().toString()
            _repos.postValue(State.Error(error))
        }

    }

    sealed class State {
        object Loading : State()
        data class SuccessCompany(val list: List<Company>) : State()
        data class SuccessCompanyQuote(val obj: CompanyQuote) : State()
        data class SuccessCompanyOverview(val obj: CompanyOverview) : State()
        data class Error(val error: String) : State()
    }
}

class AddCompanyViewModelFactory(private val repository: AddCompanyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCompanyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddCompanyViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}

