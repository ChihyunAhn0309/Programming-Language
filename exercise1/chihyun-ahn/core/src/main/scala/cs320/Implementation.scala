package cs320

object Implementation extends Template {

  def volumeOfCuboid(a: Int, b: Int, c: Int): Int = {
    if (a < 0 | b < 0 | c < 0) error()
    else a * b * c
  }

  def concat(x: String, y: String): String = {
    x + y
  }

  def addN(n: Int): Int => Int = {
    x => x + n
  }

  def twice(f: Int => Int): Int => Int = {
    x => f(f(x))
  }

  def compose(f: Int => Int, g: Int => Int): Int => Int = {
    x => g(f(x))
  }

  def double(l: List[Int]): List[Int] = {
    l match{
      case head :: next =>  head *2 :: double(next)
      case Nil => Nil
    }
  }

  def sum(l: List[Int]): Int = {
    l match{
      case Nil => 0
      case h::t => h + sum(t)
    }
  }

  def getKey(m: Map[String, Int], s: String): Int = {
    m.get(s) match{
      case None => error(s)
      case Some(value) => value
    }
  }

  def countLeaves(t: Tree): Int = {
    t match{
      case Branch(left, value, right) => countLeaves(left) + countLeaves(right) 
      case Leaf(value) => 1
    }
  }

  def flatten(t: Tree): List[Int] = {
    t match{
      case Branch(left, value, right) => {
        flatten(left) ::: List(value) ::: flatten(right)
      }
      case Leaf(value) => List(value)
    }
  }
}
