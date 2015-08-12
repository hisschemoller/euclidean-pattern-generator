package com.hisschemoller.epg.view.components;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import com.hisschemoller.epg.util.EPGPreferences;

public class MainWindow implements ComponentListener
{
	private JFrame _mainFrame;
	
	public MainWindow ( JFrame mainFrame )
	{
		_mainFrame = mainFrame;
		
		/** Initialize frame from preferences. */
		Dimension preferredSize = _mainFrame.getPreferredSize ( );
		_mainFrame.setSize ( EPGPreferences.getInt ( EPGPreferences.WINDOW_WIDTH, preferredSize.width ), EPGPreferences.getInt ( EPGPreferences.WINDOW_HEIGHT, preferredSize.height ) );
		_mainFrame.setLocation ( EPGPreferences.getInt ( EPGPreferences.WINDOW_X, 0 ), EPGPreferences.getInt ( EPGPreferences.WINDOW_Y, 0 ) );
		_mainFrame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		_mainFrame.setVisible ( true );
		_mainFrame.addComponentListener ( this );
	}

	public void setTitle ( String title )
	{
		_mainFrame.setTitle ( title );
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
