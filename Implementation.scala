package cs320

object Implementation extends Template {

  def typeCheck(e: Typed.Expr): Typed.Type = T.typeCheck(e)

  def interp(e: Untyped.Expr): Untyped.Value = U.interp(e)

  object T {
    import Typed._

    case class TypeEnv(
      vars : Map[String, (ArrowT, Boolean)] = Map(),
      tbinds : Map[String, TypeDef] = Map(),   //TypeDef가 아닌 다른거로 바꿔야 할수도도
      tyvars : Map[String, Type] = Map(),
    ){
      def addVar(x: String, t:(ArrowT, Boolean)): TypeEnv = 
        copy(vars = vars + (x -> t)) // arrow와 bool으로 만듬
        // true for "var", false for "val"
      def addTBind(x: String, cs: TypeDef): TypeEnv = 
        copy(tbinds = tbinds + (x -> cs))
      def addTyvar(x: String, v: Type): TypeEnv = 
        copy(tyvars = tyvars + (x -> v)) //리스트로 해도 되는데 리스트 어떻게 copy하는지 몰라 map으로 우선
    }

    def sttotyvar(stringl:List[String], retl:List[Type]):List[Type] = stringl match{
      case head :: next => sttotyvar(next, retl ::: List(VarT(head)))
      case Nil => retl
    }

    def tyEnvfromVariant(initappT:AppT, tyvarl:List[Type], variantl:List[Variant], tyEnv:TypeEnv):TypeEnv = variantl match{
      case head :: next => head match{
        case Variant(name, params) => params match{
          case h :: t => tyEnvfromVariant(initappT, tyvarl, next, tyEnv.addVar(name, (ArrowT(tyvarl, ArrowT(params, initappT)), false)))
          case Nil => tyEnvfromVariant(initappT, tyvarl, next, tyEnv.addVar(name, (ArrowT(tyvarl, initappT), false)))
        }
        case _ => error()
      }
      case Nil => tyEnv
    } 

    def tyEnvfromrec(recdl: List[RecDef], initEnv: TypeEnv):TypeEnv = recdl match{
      case head :: next => head match{
        case Lazy(name, typ, expr) => 
          tyEnvfromrec(next, initEnv.addVar(name, (ArrowT(List(),typ), false)))
        case RecFun(name, tparams, params, rtype, body) => 
          val paramtyvarl = sttotyvar(tparams, List())
          val tlfromsttyl = sttyltotl(params, List())
          tyEnvfromrec(next, initEnv.addVar(name, (ArrowT(paramtyvarl, ArrowT(tlfromsttyl, rtype)), false)))
        case TypeDef(name, tparams, variants) => 
          val ntyEnv = initEnv.addTBind(name, TypeDef(name, tparams, variants)) // mapping from𝑡 to type 𝑡[𝛼] 𝑤1, . . . ,𝑤𝑛 이게 맞는지 모르겠음
          val tparamtyvarl = sttotyvar(tparams, List())
          val appt = AppT(name, tparamtyvarl) // 이것도 AppT가 맞는지 모르겠다
          val rettyEnv = tyEnvfromVariant(appt, tparamtyvarl, variants, ntyEnv)
          tyEnvfromrec(next, rettyEnv)
      }
      case Nil => initEnv
    }

    def notynametyEnv(recdefl:List[RecDef], tyEnv:TypeEnv):Boolean = recdefl match{
      case head :: next => head match{
        case TypeDef(name, tparams, variants) => 
          if (tyEnv.tbinds.contains(name)) error()
          else notynametyEnv(next, tyEnv)
        case _ => notynametyEnv(next, tyEnv)
      }
      case Nil => true
    }

    def checktyExpr(tyl:List[Type], exprl:List[Expr], tyEnv:TypeEnv, retTy:Type):Type = tyl match{
      case head :: next => exprl match{
        case h :: t => 
          mustSame(head, typeChecks(h, tyEnv))
          checktyExpr(next, t, tyEnv, retTy)
        case Nil => error()
      }
      case Nil => exprl match{
        case head :: next => error()
        case Nil => retTy
      }
    }
  
    def sttyltotl(sttyl:List[(String, Type)], tl:List[Type]):List[Type] = sttyl match{
      case head :: next => head match{
        case (s:String, t:Type) => sttyltotl(next, tl ::: List(t))
        case _ => error()
      }
      case Nil => tl
    }

