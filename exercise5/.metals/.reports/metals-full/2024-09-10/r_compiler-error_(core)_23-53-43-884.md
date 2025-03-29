file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### scala.reflect.internal.FatalError: 
  ThisType(method lookup) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar;<WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(_CURSOR_env)
       tree position: line 28 of file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

    25   def exprmaptovalmap(expmap: Map[Stirng, Expr], envir:Env): Map[Stirng, Value] = {
    26     val retmap: Map[String, Value] = Map()
    27     for ((k, v) <- expmap)
    28       retmap + (k -> interps(v, _CURSOR_env))
    29   }
    30 
    31   def numOp(op:(Int,Int) => Int): (Value, Value) => Value = {

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 805
uri: file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
text:
```scala
package cs320

import Value._
import cs320.Main.name

object Implementation extends Template {
  type Env = Map[String, Value]

  def lookup(st:String, env:Env): Value = env.get(st) match{
    case Some(v) => v
    case None => error()
  }

  def paramtoargs(pa:List[String], arg:List[Expr], environ:Env): Env = pa match{
    case head :: next => arg match{
      case h :: t => paramtoargs(next, t, environ + (head -> interps(h, environ)))
      case Nil => error("wrong arity")
    }
    case Nil => arg match{
      case h :: n => error("wrong arity")
      case Nil => environ
    }
  }

  def exprmaptovalmap(expmap: Map[Stirng, Expr], envir:Env): Map[Stirng, Value] = {
    val retmap: Map[String, Value] = Map()
    for ((k, v) <- expmap)
      retmap + (k -> interps(v, @@env))
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
      case CloV(params, body, fenv) => interps(body, paramtoargs(params, args, fenv))
      case NumV(n) => error("not a closure")
      case RecV(map) => error("not a closure")
    }
    case Rec(rec) => interps(rec.get(_), env)
//      case _: Map[_, _] => Map(_ -> interps(_, env))
//    }

    case Acc(expr, name) => interps(expr, env) match{
      case RecV(map) => map.get(name) match{
        case Some(v) => v
        case None => error("no such field")
      }
      case _ => error("not a record")

      lookup(name, env)

    }
  }


  def interp(expr: Expr): Value = {
    interps(expr, Map())
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
  ThisType(method lookup) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar;<WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(_CURSOR_env)
       tree position: line 28 of file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

    25   def exprmaptovalmap(expmap: Map[Stirng, Expr], envir:Env): Map[Stirng, Value] = {
    26     val retmap: Map[String, Value] = Map()
    27     for ((k, v) <- expmap)
    28       retmap + (k -> interps(v, _CURSOR_env))
    29   }
    30 
    31   def numOp(op:(Int,Int) => Int): (Value, Value) => Value = {