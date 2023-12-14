import kotlin.math.abs
import kotlin.time.measureTime

typealias Galaxy = Pair<Long, Long>

fun main() {
  fun List<String>.format(expansion: Long = 1): List<Galaxy> {
    val yAxis = this.mapIndexedNotNull { index, line -> if (!line.contains("#")) index else null }
    val xAxis = this.maxOf { it.length }.let { (0..<it).mapNotNull { x -> if (this.all { line -> line[x] != '#' }) x else null } }

    return this.mapIndexed { y, line -> line.mapIndexedNotNull { x, c -> if (c == '#') x to y else null } }
      .flatten()
      .map { (x, y) ->
        val dx = xAxis.count { ex -> ex < x } * expansion
        val dy = yAxis.count { ey -> ey < y } * expansion
        (x + dx) to (y + dy)
      }
  }


  fun List<String>.part1(expansion: Long = 1): Long = this
    .format(expansion = expansion)
    .let { galaxies -> galaxies.map { first -> galaxies.map { second -> first to second } } }
    .flatten()
    .fold(emptyList<Pair<Galaxy, Galaxy>>()) { acc, (previous, next) ->
      val included = acc.any { (first, second) -> (first == previous && second == next) || (first == next && second == previous) }
      if (!included) acc + (previous to next) else acc
    }
    .filter { (first, second) -> first != second }
    .sumOf { (first, second) -> abs(second.first - first.first) + abs(second.second - first.second) }

  fun List<String>.part2(expansion: Long) = part1(expansion)

  val testInput = readInputs("Day11.test")
  val input = readInputs("Day11")

  check(testInput.part1() == 374L)
  measureTime { check(input.part1() == 9693756L) }.println()

  check(testInput.part2(9L) == 1030L)
  check(testInput.part2(99L) == 8410L)
  measureTime { input.part2(999_999L) }.println()
}