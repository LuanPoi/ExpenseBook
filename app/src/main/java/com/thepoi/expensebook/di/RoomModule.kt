package com.thepoi.expensebook.di

import android.app.Application
import com.thepoi.expensebook.data.data_source.local.LocalDatabase
import com.thepoi.expensebook.data.data_source.local.dao.EntryDao
import com.thepoi.expensebook.data.data_source.local.dao.MonthlyExpenseDao
import com.thepoi.expensebook.data.data_source.local.dao.RecurringEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return LocalDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideEntryDao(database: LocalDatabase): EntryDao {
        return database.entryDao()
    }

    @Singleton
    @Provides
    fun provideRecurringDao(database: LocalDatabase): RecurringEntryDao {
        return database.recurringEntryDao()
    }

    @Singleton
    @Provides
    fun provideMonthlyExpenseDao(database: LocalDatabase): MonthlyExpenseDao {
        return database.monthlyExpenseDao()
    }
}