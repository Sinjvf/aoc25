package com.example.aoc25.util

import java.util.LinkedList
import java.util.PriorityQueue

/**
 * @author Тамара Синева on 17.12.2023
 */
interface Graph<Node, Data : GraphData> {
    fun next(data: NodesWithData<Node, Data>): List<NodesWithData<Node, Data>>

    fun nextExclude(
        data: NodesWithData<Node, Data>,
        exclude: Set<Node>
    ): List<NodesWithData<Node, Data>> =
        next(data)
            .filter { it.node !in exclude }
}

abstract class SimpleGraph<Node> : Graph<Node, EmptyGraphData> {
    abstract fun next(node: Node): List<Node>
    override fun next(data: NodesWithData<Node, EmptyGraphData>): List<NodesWithData<Node, EmptyGraphData>> {
        return next(data.node).map { NodesWithData(data.node, it, EmptyGraphData) }
    }
}

interface GraphData {
    fun plus(a: GraphData): GraphData
    fun getLong(): Long
}

fun <Node, Data : GraphData> Graph<Node, Data>.search(
    startNode: Node,
    startData: Data,
    isFinish: (NodesWithData<Node, Data>) -> Boolean,
    onVisited: (NodesWithData<Node, Data>) -> Unit = { _ -> },
    heuristic: (NodesWithData<Node, Data>) -> Data? = { null },
    isFFFinish: (NodesWithData<Node, Data>, Map<Node, Pair<Node, Data>>, PriorityQueue<NodesWithData<Node, Data>>) -> Boolean = { _, _, _ -> false },
    clear: (Node) -> Unit = {},
    // переопределение получения списка next. Хз, зачем
    nextF: ((data: NodesWithData<Node, Data>) -> List<NodesWithData<Node, Data>>)? = null
): SearchResult<Node, Data> {
    val queue = PriorityQueue(compareBy<NodesWithData<Node, Data>> { it.data.getLong() })
    queue.add(NodesWithData(null, startNode, startData))
    val searchTree = mutableMapOf(startNode to (startNode to startData))

    while (true) {
        val currData = queue.poll() ?: return SearchResult(startNode, null, searchTree)
        onVisited(currData)

        if (isFinish(currData) || isFFFinish(currData, searchTree, queue)) return SearchResult(
            startNode,
            currData.node,
            searchTree
        )
        val nexts = nextF?.invoke(currData) ?: next(currData)
//println( "     current ${currData.node}, next =${nexts.map{it.node}}")
        nexts
            .filter { it.node !in searchTree }
            .forEach { nextData ->
                val newData = currData.data.plus(nextData.data) as Data
                if (newData.getLong() <= (searchTree[nextData.node]?.second?.getLong()
                        ?: Long.MAX_VALUE)
                ) {
                    val middleData = NodesWithData(
                        currData.node,
                        nextData.node,
                        newData
                    )
                    val dataWithHeu: Data =
                        (heuristic(middleData)?.let { it.plus(newData) as Data }) ?: newData
                    queue.add(
                        NodesWithData(
                            currData.node,
                            nextData.node,
                            dataWithHeu,
                        )
                    )
                    searchTree[nextData.node] = currData.node.apply { clear(this) } to newData
                }
            }
    }
}

data class SearchPath<Node, Data : GraphData>(
    val path: List<Node>,
    val cost: Data,
) : List<Node> by path

fun <Node, Data : GraphData> SearchResult<Node, Data>.pathTo(node: Node): SearchPath<Node, Data>? {
    val reversed = reversedPathTo(node)
    val cost = reversed?.cost ?: return null
    val path = reversed.path.asReversed()
    return SearchPath(path, cost)
}

fun <Node, Data : GraphData> SearchResult<Node, Data>.reversedPathTo(node: Node): SearchPath<Node, Data>? {
    val cost = searchTree[node]?.second ?: return null
    val path = buildList {
        var current = node
        while (true) {
            add(current)
            val previous = searchTree.getValue(current).first
            if (previous == current) break
            current = previous
        }
    }
    return SearchPath(path, cost)
}


fun <Node, Data : GraphData> SearchResult<Node, Data>.path(): SearchPath<Node, Data>? =
    when (destination) {
        null -> null
        else -> pathTo(destination)
    }

