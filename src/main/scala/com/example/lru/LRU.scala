//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package com.example.lru

import scala.collection.mutable

class LRU[TKey, TValue](max: Int) {
  lazy val ll = new LinkedList[(TKey, TValue)]

  lazy val lookup = new mutable.HashMap[TKey, Node[(TKey, TValue)]]()

  private var cacheSize = 0

  def values: List[TValue] = ll.toList.map(_.data._2)

  def set(key: TKey, value: TValue): Unit = {
    lookup.get(key) match {
      case Some(n) =>
        mark(n)
      case None =>
        if (cacheSize == max) {
          ll.popLast.foreach { n =>
            lookup.remove(n.data._1)
          }
        } else {
          cacheSize += 1
        }

        lookup.put(key, mark(Node((key, value))))
    }
  }

  def remove(key: TKey): Option[TValue] = {
    val node = lookup.remove(key)
    cacheSize -= 1
    node.foreach(ll.removeNode)
    node.map(_.data._2)
  }

  def get(key: TKey): Option[TValue] = {
    lookup.get(key).map(node => {
      mark(node)
      node.data._2
    })
  }

  private def mark(n: Node[(TKey, TValue)]): Node[(TKey, TValue)] = {
    ll.removeNode(n)
    ll.prepend(n)
    n
  }
}
