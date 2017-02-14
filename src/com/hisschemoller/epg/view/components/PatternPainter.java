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

package com.hisschemoller.epg.view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sound.midi.ShortMessage;

import com.hisschemoller.epg.model.data.EPGEnums.PatternState;

public class PatternPainter
{
	private static final Color COLOR_FFFFFF = new Color ( 0xFFFFFF );
	private static final Color COLOR_DDDDDD = new Color ( 0xDDDDDD );
	private static final Color COLOR_CCCCCC = new Color ( 0xCCCCCC );
	private static final Color COLOR_999999 = new Color ( 0x999999 );
	private static final Color COLOR_666666 = new Color ( 0x666666 );
	private static final Color COLOR_333333 = new Color ( 0x333333 );
	private static final Color COLOR_11000000 = new Color ( 0x11000000, true );
	private static final float CENTER_RADIUS = 20;
	private static final float ZERO_RADIUS = 2;
	private static final int STEP_ANIMATION_DURATION = 10;
	private static final int MINIMUM_STEP_CIRCLE_RADIUS = 50;
	protected static final int MAXIMUM_STEP_RADIUS = 5;
	protected static final int STEP_TILE_SIZE = 26;
	protected static final int POINTER_LENGTH_MUTE = 25;
	private BufferedImage _image;
	private Ellipse2D.Float _center;
	private Ellipse2D.Float _select;
	private Ellipse2D.Float _zero;
	private Polygon _polygon;
	private Path2D.Float _pointer;
	private Shape _pointerRotated;
	private AffineTransform _affineTransform;
	private Pattern _pattern;
	private Boolean [ ] _selections;
	private Ellipse2D.Float[] _steps;
	private ArrayList < AnimatedStep > _animatedSteps = new ArrayList < AnimatedStep > ( );
	private BufferedImage [ ] _stepAnimation;
	private PatternNoteIndicator _centerNoteIndicator;
	private FontMetrics _fontMetrics;
	private String _name = "";
	private float _nameX;
	private float _nameHeight;
	private float _interval;
	private int _numSteps;
	private float _centerRadius = CENTER_RADIUS;
	private float _stepCircleRadius;
	protected float _stepRadius;
	private float _finalStepCircleRadius = MINIMUM_STEP_CIRCLE_RADIUS;
	private float _pointerLengthFull;
	private float _pointerLength;
	private float _zeroDistance;
	private boolean _selected = false;
	private boolean _drawFlag = false;

	public PatternPainter ( Pattern pattern )
	{
		_pattern = pattern;
		_center = new Ellipse2D.Float ( -_centerRadius, -_centerRadius, _centerRadius * 2, _centerRadius * 2 );
		_select = new Ellipse2D.Float ( _center.x + 4, _center.y + 4, _center.width - 8, _center.height - 8 );
		_fontMetrics = _pattern.getFontMetrics ( _pattern.getFont ( ) );
		_nameHeight = _fontMetrics.getHeight ( );

		/** Create the step animation series of images. */
		_stepAnimation = new BufferedImage[ STEP_ANIMATION_DURATION ];
		for ( int i = 0; i < STEP_ANIMATION_DURATION; i++ )
		{
			BufferedImage image = new BufferedImage ( STEP_TILE_SIZE, STEP_TILE_SIZE, BufferedImage.TYPE_INT_ARGB );
			_stepAnimation[ i ] = image;
			float radius = MAXIMUM_STEP_RADIUS + ( 5f * ( ( float ) ( STEP_ANIMATION_DURATION - i ) / STEP_ANIMATION_DURATION ) );
			Ellipse2D.Float ellipse = new Ellipse2D.Float ( -radius, -radius, radius * 2, radius * 2 );
			Graphics2D graphics2 = ( Graphics2D ) image.getGraphics ( );
			graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			graphics2.setStroke ( new BasicStroke ( 2 ) );
			graphics2.translate ( STEP_TILE_SIZE / 2, STEP_TILE_SIZE / 2 );
			graphics2.setColor ( COLOR_999999 );
			graphics2.fill ( ellipse );
			graphics2.setColor ( COLOR_333333 );
			graphics2.draw ( ellipse );
		}

		// Manages the center note animation.
		_centerNoteIndicator = new PatternNoteIndicator ( );

		setPatternRotation ( 0 );
	}

