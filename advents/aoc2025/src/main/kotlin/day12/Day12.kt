package com.gilpereda.aoc2025.day12

import com.gilpereda.adventofcode.commons.map.ByteArrayTwoDimensionalMap
import com.gilpereda.adventofcode.commons.map.parseToByteArrayMap

private val ONE: UByte = 1u
private val ZERO: UByte = 0u

// 546 too high
fun firstTask(input: Sequence<String>): String {
    val list = input.toList()

    val presentDistributionResolver =
        list
            .take(30)
            .chunked(5)
            .mapIndexed { index, lines -> PresentDistribution.from(index, lines) }
            .let(::PresentDistributionResolver)

    val regions =
        list
            .drop(30)
            .map(Region::from)
            .sortedBy(Region::size)
            .filter(presentDistributionResolver::canBePlaced)

    return regions.count().toString()
}

fun secondTask(input: Sequence<String>): String = TODO()

data class Region(
    val size: Size,
    val presents: List<Int>,
) {
    val area: Int = size.area

    companion object {
        fun from(line: String): Region =
            line.split(": ").let { (size, presents) ->
                val (width, height) = size.split("x").map { it.toInt() }
                val presentsList = presents.split(" ").map { it.toInt() }
                Region(size = Size(width = width, height = height), presents = presentsList)
            }
    }
}

data class Size(
    val width: Int,
    val height: Int,
) : Comparable<Size> {
    override fun compareTo(other: Size): Int =
        compareBy(Size::height)
            .then(compareBy(Size::width))
            .compare(this, other)

    val area: Int = width * height
}

class PresentDistributionResolver(
    private val baseDistributions: List<PresentDistribution>,
) {
    private val presentDistributions = mutableMapOf<Size, PresentDistribution>()

    fun canBePlaced(region: Region): Boolean {
        val limit = region.presents.mapIndexed { index, count -> baseDistributions[index].points * count }.sum()
        return region.area > limit
    }
}

data class PresentDistribution(
    val map: ByteArrayTwoDimensionalMap<Boolean>,
    val presents: List<Int>,
) {
    val size: Size by lazy { Size(map.width, map.height) }
    val points: Int by lazy { map.count { it } }
    val area = map.width * map.height

    companion object Companion {
        fun from(
            index: Int,
            lines: List<String>,
        ): PresentDistribution {
            val map =
                lines.drop(1).dropLast(1).parseToByteArrayMap(
                    parse = { it == '#' },
                    fromValue = { if (it) ONE else ZERO },
                    toValue = { it == ONE },
                )
            return PresentDistribution(
                map = map,
                presents = List(6) { i -> if (i == index) 1 else 0 },
            )
        }
    }
}
