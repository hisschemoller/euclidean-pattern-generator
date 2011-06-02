package com.hisschemoller.sequencer.view.components;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.UUID;

import javax.sound.midi.MidiEvent;
import javax.swing.JPanel;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.note.PatternSequenceNote;

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
	private float _rotationPerMsNormalized;
	private float _position;
	private long _positionTime = System.currentTimeMillis ( );

	public enum Operation
	{
		CHANGE, MUTE, SOLO, LOCATION;
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
					if ( midiEvent.getTick ( ) / patternVO.quantization == stepIndexRotationCorrected )
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

		case MUTE:
			_painter.setPointerMute ( patternVO.mute );
			break;

		case SOLO:
			if ( patternVO.id == _id )
			{
				_painter.setPointerSolo ( patternVO.solo );
				_painter.setPointerMute ( ( patternVO.solo ) ? false : patternVO.mute );
			}
			else
			{
				_painter.setPointerSolo ( false );
				_painter.setPointerMute ( ( patternVO.solo ) ? true : patternVO.mute );
			}
			break;

		case LOCATION:
			if ( getX ( ) != patternVO.viewX || getY ( ) != patternVO.viewY )
			{
				setLocation ( patternVO.viewX, patternVO.viewY );
			}
			break;
		}
	}

	/**
	 * Each sequencer pulse these values are updated. _position is as part of
	 * full rotation normalized (0 to 1).
	 */
	public void updatePosition ( float position )
	{
		_positionTime = System.currentTimeMillis ( );
		_position = position;
	}

	/**
	 * Each time the playback speed changes this value is updated.
	 * _rotationPerMsNormalized = rotation normalized (0 to 1) per millisecond.
	 */
	public void updateTempo ( float bpm )
	{
		if ( bpm == 0f )
		{
			_rotationPerMsNormalized = 0;
			return;
		}

		float milliSecondsPerBeat = 60000f / bpm;
		float beatsPerRotation = _numSteps / 4f;
		float milliSecondsPerRotation = beatsPerRotation * milliSecondsPerBeat;
		_rotationPerMsNormalized = 1f / milliSecondsPerRotation;
	}

	/**
	 * The editor has it's own screen update timer, independent from the
	 * sequencer pulse.
	 */
	public void updateDraw ( )
	{
		long now = System.currentTimeMillis ( );
		float rotationSinceLastPositionUpdate = ( now - _positionTime ) * _rotationPerMsNormalized;
		float positionNow = _position + rotationSinceLastPositionUpdate;
		_painter.setPointerRotation ( ( float ) Math.PI + ( DOUBLE_PI * positionNow ) );
	}

	/**
	 * Show the MIDI note that is played.
	 */
	public void updateSequence ( PatternSequenceNote note )
	{
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

	@Override public void run ( )
	{
		while ( true )
		{
			try
			{
				_tweenPosition += _tweenIncrease;
				_painter.updateStartAnimation ( _tweenPosition );
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

		updatePattern ( patternVO, Operation.CHANGE );
		updatePattern ( patternVO, Operation.LOCATION );
		updatePattern ( patternVO, Operation.MUTE );
		updatePattern ( patternVO, Operation.SOLO );
		
		if( isAnimated )
		{
			new Thread ( this ).start ( );
		}
	}
}
