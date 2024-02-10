package com.thepoi.expensebook.data.data_source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thepoi.expensebook.data.data_source.local.dao.EntryDao
import com.thepoi.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.data.data_source.local.entities.MonthlyExpense

@Database(
    entities = [
        MonthlyExpense::class,
        Entry::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun monthlyExpenseDao(): MonthlyExpenseDao

    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "expense_book_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}