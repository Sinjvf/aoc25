package com.example.aoc25.util

class Rearrangement(
    parts: List<RearrangementData>
) {
    private val map = mutableMapOf<RearrangementData, Long>()
    private val sortedParts =
        parts.sortedWith { o1, o2 -> o1?.getLength()?.compareTo(o2?.getLength() ?: 0) ?: 0 }

    init {
        sortedParts.forEach { count(it) }
    }

    fun count(target: RearrangementData): Long {
        val cached = map[target]
        if (cached != null) return cached
        val res = sortedParts.map {
            when {
                target.getLength() < it.getLength() -> 0L
                (target.getLength() > it.getLength()) && target.startsWith(it) ->
                    count(target.subDataFrom(it.getLength()))

                target == it -> 1L
                else -> 0L
            }
        }
        return res.fold(0L) { add, it -> add + it }.apply {
            map[target] = this
        }
    }

    fun canComposite(target: RearrangementData): Boolean = count(target) != 0L
}

interface RearrangementData {

    fun getLength(): Int

    fun startsWith(other: RearrangementData): Boolean

    fun subDataFrom(index: Int): RearrangementData
}

data class StringData(val data: String) : RearrangementData {
    override fun getLength(): Int = data.length

    override fun startsWith(other: RearrangementData): Boolean {
        if (other !is StringData) throw IllegalArgumentException("StringData expected")
        return data.startsWith(other.data)
    }

    override fun subDataFrom(index: Int): RearrangementData = StringData(data.substring(index))
}

fun String.toStringData() = StringData(this)