file:///C:/Users/ach18/PL/proj2/chihyun_ahn/core/src/main/scala/cs320/Implementation.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/ach18/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/List.scala

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun_ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar [exists ], <WORKSPACE>\chihyun_ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 4428
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
          interps(head, env, hand, headval => k(exprtoval(next, env, resultlist ::: List(headval), hand, kt) match{
            case TupleV(values) => TupleV(IntV(1) :: headval :: values)
            case _ => error()
          }))
        case Nil => TupleV(List())
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
    case TupleE(expressions) => expressions m@@
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
        case CloV(parameters, body, fenv) => error()
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
java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
	java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
	java.base/java.nio.file.Path.of(Path.java:147)
	java.base/java.nio.file.Paths.get(Paths.java:69)
	scala.meta.io.AbsolutePath$.apply(AbsolutePath.scala:58)
	scala.meta.internal.metals.MetalsSymbolSearch.$anonfun$definitionSourceToplevels$2(MetalsSymbolSearch.scala:70)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.metals.MetalsSymbolSearch.definitionSourceToplevels(MetalsSymbolSearch.scala:69)
	scala.meta.internal.pc.completions.MatchCaseCompletions.scala$meta$internal$pc$completions$MatchCaseCompletions$$sortSubclasses(MatchCaseCompletions.scala:368)
	scala.meta.internal.pc.completions.MatchCaseCompletions$MatchKeywordCompletion.contribute(MatchCaseCompletions.scala:305)
	scala.meta.internal.pc.CompletionProvider.filterInteresting(CompletionProvider.scala:405)
	scala.meta.internal.pc.CompletionProvider.safeCompletionsAt(CompletionProvider.scala:569)
	scala.meta.internal.pc.CompletionProvider.completions(CompletionProvider.scala:59)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$complete$1(ScalaPresentationCompiler.scala:214)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/ach18/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/List.scala