import com.example.ILogger
import com.example.solutions.DaySolution
import com.example.solutions.DaySolutionPart

class DaySolution3(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Int = 0


        override fun handleLine(inputStr: String, pos: Int) {
            val nums = inputStr.map { it.toString().toInt() }

            var max1 = 0
            var mPos = 0
            var i  = 0
            while (i< nums.size-1) {

                if (nums[i] > max1) {
                    max1 =nums[i]
                   mPos = i
                }
                i++
            }
            var max2 = 0
            i = mPos+1
            while (i<nums.size){
                if (nums[i] > max2) {
                    max2 =nums[i]
                }
                i++
            }
            intRes += max1*10+max2
        }

        override fun obtainResult(): String = intRes.toString()
    }


    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0

        override fun handleLine(inputStr: String, pos: Int) {

            val nums = inputStr.map { it.toString().toLong() }

            var maxs = mutableListOf<Long>()
            var mPoss = mutableListOf<Int>(0)
            for (j in 0..11){
                var i  = mPoss.last()
                var max = 0L
                var mPos = 0
                while (i< nums.size-(11-j)) {

                    if (nums[i] > max) {
                        max =nums[i]
                        mPos = i +1
                    }
                    i++
                }
                maxs.add(max)
                mPoss.add(mPos)
            }
            var res = 0L
            for (i in 0..maxs.size-1){
                res = res*10+maxs[i]
            }

            intRes+=res
        }

        override fun obtainResult(): String = intRes.toString()
    }
}
