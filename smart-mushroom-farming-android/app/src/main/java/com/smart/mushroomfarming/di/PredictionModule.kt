package com.smart.mushroomfarming.di

import com.smart.mushroomfarming.data.repository.InMemoryPredictionRepository
import com.smart.mushroomfarming.domain.repository.PredictionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PredictionModule {

    @Binds
    @Singleton
    abstract fun bindPredictionRepository(
        inMemoryPredictionRepository: InMemoryPredictionRepository
    ): PredictionRepository
}
