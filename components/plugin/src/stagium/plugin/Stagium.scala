package stagium.plugin

import scala.tools.nsc.Global
import scala.tools.nsc.plugins.Plugin
import scala.tools.nsc.plugins.PluginComponent

import transform._
import metadata._

/** Main miniboxing class */
class Stagium(val global: Global) extends Plugin { plugin =>
  // import global._

  val name = "stagium"
  val description = "provides value class functionality"

  val components = List[PluginComponent](
    StagiumPreparePhaseObj,
    StagiumCoercePhaseObj,
    StagiumCommitPhaseObj
  )

  // LDL adaptation
  global.addAnnotationChecker(StagiumCoercePhaseObj.ValueAnnotationChecker)

  lazy val helper = new { val global: plugin.global.type = plugin.global } with StagiumHelper

  override def processOptions(options: List[String], error: String => Unit) {
    for (option <- options) {
      if (option == "passive")
        helper.flag_passive = true
      else
        error("Stagium: option not understood: " + option)
    }
  }

  private object StagiumPreparePhaseObj extends StagiumPreparePhase { self =>
    val global: Stagium.this.global.type = Stagium.this.global
    val runsAfter = List("typer")
    override val runsRightAfter = Some("typer")
    val phaseName = Stagium.this.name + "-prepare"

    import global._
    val helper: plugin.helper.type = plugin.helper

    var stagiumPreparePhase : StdPhase = _
    override def newPhase(prev: scala.tools.nsc.Phase): StdPhase = {
      stagiumPreparePhase = new Phase(prev)
      stagiumPreparePhase
    }
  }

  private object StagiumCoercePhaseObj extends StagiumCoercePhase { self =>
    val global: Stagium.this.global.type = Stagium.this.global
    val runsAfter = List()
    override val runsRightAfter = Some(StagiumPreparePhaseObj.phaseName)
    val phaseName = Stagium.this.name + "-coerce"

    import global._
    val helper: plugin.helper.type = plugin.helper

    var stagiumCoercePhase : StdPhase = _
    def newPhase(prev: scala.tools.nsc.Phase): StdPhase = {
      stagiumCoercePhase = new CoercePhase(prev)
      stagiumCoercePhase
    }
  }

  private object StagiumCommitPhaseObj extends StagiumCommitPhase { self =>
    val global: Stagium.this.global.type = Stagium.this.global
    val runsAfter = List()
    override val runsRightAfter = Some(StagiumCoercePhaseObj.phaseName)
    val phaseName = Stagium.this.name + "-commit"

    import global._
    val helper: plugin.helper.type = plugin.helper

    var stagiumCommitPhase : StdPhase = _
    override def newPhase(prev: scala.tools.nsc.Phase): StdPhase = {
      stagiumCommitPhase = new Phase(prev)
      stagiumCommitPhase
    }
  }
}
