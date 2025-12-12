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
    val buttons: List<List<Int>>,
    val joltage: List<Int>,
) {
    private val buttonsAsInt =
        buttons.map { btns ->
            (lights - 1 downTo 0)
                .map { index ->
                    if (index in btns) 1 else 0
                }.joinToString("")
                .toInt(2)
        }

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
                val nextSteps = buttonsAsInt.mapNotNull { step.next(it) }
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
            open: List<JoltageStep>,
            current: Int = Int.MAX_VALUE,
        ): Int =
            if (open.isEmpty()) {
                current
            } else {
                val next = open.first()
                if (next.finished()) {
                    val nextOpen = open.drop(1).filter { it.pushed < current }
                    go(nextOpen, next.pushed)
                } else {
                    val newOpen = next.next().filter { it.pushed < current }
                    go(open.drop(1) + newOpen, current)
                }
            }

        return go(listOf(JoltageStep(joltage)))
    }

    inner class JoltageStep(
        val current: List<Int>,
        val pushed: Int = 0,
    ) {
        fun finished(): Boolean = current.all { it == 0 }

        fun next(): List<JoltageStep> =
            current
                .mapIndexed { index, state -> index to state }
                .firstOrNull { (_, state) -> state > 0 }
                ?.first
                ?.let { nextIndex ->
                    buttons.filter { button -> nextIndex in button }
                }?.mapNotNull { next(it) } ?: emptyList()

        private fun next(buttons: List<Int>): JoltageStep? {
            val next = current.mapIndexed { index, state -> if (index in buttons) state - 1 else state }
            return if (isPossible(next)) {
                JoltageStep(next, pushed + 1)
            } else {
                null
            }
        }

        private fun isPossible(newJolt: List<Int>): Boolean = newJolt.all { it >= 0 }
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
                    .map { (buttonsStr) -> buttonsStr.split(",").map(String::toInt) }
                    .toList()

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
