import com.example.ILogger
import com.example.solutions.DaySolution
import com.example.solutions.DaySolutionPart

class DaySolution4(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0


        override fun handleLine(inputStr: String, pos: Int) {

        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Int = 0

        override fun handleLine(inputStr: String, pos: Int) {
        }

        override fun obtainResult(): String = intRes.toString()
    }
}
