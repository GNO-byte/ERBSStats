package com.gno.erbs.erbs.stats.model

data class DoubleParameter<T>(
    val name: String,
    val minLvlValue: T,
    val maxLvlValue: T
)
