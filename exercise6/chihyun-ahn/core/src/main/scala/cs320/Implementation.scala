package cs320

import Value._
import java.security.KeyStore
import scala.reflect.api.Exprs

object Implementation extends Template {

  type Store = Map[Addr, Value]

  def lookup(id: String, environ: Env): Value = {
    environ.get(id) match{
      case Some(value) => value
      case None => error()
    }
  }
  
  def storeLookup(add: Addr, st: Store): Value = {
    st.get(add) match{
      case Some(value) => value
      case None => error()
    }
  }

  def numOp(op: (Int, Int) => Int): (Value, Value) => Value = {
    (_,_) match{
      case (NumV(ln), NumV(rn)) => NumV(op(ln, rn))
      case _ => error()
    }
  }

  val numadd = numOp(_ + _)
  val numsub = numOp(_ - _)

  def malloc(sto: Store): Addr =
    sto.foldLeft(0){
      case (max, (add, _)) => math.max(max, add)
    } + 1

  def seqnlist(l: List[Expr], e:Env, s:Store, v:Value): (Value, Store) = {
    l match{
      case head :: next => val(lv, ls) = interps(head, e, s)
      seqnlist(next, e, ls, lv)
      case Nil => (v, s)
    }
  }

  def exprtoaddr(fd: List[(String, Expr)], e: Env, st: Store):(Map[String, Addr], Store) = {
    def etoa(fd: List[(String, Expr)], env: Env, sto: Store, resultmap: Map[String, Addr]):(Map[String, Addr], Store) = {
      fd match{
        case head :: next => {
          head match{
            case (s: String, exp: Expr) =>{
              val(ev, es) = interps(exp, env, sto)
              val addr = malloc(es)
              etoa(next, e + (s -> ev), es + (addr-> ev), resultmap + (s -> addr))
            }
            case _ => error()
          }
        }
        case Nil => (resultmap, sto)
      }
    }
    etoa(fd, e, st, Map())
  }
  
  def interps(e: Expr, env: Env, sto: Store) :(Value, Store) = e match{
    case Num(num) => (NumV(num), sto)
    case Add(left, right) => {
      val (lv, ls) = interps(left, env, sto)
      val (rv, rs) = interps(right, env, ls)
      (numadd(lv, rv), rs)
    }
    case Sub(left, right) => {
      val (lv, ls) = interps(left, env, sto)
      val (rv, rs) = interps(right, env, ls)
      (numsub(lv, rv), rs)
    }
    case Id(name) => (lookup(name, env), sto)
    case Fun(param, body) => (CloV(param, body, env), sto)
    case App(fun, arg) => {
      val(fv, fs) = interps(fun, env, sto)
      val(av, as) = interps(arg, env, fs)
      fv match {
        case CloV(param, body, fenv) => {
          interps(body, fenv + (param -> av), as)
        }
        case _ => error()
      }
    }
    case NewBox(expr) => {
      val(bv, bs) = interps(expr, env, sto)
      val address = malloc(bs)
      (BoxV(address),bs+(address -> bv))
    }
    case OpenBox(box) => {
      val(bv, bs) = interps(box, env, sto)
      bv match{
        case BoxV(addr) => (storeLookup(addr, bs), bs)
        case _ => error()
      }
    }
    case SetBox(box, expr) => {
      val(bv, bs) = interps(box, env, sto)
      val(ev, es) = interps(expr, env, bs)
      bv match{
        case BoxV(addr) => (ev, es + (addr -> ev))
        case _ => error()
      }
    }
    case Seqn(left, right) => {
      val(lv, ls) = interps(left, env, sto)
      seqnlist(right, env, ls, lv)

    }
    case Get(record, field) =>{
      val(rv, rs) = interps(record, env, sto)
      rv match{
        case RecV(fields) => fields.get(field) match{
          case Some(v) => (rs(v), rs)
          case None => error("no such field")
        }
        case _ => error("not a record")
      }
    }
    case Rec(fields) => {
      exprtoaddr(fields, env, sto) match{
        case (addmap: Map[String, Addr], store:Store) => (RecV(addmap), store)
        case _ => error()
      }
    }
    case Set(record, field, expr) => {
      val(rv, rs) = interps(record, env, sto)
      val(ev, es) = interps(expr, env, rs)
      rv match{
        case RecV(fields) => fields.get(field) match{
          case Some(v) =>{
            (ev, es + (v -> ev))
          }
          case None => error("no such field")
        }
        case _ => error("not a record")
      }
    }
  }

  def interp(expr: Expr): Value = {
    interps(expr, Map(), Map()) match {
      case (v:Value, s:Store) => v
      case _ => error()
    }
  }

}
