package scala
package collection
package mutable

import scala.collection.decorators.MutableMapDecorator

/**
  * A mutable multiset.
  */
trait MultiSet[A]
  extends collection.MultiSet[A]
    with collection.MultiSetOps[A, MultiSet, MultiSet[A]]
    with Growable[A]
    with Shrinkable [A] {

  override def iterableFactory: IterableFactory[MultiSet] = MultiSet
}

class MultiSetImpl[A] private[mutable] (val elems: Map[A, Int]) extends MultiSet[A] {

  def occurrences: collection.Map[A, Int] = elems

  def addOne(elem: A): this.type = {
    elems.updateWith(elem) {
      case None    => Some(1)
      case Some(n) => Some(n + 1)
    }
    this
  }

  def subtractOne(elem: A): this.type = {
    elems.updateWith(elem) {
      case Some(n) => if (n > 1) Some(n - 1) else None
    }
    this
  }

  def clear(): Unit = elems.clear()
}

object MultiSet extends IterableFactory[MultiSet] {

  def from[A](source: IterableOnce[A]): MultiSet[A] = (newBuilder[A] ++= source).result()

  def empty[A]: MultiSet[A] = new MultiSetImpl[A](Map.empty)

  def newBuilder[A]: Builder[A, MultiSet[A]] = new GrowableBuilder[A, MultiSet[A]](empty)

}