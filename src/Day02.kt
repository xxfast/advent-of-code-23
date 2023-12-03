fun main() {
  fun List<String>.format() =
    map { game ->
      game
        .substringAfter(":")
        .split(";")
        .map { set ->
          set.trim()
            .split(",")
            .associate { hand ->
              hand.trim()
                .split(" ")
                .let { (count, color) -> color to count.toInt() }
            }
        }
    }

  fun List<String>.part1() = format()
    .mapIndexed { id, game ->
      (id + 1) to game.all { sets ->
        sets.all { (color, count) ->
          when (color) {
            "red" -> count <= 12
            "green" -> count <= 13
            "blue" -> count <= 14
            else -> error("Unknown color $color")
          }
        }
      }
    }
    .filter { (_, possible) -> possible }
    .sumOf { (id, _) -> id }

  fun List<String>.part2(): Int = format()
    .sumOf { game ->
      val reds: Int = game.maxOf { sets -> sets["red"] ?: 0 }
      val greens: Int = game.maxOf { sets -> sets["green"] ?: 0 }
      val blues: Int = game.maxOf { sets -> sets["blue"] ?: 0 }
      reds * greens * blues
    }

  val test1Input = readInput("Day02.test")
  val input = readInput("Day02")

  check(test1Input.part1() == 8)
  check(input.part1() == 2416)

  check(test1Input.part2() == 2286)
  input.part2().println()
}