val Char.boxDrawing: Char
  get() = when (this) {
    '|' -> '║'
    '-' -> '═'
    'L' -> '╚'
    'J' -> '╝'
    '7' -> '╗'
    'F' -> '╔'
    else -> this
  }

val joins = listOf('L', 'J', '7', 'F')

typealias Coordinates = Pair<Int, Int>

data class Tiles(val map: List<String>, val loop: List<Coordinates>)

fun main() {
  fun List<String>.format(): Tiles {
    var map: MutableList<String> = this.toMutableList()
    val loop: List<Pair<Int, Int>> = buildList {
      val height: Int = this@format.size
      val width: Int = this@format.maxOf { it.length }
      // Add start to the loop
      val startY: Int = this@format.indexOfFirst { line -> line.contains('S') }
      val startX: Int = this@format[startY].indexOfFirst { char -> char == 'S' }
      add(startX to startY)

      // Add the next pipe connected to the start
      val connections = listOf(
        startX to startY - 1,
        startX + 1 to startY,
        startX to startY + 1,
        startX - 1 to startY
      ).filter { (dx, dy) ->
        if (dx !in 0..< width || dy !in 0..<height) return@filter false
        when (this@format[dy][dx]) {
          '|' -> dy == startY - 1 || dy == startY + 1
          '-' -> dx == startX - 1 || dx == startX + 1

          'L' -> dx == startX - 1 || dy == startY + 1
          'J' -> dx == startX + 1 || dy == startY + 1
          '7' -> dx == startX + 1 || dy == startY - 1
          'F' -> dx == startX - 1 || dy == startY - 1
          else -> false
        }
      }

      // Only add the first for now, second will be discovered later
      add(connections.first())

      // Replace the start of the original map
      val replacement: Char = when {
        startX to startY - 1 in connections &&
          startX to startY + 1 in connections -> '|'

        startX to startY - 1 in connections &&
          startX + 1 to startY in connections -> 'L'

        startX to startY - 1 in connections &&
          startX - 1 to startY in connections -> 'J'

        startX - 1 to startY in connections &&
          startX + 1 to startY in connections -> '-'

        startX - 1 to startY in connections &&
          startX to startY + 1 in connections -> '7'

        else -> 'F'
      }

      val newLine = this@format[startY].toMutableList()
      newLine[startX] = replacement
      map[startY] = newLine.joinToString("")

      // Add all the connected pipes to that
      while (true) {
        val (sx, sy) = this[lastIndex - 1]
        var (dx, dy) = last()
        when (val char = this@format[dy][dx]) {
          '|' -> if (sy < dy) dy++ else dy--
          '-' -> if (sx < dx) dx++ else dx--
          'L' -> if (sy < dy) dx++ else dy--
          'J' -> if (sy < dy) dx-- else dy--
          '7' -> if (sx < dx) dy++ else dx--
          'F' -> if (sy > dy) dx++ else dy++
          else -> error("Invalid pipe $char")
        }

        // Break out when we find the start
        if (contains(dx to dy)) break
        add(dx to dy)
      }
    }

    // Clean up the map and remove all the pipes that not part of the loop
    map.forEachIndexed { y, line ->
      line.forEachIndexed { x, c ->
        val newLine = map[y].toMutableList()
        newLine[x] = if (x to y in loop) c else '.'
        map[y] = newLine.joinToString("")
      }
    }

    return Tiles(map, loop)
  }


  fun List<String>.part1() = format().loop.size / 2

  fun List<String>.part2(): Int {
    val tiles: Tiles = format()
    val height: Int = tiles.map.size
    val width: Int = tiles.map.maxOf { it.length }

    val joins = listOf('L', 'J', '7', 'F')

    var count = 0
    tiles.map.forEachIndexed { y, line ->
      line.forEachIndexed { x, char ->
        if (x to y in tiles.loop) {
          print("\u001b[31m${char.boxDrawing}\u001b[0m")
          return@forEachIndexed
        }

        fun String.foldIntoPipes(isHorizontal: Boolean): List<String> {
          val straight = if (isHorizontal) '-' else '|'
          return this
            .fold(listOf()) { pipes, pipe ->
              when {
                (pipe in joins || pipe == straight) &&
                  pipes.isNotEmpty() &&
                  pipes.last().count { it in joins } in (1..<2) ->
                  pipes.dropLast(1) + "${pipes.last()}$pipe"

                else -> pipes + "$pipe"
              }
            }
        }

        fun List<String>.countVerticalCrossings() = this
          .count { pipe ->
            when {
              pipe.startsWith("F") -> pipe.endsWith("J")
              pipe.startsWith("L") -> pipe.endsWith("7")
              pipe.startsWith("|") -> pipe.endsWith("|")
              else -> false
            }
          }

        fun List<String>.countHorizontalCrossings() = this
          .count { pipe ->
            when {
              pipe.startsWith("F") -> pipe.endsWith("J")
              pipe.startsWith("L") -> pipe.endsWith("7")
              pipe.startsWith("-") -> pipe.endsWith("-")
              pipe.startsWith("7") -> pipe.endsWith("L")
              else -> false
            }
          }

        val left = tiles.map[y].substring(0..<x)
          .foldIntoPipes(true)
          .countVerticalCrossings()

        val right = tiles.map[y].substring(x + 1..<width)
          .foldIntoPipes(true)
          .countVerticalCrossings()

        val bottoms = tiles.map.map { it[x] }.joinToString("").substring((y + 1)..<height)
          .foldIntoPipes(false)
        val bottom = bottoms
          .countHorizontalCrossings()

        val tops = tiles.map.map { it[x] }.joinToString("").substring(0..<y)
          .foldIntoPipes(false)
        val top = tops
          .countHorizontalCrossings()

        val inTheLoop = top % 2 != 0 && bottom % 2 != 0 && right % 2 != 0 && left % 2 != 0
        if (inTheLoop) {
          count++
          print("\u001B[32mI\u001B[0m")
        } else print("${char.boxDrawing}")
      }
      print("\n")
    }

    return count
  }

  val test1Input = readInputs("Day10.test.1")
  val test2Input = readInputs("Day10.test.2")
  val test3Input = readInputs("Day10.test.3")
  val test4Input = readInputs("Day10.test.4")
  val test5Input = readInputs("Day10.test.5")
  val input = readInputs("Day10")

  check(test1Input.part1() == 4)
  check(test2Input.part1() == 8)
  check(input.part1() == 6768)
  check(test3Input.part2() == 4)
  check(test4Input.part2() == 8)
  check(test5Input.part2() == 10)
  input.part2().println()
}