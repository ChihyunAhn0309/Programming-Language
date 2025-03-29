package cs320

import Value._
import cs320.Main.name

object Implementation extends Template {
  type Env = Map[String, Value]

  def lookup(st:String, env:Env): Value = env.get(st) match{
    case Some(v) => v
    case None => error()
  }

  def paramtoargs(pa:List[String], arg:List[Expr], environ:Env, funcenv: Env): Env = pa match{
    case head :: next => arg match{
      case h :: t => paramtoargs(next, t, environ + (head -> interps(h, environ)), funcenv + (head -> interps(h, environ)))
      case Nil => error("wrong arity")
    }
    case Nil => arg match{
      case h :: n => error("wrong arity")
      case Nil => funcenv
    }
  }

  def exprmaptovalmap(expmap: Map[String, Expr], envir:Env): Map[String, Value] = {
    def etov(exm:Map[String, Expr], en:Env, ma: Map[String, Value]): Map[String, Value] = {
        val keylist = exm.keySet.toList
        keylist match { 
          case h :: t => etov(exm - h, en, ma + (h -> interps(exm(h), en)))
          case Nil => ma
        }
    }
    etov(expmap, envir, Map())
  }

  def numOp(op:(Int,Int) => Int): (Value, Value) => Value = {
    (_, _) match{
    case (NumV(num1), NumV(num2)) => NumV(op(num1, num2))
    case (_, _) => error()
    }
  }
  val numadd = numOp((_ + _))
  val numsub = numOp((_ - _))

  def interps(e: Expr, env:Env): Value = e match{
    case Num(num) => NumV(num)
    case Add(left, right) => numadd(interps(left, env), interps(right, env))
    case Sub(left, right) => numsub(interps(left, env), interps(right, env))
    case Id(name) => lookup(name, env)
    case Val(name, value, body) => interps(body, env + (name-> interps(value, env)))
    case Fun(params, body) => CloV(params, body, env)
    case App(func, args) => interps(func, env) match{
      case CloV(params, body, fenv) => interps(body, paramtoargs(params, args, env, fenv))
      case NumV(n) => error("not a closure")
      case RecV(map) => error("not a closure")
    }
    case Rec(rec) => RecV(exprmaptovalmap(rec, env))
    case Acc(expr, name) => interps(expr, env) match{
      case RecV(map) => map.get(name) match{
        case Some(v) => v
        case None => error("no such field")
      }
      case _ => error("not a record")
    }
  }


  def interp(expr: Expr): Value = {
    interps(expr, Map())
  }
}
