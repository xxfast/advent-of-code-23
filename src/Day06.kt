import kotlin.time.measureTime

fun main() {
  fun List<String>.part1(): Int = this
    .map { line -> line.substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
    .let { (time, distance) -> time.zip(distance) }
    .map { (time, distance) -> (0..time).count { dt -> (time - dt) * dt > distance } }
    .reduce { acc, i -> acc * i }

  fun List<String>.part2() = this
    .map { line -> line.substringAfter(":").trim().filter { it.isDigit() }.toLong() }
    .let { (time, distance) -> (0..time).count { dt -> (time - dt) * dt > distance } }

  val test1Input = readInputs("Day06.test")
  val input = readInputs("Day06")

  check(test1Input.part1() == 288)
  measureTime { check(input.part1() == 503424) }.println()

  check(test1Input.part2() == 71503)
  measureTime { check(input.part2() == 32607562) }.println()
}