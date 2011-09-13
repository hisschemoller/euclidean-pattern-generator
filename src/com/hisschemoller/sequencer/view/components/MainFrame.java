package com.hisschemoller.sequencer.view.components;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import com.hisschemoller.sequencer.util.EPGPreferences;
import com.hisschemoller.sequencer.util.EPGSwingEngine;

public class MainFrame implements ComponentListener
{
	public MainFrame ( EPGSwingEngine swingEngine )
	{
		/** Initialize frame from preferences. */
		JFrame jFrame = ( JFrame ) swingEngine.find ( EPGSwingEngine.MAIN_FRAME );
		Dimension preferredSize = jFrame.getPreferredSize ( );
		jFrame.setSize ( EPGPreferences.getInt ( EPGPreferences.WINDOW_WIDTH, preferredSize.width ), EPGPreferences.getInt ( EPGPreferences.WINDOW_HEIGHT, preferredSize.height ) );
		jFrame.setLocation ( EPGPreferences.getInt ( EPGPreferences.WINDOW_X, 0 ), EPGPreferences.getInt ( EPGPreferences.WINDOW_Y, 0 ) );
		jFrame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		jFrame.setVisible ( true );
		jFrame.addComponentListener ( this );
	}

	public void componentHidden ( ComponentEvent event )
	{
	}

	public void componentMoved ( ComponentEvent event )
	{
		setPreferences ( event );
	}

	public void componentResized ( ComponentEvent event )
	{
		setPreferences ( event );
	}

	public void componentShown ( ComponentEvent event )
	{
	}

	private void setPreferences ( ComponentEvent event )
	{
		EPGPreferences.putInt ( EPGPreferences.WINDOW_X, event.getComponent ( ).getX ( ) );
		EPGPreferences.putInt ( EPGPreferences.WINDOW_Y, event.getComponent ( ).getY ( ) );
		EPGPreferences.putInt ( EPGPreferences.WINDOW_WIDTH, event.getComponent ( ).getWidth ( ) );
		EPGPreferences.putInt ( EPGPreferences.WINDOW_HEIGHT, event.getComponent ( ).getHeight ( ) );
	}
}
