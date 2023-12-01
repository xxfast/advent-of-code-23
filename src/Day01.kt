fun main() {
  fun List<String>.part1(): Int = sumOf { line ->
    line
      .filter { it.isDigit() }
      .let { "${it.first()}${it.last()}".toInt() }
  }

  val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    .mapIndexed { index, number -> (index + 1) to number }

  fun List<String>.part2(): Int = map { line ->
    line.fold("") { current, next -> numbers
      .find { (_, number) -> "$current$next".contains(number) }
      ?.let { (value, number) -> "$current$next".replace(number, "$value$next") }
      ?: "$current$next"
    }
  }.part1()

  val input = readInput("Day01")

  val test1Input = readInput("Day01.1.test")
  check(test1Input.part1() == 142)
  check(input.part1() == 54388)

  val test2Input = readInput("Day01.2.test")
  check(test2Input.part2() == 281)
  check(input.part2() == 53515)
}
