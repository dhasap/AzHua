package com.azhua.core.database.di

import android.content.Context
import androidx.room.Room
import com.azhua.core.database.AzHuaDatabase
import com.azhua.core.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AzHuaDatabase {
        return Room.databaseBuilder(
            context,
            AzHuaDatabase::class.java,
            "azhua_database",
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDonghuaDao(db: AzHuaDatabase): DonghuaDao = db.donghuaDao()

    @Provides
    fun provideEpisodeDao(db: AzHuaDatabase): EpisodeDao = db.episodeDao()

    @Provides
    fun provideCategoryDao(db: AzHuaDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideHistoryDao(db: AzHuaDatabase): HistoryDao = db.historyDao()

    @Provides
    fun provideDownloadDao(db: AzHuaDatabase): DownloadDao = db.downloadDao()
}
