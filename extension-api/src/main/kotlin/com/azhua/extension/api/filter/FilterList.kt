package com.azhua.extension.api.filter

class FilterList(vararg filters: Filter<*>) {
    val filters: List<Filter<*>> = filters.toList()
}

sealed class Filter<T>(val name: String, var state: T) {
    class Text(name: String) : Filter<String>(name, "")
    class Select(name: String, val values: Array<String>) : Filter<Int>(name, 0)
    class TriState(name: String) : Filter<Int>(name, STATE_IGNORE) {
        companion object {
            const val STATE_IGNORE = 0
            const val STATE_INCLUDE = 1
            const val STATE_EXCLUDE = 2
        }
    }
    class CheckBox(name: String) : Filter<Boolean>(name, false)
    class Group<T : Filter<*>>(name: String, state: List<T>) : Filter<List<T>>(name, state)
}
