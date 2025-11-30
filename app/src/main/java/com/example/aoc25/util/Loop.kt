package com.example.aoc25.util

/**
 *
 *
 * @author Тамара Синева on 20.12.2023
 */

/*пока только для больших циклов*/
fun <T : CountedLoopRes> findSum(
    empty: T,
    compute: () -> T,
    n: Int
): T {

    val data = findLoopSize(compute, n)
    if (data.begin == -1) {
        /*цикл не найден*/
        return (0 until n).map {
            data.map[it]!!
        }.let {
            if (it.isEmpty()) empty
            else it.reduce { prev, next -> prev.plus(next) as T }
        }
    }
    val sumBeforeLoop = (0 until data.begin).map {
        data.map[it]!!
    }.let {
        if (it.isEmpty()) empty
        else it.reduce { prev, next -> prev.plus(next) as T }
    }
    val sumInLoop = (data.begin..(data.begin + data.size - 1)).map {
        data.map[it]!!
    }.let {
        if (it.isEmpty()) empty
        else it.reduce { prev, next -> prev.plus(next) as T }
    }
    val sumInLastLoop = (0..data.findNLoopPosition(n.toLong())).map {
        data.map[data.begin + it]!!
    }.let {
        if (it.isEmpty()) empty
        else it.reduce { prev, next -> prev.plus(next) as T }
    }

    return sumBeforeLoop.plus(sumInLoop.mult(data.findFullLoopCount(n.toLong()))).plus(sumInLastLoop) as T
}

fun <T : LoopRes> findLoopSize(
    compute: () -> T,
    max: Int = Int.MAX_VALUE
): LoopData<T> {
    val prev = mutableListOf<Int>()
    val map = mutableMapOf<Int, T>()
    var current = -1
    var id = 0
    var firstLoop = -1
    var loopSize = -1
    main@ while (true) {
        val data = compute()
        current = data.getInt()
        prev.add(current)
        map.put(id, data)
        if (id >= max) break
        id++

        val ids = prev.mapIndexed { ids, it -> ids to it }.filter { it.second == current }.map { it.first }
        if (ids.size > 2) {
            val first = ids[0]
            val second = ids[1]
            var i = first
            while (i < second && i + second < prev.size) {
                if (prev[first + i] != prev[second + i]) {
                    continue@main
                }
                i++
            }
            firstLoop = first
            loopSize = second - first
            break
        }
    }
    println("firstLoop = $firstLoop, loopSize = $loopSize")
    return LoopData(firstLoop, loopSize, map)
}

class LoopData<T : LoopRes>(
    val begin: Int,
    val size: Int,
    val map: Map<Int, T>
) {
    fun findNLoopRes(n: Long): T {
        val position = findNLoopPosition(n)
        return map[begin + position]!!
    }
    fun findNLoopPosition(n: Long): Int = ((n - 1 - begin) % size).toInt()

    fun findFullLoopCount(n:Long):Int = ((n - 1 - begin) / size).toInt()

}

interface LoopRes {
    fun getInt(): Int
}

interface CountedLoopRes : LoopRes {
    fun plus(a: CountedLoopRes): CountedLoopRes
    fun mult(a: Int): CountedLoopRes
}