	public void paintComponent ( Graphics graphics )
	{
		Graphics2D graphics2 = ( Graphics2D ) graphics;

		/** Draw the BufferedImage. */
		synchronized ( this )
		{
			if ( _image != null )
			{
				graphics2.drawImage ( _image, 0, 0, _pattern );
			}
		}

		graphics2.translate ( Pattern.PANEL_SIZE / 2, Pattern.PANEL_SIZE / 2 );

		/** Draw step animations (if any). */
		int n = _animatedSteps.size ( );
		while ( --n > -1 )
		{
			AnimatedStep step = _animatedSteps.get ( n );
			graphics2.drawImage ( _stepAnimation[ step.clock ], step.x, step.y, _pattern );
			if ( ++step.clock >= STEP_ANIMATION_DURATION )
			{
				_animatedSteps.remove ( n );
			}
		}

		/** Draw center Note On indicator (if note plays). */
		_centerNoteIndicator.paintComponent ( graphics2, _pattern );

		graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		graphics2.setStroke ( new BasicStroke ( 2 ) );
		graphics2.setColor ( COLOR_333333 );

		/** Draw pointer. */
		graphics2.draw ( _pointerRotated );
	}

	/**
	 * Set the pattern of steps to be drawn.
	 * @param selections Array of steps where true is a selected step.
	 */
	public void setPattern ( Boolean [ ] selections )
	{
		_numSteps = selections.length;
		_interval = ( float ) ( Math.PI * 2 ) / _numSteps;
		_selections = selections.clone ( );
		_steps = new Ellipse2D.Float[ _numSteps ];
		_polygon = new Polygon ( );
		_finalStepCircleRadius = MINIMUM_STEP_CIRCLE_RADIUS + ( Math.max ( 0, _numSteps - 16 ) * 0.6f );
		_stepCircleRadius = _finalStepCircleRadius;
		_stepRadius = MAXIMUM_STEP_RADIUS - ( Math.max ( 0, _numSteps - 16 ) * 0.04f );
		_zeroDistance = _stepCircleRadius + 11;
		_pointerLength = _stepCircleRadius - 4;

		for ( int i = 0; i < _numSteps; i++ )
		{
			float angle = i * _interval;
			int stepX = ( int ) ( Math.sin ( angle ) * _stepCircleRadius );
			int stepY = ( int ) ( Math.cos ( angle ) * -_stepCircleRadius );
			_steps[ i ] = new Ellipse2D.Float ( stepX - _stepRadius, stepY - _stepRadius, _stepRadius * 2, _stepRadius * 2 );

			if ( _selections[ i ] )
			{
				_polygon.addPoint ( stepX, stepY );
			}
		}
		
		drawImage ( );
	}

	public void setPatternRotation ( int rotation )
	{
		_drawFlag = rotation != 0;

		/** Update zero symbol. */
		float position = _interval * ( _numSteps - rotation );
		float locationX = -ZERO_RADIUS + ( float ) ( Math.sin ( position ) * _zeroDistance );
		float locationY = -ZERO_RADIUS + ( float ) ( Math.cos ( position ) * -_zeroDistance );
		_zero = new Ellipse2D.Float ( locationX, locationY, ZERO_RADIUS * 2, ZERO_RADIUS * 2 );

		drawImage ( );
	}

	public void setPointerRotation ( float rotation )
	{
		_affineTransform = new AffineTransform ( );
		_affineTransform.rotate ( rotation );
		_pointerRotated = _affineTransform.createTransformedShape ( _pointer );
	}

