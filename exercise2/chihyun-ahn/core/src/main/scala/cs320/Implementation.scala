package cs320

object Implementation extends Template {

  def freeIds(expr: Expr): Set[String] = {
    def checkfree(e: Expr, s: Set[String]): Set[String] = {
      e match{
        case Num(value) => Set()
        case Add(left, right) => checkfree(left,s) ++ checkfree(right,s)
        case Sub(left, right) => checkfree(left,s) ++ checkfree(right,s)
        case Val(name, expr, body) => checkfree(expr,s) ++ checkfree(body,s + name)
        case Id(id) => if (s.contains(id)) Set() else Set(id)
      }
    }
    checkfree(expr, Set())
  }
  
  def bindingIds(expr: Expr): Set[String] = {
    expr match{
      case Add(left, right) => bindingIds(left) ++ bindingIds(right)
      case Id(id) => Set()
      case Num(value) => Set()
      case Sub(left, right) => bindingIds(left) ++ bindingIds(right)
      case Val(name, expr, body) => Set(name) ++ bindingIds(expr) ++ bindingIds(body)
    }
  }

  def boundIds(expr: Expr): Set[String] = {
    def checkbound(e: Expr, sb: Set[String]): Set[String] = {
      e match{
        case Add(left, right) => checkbound(left, sb) ++ checkbound(right, sb)
        case Id(id) => if (sb.contains(id)) Set(id) else Set()
        case Num(value) => Set()
        case Sub(left, right) => checkbound(left, sb) ++ checkbound(right, sb)
        case Val(name, expr, body) => checkbound(expr, sb) ++ checkbound(body, sb + name)
      }
    }
    checkbound(expr, Set())
  }
}
