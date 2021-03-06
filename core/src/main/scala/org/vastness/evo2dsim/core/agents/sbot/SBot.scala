/*
 * This file is part of Evo2DSim.
 *
 * Evo2DSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Evo2DSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Evo2DSim.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.vastness.evo2dsim.core.agents.sbot

import org.jbox2d.common.Vec2
import org.vastness.evo2dsim.core.simulator.{AgentID, Simulator, Agent}
import org.vastness.evo2dsim.core.simulator.light.{LightCategory, LightSource}
import org.vastness.evo2dsim.core.gui.Color
import org.vastness.evo2dsim.core.data.Record

/**
 * Implements an S-Bot agent similar to the enki simulator.
 *   val radius = 0.06f  S-Bot size 6cm
 *   val mass = 0.66f S-Bot weight 660g
 */
class SBot(id: AgentID, pos: Vec2, angle: Float, sim: Simulator)
  extends Agent(id, pos, angle, sim, radius = 0.06f, mass = 0.66f) {
  val light = new LightSource(Color.BLUE, this, LightCategory.AgentLight, radius)
  sim.lightManager.addLight(light)

  override val controller = new SBotController
  controller.attachToAgent(this)

  override def signalling = light.active

  override def color = light.color

  override def dataHeader = super.dataHeader ++ Seq("light")
  override def dataRow = Record.append(super.dataRow, Seq(light.active.toString))
}