	/**
	 * Show the MIDI note that is played.
	 * This is shown by the step dot being animated on note on and off.
	 * @param midiStatus MIDI note on or off.
	 * @param stepIndex Current step in the pattern sequence. 
	 */
	public void setPlayedNote ( int midiStatus, int stepIndex )
	{
		if ( midiStatus == ShortMessage.NOTE_ON )
		{
			AnimatedStep step = new AnimatedStep ( );
			step.x = ( int ) ( _steps[ stepIndex ].x + AnimatedStep.POSITION_OFFSET );
			step.y = ( int ) ( _steps[ stepIndex ].y + AnimatedStep.POSITION_OFFSET );
			_animatedSteps.add ( step );

			_centerNoteIndicator.start ( );
		}
		else if ( midiStatus == ShortMessage.NOTE_OFF )
		{
			_centerNoteIndicator.end ( );
		}
	}

	public void setSelected ( boolean isSelected )
	{
		_selected = isSelected;
		drawImage ( );
	}

	public void setName ( String name )
	{
		_name = name;
		_nameX = _fontMetrics.stringWidth ( _name ) / -2f;
		drawImage ( );
	}

	public void updateStartupAnimation ( float position )
	{
		/** _center and _select */
		_centerRadius = CENTER_RADIUS * ( 0.2f + position * 0.8f );
		_center = new Ellipse2D.Float ( -_centerRadius, -_centerRadius, _centerRadius * 2, _centerRadius * 2 );
		_select = new Ellipse2D.Float ( _center.x + 4, _center.y + 4, _center.width - 8, _center.height - 8 );

		/** _polygon and _steps */
		_steps = new Ellipse2D.Float[ _numSteps ];
		_polygon = new Polygon ( );
		for ( int i = 0; i < _numSteps; i++ )
		{
			_stepCircleRadius = _finalStepCircleRadius * ( 0.01f + ( position * 0.99f ) );
			float angle = i * _interval;
			int stepX = ( int ) ( Math.sin ( angle ) * _stepCircleRadius );
			int stepY = ( int ) ( Math.cos ( angle ) * -_stepCircleRadius );
			_steps[ i ] = new Ellipse2D.Float ( stepX - _stepRadius, stepY - _stepRadius, _stepRadius * 2, _stepRadius * 2 );

			if ( _selections[ i ] )
			{
				_polygon.addPoint ( stepX, stepY );
			}
		}

		/** _pointer */
		_pointerLength = _pointerLengthFull * ( 0.01f + position * 0.99f );
		updatePointer ( PatternState.STARTUP_ANIMATION );

		/** _zero and _flag */
		if ( position >= 1 )
		{
			float pos = _interval * ( _numSteps - 0 );
			float locationX = -ZERO_RADIUS + ( float ) ( Math.sin ( pos ) * _zeroDistance );
			float locationY = -ZERO_RADIUS + ( float ) ( Math.cos ( pos ) * -_zeroDistance );
			_zero = new Ellipse2D.Float ( locationX, locationY, ZERO_RADIUS * 2, ZERO_RADIUS * 2 );
		}
		else
		{
			_zero = ( Ellipse2D.Float ) _center.clone ( );
		}

		drawImage ( );
	}

