package com.azhua.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.azhua.core.database.converter.Converters
import com.azhua.core.database.dao.*
import com.azhua.core.database.entity.*

@Database(
    entities = [
        DonghuaEntity::class,
        EpisodeEntity::class,
        CategoryEntity::class,
        DonghuaCategoryEntity::class,
        HistoryEntity::class,
        DownloadEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AzHuaDatabase : RoomDatabase() {
    abstract fun donghuaDao(): DonghuaDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun categoryDao(): CategoryDao
    abstract fun historyDao(): HistoryDao
    abstract fun downloadDao(): DownloadDao
}
