package org.vastness.evo2dsim.environment

import org.jbox2d.common.Vec2
import org.vastness.evo2dsim.simulator.food.StaticFoodSource
import org.vastness.evo2dsim.gui.Color
import org.vastness.evo2dsim.simulator.Agent
import org.vastness.evo2dsim.evolution.Genome
import scala.collection.Map

/**
 * @see Environment
 */
class BasicEnvironment(val timeStep:Int, val steps:Int) extends Environment{

  val origin = new Vec2(1.015f,1.015f)
  val halfSize = 1f

  def initializeStatic() {
    val sizes = Array[Vec2](new Vec2(-halfSize,-halfSize), new Vec2(-halfSize,halfSize), new Vec2(halfSize,halfSize), new Vec2(halfSize,-halfSize))
    val edges = for(i <- 0 until sizes.length) yield origin add sizes(i)

    sim.createWorldBoundary(edges.toArray)
    addFoodSources(edges)
  }

  protected def addFoodSources(edges: Seq[Vec2]) {
    val f1 = new StaticFoodSource(color = Color.RED, max = 8, reward = 1)
    val f2 = new StaticFoodSource(color = Color.RED, max = 8, reward = -0.3)

    sim.addFoodSource(edges(0) add new Vec2(0.1f, 0.1f), radius = 0.1f, activationRange = 0.15f, f1)
    sim.addFoodSource(edges(2) add new Vec2(-0.1f, -0.1f), radius = 0.1f, activationRange = 0.5f, f2)
  }

  def initializeAgents(genomes: Map[Int, (Double, Genome)]){
    def pos = newRandomPosition
    def addWithGenome(id: Int, a: Agent, g: Genome): Agent = {
      g.addId(id)
      a.controller.get.fromGenome(g)
      a
    }

    agents = ( for( (id,(_, genome)) <- genomes) yield
      (id, addWithGenome(id, sim.addAgent(pos, sim.Agents.SBotControllerLinear), genome))
      ).toMap
  }
}
