package com.example.expensebook.data.data_source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensebook.data.data_source.local.dao.EntryDao
import com.example.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.example.expensebook.data.data_source.local.dao.RecurringEntryDao
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.data.data_source.local.entities.MonthlyExpense
import com.example.expensebook.data.data_source.local.entities.RecurringEntry

@Database(
    entities = [
        MonthlyExpense::class,
        Entry::class,
        RecurringEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun monthlyExpenseDao(): MonthlyExpenseDao

    abstract fun entryDao(): EntryDao

    abstract fun recurringEntryDao(): RecurringEntryDao

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