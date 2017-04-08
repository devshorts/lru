//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package com.example.lru

import scala.collection.mutable

case class Node[T](data: T) {
  var next: Option[Node[T]] = None
  var prev: Option[Node[T]] = None

  def reset(): Unit = {
    next = None
    prev = None
  }
}

class LinkedList[T] extends mutable.Iterable[Node[T]] {
  private var _head: Option[Node[T]] = None
  private var _last: Option[Node[T]] = _head

  override def iterator: Iterator[Node[T]] = new Iterator[Node[T]] {
    var curr: Option[Node[T]] = _head

    override def hasNext: Boolean = curr.isDefined

    override def next(): Node[T] = {
      val ret = curr.get

      curr = curr.flatMap(_.next)

      ret
    }
  }

  private def revIterator: Iterator[Node[T]] = new Iterator[Node[T]] {
    var curr: Option[Node[T]] = _last

    override def hasNext: Boolean = curr.isDefined

    override def next(): Node[T] = {
      val ret = curr.get

      curr = curr.flatMap(_.prev)

      ret
    }
  }

  def dropAfter(node: Node[T]): Node[T] = {
    _last = Some(node)
    node.next = None
    node
  }

  def dropBefore(node: Node[T]): Node[T] = {
    _head = Some(node)
    node.prev = None
    node
  }

  def prepend(node: Node[T]): Node[T] = {
    node.reset()

    if (_head.isEmpty) {
      makeNodeRoot(node)
    } else {
      if (_head.contains(node)) {
        return node
      }
      _head.foreach(_.prev = Some(node))
      node.next = _head
      _head = Some(node)
    }

    node
  }

  def append(node: Node[T]): Node[T] = {
    node.reset()

    if (_head.isEmpty) {
      makeNodeRoot(node)
    } else {
      if (_last.contains(node)) {
        return node
      }
      _last.foreach(_.next = Some(node))
      node.prev = _last
      _last = Some(node)
    }

    node
  }

  def trimBegin(n: Int): List[Node[T]] = {
    val toDrop = iterator.take(n).toList

    _head = toDrop.lastOption

    toDrop
  }

  def popLast: Option[Node[T]] = {
    val originalLast = _last

    val nextLast = _last.flatMap(_.prev)
    nextLast.foreach(_.next = None)
    _last = nextLast

    originalLast
  }

  def trimEnd(n: Int): List[Node[T]] = {
    val toDrop = revIterator.take(n).toList

    _head = toDrop.lastOption

    toDrop
  }

  def makeNodeRoot(node: Node[T]): Unit = {
    if (_head.contains(node)) {
      return
    }

    _head.foreach(_.prev = Some(node))
    node.next = _head
    _head = Some(node)

    if (_last.isEmpty) {
      _last = _head
    }
  }

  def removeNode(node: Node[T]) = {
    if (_head.contains(node)) {
      _head = node.next
    }

    if (_last.contains(node)) {
      _last = node.prev
    }

    node.prev.foreach(_.next = node.next)
    node.next.foreach(_.prev = node.prev)
  }
}