data class SearchResult<Node, Data : GraphData>(
    val startedFrom: Node,
    val destination: Node?,
    val searchTree: Map<Node, Pair<Node, Data>>,
)


open class NodesWithData<Node, Data : GraphData>(
    open val prev: Node?,
    open val node: Node,
    open val data: Data,
) {
    override fun toString(): String {
        return /*"prev = $prev,*/ "node = $node, data = $data"
    }
}

interface Condition<Node> {
    fun condition(node: Node): Boolean
}

fun <Node> List<Pair<Condition<Node>, Boolean>>.conditionsKey() =
    map { it.second }.joinToString(separator = "")

fun <Node> List<Pair<Condition<Node>, Boolean>>.check() =
    isEmpty() || all { it.second }

fun <Node> List<Pair<Condition<Node>, Boolean>>.compute(node: Node) =
    map { it.first to (it.second || it.first.condition(node)) }


fun <Node> SimpleGraph<Node>.countAll(
    start: Node,
    end: Node,
    // условия при которых путь засчитывается (должны быть выполнены все)
    // в списке задается интерфейс вычисления условия и текущее вычисленное значение
    conditions: List<Pair<Condition<Node>, Boolean>>,
    cache: MutableMap<String, Long>
): Long {
    val key = "$start${conditions.conditionsKey()}"
    if (cache.containsKey(key)) {
        return cache[key]!!
    }

    if (start == end) {
        if (conditions.check()) {
            return 1
        }
        return 0
    }

    var result: Long = 0
    for (to in next(start)) {
        result += countAll(to, end, conditions.compute(to), cache)
    }

    cache[key] = result
    return result
}


// возвращает все найденные пути(nолько количество)
// ПОЛНЫЙ ОБХОД!!!!
fun <Node, Data : GraphData> Graph<Node, Data>.allPath(
    startNode: Node,
    startData: Data,
    isEndNode: (Node) -> Boolean,
    exclude: (Node) -> Boolean = { false }
): List<Node> {
    val queue = PriorityQueue(compareBy<NodesWithDataAndPath<Node, Data>> { it.data.getLong() })
    queue.add(NodesWithDataAndPath(null, startNode, startData, listOf()))
    val result = mutableListOf<Node>()

    while (true) {
        val nodesData = queue.poll() ?: return result
        when {
            exclude(nodesData.node) -> continue
            isEndNode(nodesData.node) -> result.add(startNode)

            else ->
                next(nodesData)
                    .forEach { newNData ->
                        val newData = nodesData.data.plus(newNData.data) as Data
                        queue.add(
                            NodesWithDataAndPath(
                                nodesData.node,
                                newNData.node,
                                newData,
                                listOf()
                            )
                        )
                    }
        }
    }
}

class NodesWithDataAndPath<Node, Data : GraphData>(
    override val prev: Node?,
    override val node: Node,
    override val data: Data,
    val paths: List<List<Node>>
) : NodesWithData<Node, Data>(prev, node, data) {
    override fun toString(): String {
        return /*"prev = $prev,*/ "node = $node, data = $data"
    }
}

object EmptyGraphData : GraphData {
    override fun plus(a: GraphData): GraphData {
        if (a !is EmptyGraphData) throw IllegalArgumentException("expext EmptyGraphData class")
        return EmptyGraphData
    }

    override fun getLong(): Long = 0

    override fun toString(): String {
        return ""
    }
}

class LongGraphData(val i: Long) : GraphData {
    override fun plus(a: GraphData): GraphData {
        if (a !is LongGraphData) throw IllegalArgumentException("expext LongGraphData class")
        return LongGraphData(i + a.i)
    }

    override fun getLong(): Long = i

    override fun toString(): String {
        return i.toString()
    }
}

class StringGraphData(val i: String) : GraphData {
    override fun plus(a: GraphData): GraphData {
        if (a !is StringGraphData) throw IllegalArgumentException("expext StringGraphData class")

        return StringGraphData("$i${a.i}")
    }

    override fun getLong(): Long = i.length.toLong()

    override fun toString(): String {
        return i
    }
}



