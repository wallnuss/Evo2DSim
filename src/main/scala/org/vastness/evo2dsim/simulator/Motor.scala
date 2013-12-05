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

package org.vastness.evo2dsim.simulator

import org.jbox2d.common.Vec2
import org.apache.commons.math3.util.FastMath

class Motor() {
  var agentOption: Option[Agent] = None
  val UPPER_OUTPUT_LIMIT = 0.15 // m/s  taken from Cooperative Hole Avoidance in a Swarm-bot
  val LOWER_OUTPUT_LIMIT = -0.15

  val UPPER_INPUT_LIMIT = 1.0
  val LOWER_INPUT_LIMIT = -1.0

  val a = (UPPER_OUTPUT_LIMIT-LOWER_OUTPUT_LIMIT)/(UPPER_INPUT_LIMIT-LOWER_INPUT_LIMIT)
  val b = UPPER_OUTPUT_LIMIT - UPPER_INPUT_LIMIT*(UPPER_OUTPUT_LIMIT-LOWER_OUTPUT_LIMIT)/(UPPER_INPUT_LIMIT-LOWER_INPUT_LIMIT)

  def transform(x: Double) = a*x+b //Linear transformation

  private var leftMotorVelocity = 0.0
  private var rightMotorVelocity = 0.0

  def setLeftMotorVelocity(x: Double) {
    leftMotorVelocity = transform(x)
  }

  def setRightMotorVelocity(x: Double) {
    rightMotorVelocity = transform(x)
  }

  //taken from enki speed control
  def forwardVelocity = (rightMotorVelocity + leftMotorVelocity) / 2
  def velocity(agent: Agent) = new Vec2((forwardVelocity * FastMath.cos(agent.body.getAngle)).toFloat, (forwardVelocity * FastMath.sin(agent.body.getAngle)).toFloat)
  def angularVelocity(agent: Agent) = (rightMotorVelocity-leftMotorVelocity)/(4*agent.radius) // 2* wheel distants

  def attachToAgent(agent: Agent) {
    agentOption = Some(agent)
  }

  //TODO: Check if velocities or forces are better..
  def step() {
    agentOption match {
      case Some(agent) => {
        agent.setLinearVelocity(velocity(agent))
        agent.setAngularVelocity(angularVelocity(agent).toFloat)
      }
      case None => {}
    }

  }
}
