package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "sort_order")
    val order: Int = 0,
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
)