    def addsty(sttyl:List[(String, Type)], tyEnv:TypeEnv):TypeEnv = sttyl match{
      case head :: next => head match{
        case (s:String, t:Type) => 
          addsty(next, tyEnv.addVar(s, (ArrowT(List(), t), false)))
        case _ => error()
      }
      case Nil => tyEnv
    }

    def listvalid(tyli:List[Type], tyEnv:TypeEnv, resli:List[Type]):List[Type] = tyli match{
      case head :: next => listvalid(next, tyEnv, resli ::: List(validType(head, tyEnv)))
      case Nil => resli
    }

    def substilist(tyli:List[Type], tyEnv:TypeEnv, resli:List[Type]):List[Type] = tyli match{
      case head :: next => substilist(next, tyEnv, resli ::: List(substiType(head, tyEnv)))
      case Nil => resli
    }
    
    def validType(ty: Type, tyEnv:TypeEnv): Type = ty match{
      case IntT => ty  // trivial
      case UnitT => ty  // trivial
      case BooleanT => ty  // trivial
      case ArrowT(ptypes, rtype) => 
        val afvalpty = listvalid(ptypes, tyEnv, List()) 
        val afvalrty = validType(rtype, tyEnv) 
        ArrowT(afvalpty, afvalrty) // 잘 만듬
      case VarT(name) => 
        if (tyEnv.tyvars.contains(name)) ty
        else error()
      case AppT(name, targs) => 
        val rettargs = listvalid(targs, tyEnv, List())
        val tydef = tyEnv.tbinds.getOrElse(name, error())
        val tarl = rettargs.length
        tydef match{
          case TypeDef(a, alpha, vari) => 
            if(tarl == alpha.length) ty // AppT 사용하는 곳이 있나 확인, 잘 만들어진지 확인 -> 잘 만들어진듯
            else error()
          case _ => error()
        } //notype 일지도
    }

    def substiType(ty: Type, tyEnv:TypeEnv): Type = ty match{
      case IntT => ty  // trivial
      case UnitT => ty  // trivial
      case BooleanT => ty  // trivial
      case ArrowT(ptypes, rtype) => 
        val afvalpty = substilist(ptypes, tyEnv, List()) 
        val afvalrty = substiType(rtype, tyEnv) 
        ArrowT(afvalpty, afvalrty) // 잘 만듬
      case VarT(name) => 
        if (tyEnv.tyvars.contains(name)) tyEnv.tyvars.getOrElse(name, error())
        else error()
      case AppT(name, targs) => 
        val rettargs = substilist(targs, tyEnv, List())
        val tydef = tyEnv.tbinds.getOrElse(name, error())
        val tarl = rettargs.length
        tydef match{
          case TypeDef(a, alpha, vari) => 
            if(tarl == alpha.length) AppT(name, rettargs) // AppT 사용하는 곳이 있나 확인, 잘 만들어진지 확인 -> 잘 만들어진듯
            else error()
          case _ => error()
        } //notype 일지도
    }


    def listypesame(l1: List[Type], l2: List[Type]): Boolean = l1 match{
      case head :: next => l2 match{
        case h :: t => 
          if (isSame(head, h)) listypesame(next, t)
          else false
        case Nil => false
      }
      case Nil => l2 match{
        case head :: next => false
        case Nil => true
      }
    }

    def isSame(t1:Type, t2:Type): Boolean = {
      (t1, t2) match{
        case (IntT, IntT) => true
        case (BooleanT, BooleanT) => true
        case (UnitT, UnitT) => true
        case (VarT(a), VarT(b)) => 
          if(a == b) true
          else false
        case (ArrowT(p1, r1), ArrowT(p2, r2)) => 
          if(listypesame(p1, p2)) isSame(r1, r2)
          else false
        case (AppT(s1, l1), AppT(s2, l2)) => 
          if (s1 == s2) listypesame(l1, l2)
          else false
        case _ => false
      }
    }

    def mustSame(t1: Type, t2: Type): Type = {
      if(isSame(t1, t2)) t1
      else error()
    }

    def checkandAdd(sl:List[String], tyEnv:TypeEnv, slcopy:List[String]):TypeEnv = {
      sl match{
        case head :: next => 
          if(tyEnv.tyvars.contains(head)) error()
          else checkandAdd(next, tyEnv, slcopy)
        case Nil => subcheckandAdd(slcopy, tyEnv)
      }
    }
    
