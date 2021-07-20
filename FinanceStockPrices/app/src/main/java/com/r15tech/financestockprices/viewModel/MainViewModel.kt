package com.r15tech.financestockprices.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.r15tech.financestockprices.data.model.Company
import com.r15tech.financestockprices.data.repositories.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class MainViewModel(private val companyRepository: CompanyRepository) : ViewModel() {

    fun getAll(): LiveData<List<Company>> {
        return companyRepository.getAll()
    }

    fun insert(company: Company) = runBlocking {
        launch(Dispatchers.IO) {
            companyRepository.insert(company)
        }
    }

    fun deleteData(company: Company) = runBlocking {
        launch(Dispatchers.IO) {
            companyRepository.deleteData(company)
        }
    }

}

class MainViewModelFactory(private val repository: CompanyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}