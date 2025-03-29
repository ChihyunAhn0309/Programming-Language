file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### java.lang.IndexOutOfBoundsException: -1 is out of bounds (min 0, max 2)

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
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

//  def func(goal : Map[String, Expr]) : list[Value] = {
    
//  }

  def exprmaptovalmap(expmap: Map[String, Expr], envir:Env): Map[String, Value] = {
    /*val keylist = expmap.keySet.toList
    keylist match{
      case h :: t => expmap.h  
      case Nil => 
    }*/
    def etov(exm:Map[String, Expr], en:Env, ma: Map[String, Value]): Map[String, Value] = {
      if(exm == Map())
        println(ma)
        ma
        val keylist = exp
          etov(exm - k, en, ma + (k -> interps(v, en)))
      }
      ma
    }

    /*val keylist = expmap.keySet.toList
    println(keylist)
    envir*/

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
      case CloV(params, body, fenv) => interps(body, paramtoargs(params, args, fenv))
      case NumV(n) => error("not a closure")
      case RecV(map) => error("not a closure")
    }
    case Rec(rec) => RecV(exprmaptovalmap(rec, env))
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
scala.collection.mutable.ArrayBuffer.apply(ArrayBuffer.scala:102)
	scala.reflect.internal.Types$Type.findMemberInternal$1(Types.scala:1030)
	scala.reflect.internal.Types$Type.findMember(Types.scala:1035)
	scala.reflect.internal.Types$Type.memberBasedOnName(Types.scala:661)
	scala.reflect.internal.Types$Type.nonLocalMember(Types.scala:652)
	scala.tools.nsc.typechecker.Contexts$ImportInfo.importedSelectedSymbol(Contexts.scala:1944)
	scala.tools.nsc.typechecker.Contexts$SymbolLookup.apply(Contexts.scala:1530)
	scala.tools.nsc.typechecker.Contexts$Context.lookupSymbol(Contexts.scala:1282)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdent$2(Typers.scala:5663)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdentOrWildcard$1(Typers.scala:5732)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6203)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedPattern$2(Typers.scala:6440)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedPattern$1(Typers.scala:6440)
	scala.tools.nsc.typechecker.TypeDiagnostics.typingInPattern(TypeDiagnostics.scala:71)
	scala.tools.nsc.typechecker.TypeDiagnostics.typingInPattern$(TypeDiagnostics.scala:68)
	scala.meta.internal.pc.MetalsGlobal$MetalsInteractiveAnalyzer.typingInPattern(MetalsGlobal.scala:78)
	scala.tools.nsc.typechecker.Typers$Typer.typedPattern(Typers.scala:6440)
	scala.tools.nsc.typechecker.Typers$Typer.typedCase(Typers.scala:2682)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedCases$1(Typers.scala:2716)
	scala.tools.nsc.typechecker.Typers$Typer.typedCases(Typers.scala:2715)
	scala.tools.nsc.typechecker.Typers$Typer.typedMatch(Typers.scala:2727)
	scala.tools.nsc.typechecker.Typers$Typer.typedVirtualizedMatch$1(Typers.scala:4924)
	scala.tools.nsc.typechecker.Typers$Typer.typedOutsidePatternMode$1(Typers.scala:6184)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6215)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedCase(Typers.scala:6350)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedCases$1(Typers.scala:2716)
	scala.tools.nsc.typechecker.Typers$Typer.typedCases(Typers.scala:2715)
	scala.tools.nsc.typechecker.Typers$Typer.typedMatch(Typers.scala:2727)
	scala.tools.nsc.typechecker.Typers$Typer.typedVirtualizedMatch$1(Typers.scala:4924)
	scala.tools.nsc.typechecker.Typers$Typer.typedOutsidePatternMode$1(Typers.scala:6184)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6215)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedDefDef(Typers.scala:6525)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6167)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedStat$1(Typers.scala:6339)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedStats$9(Typers.scala:3539)
	scala.tools.nsc.typechecker.Typers$Typer.typedStats(Typers.scala:3539)
	scala.tools.nsc.typechecker.Typers$Typer.typedTemplate(Typers.scala:2144)
	scala.tools.nsc.typechecker.Typers$Typer.typedModuleDef(Typers.scala:2020)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6169)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedStat$1(Typers.scala:6339)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typedStats$9(Typers.scala:3539)
	scala.tools.nsc.typechecker.Typers$Typer.typedStats(Typers.scala:3539)
	scala.tools.nsc.typechecker.Typers$Typer.typedPackageDef$1(Typers.scala:5844)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6171)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Analyzer$typerFactory$TyperPhase.apply(Analyzer.scala:125)
	scala.tools.nsc.Global$GlobalPhase.applyPhase(Global.scala:481)
	scala.tools.nsc.interactive.Global$TyperRun.applyPhase(Global.scala:1369)
	scala.tools.nsc.interactive.Global$TyperRun.typeCheck(Global.scala:1362)
	scala.tools.nsc.interactive.Global.typeCheck(Global.scala:680)
	scala.meta.internal.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:22)
	scala.meta.internal.pc.SimpleCollector.<init>(PcCollector.scala:340)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:19)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzycompute$1(PcSemanticTokensProvider.scala:19)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:19)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:73)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticTokens$1(ScalaPresentationCompiler.scala:186)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: -1 is out of bounds (min 0, max 2)