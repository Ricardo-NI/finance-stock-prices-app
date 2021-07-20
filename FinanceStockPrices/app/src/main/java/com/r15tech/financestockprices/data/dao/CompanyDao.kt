package com.r15tech.financestockprices.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.r15tech.financestockprices.data.model.Company

@Dao
interface CompanyDao {

    @Query("SELECT * FROM company ORDER BY symbol ASC")
    fun getAll(): LiveData<List<Company>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(company: Company)

    @Delete
    suspend fun deleteData(company: Company)
}