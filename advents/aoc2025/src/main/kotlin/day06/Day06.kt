package com.gilpereda.aoc2025.day06

private val NUMBERS_REGEX = "(\\d+)\\s*".toRegex()

fun firstTask(input: Sequence<String>): String {
    val lineList = input.toList()
    val operations =
        lineList.last().fold(mutableListOf<Operation>()) { acc, next ->
            when (next) {
                '+' -> acc.add(Operation.SUM)
                '*' -> acc.add(Operation.PRODUCT)
                else -> {}
            }
            acc
        }
    val operands =
        lineList
            .dropLast(1)
            .map { line ->
                NUMBERS_REGEX
                    .findAll(line)
                    .map { it.destructured }
                    .map { (value) -> value.toLong() }
                    .toList()
            }.fold(listOf<List<Long>>()) { acc, next ->
                if (acc.isEmpty()) {
                    next.map { listOf(it) }
                } else {
                    acc.zip(next).map { (op, n) -> op + n }
                }
            }

    return operands
        .zip(operations)
        .map { (ops, opers) -> opers.apply(ops) }
        .reduce { one, other -> one + other }
        .toString()
}

// 11299263618107 too low
// 11299263623062
fun secondTask(input: Sequence<String>): String {
    val intermediate =
        input
            .fold(listOf<List<Char>>()) { acc, next ->
                if (acc.isEmpty()) {
                    next.map { listOf(it) }
                } else {
                    acc.zip(next.toList()).map { (charList, nextChar) -> charList + nextChar }
                }
            }

    tailrec fun go(
        rest: List<List<Char>>,
        acc: List<OpGroup> = emptyList(),
        current: OpGroup? = null,
    ): List<OpGroup> =
        if (rest.isEmpty()) {
            current?.let { acc + current } ?: acc
        } else {
            val next = rest.first()
            if (current == null) {
                val operation = Operation.from(next.last())
                val operand =
                    next
                        .dropLast(1)
                        .joinToString("")
                        .trim()
                        .toLong()
                go(rest.drop(1), acc, OpGroup(operation, operand))
            } else {
                val operand =
                    next
                        .dropLast(1)
                        .joinToString("")
                        .trim()
                        .toLongOrNull()
                if (operand == null) {
                    go(rest.drop(1), acc + current, null)
                } else {
                    go(rest.drop(1), acc, current.addOperand(operand))
                }
            }
        }
    val operations = go(intermediate)
    return operations.sumOf { it.calculate() }.toString()
}

enum class Operation(
    val nullValue: Long,
) {
    SUM(0L),
    PRODUCT(1),
    ;

    companion object {
        fun from(char: Char): Operation =
            when (char) {
                '+' -> SUM
                '*' -> PRODUCT
                else -> throw IllegalArgumentException("Could not find operation from $char")
            }
    }

    fun apply(operands: List<Long>): Long =
        operands.fold(nullValue) { acc, next ->
            when (this) {
                SUM -> acc + next
                PRODUCT -> acc * next
            }
        }
}

class OpGroup(
    private val operation: Operation,
    initial: Long,
) {
    val operands = mutableListOf(initial)

    fun addOperand(operand: Long): OpGroup {
        operands.add(operand)
        return this
    }

    fun calculate(): Long = operation.apply(operands)
}
