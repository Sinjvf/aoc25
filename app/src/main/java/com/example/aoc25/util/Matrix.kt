package com.example.aoc25.util

import com.example.ILogger

class Matrix<T>() : Iterable<PositionData<T>> {
    private val data = mutableMapOf<Point2D, T>()

    fun copy(): Matrix<T>{
        val newM =  Matrix<T>()
        newM.data.putAll(data)
        newM.ySize = ySize
        newM.xSize = xSize
        return newM
    }

    constructor(x: Int, y: Int, space: T) : this() {
        for (i in 0 until x) {
            for (j in 0 until y) {
                data.put(Point2D(i, j), space)
            }
        }
        xSize = x
        ySize = y
    }

    constructor(x: Int, y: Int, space: (Point2D)->T) : this() {
        for (i in 0 until x) {
            for (j in 0 until y) {
                data.put(Point2D(i, j), space(Point2D(i, j)))

            }
        }
        xSize = x
        ySize = y
    }

    fun addRaw(y: Int, nodes: Collection<T>) {
        if (xSize == 0) {
            xSize = nodes.size
        } else {
            if (xSize != nodes.size) throw IllegalArgumentException("bad raw size. Expected $xSize got ${nodes.size}")
        }
        ySize = y+1

        nodes.forEachIndexed { index, t -> data.put(Point2D(index, y), t) }

    }

    fun addColumn(x: Int, nodes: Collection<T>) {
        if (ySize == 0) {
            ySize = nodes.size
        } else {
            if (ySize != nodes.size) throw IllegalArgumentException("bad column size. Expected $ySize got ${nodes.size}")
        }
        xSize = x+1
        nodes.forEachIndexed { index, t -> data.put(Point2D(x, index), t) }
    }

    fun get(point: Point2D): T {
        if (!pointInRange(point)) throw IllegalArgumentException("bad point $point")
        return data.get(point) ?: throw IllegalArgumentException("empty data $point")
    }

    fun get(x: Int, y: Int): T {
        val point = Point2D(x, y)
        if (!pointInRange(point)) throw IllegalArgumentException("bad point $point")
        return data.get(point) ?: throw IllegalArgumentException("empty data $point")
    }

    fun put(point: Point2D, node: T) {
        if (!pointInRange(point)) throw IllegalArgumentException("bad point $point")
        data.put(point, node)
    }
    fun add(point: Point2D, node: T) {
        data.put(point, node)
    }
    fun remove(point: Point2D) {
        if (!pointInRange(point)) throw IllegalArgumentException("bad point $point")
        data.remove(point)
    }

    fun put(x: Int, y: Int, node: T) {
        val point = Point2D(x, y)
        if (!pointInRange(point)) throw IllegalArgumentException("bad point $point")
        data.put(point, node)
    }

    fun pointInRange(point: Point2D): Boolean =
        data.get(point)!=null

    override fun iterator(): Iterator<PositionData<T>> = iterator {
        data.forEach {
            yield(PositionData(it.key, it.value))
        }
    }

    var ySize: Int = 0
    var xSize: Int = 0

    fun print(logger: ILogger?, separator: String = "", printString: ((T, Point2D) -> String)? = null) {
        for (y in 0 until ySize) {
            val builder = StringBuilder()
            for (x in 0 until xSize) {
                val point =  Point2D(x, y)
                val pointS = data[point]?:" "
                builder.append((printString?.invoke(get(point),point)) ?: pointS.toString())
                    .append(separator)
            }
            logger?.logD(builder.toString()) ?: println(builder.toString())
        }
    }

    fun getCol(x: Int): List<T> = buildList {
        for (i in 0 until ySize) {
            add(get(Point2D(x, i)))
        }
    }

    fun getRaw(y:Int):List<T> = buildList {
        for (i in 0 until xSize) {
            add(get(Point2D(i, y)))
        }
    }

    fun clear(){
        data.clear()
    }

    fun onEachDiagonal(action: (List<T>) -> Unit) {

        //get diagonals
        var i = 0
        var j = 0
        println("----D1----")
        val sum = xSize + ySize - 1
        var last = false
        while (!last) {
            val diagonal = buildList {
                var k = 0
                var p = i

                while (k <= j) {
                    val point1 = Point2D(k, p)
                    if (pointInRange(point1)) {
                        add(get(point1))
                    }
                    k++
                    p--
                }
            }
            if (i == sum && j == sum) last = true
            if (i < sum) {
                i++
            }
            if (j < sum) {
                j++
            }
            if (diagonal.isNotEmpty()) {
                action(diagonal)
            }
        }

        println("----D2----")
        i = 0
        j = -ySize
        last = false
        while (!last) {
            val diagonal = buildList {
                var k = 0
                var p = j

                while (k <= j) {
                    val point1 = Point2D(k, xSize - 1 - p)
                    if (pointInRange(point1)) {
                        add(get(point1))
                    }
                    k++
                    p--
                }
            }
            if (i == sum && j == sum) last = true
            if (i < sum) {
                i++
            }
            if (j < sum) {
                j++
            }
            if (diagonal.isNotEmpty()) {
                action(diagonal)
            }
        }
    }

    fun getNei(pos: Point2D):List<Point2D> = buildList {
        if (pos.y>0) {
            if (pos.x>0){
                add(Point2D(pos.x-1, pos.y-1))
            }
            add(Point2D(pos.x, pos.y-1))
            if (pos.x<xSize-1){
                add(Point2D(pos.x+1, pos.y-1))
            }
        }
        if (pos.x>0){
            add(Point2D(pos.x-1, pos.y))
        }
        if (pos.x<xSize-1){
            add(Point2D(pos.x+1, pos.y))
        }
        if (pos.y<ySize-1) {
            if (pos.x>0){
                add(Point2D(pos.x-1, pos.y+1))
            }
            add(Point2D(pos.x, pos.y+1))
            if (pos.x<xSize-1){
                add(Point2D(pos.x+1, pos.y+1))
            }
        }
    }
}


data class PositionData<T>(val pos:Point2D, val data:T)