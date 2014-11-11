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

package org.vastness.evo2dsim.core.gui

import java.awt.RenderingHints
import scala.swing.{Graphics2D, Component}
import org.vastness.evo2dsim.core.simulator.Entity

class Surface extends Component {
  def draw(g2: Graphics2D) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    for(e: Entity <- EnvironmentManager.visibleEntities){
        e.sprite.draw(g2)
        if(EnvironmentManager.showData) e.sprite.drawText(g2)
    }

    drawText(g2)
  }

  def drawText(g2: Graphics2D) =  {
    EnvironmentManager.visible match {
      case None =>
      case Some(env) =>
        g2.setColor(Color.BLACK.underlying)
        val text ="Time steps: %s".format(env.stepCounter)
        g2.drawString(text, 0, this.bounds.y - g2.getFontMetrics.getHeight)
    }

  }

  override def paintComponent(g: Graphics2D) {
    super.paintComponent(g)
    draw(g)
  }
}
