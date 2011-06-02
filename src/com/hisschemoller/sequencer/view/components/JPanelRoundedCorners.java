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
