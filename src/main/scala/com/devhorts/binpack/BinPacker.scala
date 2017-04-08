package com.devhorts.binpack

case class BitValue(index: Int, container: Long, bitNumber: Int) {
  val value = (container >> bitNumber) & 1

  def set(boolean: Boolean): Long = {
    if (boolean) {
      val maskAt = 1 << bitNumber

      container | maskAt
    } else {
      val maskAt = ~(1 << bitNumber)

      container & maskAt
    }
  }
}

case class BitFinder(container: Long) {
  def setBits: Seq[Int] = {
    (0 until 64).filter { bit =>
      (container >> bit & 1) == 1
    }
  }
}

class TwoDBinPacker(maxX: Int, maxY: Int) {
  private val maxBits = maxX * maxY
  private val requiredLongs = (maxBits / 64) + 1
  private val longArray = new Array[Long](requiredLongs)

  def get(x: Int, y: Int): Boolean = {
    longAtPosition(x, y).value == 1
  }

  def set(x: Int, y: Int, value: Boolean) = {
    val p = longAtPosition(x, y)

    longArray(p.index) = p.set(value)
  }

  def list: List[(Int, Int)] = {
    longArray.zipWithIndex.flatMap {
      case (container, idx) => {
        BitFinder(container).setBits.map(bit => {
          val position = idx * 64 + bit

          val x = position % maxX
          val y = position / maxX

          (x.toInt, y.toInt)
        })
      }
    }.toList
  }

  private def longAtPosition(x: Int, y: Int): BitValue = {
    val flattenedPosition = y * maxX + x

    val longAtPosition = flattenedPosition / 64

    val bitAtPosition = flattenedPosition % 64

    BitValue(longAtPosition, longArray(longAtPosition), bitAtPosition)
  }
}
