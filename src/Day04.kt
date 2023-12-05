data class Card(val index: Int, val left: List<Int>, val right: List<Int>) {
  val winnings: List<Int> = left.filter { number -> number in right }
}

fun main() {
  fun List<String>.format(): List<Card> = mapIndexed { index, game ->
    val (left, right) = game.substringAfter(":")
      .split("|")
      .map { it.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
    Card(index + 1, left, right)
  }

  fun List<String>.part1(): Int = format()
    .sumOf { game ->
      game.winnings
        .foldIndexed(0) { index, acc, _ -> if (index == 0) 1 else acc * 2 }
        .toInt()
    }

  fun List<String>.part2(): Int = format()
    .let { cards ->
      val process = cards.toMutableList()
      var total = 0
      while (process.isNotEmpty()) {
        val index = process.first().index
        val count = process.count { it.index == index }
        process.removeAll { it.index == index }
        val winning = cards.first { it.index == index }.winnings.count()
        val new: List<Card> = cards.subList(index, index + winning)
        process += List(count) { new }.flatten()
        total += count
      }
      return total
    }

  val test1Input = readInput("Day04.test")
  val input = readInput("Day04")

  check(test1Input.part1() == 13)
  check(input.part1() == 25004)

  check(test1Input.part2() == 30)
  check(input.part2() == 14427616)
}