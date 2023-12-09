typealias Network = Map<String, Pair<String, String>>

fun main() {
  fun List<String>.format(): Pair<String, Network> = this.let {
    this[0] to this.drop(2)
      .associate { line ->
        line.split(" = (", ", ", ")")
          .let { parts -> parts[0] to (parts[1] to parts[2]) }
      }
  }

  fun Network.traverse(start: String, instructions: String): Sequence<String> = sequence {
    var instruction = instructions.iterator()
    var current = start.also { yield(it) }
    while (true) {
      if (!instruction.hasNext()) instruction = instructions.iterator()
      current = if (instruction.next() == 'L') this@traverse[current]!!.first else this@traverse[current]!!.second
      yield(current)
    }
  }

  fun List<String>.part1() = format()
    .let { (instructions, network) -> network.traverse("AAA", instructions) }
    .takeWhile { end -> end != "ZZZ" }
    .count()

  fun List<String>.part2() = format()
    .let { (instructions, network) ->
      network.entries
        .filter { (node, _) -> node.endsWith("A") }
        .map { (node) -> network.traverse(node, instructions) }
        .map { sequence -> sequence.takeWhile { !it.endsWith("Z") }.count().toLong() }
        .reduce { acc, i -> lcm(acc, i) }
    }

  val test1Input = readInputs("Day08.test.1")
  val test2Input = readInputs("Day08.test.2")
  val test3Input = readInputs("Day08.test.3")
  val input = readInputs("Day08")

  check(test1Input.part1() == 2)
  check(test2Input.part1() == 6)
  check(input.part1() == 11911)

  check(test3Input.part2() == 6L)
  check(input.part2() == 10151663816849L)
}