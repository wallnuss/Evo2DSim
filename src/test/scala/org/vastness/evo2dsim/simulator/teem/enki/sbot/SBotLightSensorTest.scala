package org.vastness.evo2dsim.simulator.teem.enki.sbot

import org.scalatest._
import org.vastness.evo2dsim.simulator.Simulator
import org.jbox2d.common.Vec2
import org.vastness.evo2dsim.teem.enki.sbot._


class SBotLightSensorTest extends FlatSpec with Matchers {

  trait Sim {
    val sim = new Simulator(0)
    val timeStep = 50.0f/1000
  }

  trait SimWithOneAgent extends Sim with PrivateMethodTester{
    val agent1 = sim.addAgent(new Vec2(0,0), sim.Agents.SBotControllerLinearZero).asInstanceOf[SBot]
    val lightSensor1 = agent1.controller.get match{
      case c: SBotController => c.lightSensor
    }
    val visionStripPM = PrivateMethod[Array[Array[Float]]]('visionStrip)
  }

  trait SimWithTwoAgents extends SimWithOneAgent {
    val agent2 = sim.addAgent(new Vec2(0,1), sim.Agents.SBotControllerLinearZero).asInstanceOf[SBot]
  }

  "Position and angle" should "be Zero for Agent1" in new SimWithOneAgent {
    agent1.body.getPosition should be (new Vec2(0,0))
    agent1.body.getAngle should be (0)
  }

  "Lights" should "be on by default" in new SimWithTwoAgents {
    sim.step(timeStep)
    agent1.light.active should be (true)
    agent2.light.active should be (true)
  }

  "SBotLightSensor" should "return 8 input neurons" in new SimWithOneAgent{
    lightSensor1.getNeurons.size should be (8)
  }

  "SBotLightSensor" should "have an zero vision Strip" in new SimWithOneAgent {
    sim.step(timeStep)
    val visionStrip = lightSensor1 invokePrivate visionStripPM()
    for(strip <- visionStrip) strip.sum should be (0)
  }

  "SBotLightSensor" should "have some activity" in new SimWithTwoAgents {
    sim.step(timeStep)
    val visionStrip = lightSensor1 invokePrivate visionStripPM()
    visionStrip(agent2.light.color) should not be 0
  }

  "Relative position from agent1 to light source" should
    "be equal to global position due to origin and zero rotation" in new SimWithTwoAgents {
    agent1.body.getLocalPoint(agent2.body.getPosition) should be (agent2.body.getPosition)
  }

  "Angle between Agent1 and Agent 2" should "be 0" in new SimWithTwoAgents {
    val centerPoint = agent1.body.getLocalPoint(agent2.light.position) mul agent1.radius
    val bearingRad = math.atan2(centerPoint.x, centerPoint.y) // clockwise angle
    (math.toDegrees(bearingRad)+360) % 360 should be (0)
  }

  "Activity" should "be equally distributed between 0-179 to 180-359 Degree" in new SimWithTwoAgents {
    sim.step(timeStep)
    val visionStrip = (lightSensor1 invokePrivate visionStripPM())(agent2.light.color)
    visionStrip.view(0,180).sum should be (visionStrip.view(180,360).sum)
  }

  "Activity" should "be one at 0 at zero at 180" in new SimWithTwoAgents {
    sim.step(timeStep)
    val visionStrip = (lightSensor1 invokePrivate visionStripPM())(agent2.light.color)
    visionStrip(0) should be (1)
    visionStrip(180) should be (0)
  }





}