	/**
	 * STARTUP_ANIMATION: Draw at startup.
	 * MUTE: Always draw muted pointer.
	 * SOLO: Draw solo pointer.
	 * UNTRIGGERED: Draw if not muted.
	 * UNTRIGGERED_SOLO: Draw if not muted.
	 * DEFAULT: Draw if not muted, soloed or untriggered.
	 * patternState : One of the PatternState enum values.
	 */
	public void updatePointer ( PatternState patternState )
	{
		_pointerLength = ( patternState == PatternState.MUTE ) ? POINTER_LENGTH_MUTE : _stepCircleRadius - 4;

		_pointer = new Path2D.Float ( );

		if ( patternState == PatternState.UNTRIGGERED || patternState == PatternState.UNTRIGGERED_SOLO )
		{
			_pointer.moveTo ( 0, _centerRadius );
			_pointer.lineTo ( -4, _centerRadius + 4 );
			_pointer.lineTo ( 0, _centerRadius + 8 );
			_pointer.lineTo ( 4, _centerRadius + 4 );
			_pointer.lineTo ( 0, _centerRadius );

			if ( patternState == PatternState.UNTRIGGERED_SOLO )
			{
				_pointer.lineTo ( 0, _centerRadius + 8 );
			}
		}
		else if ( patternState == PatternState.TRIGGERED || patternState == PatternState.TRIGGERED_SOLO )
		{
			float half = _centerRadius + ( ( _pointerLength - _centerRadius ) / 2 );
			_pointer.moveTo ( 0, _centerRadius );
			_pointer.lineTo ( -6, half );
			_pointer.lineTo ( 0, _pointerLength );
			_pointer.lineTo ( 6, half );
			_pointer.lineTo ( 0, _centerRadius );

			if ( patternState == PatternState.TRIGGERED_SOLO )
			{
				_pointer.lineTo ( 0, _pointerLength );
			}
		}
		else
		{
			_pointer.moveTo ( -8, _centerRadius - 2 );
			_pointer.lineTo ( 0, _pointerLength );
			_pointer.lineTo ( 8, _centerRadius - 2 );

			if ( patternState == PatternState.SOLO )
			{
				_pointer.moveTo ( 0, _centerRadius + 1 );
				_pointer.lineTo ( 0, _pointerLength );
			}
		}

		_affineTransform = new AffineTransform ( );
		_pointerRotated = _affineTransform.createTransformedShape ( _pointer );
	}

	private void drawImage ( )
	{
		synchronized ( this )
		{
			_image = new BufferedImage ( Pattern.PANEL_SIZE, Pattern.PANEL_SIZE, BufferedImage.TYPE_INT_ARGB );

			Graphics2D graphics2 = ( Graphics2D ) _image.getGraphics ( );
			graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			graphics2.translate ( Pattern.PANEL_SIZE / 2, Pattern.PANEL_SIZE / 2 );
			graphics2.setStroke ( new BasicStroke ( 2 ) );

			/** Draw polygon. */
			if ( _polygon != null )
			{
				graphics2.setColor ( COLOR_11000000 );
				graphics2.fillPolygon ( _polygon );
				graphics2.setColor ( COLOR_CCCCCC );
				graphics2.drawPolygon ( _polygon );
			}

			/** Draw steps. */
			for ( int i = 0; i < _numSteps; i++ )
			{
				Ellipse2D.Float step = _steps[ i ];
				graphics2.setColor ( _selections[ i ] ? COLOR_999999 : COLOR_FFFFFF );
				graphics2.fill ( step );
				graphics2.setColor ( COLOR_333333 );
				graphics2.draw ( step );
			}

			/** Draw center circle. */
			graphics2.setColor ( COLOR_DDDDDD );
			graphics2.fill ( _center );
			graphics2.setColor ( COLOR_333333 );
			graphics2.draw ( _center );

			/** Draw select circle. */
			if ( _selected )
			{
				graphics2.setColor ( COLOR_DDDDDD );
				graphics2.fill ( _select );
				graphics2.setColor ( COLOR_333333 );
				graphics2.draw ( _select );
			}

			/** Draw the zero. */
			graphics2.draw ( _zero );

			/** Draw the flag. */
			if ( _drawFlag )
			{
				graphics2.translate ( 0, -_stepCircleRadius - 11 );
				graphics2.drawLine ( 0, -3, 0, 3 );
				graphics2.drawLine ( 4, -1, 0, -3 );
				graphics2.drawLine ( 0, 1, 4, -1 );
				graphics2.translate ( 0, _stepCircleRadius + 11 );
			}

			/** Draw the pattern name. */
			if ( !_name.equals ( "" ) )
			{
				graphics2.setColor ( COLOR_666666 );
				graphics2.drawString ( _name, _nameX, _stepCircleRadius + 4f + _nameHeight );
			}

			graphics2.dispose ( );
		}
	}
}

final class AnimatedStep
{
	public static final float POSITION_OFFSET = PatternPainter.MAXIMUM_STEP_RADIUS - ( PatternPainter.STEP_TILE_SIZE / 2 );
	public int x;
	public int y;
	public int clock = 0;
}
