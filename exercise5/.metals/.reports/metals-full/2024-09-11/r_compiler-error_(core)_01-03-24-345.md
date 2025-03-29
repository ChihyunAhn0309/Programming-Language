file:///C:/Users/ach18/PL/exercise5/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/ach18/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/collection/immutable/List.scala

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-y8n_t2KeQ5yivAVu5KslCA== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 1109
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
        val keylist = expmap.keySet.toList
        keylist m@@
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