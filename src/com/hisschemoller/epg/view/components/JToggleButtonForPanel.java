package com.hisschemoller.epg.view.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class JToggleButtonForPanel extends JToggleButton
{
	private static final long serialVersionUID = 7670784222283949316L;

	public JToggleButtonForPanel ( )
	{
		super ( );

		setLayout ( null );
		setIcon ( createImageIcon ( "/res/images/arrow_down.png", "Open" ) );
		setSelectedIcon ( createImageIcon ( "/res/images/arrow_right.png", "Close" ) );
		setBorderPainted ( false );
		setBackground ( Color.WHITE );
		setForeground ( Color.BLACK );
		setFont ( new Font ( "Dialog", Font.PLAIN, 13 ) );
		setText ( "Panel Button" );
		setSize ( 250, 30 );
		setPreferredSize ( getSize ( ) );
	}

	public void paintComponent ( Graphics g )
	{
		super.paintComponent ( g );

		int w = getWidth ( );
		int h = getHeight ( );
		g.setColor ( getBackground ( ) );
		g.fillRect ( 0, 0, w, h );
		g.setColor ( getForeground ( ) );
		g.drawString ( getText ( ), 10, ( h + g.getFontMetrics ( ).getAscent ( ) ) / 2 - 1 );

		Icon icon = isSelected ( ) ? getSelectedIcon ( ) : getIcon ( );
		icon.paintIcon ( this, g, w - 30, ( h - getIcon ( ).getIconHeight ( ) ) >> 1 );
	}

	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 */
	private static ImageIcon createImageIcon ( String path, String description )
	{
		URL imgURL = JToggleButtonForPanel.class.getClass ( ).getResource ( path );
		if ( imgURL != null )
		{
			return new ImageIcon ( imgURL, description );
		}
		else
		{
			System.err.println ( "Couldn't find file: " + path );
			return null;
		}
	}
}
