fun main() {
  fun List<String>.format() = this
    .map { line -> line.split(" ").map { it.toLong() }}

  fun List<List<Long>>.extrapolate() = map { line ->
    buildList<List<Long>> {
      add(line)
      do { this += last().zipWithNext { a, b -> b - a } }
      while (last().any { it != 0L })
    }
  }

  fun List<String>.part1() = format()
    .extrapolate()
    .sumOf { sequence -> sequence.sumOf { it.last() } }

  fun List<String>.part2() = format()
    .map { it.reversed() }
    .extrapolate()
    .sumOf { sequence -> sequence.sumOf { it.last() } }

  val testInput = readInputs("Day09.test")
  val input = readInputs("Day09")

  check(testInput.part1() == 114L)
  check(input.part1() == 1842168671L)

  check(testInput.part2() == 2L)
  check(input.part2() == 903L)
}