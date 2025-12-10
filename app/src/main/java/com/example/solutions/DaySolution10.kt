package com.example.solutions

import com.example.ILogger
import com.example.aoc25.util.sameAs
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status

class DaySolution10(private val logger: ILogger) : DaySolution {

    override val part1 = object : DaySolutionPart {
        private var intRes: Long = 0


        override fun handleLine(inputStr: String, pos: Int) {

            val split = inputStr.split(' ')
            val sh = split[0].substring(1..split[0].length - 2)

            val schemes = sh.map { if (it == '.') 0 else 1 }.joinToString(separator = "").toInt(2)

            var i = 1
            val buttons = mutableListOf<Int>()
            while (i < split.size && split[i].startsWith('(')) {
                val nb = split[i].substring(1..split[i].length - 2).split(',').map { it.toInt() }

                val intButt = MutableList<Int>(sh.length, { _ -> 0 })
                nb.forEach { intButt[it] = 1 }

                buttons.add(intButt.joinToString(separator = "").toInt(2))
                i++
            }


            val cache: MutableMap<Int, Set<Int>> = mutableMapOf()

            var j = 0
            while (true) {
                val btt = computeButtons(buttons, cache, j)
                logger.logD("$j: $btt")
                if (btt.any { it == schemes }) {
                    break
                }
                //     logger.logD("$j")
                j++

            }
            intRes += j
        }

        override fun finish() {}

        override fun obtainResult(): String = intRes.toString()
    }

    private data class MachineConfiguration(
        val buttons: List<Set<Int>>,
        val targetLights: List<Boolean>,
        val targetJoltages: List<Int>,
    )

    override val part2 = object : DaySolutionPart {
        private var intRes: Long = 0


        override fun handleLine(inputStr: String, pos: Int) {

            val split = inputStr.split(' ')
            val sh = split[0].substring(1..split[0].length - 2)

            //    val schemes = sh.map { if (it == '.') 0 else 1 }.joinToString(separator = "").toInt(2)

            var i = 1
            val buttons = mutableListOf<Set<Int>>()
            while (i < split.size && split[i].startsWith('(')) {

                buttons.add(split[i].substring(1..split[i].length - 2).split(',').map { it.toInt() }
                    .toSet())

                i++
            }
            val joltage =
                split.last().substring(1..split.last().length - 2).split(',').map { it.toInt() }

            // if (pos>7) {
            //   val next = shrink(joltage, buttons).res
            intRes += solveJoltages(MachineConfiguration(buttons, listOf(), joltage))
            logger.logD("pos $pos, size = ${joltage.size}, buttons = ${buttons.size}")
            //   }
        }

        fun computeNext(joltage: Lights, buttons: MutableList<Lights>): Int {

            val cache: MutableMap<Lights, Int> = mutableMapOf()
            buttons.forEach { cache.put(it, 1) }
            cache.put(empty(joltage.size), 0)
            logger.logD("joltage $joltage:   $buttons")
            return computeButtons2(joltage, buttons, cache)
        }


        override fun finish() {

        }

        override fun obtainResult(): String = intRes.toString()

        private fun shrink(
            joltage: Lights,
            but: List<Lights>,
        ): SrinkData {
            var res = 0
            var shrink = true
            val buttons = mutableListOf<Lights>()
            buttons.addAll(but)

            var jolt = joltage
            while (shrink) {
                shrink = false
                val buttonsSum = buttons.fold(empty(jolt.size), { s, n -> s.plus(n) })

                buttonsSum.a.forEachIndexed { id, el ->
                    when {
                        el == 0 -> if (jolt.a[id] != 0) return SrinkData(jolt, buttons, 0)

                        el == 1 -> {
                            shrink = true
                            val count = jolt.a[id]
                            val single = buttons.firstOrNull { it.a[id] == 1 }
                            if (single != null) {
                                res += count
                                buttons.remove(single)
                                repeat(count) {
                                    jolt = jolt.minus(single)
                                }
                            }
                        }
                    }
                }
                for (i in 0..jolt.size - 1) {
                    if (jolt.a[i] == 0) {
                        if (buttons.removeIf { (it.a[i] != 0) }) shrink = true
                    }
                }
            }

            //    logger.logD("joltage = $joltage -> $jolt, buttons = $buttons,  res = $res")
            return SrinkData(jolt, buttons, res)
        }

        fun compute2(buttons: Set<Int>, prev: Lights): Lights {
            val list = MutableList(prev.size, { i -> prev.a[i] })
            buttons.forEach { list[it] = list[it] + 1 }
            return Lights(list)
        }


        fun computeButtons2(
            joltage: Lights,
            buttons: List<Lights>,
            cache: MutableMap<Lights, Int>,
        ): Int {
            return (when {
                cache[joltage] != null -> cache[joltage]!!
                joltage.isEmpty() -> 0
                joltage.isBad() -> -1

                buttons.any { it == joltage } -> 1
                else -> {
                    //    logger.logD("here $joltage, $buttons")
                    val srinkData = shrink(joltage, buttons)
                    if (srinkData.joltage.isEmpty()) return srinkData.res

                    val iterator = buttons.iterator()
                    var min = -1
                    while (iterator.hasNext()) {
                        min = computeButtons2(
                            srinkData.joltage.minus(iterator.next()),
                            srinkData.buttons,
                            cache
                        )
                        if (min != -1) break

                    }
                    if (min == -1) -1
                    else min + srinkData.res + 1
                }
            }).also {
                if (joltage.isBad()) return@also
                if (cache[joltage] == null) cache[joltage] = it

                //       logger.logD("joltage $joltage: $it  $buttons")
                /*if (cache.size % 10000 == 0) {

                    //
                }*/
            }
        }

    }

