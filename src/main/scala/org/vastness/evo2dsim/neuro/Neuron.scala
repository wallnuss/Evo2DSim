package org.vastness.evo2dsim.neuro

import org.vastness.evo2dsim.neuro.TransferFunction.TransferFunction
import scala.collection.mutable.ArrayBuffer


class Neuron(v_bias: Double, t_func: TransferFunction ){
  var bias = v_bias

  var inputSynapses =  ArrayBuffer[Synapse]()
  var outputSynapses = ArrayBuffer[Synapse]()

  var activity = 0.0

  def calcActivity = inputSynapses.par.foldLeft(0.0)( _ + _.value ) + bias
  def calcOutput = t_func(activity)

  def step() {
    activity = calcActivity
  }

  def addInput(s: Synapse){
    inputSynapses += s
  }

  def removeInput(s: Synapse){
    inputSynapses = inputSynapses.filterNot(_ == s)
  }

  def addOutput(s: Synapse){
    inputSynapses += s
  }

  def removeOutput(s: Synapse){
    inputSynapses = inputSynapses.filterNot(_ == s)
  }

}


