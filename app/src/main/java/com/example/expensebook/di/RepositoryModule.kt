package com.example.expensebook.di

import com.example.expensebook.data.repository.EntryRepositoryImpl
import com.example.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.example.expensebook.data.repository.RecurringEntryRepositoryImpl
import com.example.expensebook.domain.repository.EntryRepository
import com.example.expensebook.domain.repository.MonthlyExpenseRepository
import com.example.expensebook.domain.repository.RecurringEntryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideEntryRepository(impl: EntryRepositoryImpl): EntryRepository

    @Singleton
    @Binds
    abstract fun provideRecurringEntryRepository(impl: RecurringEntryRepositoryImpl): RecurringEntryRepository

    @Singleton
    @Binds
    abstract fun provideMonthlyExpenseRepository(impl: MonthlyExpenseRepositoryImpl): MonthlyExpenseRepository
}