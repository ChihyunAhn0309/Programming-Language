file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/ach18/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/Option.scala

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-zmJ0VdSISuOfDJunQOWEIQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 2727
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
        case RecV(fields) => fields.get(field) m@@
        case _ => error()
      }
    }
    case Rec(fields) => 
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

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/ach18/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.14/scala-library-2.13.14-sources.jar!/scala/Option.scala