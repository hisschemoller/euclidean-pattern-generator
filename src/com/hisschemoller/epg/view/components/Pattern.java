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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.UUID;

import javax.sound.midi.MidiEvent;
import javax.swing.JPanel;

import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.notification.note.PatternSequenceNote;

public class Pattern extends JPanel implements Runnable
{
	public static final long serialVersionUID = -1L;
	public static final int PANEL_SIZE = 200;
	private static final float DOUBLE_PI = ( float ) Math.PI * 2;
	private UUID _id;
	private PatternPainter _painter;
	private int _rotation = -1;
	private int _numSteps;
	private float _tweenPosition = 0;
	private float _tweenDuration = 1;
	private float _tweenIncrease = 0.1f;
	private float _position;

	public enum Operation
	{
		CHANGE, LOCATION, NAME, POINTER;
	}

	public Pattern ( PatternVO patternVO, Boolean isAnimated )
	{
		this ( null, patternVO, isAnimated );
	}

	public Pattern ( LayoutManager layoutManager, PatternVO patternVO, Boolean isAnimated )
	{
		super ( layoutManager );
		setup ( patternVO, isAnimated );
	}

	public void dispose ( )
	{

	}
	
	/**
	 * An aspect of a pattern has changed.
	 * @param patternVO Pattern data of the changed pattern.
	 * @param operation Type of update, change in location, pointer, location or name.
	 */
	public void updatePattern ( PatternVO patternVO, Pattern.Operation operation )
	{
		switch ( operation )
		{
		case CHANGE:

			/** Pattern change. */
			Boolean [ ] selections = new Boolean[ patternVO.steps ];
			for ( int i = 0; i < patternVO.steps; i++ )
			{
				/** Check if the step is selected. */
				Boolean selected = false;
				int stepIndexRotationCorrected = ( i + patternVO.rotation ) % patternVO.steps;
				int m = patternVO.events.size ( );
				while ( --m > -1 )
				{
					MidiEvent midiEvent = patternVO.events.get ( m );
					if ( midiEvent.getTick ( ) / patternVO.stepLength == stepIndexRotationCorrected )
					{
						selected = true;
						break;
					}
				}
				selections[ i ] = selected;
			}

			_painter.setPattern ( selections );

			/** Rotation change. */
			if ( _rotation != patternVO.rotation || _numSteps != patternVO.steps )
			{
				_rotation = patternVO.rotation;
				_painter.setPatternRotation ( patternVO.rotation );
			}

			_numSteps = patternVO.steps;
			break;

		case POINTER:
			_painter.updatePointer ( patternVO.patternState );
			break;

//		case MUTE:
//			_painter.setPointerMute ( patternVO.mute );
//			break;
//
//		case SOLO:
//			_painter.setPointerSolo ( patternVO.solo );
//			_painter.setPointerMute ( patternVO.mutedBySolo || ( patternVO.mute && !patternVO.solo ) );
//			break;

		case LOCATION:
			if ( getX ( ) != patternVO.viewX || getY ( ) != patternVO.viewY )
			{
				setLocation ( patternVO.viewX, patternVO.viewY );
			}
			break;

		case NAME:
			_painter.setName ( patternVO.name );
			break;
		}
	}

	/**
	 * Each sequencer pulse these values are updated. _position is as part of
	 * full rotation normalized (0 to 1).
	 */
	public void updatePosition ( float position )
	{
		_position = position;
	}

	/**
	 * The editor has it's own screen update timer, independent from the
	 * sequencer pulse.
	 */
	public void updateDraw ( )
	{
		_painter.setPointerRotation ( ( float ) Math.PI + ( DOUBLE_PI * _position ) );
	}

	/**
	 * Show the MIDI note that is played.
	 */
	public void updateSequence ( PatternSequenceNote note )
	{
		// Fast user generated UI updates may conflict with clock generated updates,
		// resulting in the rotation being larger than the number of steps in a pattern.
		// This is kept within limits here.
		if ( _rotation > _numSteps )
		{
			_rotation %= _numSteps;
		}
		
		int stepIndexRotationCorrected = note.stepIndex - _rotation;

		if ( stepIndexRotationCorrected < 0 )
		{
			stepIndexRotationCorrected += _numSteps;
		}

		_painter.setPlayedNote ( note.midiStatus, stepIndexRotationCorrected );
	}

	public void select ( Boolean isSelected )
	{
		_painter.setSelected ( isSelected );
	}

	public UUID getID ( )
	{
		return _id;
	}

	public void run ( )
	{
		while ( true )
		{
			try
			{
				_tweenPosition += _tweenIncrease;
				_painter.updateStartupAnimation ( _tweenPosition );
				if ( _tweenPosition >= _tweenDuration )
				{
					break;
				}
				Thread.sleep ( 40 );
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
		_painter.paintComponent ( graphics );
	}

	private void setup ( PatternVO patternVO, Boolean isAnimated )
	{
		_id = patternVO.id;

		_painter = new PatternPainter ( this );

		setOpaque ( false );
		// setBackground ( new Color ( 0xFFEEEEEE ) );
		setSize ( PANEL_SIZE, PANEL_SIZE );
		setPreferredSize ( new Dimension ( PANEL_SIZE, PANEL_SIZE ) );

		updatePattern ( patternVO, Operation.CHANGE );
		updatePattern ( patternVO, Operation.LOCATION );
		updatePattern ( patternVO, Operation.NAME );
		updatePattern ( patternVO, Operation.POINTER );

		if ( isAnimated )
		{
			new Thread ( this ).start ( );
		}
	}
}
