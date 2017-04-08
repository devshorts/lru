package com.example.lru

import org.scalatest.{FlatSpec, Matchers}

class LRUSpec extends FlatSpec with Matchers {
  "LL" should "make" in {
    val l = new LinkedList[Int]

    val n1 = l.append(Node(1))
    val n2 = l.append(Node(2))
    val n3 = l.append(Node(3))

    l.removeNode(n2)

    assert(l.toList == List(n1, n3))
  }

  it should "prepend" in {
    val l = new LinkedList[Int]

    val n1 = l.prepend(Node(1))
    val n2 = l.prepend(Node(2))
    val n3 = l.prepend(Node(3))

    assert(l.toList == List(n3, n2, n1))
  }

  it should "drop after" in {
    val l = new LinkedList[Int]

    val n1 = l.append(Node(1))
    val n2 = l.append(Node(2))
    val n3 = l.append(Node(3))

    l.dropAfter(n2)

    assert(l.toList == List(n1, n2))
  }

  it should "drop before" in {
    val l = new LinkedList[Int]

    val n1 = l.append(Node(1))
    val n2 = l.append(Node(2))
    val n3 = l.append(Node(3))

    l.dropBefore(n2)

    assert(l.toList == List(n2, n3))
  }

  it should "pop last" in {
    val l = new LinkedList[Int]

    val n1 = l.append(Node(1))
    val n2 = l.append(Node(2))
    val n3 = l.append(Node(3))

    assert(l.popLast.get == n3)

    assert(l.toList == List(n1, n2))
  }

  "LRU" should "cycle" in {
    val lru = new LRU[String, Int](3)

    lru.set("one", 1)
    lru.set("two", 2)
    lru.set("three", 3)
    lru.set("four", 4)

    lru.values shouldEqual List(4, 3, 2)

    lru.get("three")

    lru.values shouldEqual List(3, 4, 2)

    lru.get("three")

    lru.values shouldEqual List(3, 4, 2)

    lru.get("missing")

    lru.values shouldEqual List(3, 4, 2)

    lru.get("four")

    lru.values shouldEqual List(4, 3, 2)

    lru.set("one", 1)

    lru.values shouldEqual List(1, 4, 3)
  }
}

