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
            machine.buttonsToPushToInitializeTheMachine()
        }.toString()
}

fun secondTask(input: Sequence<String>): String {
    var count = 0
    return input
        .map {
            println("${count++}: $it")
            Machine.fromLine(it)
        }.sumOf { machine ->
            machine.buttonsToPushToConfigureTheJoltage()
        }.toString()
}

private val LIGHT_STATE_REGEX = "\\[([.#]+)]".toRegex()
private val BUTTONS_REGEX = "\\(([0-9,]+)\\)\\s".toRegex()
private val JOLTAGE_STATE_REGEX = "\\{([0-9,]+)}".toRegex()

private val participantsToCountToPermutations = mutableMapOf<Int, MutableMap<Int, List<List<Int>>>>()

data class Machine(
    val lights: Int,
    val initialLights: Int,
    val buttonsList: List<Buttons>,
    val joltageGoal: Joltage,
) {
    private val joltageStepsPermutations = mutableMapOf<List<Buttons>, MutableMap<Int, List<JoltageStep>>>()

    private val buttonsAsInt =
        buttonsList.map { btns ->
            (lights - 1 downTo 0)
                .map { index ->
                    if (index in btns) 1 else 0
                }.joinToString("")
                .toInt(2)
        }

    fun buttonsToPushToInitializeTheMachine(): Int {
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

    fun buttonsToPushToConfigureTheJoltage(): Int {
        tailrec fun go(
            open: List<JoltageStep>,
            current: Int = Int.MAX_VALUE,
        ): Int = TODO()

        return go(listOf(JoltageStep(List(lights) { 0 })))
    }

    inner class JoltageStep(
        val current: Joltage,
        val pushed: Int = 0,
    ) {
        private val isPossible: Boolean by lazy { current.mapIndexed { index, status -> status <= joltageGoal[index] }.all { it } }

        fun finished(): Boolean = current.mapIndexed { index, value -> value == joltageGoal[index] }.all { it }
    }

    private fun findPermutations(
        participants: Int,
        count: Int,
    ): List<List<Int>> {
        val countToPermutations = participantsToCountToPermutations.computeIfAbsent(participants) { mutableMapOf() }
        val permutations = countToPermutations[count]
        if (permutations == null) {
            if (participants == 2) {
                countToPermutations[count] = List(count + 1) { next -> listOf(next, count - next) }
            } else {
                List(count + 1) { it }
                    .flatMap { next -> findPermutations(participants - 1, count - next) }
            }
        }
        return countToPermutations.getValue(count)
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
                joltageGoal = joltage,
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
