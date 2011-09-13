package com.hisschemoller.sequencer.view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.sound.midi.ShortMessage;
import javax.swing.JPanel;

public class PatternStep extends JPanel implements Runnable
{
	public static final long serialVersionUID = -1L;
	private static final int MARGIN = 2;
	private int _radius;
	private Boolean _selected = false;
	private boolean _isNoteOn = false;
	private float _tweenTime = 10;
	private float _tweenDuration = 10;
	private Point _location;

	public PatternStep ( int radius, Boolean selected )
	{
		this ( null, radius, selected );
	}

	public PatternStep ( LayoutManager layoutManager, int radius, Boolean selected )
	{
		super ( layoutManager );
		setup ( radius, selected );
	}

	public void dispose ( )
	{
		if ( _isNoteOn )
		{
			_tweenTime = _tweenDuration;
		}
	}

	public void showMidiNoteEvent ( int midiStatus )
	{
		if ( midiStatus == ShortMessage.NOTE_ON )
		{
			_isNoteOn = true;
			_tweenTime = 0;
			new Thread ( this ).start ( );
		}
		else if ( midiStatus == ShortMessage.NOTE_OFF )
		{
			_isNoteOn = false;
		}

		repaint ( );
	}

	public void run ( )
	{
		while ( true )
		{
			try
			{
				_tweenTime++;
				repaint ( );
				if ( _tweenTime >= _tweenDuration )
				{
					break;
				}
				Thread.sleep ( 25 );
			}
			catch ( InterruptedException exception )
			{
				System.out.println ( "InterruptedException: PatternStep animation thread interrupted." );
			}
		}
	}

	public void paintComponent ( Graphics graphics )
	{
		super.paintComponent ( graphics );

		float value = 1 + ( ( ( _tweenDuration - _tweenTime ) / _tweenDuration ) * 1 );
		
		float circleRadius = _radius / 2;
		float size = ( ( circleRadius * 2.0f ) - ( MARGIN * 2.0f ) ) * value;
		float location = ( circleRadius * 2 ) - ( size / 2 );

		int strokeColorNr = ( _isNoteOn ) ? 0xFFFF0000 : 0xFF333333;
		int fillColorNr = ( _selected ) ? ( _isNoteOn ) ? 0xFFFFCCCC : 0xFF333333 : 0xFFDDDDDD;

		Ellipse2D.Double center = new Ellipse2D.Double ( location, location, size, size );

		Graphics2D graphics2 = ( Graphics2D ) graphics;
		graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		graphics2.setStroke ( new BasicStroke ( 2 ) );
		graphics2.setColor ( new Color ( fillColorNr ) );
		graphics2.fill ( center );
		graphics2.setColor ( new Color ( strokeColorNr ) );
		graphics2.draw ( center );
	}

	public void setFinalLocation ( Point location )
	{
		this._location = location;
	}

	public Point getFinalLocation ( )
	{
		return _location;
	}

	public boolean getSelected ( )
	{
		return _selected;
	}

	private void setup ( int radius, Boolean selected )
	{
		_radius = radius;
		_selected = selected;

		setOpaque ( false );
		// setBackground ( new Color ( 0xFFCCFFCC ) );
		setSize ( _radius * 2, _radius * 2 );
	}
}
