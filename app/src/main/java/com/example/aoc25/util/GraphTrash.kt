package com.example.aoc25.util

import java.util.LinkedList

// старые методы, не факт, что сработают


fun <Node, Data : GraphData> Graph<Node, Data>.searchLoop(
    startNode: Node,
    startData: Data,
    nextF: ((data: NodesWithData<Node, Data>) -> List<NodesWithData<Node, Data>>)? = null
): List<List<Pair<Node, Node>>> {

    val visitedR = mutableListOf<Pair<Node, Node>>()

    return searchLoop_(startNode, startData, startNode, visitedR)
}

private fun <Node, Data : GraphData> Graph<Node, Data>.searchLoop_(
    startNode: Node,
    startData: Data,
    endNode: Node,
    visitedR: List<Pair<Node, Node>>,
    nextF: ((data: NodesWithData<Node, Data>) -> List<NodesWithData<Node, Data>>)? = null
): List<List<Pair<Node, Node>>> {

    val currData = NodesWithData(
        null,
        startNode,
        startData,
    )

    //TODO
    if (visitedR.size > 3) return emptyList()

    if (currData.node == endNode && visitedR.isNotEmpty()) {
        return listOf(visitedR)
    }
    val nexts = nextF?.invoke(currData) ?: next(currData).filter {
        !visitedR.contains(it.node to currData.node) && !visitedR.contains(currData.node to it.node)
    }
    if (nexts.isEmpty()) return listOf(visitedR)
    // println("${nexts.size}   ${visitedR.size}")
    val res = mutableListOf<List<Pair<Node, Node>>>()


    nexts.forEach { nextData ->
//println("visit ${currData.node to nextData.node} $visitedR")
        val newData = currData.data.plus(nextData.data) as Data
        //  visitedR.add(currData.node to nextData.node)
        val ppp = searchLoop_(
            nextData.node,
            newData,
            endNode,
            buildList { addAll(visitedR); add(currData.node to nextData.node) }
        )
        if (ppp.isNotEmpty()) {
            res.addAll(ppp)
        }
    }

    return res
}


fun <Node, Data : GraphData> Graph<Node, Data>.findAll(
    startNode: Node,
    startData: Data,
    end: Node,
): List<SearchResult<Node, Data>> = buildList {
    val search1Res = search(
        startNode = startNode,
        startData = startData,
        isFinish = { nodesWithData -> nodesWithData.node == end },
    )
    val search1 = search1Res.reversedPathTo(end)
    val cost = search1?.cost?.getLong() ?: return emptyList()

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
        nextF = { nextExclude(it, exclude) }
    )
    val itSearch = itSearchRes.reversedPathTo(end)
    return buildList {
        //   println("findAnother exclude= $exclude  = $itSearch")
        if (itSearch?.cost?.getLong() == cost) {
            add(itSearchRes)
            /// println("findAnother exclude= $exclude  = $itSearchRes")
            itSearch.path.forEach {
                addAll(
                    findAnother(
                        startNode,
                        startData,
                        end,
                        cost,
                        buildSet { addAll(exclude);add(it) })
                )
            }
        }
    }
}

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
