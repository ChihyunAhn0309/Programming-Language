file:///C:/Users/ach18/PL/proj1/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### java.lang.IndexOutOfBoundsException: -1 is out of bounds (min 0, max 2)

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-TYzxCa9SSjGVNWtiteF0QQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-TYzxCa9SSjGVNWtiteF0QQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
uri: file:///C:/Users/ach18/PL/proj1/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
text:
```scala
package cs320

import Value._

object Implementation extends Template {

  def lookup(x:String, environ:Env): Value = {
    environ.get(x) match{
      case Some(value) => value
      case None => error()
    }
  }

  def numAdd(x:Va): (Value, Value) => (Value) = {
    (_,_)match{
      case (IntV(x), IntV(y)) => IntV(x + y)
      case _ => error()
    }
  }

  def numdmOp(op: (BigInt, BigInt) => Int): (Value, Value) => (Value) = {
    (_,_)match{
      case (IntV(x), IntV(y)) => if(y == 0) error() else IntV(op(x, y))
      case _ => error()
    }
  }

  def numboolOp(op: (BigInt, BigInt) => Boolean): (Value, Value) => (Value) = {
    (_,_)match{
      case (IntV(x), IntV(y)) => if (op(x, y) == true) BooleanV(true) else BooleanV(false)
      case _ => error()
    }
  }

  val numadd = numOp(_ + _)
  val nummul = numOp(_ * _)
  val numdiv = numdmOp(_ / _)
  val nummode = numdmOp(_ % _)
  val numsame = numboolOp(_ == _)
  val numbig = numboolOp(_ < _)
  // 아직 나눗셈 mod < == 못만듬

  def exprtovallist(l: List[Expr], e: Env): List[Value] = {
    def exprtoval(list: List[Expr], env: Env, resultlist: List[Value]): List[Value] = {
      l match{
        case head :: next => exprtoval(next, env, resultlist :: interps(head, env))
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
        case CloV(parameters, body, fenv) => {
          fenv = env
          changeenv(next, env)
        }
        case _ => error()
      }
      case Nil => env
    }
  }

  def paratoarg(param:List[String], a:List[Expr], e:Env): Env = {
    param match{
      case head :: next => a match{
        case h :: t => paratoarg(next, t, e + (head -> interps(h, e)))
        case Nil => error()
      }
      case Nil => a match{
        case head :: next => error()
        case Nil => e
      }
    }
  }

  def interps(e:Expr, env: Env): Value = e match{
    case Id(name) => lookup(name, env)
    case IntE(value) => IntV(value)
    case BooleanE(value) => BooleanV(value)
    case Add(left, right) => numadd(interps(left, env), interps(right, env))
    case Mul(left, right) => nummul(interps(left, env), interps(right, env))
    case Div(left, right) => numdiv(interps(left, env), interps(right, env))
    case Mod(left, right) => nummode(interps(left, env), interps(right, env))
    case Eq(left, right) => numsame(interps(left, env), interps(right, env))
    case Lt(left, right) => numbig(interps(left, env), interps(right, env))
    case If(condition, trueBranch, falseBranch) => {
      if (interps(condition, env) == BooleanV(true)) interps(trueBranch, env) else interps(falseBranch, env)
    }
    case TupleE(expressions) => TupleV(exprtovallist(expressions, env))
    case Proj(expression, index) => interps(expression, env) match{
      case TupleV(values) => listind(values, index, 1)
      case _ => error()
    }
    case NilE => NilV
    case ConsE(head, tail) => interps(tail, env) match{
      case TupleV(values) => ConsV(interps(head, env), TupleV(values))
      case NilV => interps(head, env)
      case _ => error()
    }
    case Empty(expression) => interps(expression, env) match{
      case TupleV(values) => BooleanV(false)
      case NilV => BooleanV(true)
      case _ => error()
    }
    case Head(expression) => interps(expression, env) match{
      case TupleV(values) => values match{
        case head :: next => head
        case Nil => error()
      }
      case _ => error()
    }
    case Tail(expression) => interps(expression, env) match{
      case TupleV(values) => taillist(values, IntV(0))
      case _ => error()
    }
    case Val(name, expression, body) => interps(body, env + (name -> interps(expression, env)))
    case Fun(parameters, body) => CloV(parameters, body, env)
    case RecFuns(functions, body) =>  interps(body, changeenv(functions, functoclo(functions, env)))
    case App(function, arguments) => interps(function, env) match{
      case CloV(parameters, body, fenv) => interps(body, paratoarg(parameters, arguments, fenv))
      case _ => error()
    }
    case Test(expression, typ) => interps(expression, env) match{
      case IntV(value) => if(typ == Int) BooleanV(true) else BooleanV(false)
      case BooleanV(value) => if(typ == Boolean) BooleanV(true) else BooleanV(false)
      case TupleV(values) => if(typ == List) BooleanV(true) else BooleanV(false)
      case NilV => if(typ == List) BooleanV(true) else BooleanV(false)
      case ConsV(head, tail) => if(typ == List) BooleanV(true) else BooleanV(false)
      case CloV(parameters, body, env) => if(typ == Function) BooleanV(true) else BooleanV(false)
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
	scala.tools.nsc.typechecker.Typers$Typer.member(Typers.scala:684)
	scala.tools.nsc.typechecker.Typers$Typer.typedSelect$1(Typers.scala:5442)
	scala.tools.nsc.typechecker.Typers$Typer.typedSelectOrSuperCall$1(Typers.scala:5604)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6206)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.makeAccessible(Typers.scala:6359)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdent$2(Typers.scala:5708)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdentOrWildcard$1(Typers.scala:5732)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6203)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedType(Typers.scala:6445)
	scala.tools.nsc.typechecker.Typers$Typer.typedHigherKindedType(Typers.scala:6452)
	scala.tools.nsc.typechecker.Typers$Typer.$anonfun$typed1$81(Typers.scala:5781)
	scala.tools.nsc.typechecker.Typers$Typer.typedAppliedTypeTree$1(Typers.scala:5770)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6155)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6261)
	scala.tools.nsc.typechecker.Typers$Typer.typedType(Typers.scala:6445)
	scala.tools.nsc.typechecker.Typers$Typer.typedType(Typers.scala:6448)
	scala.tools.nsc.typechecker.Namers$Namer.valDefSig(Namers.scala:1767)
	scala.tools.nsc.typechecker.Namers$Namer.memberSig(Namers.scala:1953)
	scala.tools.nsc.typechecker.Namers$Namer.typeSig(Namers.scala:1903)
	scala.tools.nsc.typechecker.Namers$Namer$MonoTypeCompleter.completeImpl(Namers.scala:854)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete(Namers.scala:2100)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete$(Namers.scala:2098)
	scala.tools.nsc.typechecker.Namers$TypeCompleterBase.complete(Namers.scala:2093)
	scala.reflect.internal.Symbols$Symbol.completeInfo(Symbols.scala:1566)
	scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1538)
	scala.tools.nsc.typechecker.Namers$DependentTypeChecker.$anonfun$check$2(Namers.scala:2178)
	scala.tools.nsc.typechecker.Namers$DependentTypeChecker.$anonfun$check$1(Namers.scala:2177)
	scala.tools.nsc.typechecker.Namers$DependentTypeChecker.check(Namers.scala:2176)
	scala.tools.nsc.typechecker.Namers$Namer.methodSig(Namers.scala:1456)
	scala.tools.nsc.typechecker.Namers$Namer.memberSig(Namers.scala:1952)
	scala.tools.nsc.typechecker.Namers$Namer.typeSig(Namers.scala:1903)
	scala.tools.nsc.typechecker.Namers$Namer$MonoTypeCompleter.completeImpl(Namers.scala:854)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete(Namers.scala:2100)
	scala.tools.nsc.typechecker.Namers$LockingTypeCompleter.complete$(Namers.scala:2098)
	scala.tools.nsc.typechecker.Namers$TypeCompleterBase.complete(Namers.scala:2093)
	scala.reflect.internal.Symbols$Symbol.completeInfo(Symbols.scala:1566)
	scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1538)
	scala.reflect.internal.Symbols$Symbol.initialize(Symbols.scala:1733)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5835)
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