package org.vastness.evo2dsim.neuro


class MotorNeuron(v_bias: Double, t_func: (Double) => Double, var m_func: (Double) => Unit = (_) => {} ) extends Neuron(v_bias, t_func){
  override def step() {
    super.step()
    m_func(calcOutput)
  }

}
