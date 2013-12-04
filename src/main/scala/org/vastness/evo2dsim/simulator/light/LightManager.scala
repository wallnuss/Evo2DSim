package org.vastness.evo2dsim.simulator.light

import scala.collection.mutable.ArrayBuffer
import org.vastness.utils.Enum

class LightManager {
  var lightSources = ArrayBuffer[LightSource]()

  def addLight(l: LightSource){
    lightSources += l
  }

  def findByCategory(category: LightCategory) = lightSources.filter(_.category == category)

  def toggleByCategory(category: LightCategory) =
    findByCategory(category).foreach {l => l.forced_disable = !l.forced_disable}

  def disableByCategory(category: LightCategory) =
    findByCategory(category).foreach(_.forced_disable = true)

  def enableByCategory(category: LightCategory) =
    findByCategory(category).foreach(_.forced_disable = false)
}

sealed trait LightCategory

object LightCategory extends Enum[LightCategory] {
  case object AgentLight extends LightCategory
  case object FoodSourceLight extends LightCategory
}