    private fun solveJoltages(config: MachineConfiguration): Int = Context().use { ctx ->
        val solver = ctx.mkOptimize()
        val zero = ctx.mkInt(0)

        // Counts number of presses for each button, and ensures it is positive.
        val buttons = config.buttons.indices
            .map { ctx.mkIntConst("button#$it") }
            .onEach { button -> solver.Add(ctx.mkGe(button, zero)) }
            .toTypedArray()

        // For each joltage counter, require that the sum of presses of all buttons that increment it is equal to the
        // target value specified in the config.
        config.targetJoltages.forEachIndexed { counter, targetValue ->
            val buttonsThatIncrement = config.buttons
                .withIndex()
                .filter { (_, counters) -> counter in counters }
                .map { buttons[it.index] }
                .toTypedArray()
            val target = ctx.mkInt(targetValue)
            val sumOfPresses = ctx.mkAdd(*buttonsThatIncrement) as IntExpr
            solver.Add(ctx.mkEq(sumOfPresses, target))
        }

        // Describe that the presses (solution answer) is the sum of all individual button presses, and should be as
        // low as possible.
        val presses = ctx.mkIntConst("presses")
        solver.Add(ctx.mkEq(presses, ctx.mkAdd(*buttons)))
        solver.MkMinimize(presses)

        // Find solution and return.
        if (solver.Check() != Status.SATISFIABLE) error("No solution found for machine: $config.")
        solver.getModel().evaluate(presses, false).let { it as IntNum }.int
    }


    class SrinkData(
        val joltage: Lights,
        val buttons: List<Lights>,
        val res: Int
    )

    class ButtonsComp() : Comparator<Lights> {
        override fun compare(o1: Lights?, o2: Lights?): Int {
            return o1?.sum?.compareTo(o2?.sum ?: 0) ?: 0
        }

    }

    data class Lights(
        val a: List<Int>,
    ) {
        val size = a.size
        val sum = a.fold(0, { s, m -> s + m })

        override fun equals(other: Any?): Boolean {
            if (other !is Lights) return false
            return a.sameAs(other.a)
        }

        override fun hashCode(): Int {
            return a.fold(0, { a, b -> a * 10 + b.hashCode() })
        }

        override fun toString(): String {
            return a.joinToString(separator = ",")
        }

        fun plus(l: Lights): Lights =
            Lights(List(size, { i -> a[i] + l.a[i] }))

        fun minus(l: Lights): Lights =
            Lights(List(size, { i -> a[i] - l.a[i] }))

        fun isEmpty() = a.all { it == 0 }
        fun isBad() = a.any { it < 0 }
    }

    fun fromList(l: List<Int>) = Lights(l)
    fun empty(size: Int) = Lights(List(size, { _ -> 0 }))

    fun computeButtons(
        buttons: List<Int>,
        cache: MutableMap<Int, Set<Int>>,
        count: Int
    ): Set<Int> {
        return (when {
            cache[count] != null -> cache[count]!!
            count == 0 -> emptySet()
            count == 1 -> buttons.toSet()
            else -> {
                buildSet {
                    for (prev in computeButtons(buttons, cache, count - 1)) {
                        for (butt in buttons) {
                            add(prev xor butt)
                        }
                    }
                }
            }
        }).also {
            if (cache[count] == null) cache[count] = it
        }
    }
}

/*      fun computeButtons2(
                 light: Int,
                 buttons: List<Int>,
                 cache: MutableMap<Int, List<Int>>,
                 count: Int
             ): Boolean {
                 return (when {
                     cache[count] != null -> cache[count]!!.any { it == light }
                     count == 0 -> light == 0
                     count == 1 -> buttons.any { it == light }
                     else -> {
                         buttons.any {
                             computeButtons2(
                                 light,
                                 buildList { add(it) }
                             )
                         }
                         for (butt in buttons) {
                             add(prev xor butt)
                         }
                     }
                 }
             }
         }).also

         {
             if (count < 6 && cache[count] == null) cache[count] = it
         }
     }


        fun compute(lighs: Int, buttons: Map<Int, Int>): Boolean =
            (buttons.entries.fold(0, { a, b ->
                var res = a
                repeat(b.value) {
                    res = res xor b.key
                }
                res
            }
            ) == lighs).also {
                //          logger.logD("check buttons $buttons asw = $it")
            }


        fun getButtons(
            buttons: List<Int>,
            cache: MutableMap<Int, List<Map<Int, Int>>>,
            count: Int
        ): List<Map<Int, Int>> {
            return (when {
                cache[count] != null -> cache[count]!!
                count == 0 -> emptyList()
                count == 1 -> buttons.map { buildMap { put(it, 1) } }
                else -> {
                    buildList {
                        for (prev in getButtons(buttons, cache, count - 1)) {
                            for (butt in buttons) {

                                add(
                                    buildMap {
                                        putAll(prev)
                                        put(butt, (prev[butt] ?: 0) + 1)
                                    }
                                )
                            }
                        }
                    }
                }


            }).also {
                if (cache[count] == null) cache[count] = it
            }
        }

     */