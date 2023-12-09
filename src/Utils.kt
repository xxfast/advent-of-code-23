import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInputs(name: String) = Path("src/$name.txt").readLines()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
  .toString(16)
  .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun lcm(a: Long, b: Long): Long {
  val larger = if (a > b) a else b
  val maxLcm = a * b
  var lcm = larger
  while (lcm <= maxLcm) {
    if (lcm % a == 0L && lcm % b == 0L) {
      return lcm
    }
    lcm += larger
  }
  return maxLcm
}
