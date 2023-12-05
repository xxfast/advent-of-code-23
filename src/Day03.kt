fun main() {
  fun Char.isSymbol(): Boolean = !this.isDigit() && this != '.'

  data class PartNumber(val value: Int, val x: Int, val y: Int)

  fun List<String>.format(): List<Pair<Char, Set<PartNumber>>> =
    mapIndexed { y, line ->
      line.mapIndexed { x, char ->
        // Stop if this is a symbol
        if (!char.isSymbol()) return@mapIndexed null

        char to ((x - 1)..(x + 1)).map { dx ->
            ((y - 1)..(y + 1)).map { dy ->
              if (dx == x && dy == y) return@map null
              if (dx < 0 || dx >= this.size) return@map null
              if (dy < 0 || dy >= line.length) return@map null

              val neighbour = this[dy][dx]

              // If neighbour is a digit
              if (!neighbour.isDigit()) return@map null

              var number = "$neighbour"
              // Scan to the left
              var sx = dx - 1
              var charToTheLeft = this[dy][sx]
              while (charToTheLeft.isDigit()) {
                number = "$charToTheLeft$number"
                sx -= 1
                if (sx < 0) break
                charToTheLeft = this[dy][sx]
              }

              // Keep track of the start to uniquely identify part numbers later
              val startX = sx + 1

              // Scan to the right
              sx = dx + 1
              var charToTheRight = this[dy][sx]
              while (charToTheRight.isDigit()) {
                number = "$number$charToTheRight"
                sx += 1
                if (sx >= this[dy].length) break
                charToTheRight = this[dy][sx]
              }

              return@map PartNumber(value = number.toInt(), x = startX, y = dy,)
            }
          }
          .flatten()
          .filterNotNull()
          .toSet()
      }
    }
    .flatten()
    .filterNotNull()

  fun List<String>.part1(): Int = format()
    .map { (_, parts) -> parts }
    .flatten()
    .sumOf { it.value }

  fun List<String>.part2() = format()
    .filter { (char, parts) ->  char == '*' && parts.size > 1 }
    .sumOf { (_, parts) ->
      parts
        .map { it.value }
        .reduce { previous, partNumber -> previous * partNumber }
    }

  val test1Input = readInputs("Day03.test")
  val input = readInputs("Day03")

  check(test1Input.part1() == 4361)
  check(input.part1() == 532428)

  check(test1Input.part2() == 467835)
  check(input.part2() == 84051670)
}