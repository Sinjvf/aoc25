package com.example.aoc25.util

import kotlin.math.max
import kotlin.math.min

fun <T> List<T>.sameAs(list: List<T>): Boolean {
    if (size != list.size) return false

    for ((id, ch) in this.withIndex()) {
        if (ch != list[id]) return false
    }

    return true
}

fun <T> List<T>.diffByOne( list: List<T>): Pair<Boolean, Int?> {
    var pos: Int? = null
    var diff: Int = 0
    if (size != list.size) return Pair(false, pos)

    for ((id, ch) in this.withIndex()) {
        if (ch != list[id]) {
            if (diff > 0)
                return Pair(false, pos)
            else {
                diff++
                pos = id
            }
        }
    }

    return Pair(true, pos)
}

fun andRanges(ranges: List<IntRange>,  minVal:Int,  maxVal:Int): IntRange {
    var i = 1
    var range = if (ranges.isEmpty()) minVal..maxVal else ranges[0]
    while (i < ranges.size) {
        val newRange = ranges[i]
        var newMin = range.first
        var newMax = range.last
        if (newRange.first in range) {
            newMin = max(newMin, newRange.first)
        }
        if (newRange.last in range) {
            newMax = min(newMax, newRange.last)
        }
        if (newRange.last < range.first
            || newRange.first > range.last
            || newMax < newMin
        ) {
            return IntRange.EMPTY
        }

        range = newMin..newMax
        i++
    }
    return (range)
}

fun orRanges(ranges: List<IntRange>,  minVal:Int,  maxVal:Int): IntRange {
    var i = 1
    var range = if (ranges.isEmpty()) minVal..maxVal else ranges[0]
    while (i < ranges.size) {
        val newRange = ranges[i]
        range = min(range.first, newRange.first)..max(range.last, newRange.last)
        i++
    }
    return (range)
}