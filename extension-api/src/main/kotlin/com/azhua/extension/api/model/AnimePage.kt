package com.azhua.extension.api.model

data class AnimePage(
    val animes: List<AnimeInfo>,
    val hasNextPage: Boolean,
    val currentPage: Int,
)
