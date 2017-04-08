import com.devhorts.binpack.TwoDBinPacker
import org.scalatest.{FlatSpec, Matchers}

class BitPack extends FlatSpec with Matchers {
  "BitPacker" should "pack" in {
    val packer = new TwoDBinPacker(2, 2)

    packer.set(1, 1, true)
    packer.set(0, 0, true)
    assert(packer.get(0, 0))
    assert(!packer.get(0, 1))
    assert(!packer.get(1, 0))
    assert(packer.get(1, 1))

    packer.set(0, 1, true)
    assert(packer.get(0, 0))
    assert(packer.get(0, 1))
    assert(!packer.get(1, 0))
    assert(packer.get(1, 1))

    packer.set(0, 1, false)
    assert(packer.get(0, 0))
    assert(!packer.get(0, 1))
    assert(!packer.get(1, 0))
    assert(packer.get(1, 1))

    packer.list shouldEqual List(
      (0, 0),
      (1, 1)
    )
  }

  it should "pack large sets (10 billion!)" in {
    val packer = new TwoDBinPacker(100000, 100000)

    packer.set(0, 0, true)
    packer.set(200, 400, true)

    assert(packer.get(0, 0))
    assert(packer.get(200, 400))
    assert(!packer.get(99999, 88888))
  }
}
