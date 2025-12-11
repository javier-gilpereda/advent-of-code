package com.gilpereda.aoc2025.day10

fun firstTask(input: Sequence<String>): String {
    var count = 0
    return input
        .map {
            println("${count++}: $it")
            Machine.fromLine(it)
        }.sumOf { machine ->
            machine.toPushToInitialize()
        }.toString()
}

fun secondTask(input: Sequence<String>): String {
    var count = 0
    return input
        .map {
            println("${count++}: $it")
            Machine.fromLine(it)
        }.sumOf { machine ->
            machine.toPushToConfigureJoltage()
        }.toString()
}

private val LIGHT_STATE_REGEX = "\\[([.#]+)]".toRegex()
private val BUTTONS_REGEX = "\\(([0-9,]+)\\)\\s".toRegex()
private val JOLTAGE_STATE_REGEX = "\\{([0-9,]+)}".toRegex()

data class Machine(
    val lights: Int,
    val initialLights: Int,
    val buttons: List<Int>,
    val joltage: List<Int>,
) {
    val buttonsAsString: List<String> = buttons.map { it.toString(2).padStart(lights, '0').reversed() }

    fun toPushToInitialize(): Int {
        tailrec fun go(
            rest: List<InitStep>,
            last: InitStep? = null,
        ): Int {
            val step = rest.first()
            if (last == null || last.buttonsPushed != step.buttonsPushed) {
                println("${step.buttonsPushed}")
            }
            return if (step.lights == 0) {
                step.buttonsPushed
            } else {
                val nextSteps = buttons.mapNotNull { step.next(it) }
                val winner = nextSteps.firstOrNull { it.lights == 0 }

                if (winner != null) {
                    println("")
                    winner.buttonsPushed
                } else {
                    val nextInitSteps = (rest.drop(1) + nextSteps).sortedBy(InitStep::buttonsPushed)
                    go(nextInitSteps, step)
                }
            }
        }
        return go(listOf(InitStep(0, initialLights)))
    }

    fun toPushToConfigureJoltage(): Int {
        tailrec fun go(
            rest: Set<JoltageStep>,
            acc: Int = Int.MAX_VALUE,
        ): Int =
            if (rest.isEmpty()) {
                acc
            } else {
                val restSorted = rest.sortedWith(score)
                val step = restSorted.first()

                val nextSteps = buttonsAsString.mapNotNull { step.next(it, joltage, acc) }
                val winner = nextSteps.firstOrNull { it.finished(joltage) }
                if (winner != null) {
                    println("Winner: $winner")
                    go(restSorted.drop(1).toSet(), minOf(acc, winner.timesPushed))
                } else {
                    val nextInitSteps = (restSorted.drop(1) + nextSteps).toSet()
                    go(nextInitSteps, acc)
                }
            }
        val seed = buttonsAsString.mapNotNull { JoltageStep(joltage.map { 0 }).next(it, joltage, Int.MAX_VALUE) }.toSet()
        return go(seed)
    }

    private val score: Comparator<JoltageStep> =
        compareByDescending(JoltageStep::timesPushed)
            .then(
                compareBy {
                    it.current.zip(joltage).sumOf { (one, other) -> other - one }
                },
            )

    data class JoltageStep(
        val current: List<Int>,
        val steps: List<String> = emptyList(),
    ) {
        val timesPushed: Int = steps.count()

        fun next(
            buttons: String,
            goal: List<Int>,
            limit: Int,
        ): JoltageStep? {
            val nextJoltage = current.mapIndexed { index, state -> if (buttons[index] == '1') state + 1 else state }
            return if (timesPushed < limit && isPossibleJoltage(nextJoltage, goal)) {
                JoltageStep(
                    current = nextJoltage,
                    steps = (steps + buttons).sorted(),
                )
            } else {
                null
            }
        }

        fun finished(goal: List<Int>): Boolean = current.mapIndexed { index, state -> state == goal[index] }.all { it }

        private fun isPossibleJoltage(
            next: List<Int>,
            goal: List<Int>,
        ): Boolean = next.mapIndexed { index, state -> state <= goal[index] }.all { it }
    }

    companion object {
        fun fromLine(line: String): Machine {
            val (rawLightStatus) = LIGHT_STATE_REGEX.find(line)?.destructured!!
            val initialLights =
                rawLightStatus
                    .map {
                        when (it) {
                            '#' -> 1
                            '.' -> 0
                            else -> throw IllegalStateException("Unknown light-state: $it")
                        }
                    }.reversed()
                    .joinToString("")
                    .toInt(2)
            val buttons =
                BUTTONS_REGEX
                    .findAll(line)
                    .map { it.destructured }
                    .map { (buttonsStr) ->
                        val btns = buttonsStr.split(",").map(String::toInt)
                        (rawLightStatus.length - 1 downTo 0)
                            .map {
                                if (it in btns) 1 else 0
                            }.joinToString("")
                            .toInt(2)
                    }.toList()

            val joltage =
                JOLTAGE_STATE_REGEX.find(line)?.destructured?.let { (joltage) -> joltage.split(",").map(String::toInt) }!!
            return Machine(
                initialLights = initialLights,
                lights = rawLightStatus.length,
                buttons = buttons,
                joltage = joltage,
            )
        }
    }
}

data class InitStep(
    val buttonsPushed: Int = 0,
    val lights: Int,
) {
    private fun matches(buttons: Int): Boolean {
        val firstLight = lights.toString(2).indexOfFirst { it == '1' }
        return buttons.toString(2)[firstLight] == '1'
    }

    fun next(buttons: Int): InitStep? =
        if (matches(buttons)) {
            InitStep(
                buttonsPushed = buttonsPushed + 1,
                lights = buttons xor lights,
            )
        } else {
            null
        }
}
