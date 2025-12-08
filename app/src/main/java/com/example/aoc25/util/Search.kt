package com.example.aoc25.util


/**
 * бинарный поиск по сортированному списку.
 * start, end - начальный и конечный индексы поиска и результат поиска
 *
 * возвращает индекс найденного элемента
 * либо -1 если искомый не найден
 * */
fun binarySearch(
    start: Pair<Int, SearchResultType>,
    end: Pair<Int, SearchResultType>,
    compute: (Int) -> SearchResultType
): Int {
    return when (end.first - start.first) {
        0 -> {
            val res = compute(start.first)
            if (res == SearchResultType.LESS || res == SearchResultType.MORE) -1
            else start.first
        }

        1 -> {
            when {
                start.second == SearchResultType.LESS ->
                    if (end.second == SearchResultType.MORE) -1
                    else end.first

                else -> start.first
            }
        }

        else -> {
            val middleId = (end.first + start.first) / 2
            when (val middleVal = compute(middleId)) {
                SearchResultType.EQUAL -> middleId

                SearchResultType.MORE,
                SearchResultType.MORE_OR_EQUAL -> binarySearch(
                    start,
                    middleId to middleVal,
                    compute
                )

                else -> binarySearch(
                    middleId to middleVal,
                    end,
                    compute
                )

            }
        }
    }
}

enum class SearchResultType {
    LESS,
    MORE,
    EQUAL,
    LESS_OR_EQUAL,
    MORE_OR_EQUAL
}