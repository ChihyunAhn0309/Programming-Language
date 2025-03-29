package cs320

import Value._

object Implementation extends Template {

  def lookup(x:String, environ:Env): Value = {
    environ.get(x) match{
      case Some(value) => value
      case None => error()
    }
  }

  def numAdd(x:Value, y:Value): Value = {
    (x, y)match{
      case (IntV(p), IntV(q)) => IntV(p + q)
      case _ => error()
    }
  }

  def numMul(x:Value, y:Value): Value = {
    (x, y)match{
      case (IntV(p), IntV(q)) => IntV(p * q)
      case _ => error()
    }
  }

  def numDiv(x:Value, y: Value): Value = {
    (x, y)match{
      case (IntV(x), IntV(y)) => if(y == 0) error() else IntV(x / y)
      case _ => error()
    }
  }

  def numMode(x:Value, y: Value): Value = {
    (x, y)match{
      case (IntV(x), IntV(y)) => if(y == 0) error() else IntV(x % y)
      case _ => error()
    }
  }

  def numboolOp(op: (BigInt, BigInt) => Boolean): (Value, Value) => (Value) = {
    (_,_)match{
      case (IntV(x), IntV(y)) => if (op(x, y) == true) BooleanV(true) else BooleanV(false)
      case _ => error()
    }
  }

  val numsame = numboolOp(_ == _)
  val numbig = numboolOp(_ < _)
  // 아직 나눗셈 mod < == 못만듬

  def exprtovallist(l: List[Expr], e: Env): List[Value] = {
    def exprtoval(list: List[Expr], env: Env, resultlist: List[Value]): List[Value] = {
      list match{
        case head :: next => exprtoval(next, env, resultlist ::: List(interps(head, env)))
        case Nil => resultlist
      }
    }
    exprtoval(l, e, List())
  }

  def listind(l: List[Value], rightindex: Int, i: Int): Value = {
    l match{
      case head :: next => if(rightindex == i) head else listind(next, rightindex, i + 1)
      case Nil => error()
    }
  }

  def taillist(list: List[Value], result: Value): Value = {
    list match{
      case head :: next => taillist(next, head)
      case Nil => result
    }
  }

  def functoclo(flist: List[FunDef], env: Env): Env = {
    flist match{
      case head :: next => head match{
        case FunDef(n, param, b) => functoclo(next, env + (n -> CloV(param, b, env)))
        case _ => error()
      }
      case Nil => env
    }
  }

  def changeenv(flist: List[FunDef], env: Env): Env = {
    flist match{
      case head :: next => env(head.name) match{
        case ev@CloV(parameters, body, fenv) => {
          ev.env = env
          changeenv(next, env)
        }
        case _ => error()
      }
      case Nil => env
    }
  }

  def paratoarg(param:List[String], a:List[Expr], fe:Env, e:Env): Env = {
    param match{
      case head :: next => a match{
        case h :: t => paratoarg(next, t, fe + (head -> interps(h, e)), e)
        case Nil => error()
      }
      case Nil => a match{
        case head :: next => error()
        case Nil => fe
      }
    }
  }

  def interps(e:Expr, env: Env): Value = e match{
    case Id(name) => lookup(name, env)
    case IntE(value) => IntV(value)
    case BooleanE(value) => BooleanV(value)
    case Add(left, right) => numAdd(interps(left, env), interps(right, env))
    case Mul(left, right) => numMul(interps(left, env), interps(right, env))
    case Div(left, right) => numDiv(interps(left, env), interps(right, env))
    case Mod(left, right) => numMode(interps(left, env), interps(right, env))
    case Eq(left, right) => numsame(interps(left, env), interps(right, env))
    case Lt(left, right) => numbig(interps(left, env), interps(right, env))
    case If(condition, trueBranch, falseBranch) => {
      interps(condition, env) match{
        case BooleanV(value) => if(value == true) interps(trueBranch, env) else interps(falseBranch, env)
        case _ => error()
      }
    }
    case TupleE(expressions) => TupleV(exprtovallist(expressions, env))
    case Proj(expression, index) => interps(expression, env) match{
      case TupleV(values) => listind(values, index, 1)
      case _ => error()
    }
    case NilE => NilV
    case ConsE(head, tail) => interps(tail, env) match{
      case ConsV(h, t) => ConsV(interps(head, env), interps(tail, env))
      case NilV => ConsV(interps(head, env), NilV)
      case _ => error()
    }
    case Empty(expression) => interps(expression, env) match{
      case ConsV(head, tail) => BooleanV(false)
      case NilV => BooleanV(true)
      case _ => error()
    }
    case Head(expression) => interps(expression, env) match{
      case ConsV(head, tail) => head
      case _ => error()
    }
    case Tail(expression) => interps(expression, env) match{
      case ConsV(head, tail) => tail
      case _ => error()
    }
    case Val(name, expression, body) => interps(body, env + (name -> interps(expression, env)))
    case Fun(parameters, body) => CloV(parameters, body, env)
    case RecFuns(functions, body) =>  interps(body, changeenv(functions, functoclo(functions, env)))
    case App(function, arguments) => interps(function, env) match{
      case CloV(parameters, body, fenv) => interps(body, paratoarg(parameters, arguments, fenv, env))
      case _ => error()
    }
    case Test(expression, typ) => interps(expression, env) match{
      case IntV(value) => if(typ == IntT) BooleanV(true) else BooleanV(false)
      case BooleanV(value) => if(typ == BooleanT) BooleanV(true) else BooleanV(false)
      case TupleV(values) => if(typ == TupleT) BooleanV(true) else BooleanV(false)
      case NilV => if(typ == ListT) BooleanV(true) else BooleanV(false)
      case ConsV(head, tail) => if(typ == ListT) BooleanV(true) else BooleanV(false)
      case CloV(parameters, body, env) => if(typ == FunctionT) BooleanV(true) else BooleanV(false)

    }


  }

  def interp(expr: Expr): Value = {
    interps(expr, Map())
  }

}
