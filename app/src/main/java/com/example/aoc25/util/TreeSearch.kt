package com.example.aoc25.util

import java.util.LinkedList
import java.util.Queue

/**
 * обход дерева в ширину
 * возвращает все пройденные вершины и количество раз, сколько их посетили
 * */
fun bfsVisitTree(
    initNode: BFSTreeNode,
    getNext: (BFSTreeNode) -> List<BFSTreeNode>,
    onVisit: (BFSTreeNode) -> Unit = {},
): Set<BFSTreeNode> {
    val queue: Queue<BFSTreeNode> = LinkedList()
    queue.add(initNode)
    val visited = mutableSetOf<BFSTreeNode>()
    while (queue.isNotEmpty()) {
        var point = queue.poll()
        val nexts = mutableListOf<BFSTreeNode>()
        while (point != null) {
            if (!visited.contains(point)) {
                onVisit(point)
                visited.add(point)
                getNext(point).forEach { newNext ->
                    val id = nexts.indexOfFirst { it.pos == newNext.pos }
                    if (id == -1) {
                        nexts.add(newNext)
                    } else {
                        nexts[id] = nexts[id].plus(newNext)
                    }
                }
            } else {
                getNext(point).forEach { newNext ->
                    val id = nexts.indexOfFirst { it.pos == newNext.pos }
                    nexts[id] = nexts[id].plus(newNext)
                }
            }

            point = queue.poll()
        }
        queue.addAll(nexts)
    }
    return visited
}


class BFSTreeNode(val pos: Point2D, val count: Long) {
    fun plus(another: BFSTreeNode): BFSTreeNode =
        if (another.pos != pos) this
        else BFSTreeNode(pos, count + another.count)

    override fun toString(): String {
        return "$pos:$count"
    }

}