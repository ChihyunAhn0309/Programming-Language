### java.lang.IndexOutOfBoundsException: -1 is out of bounds (min 0, max 2)

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.14
Classpath:
<WORKSPACE>\chihyun_ahn\.bloop\core\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar [exists ], <WORKSPACE>\chihyun_ahn\.bloop\macros\bloop-bsp-clients-classes\classes-Metals-Lzb6Gl2wSja55k3vlmJ-kQ== [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.14\scala-library-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\modules\scala-parser-combinators_2.13\1.1.2\scala-parser-combinators_2.13-1.1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline\3.21.0\jline-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal-jna\3.21.0\jline-terminal-jna-3.21.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.14\scala-reflect-2.13.14.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\net\java\dev\jna\jna\5.9.0\jna-5.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jline\jline-terminal\3.21.0\jline-terminal-3.21.0.jar [exists ]
Options:
-feature -deprecation -Xlint:unused -Yrangepos -Xplugin-require:semanticdb


action parameters:
<NONE>


#### Error stacktrace:

```
scala.collection.mutable.ArrayBuffer.apply(ArrayBuffer.scala:102)
	scala.reflect.internal.Types$Type.findMemberInternal$1(Types.scala:1030)
	scala.reflect.internal.Types$Type.findMember(Types.scala:1035)
	scala.reflect.internal.Types$Type.memberBasedOnName(Types.scala:661)
	scala.reflect.internal.Types$Type.member(Types.scala:625)
	scala.tools.nsc.typechecker.Contexts$SymbolLookup.nextDefinition$1(Contexts.scala:1442)
	scala.tools.nsc.typechecker.Contexts$SymbolLookup.apply(Contexts.scala:1516)
	scala.tools.nsc.typechecker.Contexts$Context.lookupSymbol(Contexts.scala:1282)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdent$2(Typers.scala:5663)
	scala.tools.nsc.typechecker.Typers$Typer.typedIdentOrWildcard$1(Typers.scala:5732)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6203)
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
	scala.meta.internal.pc.CompletionItemResolver.fullDocstring(CompletionItemResolver.scala:66)
	scala.meta.internal.pc.CompletionItemResolver.$anonfun$handleSymbol$2(CompletionItemResolver.scala:39)
	scala.meta.internal.pc.ItemResolver.enrichDocs(ItemResolver.scala:75)
	scala.meta.internal.pc.ItemResolver.enrichDocs$(ItemResolver.scala:14)
	scala.meta.internal.pc.CompletionItemResolver.enrichDocs(CompletionItemResolver.scala:8)
	scala.meta.internal.pc.CompletionItemResolver.handleSymbol(CompletionItemResolver.scala:40)
	scala.meta.internal.pc.CompletionItemResolver.resolve(CompletionItemResolver.scala:24)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$completionItemResolve$1(ScalaPresentationCompiler.scala:327)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: -1 is out of bounds (min 0, max 2)