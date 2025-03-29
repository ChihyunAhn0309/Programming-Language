file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
### scala.reflect.internal.FatalError: 
  ThisType(method interparatoarg) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun_ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar;<WORKSPACE>\chihyun_ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(error)
       tree position: line 193 of file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

   190           }
   191           case Nil => error()
   192           }
   193         case _ => error()
   194       }
   195       )
   196     case Test(expression, typ) => interps(expression, env, ha, ev =>

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun_ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar [exists ], <WORKSPACE>\chihyun_ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 5867
uri: file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
text:
```scala
package cs320

import Value._

object Implementation extends Template {

  def lookup(environ:Env, x:String):Value = {
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

  def numMod(x:Value, y: Value): Value = {
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

  def exprtovallist(l: List[Expr], e: Env, h:Value, k:Cont): Value = {
    def exprtoval(list: List[Expr], env: Env, resultlist: List[Value], hand:Value, kt:Cont): Value = {
      list match{
        case head :: next => 
          interps(head, env, hand, hv => k(exprtoval(next, env, resultlist ::: List(hv), hand, kt)))
        case Nil => TupleV(resultlist)
      }
    }
    exprtoval(l, e, List(), h, k)
  }

  def listind(l: List[Value], rightindex: Int, i: Int): Value = {
    l match{
      case head :: next => if(rightindex == i) head else listind(next, rightindex, i + 1)
      case Nil => error()
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

  def interparatoarg(param:List[String], av:Value, body:Expr, fe:Env, hand:Value, k:Cont): Value = {
    av match{
      case TupleV(list) =>
        param match{
          case head :: next => list match{
             case h :: t => interparatoarg(next, TupleV(t), body, fe + (head -> h), hand, k)
            case Nil => error()
          }
           case Nil => list match{
            case head :: next => error()
            case Nil => interps(body, fe, hand, k)
           }
        }
      case _ => error()
    }
  }


  def interps(e:Expr, env:Env, ha:Value, k:Cont): Value = e match {
    case Id(name) => k(lookup(env, name))
    case IntE(value) => k(IntV(value))
    case BooleanE(value) => k(BooleanV(value))
    case Add(left, right) => 
      interps(left, env, ha,lv => 
        interps(right, env, ha, rv => 
          k(numAdd(lv,rv))))
    case Mul(left, right) => 
      interps(left, env, ha, lv => 
        interps(right,env,ha,rv => 
          k(numMul(lv,rv))))
    case Div(left, right) => 
      interps(left, env, ha, lv => 
        interps(right,env,ha,rv => 
          k(numDiv(lv,rv))))
    case Mod(left, right) => 
      interps(left, env, ha, lv => 
        interps(right,env,ha,rv => 
          k(numMod(lv,rv))))
    case Eq(left, right) => 
      interps(left, env, ha, lv => 
        interps(right,env,ha,rv => 
          k(numsame(lv,rv))))
    case Lt(left, right) => 
      interps(left, env, ha, lv => 
        interps(right,env,ha,rv => 
          k(numbig(lv,rv))))
    case If(condition, trueBranch, falseBranch) => 
      interps(condition, env, ha, cv =>
        cv match{
          case BooleanV(value) => 
            if(value == true) interps(trueBranch, env, ha, k)
            else interps(falseBranch, env, ha, k)
          case _ => error()
        })
    case TupleE(expressions) => k(exprtovallist(expressions,env,ha,k))
    case Proj(expression, index) => interps(expression, env, ha, ev =>
      ev match{
        case TupleV(values) => k(listind(values, index, 1))
        case _ => error()
      })
    case NilE => k(NilV)
    case ConsE(head, tail) => 
      interps(head, env, ha, hv => 
        interps(tail, env, ha, tv =>
          tv match{
            case ConsV(head, tail) => k(ConsV(hv, tv))
            case NilV => k(ConsV(hv, NilV))
            case _ => error()
          }))
    case Empty(expression) => interps(expression, env, ha, ev => 
      ev match{
        case ConsV(head, tail) => k(BooleanV(false))
        case NilV => k(BooleanV(true))
        case _ => error()
      })
    case Head(expression) => interps(expression, env, ha, ev => 
      ev match{
        case ConsV(head, tail) => k(head)
        case _ => error()
      })
    case Tail(expression) => interps(expression, env, ha, ev => 
      ev match{
        case ConsV(head, tail) => k(tail)
        case _ => error()
      })
    case Val(name, expression, body) => interps(expression, env, ha, ev =>
      interps(body, env + (name -> ev), ha, k))
    case Vcc(name, body) => interps(body, env + (name -> ContV(k)), ha, k)
    case Fun(parameters, body) => k(CloV(parameters, body, env))
    case RecFuns(functions, body) => interps(body, changeenv(functions,functoclo(functions, env)), ha, k)
    case App(function, arguments) => interps(function, env, ha, fv =>
        fv match{
        case CloV(parameters, body, fenv) => erro@@r
        case ContV(continuation) => arguments match{
          case head :: next => next match{
            case h :: t => error()
            case Nil => interps(head, env, ha, continuation)
          }
          case Nil => error()
          }
        case _ => error()
      }
      )
    case Test(expression, typ) => interps(expression, env, ha, ev =>
      ev match{
        case IntV(value) => if(typ == IntT) k(BooleanV(true)) else k(BooleanV(false))
        case BooleanV(value) => if(typ == BooleanT) k(BooleanV(true)) else k(BooleanV(false))
        case TupleV(values) => if(typ == TupleT) k(BooleanV(true)) else k(BooleanV(false))
        case NilV => if(typ == ListT) k(BooleanV(true)) else k(BooleanV(false))
        case ConsV(head, tail) => if(typ == ListT) k(BooleanV(true)) else k(BooleanV(false))
        case CloV(parameters, body, env) => if(typ == FunctionT) k(BooleanV(true)) else k(BooleanV(false))
        case ContV(continuation) => if(typ == FunctionT) k(BooleanV(true)) else k(BooleanV(false))
      })
    case Throw(expression) => interps(expression, env, ha, ev => 
      ha match{
        case NilV => error()
        case ConsV(h, t) => h match{
          case CloV(parameters, b, fenv) => t match{
            case ConsV(head, htail) => head match{
              case ContV(conti) => htail match{
                case ConsV(head, tail) => interps(b, fenv, htail, expval => 
                  expval match{
                    case CloV(param, body, f2env) => param match{
                      case paramhead :: paramtail => paramtail match{
                        case first :: next => error()
                        case Nil => interps(body, f2env+(paramhead -> ev), htail, conti)
                      }
                      case Nil => error()
                    }
                    case ContV(continuation) => conti(ev)
                    case _ => error()
                  })
                case _ => error()
              }
              case _ => error()
            }
            case _ => error()
          }
          case _ => error()
        }
        case _ => error()
      })
    case Try(expression, handler) => interps(expression, env, ConsV(CloV(List(), handler, env), ConsV(ContV(k), ha)), k)
  }

  def interp(expr: Expr): Value = {
    interps(expr, Map(), NilV, x => x)
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
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:75)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:60)
	scala.collection.immutable.List.collect(List.scala:268)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:60)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:306)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  ThisType(method interparatoarg) for sym which is not a class
     while compiling: file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.14
    compiler version: version 2.13.14
  reconstructed args: -deprecation -feature -Wconf:cat=deprecation:w -Wconf:cat=feature:w -Wconf:cat=deprecation:ws -Wconf:cat=feature:ws -Wconf:cat=optimizer:ws -Wunused:imports -Wunused:privates -Wunused:locals -Wunused:implicits -Wunused:nowarn -Xlint:unused -classpath <WORKSPACE>\chihyun_ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar;<WORKSPACE>\chihyun_ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ==;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(error)
       tree position: line 193 of file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
            tree tpe: <error>
              symbol: value <error> in class <error>
   symbol definition: val <error>: <error> (a TermSymbol)
      symbol package: <none>
       symbol owners: value <error> -> class <error>
           call site: <none> in <none>

== Source file context for tree position ==

   190           }
   191           case Nil => error()
   192           }
   193         case _ => error()
   194       }
   195       )
   196     case Test(expression, typ) => interps(expression, env, ha, ev =>