    def subcheckandAdd(sl:List[String], tyEnv:TypeEnv):TypeEnv ={
      sl match{
        case head :: next => subcheckandAdd(next, tyEnv.addTyvar(head, VarT(head)))
        case Nil => tyEnv
      }
    }

    def validstpair(sttyl:List[(String, Type)], tyEnv:TypeEnv):TypeEnv = sttyl match{
      case head :: next => head match{
        case (s, t) => {
          validType(t, tyEnv)
          validstpair(next, tyEnv)
        }
        case _ => error()
      }
      case Nil => tyEnv
    }

    def stpairtyl(sttyl:List[(String, Type)], retEnv:TypeEnv):TypeEnv = sttyl match{
      case head :: next => head match{
        case (s, t) => stpairtyl(next, retEnv.addVar(s, (ArrowT(List(), t), false)))
        case _ => error()
      }
      case Nil => retEnv
    }

    def checkwVariant(wl: List[Variant], tyEnv:TypeEnv, retli: List[Variant]):List[Variant] = wl match{
      case head :: next => head match{
        case Variant(name, params) => 
          listvalid(params, tyEnv, List())
          checkwVariant(next, tyEnv, retli ::: List(head))
        case _ => error()
      }
      case Nil => retli
    }

    def validRecDef(recdef: RecDef, tyEnv: TypeEnv):RecDef = recdef match{
      case Lazy(name, typ, expr) => 
        if(isSame(validType(typ, tyEnv), typeChecks(expr, tyEnv))) recdef
        else error()
      case RecFun(name, tparams, params, rtype, body) => 
        val ntyEnv = checkandAdd(tparams, tyEnv, tparams)
        validstpair(params, ntyEnv)
        val realrtype = validType(rtype, ntyEnv)
        val n2tyEnv = stpairtyl(params, ntyEnv)
        if (isSame(typeChecks(body, n2tyEnv), realrtype)) recdef//만약 typeChecks(body, n2tyEnv)여기에서 valid체크가 안되면?
        else error()
      case TypeDef(name, tparams, variants) => 
        val ntyEnv = checkandAdd(tparams, tyEnv, tparams)
        checkwVariant(variants, ntyEnv, List())
        recdef
    }

    def validRecDeflist(recdefl:List[RecDef], tyEnv:TypeEnv):List[RecDef] = recdefl match{
      case head :: next => 
        validRecDef(head, tyEnv) // head가 tyEnv 밑에서 valid 한지 판단하기
        validRecDeflist(next, tyEnv)
      case Nil => recdefl
    }

    def otomapping(typa: List[Type], realty: List[Type], tyEnv:TypeEnv):TypeEnv = typa match{
      case head :: next => realty match{
        case h :: t => head match{
          case VarT(name) => otomapping(next, t, tyEnv.addTyvar(name, h))
          case _ => error()
        } 
        case Nil => error()
      }
      case Nil => realty match{
        case head :: next => error()
        case Nil => tyEnv
      }
    }


