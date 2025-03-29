package cs320

object Implementation extends Template {

  // apply a binary numeric function on all the combinations of numbers from
  // the two input lists, and return the list of all the results
  def binOp(
    op: (Int, Int) => Int,
    ls: List[Int],
    rs: List[Int]
  ): List[Int] = ls match {
    case Nil => Nil
    case l :: rest =>
      def f(r: Int): Int = {
        op(l,r)
      }
      rs.map(f) ++ binOp(op, rest, rs)
  }

  def interp(expr: Expr): List[Int] = {
    def lookup(i: String, ma: Map[String, List[Int]]): List[Int] = {
      ma.get(i) match{
        case None => error("free identifier");
        case Some(value) => value
      }
    }
    def addV: (Int,Int) => Int = {
      (x,y) => x + y
    }
    def subV: (Int,Int) => Int = {
      (x,y) => x - y
    }
    def biggerV: (Int,Int) => Int = {
      (x,y) => if (x > y) x else y
    }
    def smallerV: (Int,Int) => Int = {
      (x,y) => if (x < y) x else y
    }
    def interps(e: Expr, m: Map[String, List[Int]]): List[Int] ={
      e match{
        case Num(values) => values
        case Add(left, right) => binOp(addV, interps(left, m), interps(right, m))
        case Sub(left, right) => binOp(subV, interps(left, m), interps(right, m))
        case Id(id) => lookup(id, m)
        case Val(name, exp, body) => interps(body, m + (name->interps(exp, m)))
        case Max(left, mid, right) => binOp(biggerV, interps(right, m), binOp(biggerV, interps(left,m), interps(mid,m)))
        case Min(left, mid, right) => binOp(smallerV, interps(right, m), binOp(smallerV, interps(left,m), interps(mid,m)))
      }
    }
    interps(expr, Map())
  }
}
