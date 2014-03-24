package stagium.plugin
package transform
package convert

import scala.tools.nsc.transform.InfoTransform

trait StagiumConvertInfoTransformer extends InfoTransform {
  self: StagiumConvertPhase =>

  import global._
  import definitions._
  import helper._

  override def transformInfo(sym: Symbol, tpe: Type): Type = {
//    // see comments to StagiumConvertTreeTransformer to see what needs to be transformed
//    def logTransform(tpe1: Type): Type = {
//      if (!(tpe =:= tpe1)) stagiumlog(s"$sym: $tpe -> $tpe1")
//      tpe1
//    }
//    if (sym.isMethod && !sym.isInjected) {
//      def explode(params: List[Symbol]): List[Symbol] = {
//        // TODO: we don't need to worry about throwing away param symbols, because stagium-based DMT's are prohibited in stagium-verify
//        // we need to ban p.type types though, but that should also be done in stagium-verify
//        def explode(p: Symbol) = p.info.stagiumFields.map(x => p.explodedSymbols.get(x.name) getOrElse {
//          val exploded = sym.newSyntheticValueParam(p.info.memberInfo(x).finalResultType, nme.paramExplode(p, x))
//          p.registerExploded(x, exploded)
//          stagiumlog(s"PARAMSYM: $p -> $exploded")
//          exploded
//        })
//        params.flatMap(p => if (p.isUnboxedStagiumRef) explode(p) else List(p))
//      }
//      def unboxret(tpe: Type): Type = {
//        if (tpe.isUnboxedStagiumRef && tpe.stagiumFields.length == 1) tpe.memberInfo(tpe.stagiumFields.head).finalResultType
//        else tpe.toBoxedStagiumRef
//      }
//      def loop(tpe: Type): Type = tpe match {
//        case MethodType(params, restpe @ MethodType(_, _)) => MethodType(explode(params), loop(restpe))
//        case MethodType(params, restpe)                    => MethodType(explode(params), unboxret(restpe))
//        case NullaryMethodType(restpe)                     => NullaryMethodType(loop(restpe))
//        case PolyType(tparams, restpe)                     => PolyType(tparams, loop(restpe))
//      }
//      logTransform(loop(tpe))
//    // after coerce, we want to remove all the magic from injectors
//    // in particular, we no longer want coercion functions to produce annotated types when typechecking
//    } else if (sym == box2unbox) {
//      val PolyType(tparams, MethodType(vparams, restpe)) = tpe
//      logTransform(PolyType(tparams, MethodType(vparams, restpe.removeAnnotation(UnboxedClass))))
//    } else if (sym.owner == unbox2box) {
//      logTransform(tpe.removeAnnotation(UnboxedClass))
//    } else if (sym == UnboxedClass) {
//      val ClassInfoType(parents, scope, sym) = tpe
//      val parents1 = parents.filter(_.typeSymbol != TypeConstraintClass)
//      ClassInfoType(parents1, scope, sym)
//    } else {
//      tpe
//    }
    tpe
  }
}
