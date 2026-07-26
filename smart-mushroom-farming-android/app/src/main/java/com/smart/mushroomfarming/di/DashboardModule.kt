package com.smart.mushroomfarming.di

import com.smart.mushroomfarming.data.repository.DemoDashboardRepository
import com.smart.mushroomfarming.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        demoDashboardRepository: DemoDashboardRepository
    ): DashboardRepository
}
