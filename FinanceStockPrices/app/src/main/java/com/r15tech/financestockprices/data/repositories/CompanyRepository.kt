package com.r15tech.financestockprices.data.repositories

import com.r15tech.financestockprices.data.dao.CompanyDao
import com.r15tech.financestockprices.data.model.Company

class CompanyRepository(private val dao: CompanyDao) {

    fun getAll() = dao.getAll()

    suspend fun insert(company: Company) {
        dao.insert(company)
    }

    suspend fun deleteData(company: Company) {
        dao.deleteData(company)
    }

}