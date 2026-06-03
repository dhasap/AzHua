package com.azhua.core.model

/**
 * Domain model for a Library Category.
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val order: Int = 0,
    val isDefault: Boolean = false,
)

/**
 * Category with its associated Donghua entries.
 */
data class CategoryWithDonghua(
    val category: Category,
    val donghuaList: List<Donghua>,
)
