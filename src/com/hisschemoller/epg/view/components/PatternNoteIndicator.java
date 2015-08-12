package com.hisschemoller.epg.view.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class PatternNoteIndicator
{
	private static final int TILE_SIZE = 30;
	private static final int CENTER_NOTE_MAXIMUM_RADIUS = 6;
	private static final int ANIMATION_DURATION = 3;
	private BufferedImage [ ] _images;
	private int _count = 0;
	private State _state = State.OFF;
	private int _tileCenter = TILE_SIZE / -2;

	private static enum State
	{
		START, END, HOLD, OFF
	};

	public PatternNoteIndicator ( )
	{
		Color fillColor = new Color ( 0x333333 );

		_images = new BufferedImage[ ANIMATION_DURATION ];

		// Create the note animation series of images.
		for ( int i = 0; i < ANIMATION_DURATION; i++ )
		{
			BufferedImage image = new BufferedImage ( TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB );
			_images[ i ] = image;
			float radius = ( ( i + 1f ) / ANIMATION_DURATION ) * CENTER_NOTE_MAXIMUM_RADIUS;
			Ellipse2D.Float ellipse = new Ellipse2D.Float ( -radius, -radius, radius * 2, radius * 2 );
			Graphics2D graphics2 = ( Graphics2D ) image.getGraphics ( );
			graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			// graphics2.setStroke ( new BasicStroke ( 2 ) );
			graphics2.translate ( TILE_SIZE / 2, TILE_SIZE / 2 );
			graphics2.setColor ( fillColor );
			graphics2.fill ( ellipse );
			// graphics2.setColor ( COLOR_333333 );
			// graphics2.draw ( ellipse );
		}
	}

	public void start ( )
	{
		_count = 0;
		_state = State.START;
	}

	public void end ( )
	{
		_count = ANIMATION_DURATION - 1;
		_state = State.END;
	}

	public void paintComponent ( Graphics2D graphics2, Pattern pattern )
	{
		if ( _state == State.OFF )
		{
			return;
		}
		else if ( _state == State.START )
		{
			graphics2.drawImage ( _images[ _count ], _tileCenter, _tileCenter, pattern );
			_count++;
			if ( _count == ANIMATION_DURATION )
			{
				_state = State.HOLD;
			}
		}
		else if ( _state == State.END )
		{
			graphics2.drawImage ( _images[ _count ], _tileCenter, _tileCenter, pattern );
			_count--;
			if ( _count < 0 )
			{
				_state = State.OFF;
			}
		}
		else if ( _state == State.HOLD )
		{
			graphics2.drawImage ( _images[ ANIMATION_DURATION - 1 ], _tileCenter, _tileCenter, pattern );
		}
	}
}
