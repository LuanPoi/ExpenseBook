package com.thepoi.expensebook.di

import com.thepoi.expensebook.data.repository.EntryRepositoryImpl
import com.thepoi.expensebook.data.repository.MonthlyExpenseRepositoryImpl
import com.thepoi.expensebook.data.repository.RecurringEntryRepositoryImpl
import com.thepoi.expensebook.domain.repository.EntryRepository
import com.thepoi.expensebook.domain.repository.MonthlyExpenseRepository
import com.thepoi.expensebook.domain.repository.RecurringEntryRepository
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