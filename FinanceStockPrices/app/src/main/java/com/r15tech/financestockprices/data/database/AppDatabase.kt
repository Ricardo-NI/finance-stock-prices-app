package com.r15tech.financestockprices.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.r15tech.financestockprices.data.dao.CompanyDao
import com.r15tech.financestockprices.data.model.Company

@Database(entities = [Company::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "financesp_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}