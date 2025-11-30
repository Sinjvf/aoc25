package com.example.aoc25.util

import java.util.LinkedList
import java.util.PriorityQueue

/**
 * @author Тамара Синева on 17.12.2023
 */
interface Graph<Node, Data : GraphData> {
    fun next(data: NodesWithData<Node, Data>): List<NodesWithData<Node, Data>>

    fun nextExclude(data: NodesWithData<Node, Data>, exclude: Set<Node>): List<NodesWithData<Node, Data>> =
        next(data)
            .filter { it.node !in exclude }
}


interface GraphData {
    fun plus(a: GraphData): GraphData
    fun getLong(): Long
}
fun <Node, Data : GraphData> Graph<Node, Data>.searchLoop(
    startNode: Node,
    startData: Data,
    nextF: ((data: NodesWithData<Node, Data>) -> List<NodesWithData<Node, Data>>)? = null
): List<List<Pair<Node, Node>>> {

    val visitedR= mutableListOf<Pair<Node, Node>>()

    return searchLoop_(startNode, startData, startNode, visitedR)
}

private fun <Node, Data : GraphData> Graph<Node, Data>.searchLoop_(
    startNode: Node,
    startData: Data,
    endNode: Node,
    visitedR: List<Pair<Node, Node>> ,
    nextF: ((data: NodesWithData<Node, Data>) -> List<NodesWithData<Node, Data>>)? = null
): List<List<Pair<Node, Node>>> {

    val currData = NodesWithData(
        null,
        startNode,
        startData,
    )

    //TODO
    if (visitedR.size >3) return emptyList()

    if (currData.node == endNode && visitedR.isNotEmpty()) {
        return listOf(visitedR)
    }
    val nexts = nextF?.invoke(currData) ?: next(currData).filter {
        !visitedR.contains(it.node to currData.node) && !visitedR.contains(currData.node to it.node)
    }
    if (nexts.isEmpty())return listOf(visitedR)
   // println("${nexts.size}   ${visitedR.size}")
    val res =  mutableListOf<List<Pair<Node, Node>>>()


    nexts.forEach { nextData ->
//println("visit ${currData.node to nextData.node} $visitedR")
        val newData = currData.data.plus(nextData.data) as Data
      //  visitedR.add(currData.node to nextData.node)
        val ppp = searchLoop_(
            nextData.node,
            newData,
            endNode,
            buildList{addAll(visitedR); add(currData.node to nextData.node)}
            )
        if (ppp.isNotEmpty()) {
            res.addAll(ppp)
        }
    }

    return res
}

fun <Node, Data : GraphData> Graph<Node, Data>.search(
    startNode: Node,
    startData: Data,
    isFinish: (NodesWithData<Node, Data>) -> Boolean,
    onVisited: (NodesWithData<Node, Data>) -> Unit = { _ -> },
    heuristic: (NodesWithData<Node, Data>)  -> Data? = { null },
    isFFFinish: (NodesWithData<Node, Data>, Map<Node, Pair<Node, Data>>, PriorityQueue<NodesWithData<Node, Data>>) -> Boolean = { _, _, _ -> false },
    clear: (Node) -> Unit = {},
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
                if (newData.getLong() <= (searchTree[nextData.node]?.second?.getLong() ?: Long.MAX_VALUE)) {
                    val middleData = NodesWithData(
                        currData.node,
                        nextData.node,
                        newData)
                    val dataWithHeu: Data = (heuristic(middleData)?.let { it.plus(newData) as Data }) ?: newData
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

fun <Node, Data : GraphData> Graph<Node, Data>.findAll(
    startNode: Node,
    startData: Data,
    end: Node,
): List<SearchResult<Node, Data>>  = buildList{
    val search1Res = search(
        startNode = startNode,
        startData = startData,
        isFinish = { nodesWithData -> nodesWithData.node == end },
    )
    val search1 = search1Res.reversedPathTo(end)
    val cost = search1?.cost?.getLong()?:return emptyList()

    val path = search1.path
    add(search1Res)

  //  println("findAll path = $path")
    path.forEach {
        addAll(findAnother(startNode, startData, end, search1.cost.getLong(), setOf(it)))
    }
}

fun <Node, Data : GraphData> Graph<Node, Data>.findAnother(
    startNode: Node,
    startData: Data,
    end: Node,
    cost: Long,
    exclude: Set<Node>
): List<SearchResult<Node, Data>> {
    if (exclude.contains(startNode) || exclude.contains(end)) return emptyList()

    val itSearchRes = search(
        startNode = startNode,
        startData = startData,
        isFinish = { nodesWithData -> nodesWithData.node == end },
        nextF = {nextExclude(it, exclude)}
    )
    val itSearch = itSearchRes.reversedPathTo(end)
    return buildList {
     //   println("findAnother exclude= $exclude  = $itSearch")
        if (itSearch?.cost?.getLong() == cost) {
            add(itSearchRes)
           /// println("findAnother exclude= $exclude  = $itSearchRes")
            itSearch.path.forEach {
                addAll(
                    findAnother(startNode, startData, end, cost, buildSet { addAll(exclude);add(it) })
                )
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

// возвращает "был ли он прерван"
fun <Node, Data : GraphData> Graph<Node, Data>.fullPath(
    startNode: Node,
    startData: Data,
    isFinish: (NodesWithData<Node, Data>) -> Boolean,
    onVisited: (NodesWithData<Node, Data>) -> Unit = { _ -> }
): Boolean {
    val queue =/* PriorityQueue(compareBy<Pair<Node, Data>> { it.second.getLong() })*/
        LinkedList<NodesWithData<Node, Data>>()
    queue.add(NodesWithData(null, startNode, startData))

    while (true) {
        val nodesWithData = queue.poll() ?: return false
        onVisited(nodesWithData)

        if (isFinish(nodesWithData)) return true

        next(nodesWithData)
            .forEach { newNData ->
                val newData = nodesWithData.data.plus(newNData.data) as Data
                queue.add(NodesWithData(nodesWithData.node, newNData.node, newData))
            }
    }
}

open class NodesWithData<Node, Data : GraphData>(
    open val prev: Node?,
    open val node: Node,
    open val data: Data,
) {
    override fun toString(): String {
        return /*"prev = $prev,*/ "node = $node, data = $data"
    }
}

// возвращает все найденные пути(nолько количество)
fun <Node, Data : GraphData> Graph<Node, Data>.allPath(
    startNode: Node,
    startData: Data,
    endNode: Node
): List<Node > {
    val paths = mutableListOf<List<Node>>()
    val queue = PriorityQueue(compareBy<NodesWithDataAndPath<Node, Data>> { it.data.getLong() })
    queue.add(NodesWithDataAndPath(null, startNode, startData, paths))
    val result = mutableListOf<Node>()

    while (true) {
        val nodesData = queue.poll() ?: return result
        if (nodesData.node==endNode){
            result.add(startNode)
        }

        next(nodesData)
            .forEach { newNData ->
                val newData = nodesData.data.plus(newNData.data) as Data
                queue.add(NodesWithDataAndPath(nodesData.node, newNData.node, newData, listOf()))
            }
    }
}

class NodesWithDataAndPath<Node, Data : GraphData>(
    override val prev: Node?,
    override val node: Node,
    override val data: Data,
    val paths: List<List<Node>>
):NodesWithData<Node, Data>(prev, node, data) {
    override fun toString(): String {
        return /*"prev = $prev,*/ "node = $node, data = $data"
    }
}


