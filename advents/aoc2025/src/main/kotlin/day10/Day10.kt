package com.gilpereda.aoc2025.day10

typealias Joltage = List<Int>
typealias Buttons = List<Int>

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
    val buttonsList: List<Buttons>,
    val joltage: Joltage,
) {
    private val buttonsPermutations = mutableMapOf<List<Buttons>, MutableMap<Int, List<Joltage>>>()
    private val buttonsAsInt =
        buttonsList.map { btns ->
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
                    val newOpen = open.drop(1) + next.next().filter { it.pushed < current }
                    go(newOpen.sortedByDescending { it.pushed }, current)
                }
            }

        return go(listOf(JoltageStep(joltage)))
    }

    inner class JoltageStep(
        val current: Joltage,
        val pushed: Int = 0,
    ) {
        fun finished(): Boolean = current.all { it == 0 }

        fun next(): List<JoltageStep> =
            current
                .mapIndexed { index, state -> index to state }
                .firstOrNull { (_, state) -> state > 0 }
                ?.let { (index, count) ->
                    val nextButtons = buttonsList.filter { btns -> index in btns }
                    findPermutations(nextButtons, count)
                        .mapNotNull { permutation -> next(permutation) }
                }
                ?: emptyList()

        private fun next(buttons: Buttons): JoltageStep? {
            val next = current.mapIndexed { index, state -> if (index in buttons) state - 1 else state }
            return if (isPossible(next)) {
                JoltageStep(next, pushed + 1)
            } else {
                null
            }
        }

        private fun isPossible(newJolt: Joltage): Boolean = newJolt.all { it >= 0 }
    }

    private fun findPermutations(
        buttonsList: List<Buttons>,
        count: Int,
    ): List<Joltage> =
        buttonsPermutations
            .computeIfAbsent(buttonsList) { mutableMapOf() }
            .computeIfAbsent(count) {
                findPermutations(buttonsList, count - 1).flatMap { permutation ->
                    buttonsList.map { buttons ->
                        permutation.mapIndexed { index, item -> if (index in buttons) item + 1 else item }
                    }
                }
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
                buttonsList = buttons,
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
