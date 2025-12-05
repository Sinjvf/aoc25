package com.example.aoc25.util

import kotlin.math.max
import kotlin.math.min


/**
 *  объединение 2х пересекающихся диапазонов
 *  */
fun combineRanges(first: LongRange, second: LongRange): LongRange {
    return min(first.first, second.first)..max(first.last, second.last)
}

/**
 *  проверяет пересекаются ли указанные диапазоны.
 *  пересечением считается в тч "касание", т.е. если один начинается сразу за другим
 *  */
fun containsRanges(range: LongRange, host: LongRange): Boolean {
    return range.first in host || range.last in host || host.first in range || host.last in range
            || range.last + 1 == host.first || host.last + 1 == range.first
}

/**
 *  объединение списка диапазонов
 *
 *  меняет исходный список - внутри остаются объединенные и множество пустых диапазонов
 *  */
fun orRanges(ranges: MutableList<LongRange>) {
    for (i in 0..ranges.size - 1) {
        var range = ranges[i]
        if (range.isEmpty()) continue
        var has = true
        while (has) {
            has = false
            range = ranges[i]
            for (hostId in i + 1..ranges.size - 1) {
                val host = ranges[hostId]
                if (containsRanges(range, host)) {
                    has = true
                    ranges[i] = combineRanges(range, host)
                    range = ranges[i]
                    ranges[hostId] = LongRange.EMPTY
                }
            }
        }
    }
}

fun crossRanges(range1: LongRange, range2: LongRange): LongRange {
    return max(range1.first, range2.first)..min(range1.last, range2.last)
}
