package com.gilpereda.aoc2025.day11

private const val OUT = "out"
private const val YOU = "you"
private const val FFT = "fft"
private const val DAC = "dac"
private const val SVR = "svr"

fun firstTask(input: Sequence<String>): String {
    val network = Network.from(input)

    return network.pathsToOut().toString()
}

// 1149196043 too low
fun secondTask(input: Sequence<String>): String = Network.from(input).pathToOutWithFftAndDac().toString()

class Network(
    n: Map<String, List<String>>,
) {
    val nodes = n.mapValues { Node(it.key, it.value) }

    fun pathsToOut(): Int = nodes.getValue(YOU).pathsToOut

    fun pathToOutWithFftAndDac(): Long {
        val value = nodes.getValue(SVR)
        return value.pathsToOutThroughFftAndDac.withFftAndDac
    }

    inner class Node(
        val name: String,
        val connections: List<String>,
    ) {
        val pathsToOut: Int by lazy {
            connections.sumOf {
                if (it == OUT) {
                    1
                } else {
                    nodes.getValue(it).pathsToOut
                }
            }
        }

        val pathsToOutThroughFftAndDac: Path by lazy {
            when (name) {
                DAC -> {
                    next(name)
                        .let {
                            if (it.withFftOnly == 0L) {
                                Path(total = it.total, withDacOnly = it.total)
                            } else {
                                Path(total = it.total, withFftOnly = 0, withFftAndDac = it.withFftOnly)
                            }
                        }
                }

                FFT -> {
                    next(name)
                        .let {
                            if (it.withDacOnly == 0L) {
                                Path(total = it.total, withFftOnly = it.total)
                            } else {
                                Path(total = it.total, withDacOnly = 0, withFftAndDac = it.withDacOnly)
                            }
                        }
                }

                OUT -> {
                    Path()
                }

                else -> {
                    next(name)
                }
            }
        }

        private fun next(node: String): Path =
            connections
                .map { nodes.getValue(it).pathsToOutThroughFftAndDac }
                .reduce { one, other ->
                    Path(
                        total = one.total + other.total,
                        withFftOnly = one.withFftOnly + other.withFftOnly,
                        withDacOnly = one.withDacOnly + other.withDacOnly,
                        withFftAndDac = one.withFftAndDac + other.withFftAndDac,
                    )
                }
    }

    companion object {
        fun from(input: Sequence<String>) = Network(input.associate(::parseLine) + (OUT to listOf()))

        private fun parseLine(line: String): Pair<String, List<String>> =
            try {
                val (name, connections) = line.split(": ")
                val connectionList = connections.split(" ")
                Pair(name, connectionList)
            } catch (ex: Exception) {
                throw ex
            }
    }
}

data class Path(
    val total: Long = 1,
    val withFftOnly: Long = 0,
    val withDacOnly: Long = 0,
    val withFftAndDac: Long = 0,
)
