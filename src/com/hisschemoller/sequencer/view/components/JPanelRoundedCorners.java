/**
 * Copyright 2011 Wouter Hisschemšller
 * 
 * This file is part of Euclidean Pattern Generator.
 * 
 * Euclidean Pattern Generator is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 * 
 * Euclidean Pattern Generator is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; without even the 
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Euclidean Pattern Generator.  If not, 
 * see <http://www.gnu.org/licenses/>.
 */

package com.hisschemoller.sequencer.view.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class JPanelRoundedCorners extends JPanel
{
	public static final long serialVersionUID = -1L;

	public JPanelRoundedCorners ()
	{
		this ( new FlowLayout ( ) );
	}

	public JPanelRoundedCorners ( LayoutManager layoutManager )
	{
		super ( layoutManager );
		setup ( );
	}

	public void paintComponent ( Graphics graphics )
	{
		super.paintComponent ( graphics );

		Graphics2D graphics2 = (Graphics2D) graphics;
		graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		int radius = 8;
		Color color = new Color ( 0xFFFFFFFF );
		graphics2.setColor ( new Color ( color.getRed ( ), color.getGreen ( ), color.getBlue ( ), 255 ) );
		graphics2.fillRoundRect ( 0, 0, getWidth ( ), getHeight ( ), radius, radius );
	}

	protected void setup ()
	{
		setOpaque ( false );
	}
}