    def typeChecks(expr: Expr, tyEnv:TypeEnv): Type = expr match{ //typeChecks를 하면 그해당type에 대해 바로 validType은 안해도 되는 것인가?
      case Id(name, targs) =>
        listvalid(targs, tyEnv, List())
        val t = tyEnv.vars.getOrElse(name, error())
        t match{
          case (ArrowT(args, ty), b) => 
            if(args.length == targs.length) 
              if(targs.length == 0) ty
              else substiType(ty, otomapping(args, targs, tyEnv))
            else error()
          case _ => error()
        }
      case IntE(value) => IntT
      case BooleanE(value) => BooleanT
      case UnitE => UnitT
      case Add(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        IntT
      case Mul(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        IntT
      case Div(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        IntT
      case Mod(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        IntT
      case Eq(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        BooleanT
      case Lt(left, right) => 
        mustSame(typeChecks(left, tyEnv), IntT)
        mustSame(typeChecks(right, tyEnv), IntT)
        BooleanT
      case Sequence(left, right) => 
        validType(typeChecks(left, tyEnv), tyEnv)
        typeChecks(right, tyEnv)
      case If(cond, texpr, fexpr) => 
        mustSame(typeChecks(cond, tyEnv), BooleanT)
        val truety = typeChecks(texpr, tyEnv)
        val falsety = typeChecks(fexpr, tyEnv)
        mustSame(truety, falsety)
      case Val(mut, name, typ, expr, body) => typ match{
        case Some(value) => 
          val realvalue = validType(value, tyEnv)
          val exprty = typeChecks(expr, tyEnv)
          mustSame(exprty, realvalue)
          typeChecks(body, tyEnv.addVar(name, (ArrowT(List(), exprty), mut)))
        case None => 
          val exprty = typeChecks(expr, tyEnv)
          typeChecks(body, tyEnv.addVar(name, (ArrowT(List(), exprty), mut)))
      }
      case RecBinds(defs, body) =>
        if (notynametyEnv(defs, tyEnv)){
          val ntyEnv = tyEnvfromrec(defs, tyEnv) // Tn 만들기 잘 만듬
          validRecDeflist(defs, ntyEnv) // 모든 di들 ntyEnv아래에서  valid해야한다.
          val retty = typeChecks(body, ntyEnv)
          validType(retty, tyEnv)
        } // 잘 만듬
        else error()
      case Fun(params, body) => 
        val ptypel = sttyltotl(params, List())
        val v_ty_l = listvalid(ptypel, tyEnv, List())
        val ntyEnv = addsty(params, tyEnv)
        ArrowT(v_ty_l, typeChecks(body, ntyEnv)) // 잘 만듬
      case Assign(name, expr) => 
        tyEnv.vars.getOrElse(name, error()) match{
          case (ArrowT(argl, ty), muu) => 
            if((argl.length == 0) && (muu == true)) {
              mustSame(ty, typeChecks(expr, tyEnv))
              UnitT
            }
            else error()
          case _ => error()
        }
      case App(fun, args) => typeChecks(fun, tyEnv) match{
        case ArrowT(ptypes, rtype) => 
          if (args.length == ptypes.length){
            checktyExpr(ptypes, args, tyEnv, rtype)
          }
          else error()
        case _ => error()
      }
      case Match(expr, cases) => typeChecks(expr, tyEnv) match{
        case AppT(name, targs) => tyEnv.tbinds.getOrElse(name, error()) match{
          case TypeDef(name:String, alphal:List[String], variantl:List[Variant]) =>
            if((targs.length == alphal.length) && (cases.length == variantl.length)){
              findcstype(cases, targs, alphal, variantl, tyEnv, UnitT)
            }
            else error()
          }
        case _ => error()
      }
      case _ => error()
    }
//  여기 다시한번 생각하기 substitution아예 안됨

    def stmaptype(strl:List[String], tyl:List[Type], tyEnv:TypeEnv): TypeEnv = strl match{
      case head :: next => tyl match{
        case h :: t => stmaptype(next, t, tyEnv.addVar(head, (ArrowT(List(), h), false)))
        case Nil => error() 
      }
      case Nil => tyl match{
        case head :: next => error()
        case Nil => tyEnv
      }
    }// 잘 만듬

    def stomapping(stringl: List[String], realty: List[Type], tyEnv:TypeEnv):TypeEnv = stringl match{
      case head :: next => realty match{
        case h :: t => stomapping(next, t, tyEnv.addTyvar(head, h))
        case Nil => error()
      }
      case Nil => realty match{
        case head :: next => error()
        case Nil => tyEnv
      }
    }

    def stmatchvariant(thecase:Case, variantl:List[Variant], alphal:List[String], typel:List[Type], tyEnv:TypeEnv):Type = thecase match{
      case Case(variant, names, body) => variantl match{
        case head :: next => head match{
          case Variant(name, params) => 
            if((variant == name) && (names.length == params.length)){
              val ntyEnv = stomapping(alphal, typel, tyEnv)
              val realparaml = substilist(params, ntyEnv, List())
              val realtyEnv = stmaptype(names, realparaml, tyEnv)
              typeChecks(body, realtyEnv)
            }
            else stmatchvariant(thecase, next, alphal, typel, tyEnv)
          case _ => error()
        }
      case Nil => error()
    }
      case _ => error()
    }// tyEnv가 갱신되어서 넘어가면 안된다.  잘 만듬

    def findcstype(csl:List[Case], tyl:List[Type], apl:List[String], variantl:List[Variant], tyEnv:TypeEnv, firstTy:Type):Type = csl match{
      case head :: next => everycstype(next, tyl, apl, variantl, tyEnv, stmatchvariant(head, variantl, apl, tyl, tyEnv))
      case Nil => error()
    } // 잘 만듬

    def everycstype(csl:List[Case], tyl:List[Type], apl:List[String], variantl:List[Variant], tyEnv:TypeEnv, firstTy:Type):Type = csl match{
      case head :: next => 
        mustSame(stmatchvariant(head, variantl, apl, tyl, tyEnv), firstTy)
        everycstype(next, tyl, apl, variantl, tyEnv, firstTy)
      case Nil => firstTy
    }

    def typeCheck(expr: Expr): Type ={
      val tyEnv = new TypeEnv()
      typeChecks(expr, tyEnv)
    }
  }

  object U {
    import Untyped._

    type Sto = Map[Addr, Value]

  def lookup(environ:Env, x:String):Addr = {
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

    def malloc(sto:Sto): Addr = 
      maxAddress(sto) + 1

    def maxAddress(sto:Sto): Addr = 
      sto.keySet.+(0).max

    def invertmap(befi:Map[String, Addr]):Map[Addr, String] = befi.map{
      case (string, addr) => (addr, string)
    }

    def mkEnvforVariant(variantl:List[Variant], prevenv:Env, sto:Sto):Env = variantl match{
      case head :: next => head match{
        case Variant(name, empty) =>
          if (empty == true) {
            val newadd = (sto.keySet ++ invertmap(prevenv).keySet).+(0).max + 1
            val nenv = prevenv + (name -> newadd)
            mkEnvforVariant(next, nenv, sto)
          }
          else{
            val newadd = (sto.keySet ++ invertmap(prevenv).keySet).+(0).max + 1
            val nenv = prevenv + (name -> newadd)
            mkEnvforVariant(next, nenv, sto)
          }
        case _ => error()
      }
      case Nil => prevenv //type W1, W2, .. 상황에서 W가 아무것도 없는 상황은 없다라고 하고 그냥 함 근데 나중에 error해야할수도...
    }

    def mkStoforVariant(variantl:List[Variant], env:Env, prevsto:Sto):Sto = variantl match{
      case head :: next => head match{
        case Variant(name, empty) =>
          if (empty == true) {
            mkStoforVariant(next, env, prevsto + (env.getOrElse(name, error()) -> VariantV(name, List())))
          }
          else{
            mkStoforVariant(next, env, prevsto + (env.getOrElse(name, error()) -> ConstructorV(name)))
          }
        case _ => error()
      }
      case Nil => prevsto //type W1, W2, .. 상황에서 W가 아무것도 없는 상황은 없다라고 하고 그냥 함 근데 나중에 error해야할수도...
    }

    def makeEnvRec(recdefl:List[RecDef], prevenv:Env, sto:Sto):Env = recdefl match{
      case head :: next => head match{
        case Lazy(name, expr) => 
          val newadd = (sto.keySet ++ invertmap(prevenv).keySet).+(0).max + 1
          val nenv = prevenv + (name -> newadd)
          makeEnvRec(next, nenv, sto)
        case RecFun(name, params, body) => 
          val newadd = (sto.keySet ++ invertmap(prevenv).keySet).+(0).max + 1
          val nenv = prevenv + (name -> newadd)
          makeEnvRec(next, nenv, sto)
        case TypeDef(variants) => 
          val nenv = mkEnvforVariant(variants, prevenv, sto)
          makeEnvRec(next, nenv, sto)
      }
      case Nil => prevenv
    }

    def makeStoRec(recdefl:List[RecDef], env:Env, prevsto:Sto):Sto = recdefl match{
      case head :: next => head match{
        case Lazy(name, expr) => makeStoRec(next, env, prevsto + (env.getOrElse(name, error()) -> ExprV(expr, env)))
        case RecFun(name, params, body) => makeStoRec(next, env, prevsto + (env.getOrElse(name, error()) -> CloV(params, body, env)))
        case TypeDef(variants) => 
          val nsto = mkStoforVariant(variants, env, prevsto)
          makeStoRec(next, env, nsto)
      }
      case Nil => prevsto
    }


    def interps(expr:Expr, env:Env, sto:Sto): (Value, Sto) = expr match{
      case Id(name) => 
        val add = env.getOrElse(name, error())
        val addval = sto.getOrElse(add, error())
        addval match{
          case ExprV(exp, environ) =>
            val (retval, currs) =  interps(exp, environ, sto)
            (retval, currs + (add -> retval))
          case _ => (addval, sto)
        }
      case IntE(value) => (IntV(value), sto)
      case BooleanE(value) => (BooleanV(value), sto)
      case UnitE => (UnitV, sto)
      case Add(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numAdd(v1, v2), s2)
      case Mul(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numMul(v1, v2), s2)
      case Div(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numDiv(v1, v2), s2)
      case Mod(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numMod(v1, v2), s2)
      case Eq(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numsame(v1, v2), s2)
      case Lt(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        val (v2, s2) = interps(right, env, s1)
        (numbig(v1, v2), s2)
      case Sequence(left, right) => 
        val (v1, s1) = interps(left, env, sto)
        interps(right, env, s1)
      case If(cond, texpr, fexpr) => interps(cond, env, sto) match{
        case (v, s) => v match{
          case BooleanV(value) => 
            if (value == true){
              interps(texpr, env, s)
            }
            else if (value == false){
              interps(fexpr, env, s)
            }
            else error()
          case _ => error()
        }
      }
      case Val(name, expr, body) => 
        val (v1, s1) = interps(expr, env, sto)
        val add = malloc(s1)
        interps(body, env + (name -> add), s1 + (add -> v1))
      case RecBinds(defs, body) => 
        val nenv = makeEnvRec(defs, Map(), sto)
        val finalenv = (env ++ nenv)
        val nsto = makeStoRec(defs, finalenv, sto)
        interps(body, finalenv, nsto)
      case Fun(params, body) => (CloV(params, body, env), sto)
      case Assign(name, expr) => 
        val addr = env.getOrElse(name, error())
        val (value, nwsto) = interps(expr, env, sto)
        (UnitV, nwsto + (addr -> value))
      case App(fun, args) => 
        val (funval, s1) = interps(fun, env, sto)
        val (argvallist, s2) = exprtovalue(args, env, s1, List())
        funval match{
          case CloV(params, body, fenv) => 
            if (argvallist.length == params.length){
              val (finalenv, finalsto) = makeenvstore(params, argvallist, fenv, s2)
              interps(body, finalenv, finalsto)
            }
            else{
              error()
            }
          case ConstructorV(name) => (VariantV(name, argvallist), s2)
          case _ => error()
        }
      case Match(expr, cases) => 
        val (evalue, s1) = interps(expr, env, sto)
        evalue match{
          case VariantV(name, values) => casematchname(cases, name, values) match{
            case Case(variant, names, body) => 
              val (finalenv, finalsto) = makeenvstore(names, values, env, s1)
              interps(body, finalenv, finalsto)
            case _ => error()
          }
          case _ => error()
        }
      case _ => error()
    }

    def casematchname(cases:List[Case], thename:String, valuel:List[Value]):Case = cases match{
      case head :: next => head match{
        case Case(variant, names, body) => 
          if ((thename == variant) && (names.length == valuel.length)) Case(variant, names, body)
          else casematchname(next, thename, valuel)
        case _ => error()
      }
      case Nil => error()
    }

    def makeenvstore(params:List[String], values:List[Value], env:Env, sto:Sto):(Env, Sto) = params match{
      case head :: next => values match{
        case h :: t => 
          val addr = malloc(sto)
          makeenvstore(next, t, env+(head -> addr), sto+(addr -> h))
        case Nil => error()
      }
      case Nil => values match{
        case head :: next => error()
        case Nil => (env, sto)
      }
    }

    def exprtovalue(expr:List[Expr], env:Env, sto:Sto, retval:List[Value]):(List[Value], Sto) = expr match{
      case head :: next => 
        val (v1, s1) = interps(head, env, sto)
        exprtovalue(next, env, s1, retval ::: List(v1))
      case Nil => (retval, sto) // 무조건 function의 argument가 없는 것도 있을것이라고 생각해서 함. 만약 무조건 1개는 있어야 한다면 조정하기 
    }

    def interp(expr: Expr): Value = 
      interps(expr, Map(), Map()) match{
        case (finalval:Value, finalsto:Sto) => finalval
        case _ => error()
      }
  }
}