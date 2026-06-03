package com.azhua.data.di

import com.azhua.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDonghuaRepository(impl: DonghuaRepositoryImpl): DonghuaRepository

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
}
