file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
### java.lang.StringIndexOutOfBoundsException: offset 785, count -5, length 2089

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun-ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-lEU97PenQielXJTFv31FVg== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.0\semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>\chihyun-ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-lEU97PenQielXJTFv31FVg== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 787
uri: file:///C:/Users/ach18/PL/exercise6/chihyun-ahn/core/src/main/scala/cs320/Implementation.scala
text:
```scala
package cs320

import Value._
import java.security.KeyStore

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

  def malloc(sto: Store): Addr = {
    sto.foldLeft(0){
      case (max, (add, _)) => math.max(max@@)
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
    }
    case OpenBox(box) => 
    case SetBox(box, expr) => 
    case Get(record, field) => 
    case Rec(fields) => 
    case Seqn(left, right) => 
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
java.base/java.lang.String.checkBoundsOffCount(String.java:4586)
	java.base/java.lang.String.rangeCheck(String.java:304)
	java.base/java.lang.String.<init>(String.java:300)
	scala.tools.nsc.interactive.Global.typeCompletions$1(Global.scala:1244)
	scala.tools.nsc.interactive.Global.completionsAt(Global.scala:1282)
	scala.meta.internal.pc.SignatureHelpProvider.$anonfun$treeSymbol$1(SignatureHelpProvider.scala:390)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.pc.SignatureHelpProvider.treeSymbol(SignatureHelpProvider.scala:388)
	scala.meta.internal.pc.SignatureHelpProvider$MethodCall$.unapply(SignatureHelpProvider.scala:205)
	scala.meta.internal.pc.SignatureHelpProvider$MethodCallTraverser.visit(SignatureHelpProvider.scala:316)
	scala.meta.internal.pc.SignatureHelpProvider$MethodCallTraverser.traverse(SignatureHelpProvider.scala:310)
	scala.meta.internal.pc.SignatureHelpProvider$MethodCallTraverser.fromTree(SignatureHelpProvider.scala:279)
	scala.meta.internal.pc.SignatureHelpProvider.signatureHelp(SignatureHelpProvider.scala:27)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$signatureHelp$1(ScalaPresentationCompiler.scala:339)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: offset 785, count -5, length 2089