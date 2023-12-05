import kotlin.time.measureTime

typealias Mapping = List<Row>
typealias Row = Pair<LongRange, LongRange>

fun main() {
  fun String.format(): Pair<List<Long>, List<Mapping>> {
    val almanac = this.split("\n\n")
    val seeds: List<Long> = almanac.first().split(":").last().trim().split(" ").map { it.toLong() }
    val mappings: List<Mapping> = almanac.drop(1).map { mappings ->
      mappings.split("\n").drop(1).map { line ->
        val (destination: Long, source: Long, length: Long) = line.split(" ").map { it.toLong() }
        source..(source + length) to destination..(destination + length)
      }
    }

    return seeds to mappings
  }

  fun Long.resolve(mappings: List<Mapping>) = mappings.fold(this) { value, mapping ->
    mapping.find { (source, _) -> value in source }
      ?.let { (source, destinations) ->
        if (value !in source) value else destinations.first + (value - source.first)
      }
      ?: value
  }

  fun String.part1() = format()
    .let { (seeds, mappings) -> seeds.minOfOrNull { seed -> seed.resolve(mappings) } }

  fun String.part2() = format()
    .let { (seedRanges, mappings) ->
      seedRanges
        .chunked(2).map { (start, length) -> start..(start + length) }
        .minOf { seedRange -> seedRange.minOf { seed -> seed.resolve(mappings) } }
    }

  val test1Input = readInput("Day05.test")
  val input = readInput("Day05")

  check(test1Input.part1() == 35L)
  check(input.part1() == 322500873L)

  check(test1Input.part2() == 46L)
  measureTime { check(input.part2() == 108956227L) }.println()
}