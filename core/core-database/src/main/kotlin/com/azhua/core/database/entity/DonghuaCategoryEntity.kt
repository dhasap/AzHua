package com.azhua.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "donghua_category_table",
    primaryKeys = ["donghua_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = DonghuaEntity::class,
            parentColumns = ["id"],
            childColumns = ["donghua_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class DonghuaCategoryEntity(
    @ColumnInfo(name = "donghua_id")
    val donghuaId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
)
