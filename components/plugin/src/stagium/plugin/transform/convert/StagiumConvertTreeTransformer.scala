package stagium.plugin
package transform
package convert

trait StagiumConvertTreeTransformer {
  this: StagiumConvertPhase =>

  import global._
  import definitions._
  import treeInfo.{AsInstanceOf => _, _}
  import helper._
  import Flag._

  class TreeConverter(unit: CompilationUnit) extends TreeRewriter(unit) { self =>
    override def rewrite(tree: Tree)(implicit state: State) = {
//      case ValDef(_, _, VMu(fields), am @ AM(_, _)) =>
//        commit("A01", fields.map(x => explode(tree.symbol, x, unbox2box(am, x))))
//      case ValDef(_, _, VMu(fields), Box2unbox(Apply(Select(New(V(_)), nme.CONSTRUCTOR), args))) =>
//        commit("A02", fields.zip(args).map{ case (x, e) => explode(tree.symbol, x, e) })
//      case ValDef(_, _, VMu(fields), Box2unbox(em @ EM(_, _))) =>
//        val precomputed = temp(nme.valuePrecompute(tree.symbol), em)
//        val exploded = fields.map(x => explode(tree.symbol, x, Selectx(gen.mkAttributedRef(precomputed.symbol), x)))
//        commit("A03", precomputed +: exploded)
//      case ValDef(_, _, VMu(fields), bm @ BM(_, _)) =>
//        error(s"unauthorized bm detected: $tree")
//      case ValDef(_, _, VSu(x :: Nil), cs @ CS(_, _)) =>
//        commit("A04", explode(tree.symbol, x, unbox2box(cs, x)))
//      case ValDef(_, _, tpt @ Vu(fields), EmptyTree) =>
//        tree.symbol.owner.info.decls.unlink(tree.symbol)
//        commit("A05", fields.map(x => explode(tree.symbol, x, tpt.tpe.memberInfo(x).finalResultType, EmptyTree)))
//      case DefDef(mods, name, tparams, vparamss @ Vuss(), tpt, e) =>
//        // note that we explode default parameters into non-default parameters
//        // we do that because handling that properly would require to also explode default getters, which would be a huge pain
//        // luckily this is completely unnecessary, because typer's desugaring of default arguments works with us out of the box
//        def explode(p: Symbol) = p.stagiumFields.map(x => newValDef(self.explode(p, x), EmptyTree)() setType NoType)
//        val vparamss1 = vparamss.map(_.flatMap { case vdef @ ValDef(_, _, Vu(_), _) => explode(vdef.symbol); case vparam => List(vparam) })
//        commit("A06", treeCopy.DefDef(tree, mods, name, tparams, vparamss1, tpt, e) setType NoType)
//      case LabelDef(name, params @ Vus(), rhs) =>
//        val params1 = params.zip(tree.symbol.paramss.flatten).map {
//          case (ptree @ Vu(_), psym) => Ident(psym) setType psym.info
//          case (ptree, psym) => ptree
//        }
//        commit("A06", treeCopy.LabelDef(tree, name, params1, rhs))
//      case DefDef(mods, name, tparams, vparamss, tpt @ VSu(_), c) =>
//        commit("A07", treeCopy.DefDef(tree, mods, name, tparams, vparamss, tpt.toStagiumField, c) setType NoType)
//      case DefDef(mods, name, tparams, vparamss, tpt @ VMu(_), c) =>
//        error(s"unauthorized bm detected: $tree")
//      case Selectf(Unbox2box(Box2unbox(e)), f) =>
//        commit("B01", Select(e, f))
//      case Unbox2box(Box2unbox(e)) =>
//        commit("B02", e)
//      case Selectx(Unbox2box(A(e, a)), x) =>
//        commit("B03", Eax(e, a, x))
//      case Unbox2box(A(e, a)) =>
//        val args = tree.tpe.stagiumFields.map(x => Eax(e, a, x))
//        commit("B04", Apply(Select(New(TypeTree(tree.tpe)), nme.CONSTRUCTOR), args))
//      case Selectx(Unbox2box(cs @ CS(_, _)), x) =>
//        commit("B05", cs setType cs.tpe.toStagiumField)
//      case Unbox2box(cs @ CS(_, _)) =>
//        commit("B06", Apply(Select(New(TypeTree(tree.tpe)), nme.CONSTRUCTOR), List(unbox2box(cs, cs.stagiumField))))
//      case Unbox2box(bm @ BM(_, _)) =>
//        error(s"unauthorized bm detected: $tree")
//      case Box2unbox(es @ ES(_, _)) =>
//        commit("B07", Select(es, es.tpe.stagiumField))
//      case Box2unbox(em @ EM(_, _)) =>
//        commit("B08", em)
//      case A(e, a) =>
//        commit("B09", Eax(e, a, a.stagiumField))
//      case bs @ BS(_, _) =>
//        commit("B10", bs setType bs.tpe.toStagiumField)
//      case bm @ BM(_, _) =>
//        error(s"unauthorized bm detected: $tree")
//      case U(core, args) =>
//        // TODO: implement prefix precomputation
//        var precomputeds = List[ValDef]()
//        val vals = flatMap2(args, core.tpe.params)((arg, p) => {
//          if (p.isUnboxedStagiumRef) {
//            val precomputed = if (isB(arg) && p.stagiumFields.length > 1) List(temp(nme.argPrecompute(p), arg)) else Nil
//            precomputeds ++= precomputed
//            val arg1 = if (precomputed.nonEmpty) atPos(arg.pos)(gen.mkAttributedIdent(precomputed.head.symbol)) else arg
//            val exploded = p.stagiumFields.map(x => temp(nme.argExplode(p, x), unbox2box(arg1, x)))
//            precomputed ++ exploded
//          } else {
//            List(temp(nme.EMPTY, arg))
//          }
//        })
//        def apply1(args1: List[Tree]) = {
//          val core1 = core match {
//            case tapp @ TypeApply(core, targs) => treeCopy.TypeApply(tapp, core.clearType(), targs).clearType()
//            case _ => core.clearType()
//          }
//          treeCopy.Apply(tree, core1, args1).clearType()
//        }
//        if (precomputeds.nonEmpty) {
//          val args1 = vals.diff(precomputeds).map(vdef => Ident(vdef.symbol))
//          commit("B12", vals :+ apply1(args1))
//        } else {
//          val args1 = vals.map(_.rhs).map{
//            case rhs @ Select(qual @ V(_), _) => rhs setType qual.tpe.memberInfo(rhs.symbol).finalResultType
//            case rhs => rhs
//          }
//          commit("B11", apply1(args1))
//        }
//      case Assign(A(e1, a1), a2 @ A(_, _)) =>
//        commit("B13", a2.stagiumFields.map(x => Assign(Eax(e1, a1, x), unbox2box(a2, x))))
//      case Assign(lhs @ A(e1, a1), b2 @ B(_, _)) =>
//        val precomputed = temp(nme.assignPrecompute(), b2)
//        commit("B14", List(precomputed, lhs, Ident(precomputed.symbol)))
//      case Assign(A(e1, b1), c2 @ C(_, _)) =>
//        val precomputed = temp(nme.assignPrecompute(), e1)
//        commit("B15", List(precomputed, Assign(Select(Ident(precomputed.symbol), b1), c2)))
//      case Return(cs @ CS(_, _)) =>
//        commit("B16", Select(unbox2box(cs), cs.stagiumField))
//      case Selectx(Apply(Select(New(V(fields)), nme.CONSTRUCTOR), args), x) =>
//        commit("B17", args(fields.indexOf(x)))
//      case AsInstanceOf(Literal(Constant(null)), tpt @ V(fields)) =>
//        val fields1 = fields.map(x => gen.mkAsInstanceOf(Literal(Constant(null)), tpt.tpe.memberInfo(x).finalResultType))
//        commit("B18", Apply(Select(New(tpt), nme.CONSTRUCTOR), fields1))
      case _ =>
        tree
    }
  }
}