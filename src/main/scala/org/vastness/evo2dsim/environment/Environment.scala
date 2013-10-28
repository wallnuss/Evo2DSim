package org.vastness.evo2dsim.environment

import org.vastness.evo2dsim.simulator.Simulator
import org.vastness.evo2dsim.App

/**
 * Implements the very basics for an environment
 * @param timeStep in ms
 * @param simSpeed relative to real time
 * @param steps how many steps should the evaluation run?
 */
abstract class Environment(val timeStep: Int = 50, val simSpeed: Int = 1, val steps:Int = 0) {
  protected var stepCounter = 0
  val sim = new Simulator(new scala.util.Random().nextLong())

  var running = true

  private def updateSimulation() {
    sim.step(timeStep/1000.0f)
    stepCounter += 1
    if(steps == stepCounter) running = false
  }

  private class SimulationLoop extends java.util.TimerTask
  {
    override def run()
    {
      updateSimulation()

      if (!running)
      {
        this.cancel()
      }
    }
  }

  App.timer.schedule(new SimulationLoop, 0, timeStep / simSpeed)

  def initialize()
}
