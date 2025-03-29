file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### scala.reflect.internal.FatalError: 
  ThisType(method storeLookup) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar;<WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(st_CURSOR_o)
       tree position: line 126 of file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

   123         case _ => error()
   124       }
   125     }
   126     case Rec(fields) => exprtoaddr(fields, env, st_CURSOR_o)
   127     case Set(record, field, expr) => 
   128   }
   129 

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 3550
uri: file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
text:
```scala
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

  def exprtoaddr(fd: List[(String, Expr)], e: Env, st: Store):Map[String, Addr] = {
    def etoa(fd: List[(String, Expr)], env: Env, sto: Store, resultmap: Map[String, Addr]):Map[String, Addr] = {
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
        case Nil => resultmap
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
          case None => error()
        }
        case _ => error()
      }
    }
    case Rec(fields) => exprtoaddr(fields, env, st@@o)
    case Set(record, field, expr) => 
  }

  def interp(expr: Expr): Value = {
    interps(expr, Map(), Map()) match {
      case (v:Value, s:Store) => v
      case _ => error()
    }
  }

}

```



#### Error stacktrace:

```
scala.reflect.internal.Reporting.abort(Reporting.scala:70)
	scala.reflect.internal.Reporting.abort$(Reporting.scala:66)
	scala.reflect.internal.SymbolTable.abort(SymbolTable.scala:28)
	scala.reflect.internal.Types$ThisType.<init>(Types.scala:1394)
	scala.reflect.internal.Types$UniqueThisType.<init>(Types.scala:1414)
	scala.reflect.internal.Types$ThisType$.apply(Types.scala:1418)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:74)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:60)
	scala.collection.immutable.List.collect(List.scala:268)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:60)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:306)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  ThisType(method storeLookup) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar;<WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(st_CURSOR_o)
       tree position: line 126 of file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

   123         case _ => error()
   124       }
   125     }
   126     case Rec(fields) => exprtoaddr(fields, env, st_CURSOR_o)
   127     case Set(record, field, expr) => 
   128   }
   129 