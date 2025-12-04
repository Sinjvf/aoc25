import com.example.ILogger
import com.example.aoc25.util.Matrix
import com.example.aoc25.util.Point2D
import com.example.solutions.DaySolution
import com.example.solutions.DaySolutionPart

class DaySolution4(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0
        val matrix = Matrix<Node>()


        override fun handleLine(inputStr: String, pos: Int) {
            matrix.addRaw(pos, inputStr.map { it.toNode() })
        }

        override fun obtainResult(): String = intRes.toString()
        override fun finish() {
            val iterator = matrix.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                if (node.data.isPaper() && canGet(matrix, node.pos)) intRes++
            }
        }


    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Int = 0
        val matrix = Matrix<Node>()

        override fun handleLine(inputStr: String, pos: Int) {
            matrix.addRaw(pos, inputStr.map { it.toNode() })
        }

        override fun obtainResult(): String = intRes.toString()

        override fun finish() {
           while (iteration()!=0){ }
        }

        fun iteration():Int{
            var num = 0
            val iterator = matrix.iterator()
            while (iterator.hasNext()) {
                val node = iterator.next()
                if (node.data.isPaper() && canGet(matrix, node.pos)) {
                    num++
                    matrix.put(node.pos, Space())
                }
            }
            intRes+=num
            return num
        }
    }
}

fun canGet(matrix:Matrix<Node>, pos: Point2D): Boolean =
    matrix.getNei(pos).filter { matrix.get(it).isPaper() }.size < 4


fun Char.toNode() = if (this == '@') Paper() else Space()
sealed class Node(
    val char: Char

){
    fun isPaper() = this is Paper
}

class Paper : Node('@')
class Space : Node('.')
