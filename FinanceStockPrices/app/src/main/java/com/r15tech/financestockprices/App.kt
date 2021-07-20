package com.r15tech.financestockprices

import android.app.Application
import com.r15tech.financestockprices.data.database.AppDatabase
import com.r15tech.financestockprices.data.repositories.CompanyRepository

class App : Application() {

    val database by lazy{ AppDatabase.getDatabase(this)}
    val repository by lazy {CompanyRepository(database.companyDao()) }
}