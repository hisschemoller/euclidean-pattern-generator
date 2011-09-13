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

package com.hisschemoller.sequencer.model;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import oscP5.OscMessage;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.notification.note.PatternPositionNote;
import com.hisschemoller.sequencer.notification.note.PatternSequenceNote;
import com.hisschemoller.sequencer.util.ISequenceable;
import com.hisschemoller.sequencer.util.NoteOffThread;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.util.TimerThread;

public class SequencerProxy extends Proxy implements ISequenceable
{
	public static final String NAME = SequencerProxy.class.getName ( );
	private TimerThread _timerThread;
	private Vector<PatternVO> _patterns = new Vector<PatternVO> ( );
	private PatternVO _selectedPattern;
	private long _screenRedrawInterval = 30l;
	private long _screenRedrawTime;
	private NoteOffThread [ ][ ] _noteMatrix = new NoteOffThread[ 16 ][ 128 ];
	private PatternPositionNote [ ] _positionNotes = new PatternPositionNote [ 0 ];

	public SequencerProxy ( )
	{
		super ( NAME );
	}

	@Override public final void onRegister ( )
	{
		_timerThread = new TimerThread ( this, 120.0f );
		_timerThread.start ( );
	}

	@Override public final void onRemove ( )
	{
		_timerThread.stopTimer ( );
	}

	/**
	 * Called by TimerThread on each PPQN pulse.
	 */
	public void onPulse ( )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );

			/** Check if events take place this pulse. */
			int patternPositionRotationCorrected = ( patternVO.position + ( patternVO.rotation * patternVO.quantization ) ) % patternVO.length;
			int m = patternVO.events.size ( );
			while ( --m > -1 )
			{
				MidiEvent midiEvent = patternVO.events.get ( m );
				if ( midiEvent.getTick ( ) == patternPositionRotationCorrected && ( patternVO.solo || ( !patternVO.mute && !patternVO.mutedBySolo ) ) )
				{
					onNoteStart ( patternVO, midiEvent );
				}
				else if ( midiEvent.getTick ( ) < patternPositionRotationCorrected )
				{
					break;
				}
			}
			
			/** Get position for pattern visual to update. */
			_positionNotes[ n ].position = (float) patternVO.position / patternVO.length;

			/** Advance position in pattern. */
			patternVO.position = ( patternVO.position + 1 ) % patternVO.length;
		}

		/** Check if it's time to update the screen. */
		if ( new Date ( ).getTime ( ) >= _screenRedrawTime )
		{
			_screenRedrawTime += _screenRedrawInterval;
			sendNotification ( SeqNotifications.REDRAW, _positionNotes );
		}
	}

	/**
	 * View update notification: On | Off, pattern ID, time Dus: Welk patroon,
	 * welke stap, wat voor soort event (on | off).
	 */
	public void onNoteStart ( PatternVO patternVO, MidiEvent midiEvent )
	{
		ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );
		NoteOffThread oldThread = _noteMatrix[ message.getChannel ( ) ][ message.getData1 ( ) ];
		if ( oldThread != null && oldThread.isAlive ( ) )
		{
			/** Stop running threads for this channel and pitch. */
			oldThread.interrupt ( );

			/** Send MIDI Note Off and pattern update notifications. */
			onNoteEnd ( oldThread.getPatternVO ( ), oldThread.getMidiEvent ( ) );
		}

		/** Start thread for this channel and pitch. */
		long duration = ( long ) ( patternVO.noteLength * _timerThread.getMillisPerPulse ( ) );
		NoteOffThread noteOffThread = new NoteOffThread ( this, patternVO, midiEvent, duration );
		_noteMatrix[ message.getChannel ( ) ][ message.getData1 ( ) ] = noteOffThread;
		noteOffThread.start ( );

		/** Send MIDI Note On notification. */
		try
		{
			ShortMessage shortMessage = new ShortMessage ( );
			shortMessage.setMessage ( ShortMessage.NOTE_ON, message.getChannel ( ), message.getData1 ( ), message.getData2 ( ) );
			sendNotification ( SeqNotifications.SEND_MIDI_MESSAGE, shortMessage );
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SequencerProxy.onNoteStart() exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}

		/** Send OSC Note On notification. */
		if ( patternVO.address != null && !patternVO.address.isEmpty () )
		{
		    OscMessage oscMessage = new OscMessage ( patternVO.address );
		    oscMessage.add ( 1 );
		    oscMessage.add ( message.getChannel ( ) );
		    oscMessage.add ( message.getData1 () );
		    oscMessage.add ( message.getData2 () );
		    sendNotification ( SeqNotifications.SEND_OSC_MESSAGE, oscMessage );
		}

		/** Send view update notification. */
		PatternSequenceNote note = new PatternSequenceNote ( ( int ) ( midiEvent.getTick ( ) / patternVO.quantization ), ShortMessage.NOTE_ON, patternVO.id );
		sendNotification ( SeqNotifications.PATTERN_SEQUENCE_UPDATED, note );
	}

	/**
	 * 
	 */
	public void onNoteEnd ( PatternVO patternVO, MidiEvent midiEvent )
	{
		ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );

		/** Send MIDI Note Off notification. */
		try
		{
			ShortMessage shortMessage = new ShortMessage ( );
			shortMessage.setMessage ( ShortMessage.NOTE_OFF, message.getChannel ( ), message.getData1 ( ), message.getData2 ( ) );
			sendNotification ( SeqNotifications.SEND_MIDI_MESSAGE, shortMessage );
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SequencerProxy.onNoteStart() exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}

		/** Send OSC Note Off notification. */
		if ( patternVO.address != null && !patternVO.address.isEmpty () )
		{
		    OscMessage oscMessage = new OscMessage ( patternVO.address );
		    oscMessage.add ( 0 );
		    oscMessage.add ( message.getChannel ( ) );
		    oscMessage.add ( message.getData1 () );
		    oscMessage.add ( message.getData2 () );
		    sendNotification ( SeqNotifications.SEND_OSC_MESSAGE, oscMessage );
		}

		/** Send view update notification. */
		PatternSequenceNote note = new PatternSequenceNote ( ( int ) ( midiEvent.getTick ( ) / patternVO.quantization ), ShortMessage.NOTE_OFF, patternVO.id );
		sendNotification ( SeqNotifications.PATTERN_SEQUENCE_UPDATED, note );
	}

	public void start ( )
	{
		_timerThread.startTimer ( );
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );
			patternVO.position = 0;
		}
	}

	public void stop ( )
	{
		_timerThread.stopTimer ( );
	}

	public void clear ( )
	{
		if ( _timerThread != null && _timerThread.getIsRunning ( ) )
		{
			_timerThread.stopTimer ( );
			sendNotification ( SeqNotifications.PLAYBACK_CHANGED, SequencerEnums.Playback.STOP );
		}

		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO removedPatternVO = _patterns.remove ( n );
			sendNotification ( SeqNotifications.PATTERN_DELETED, removedPatternVO );
		}

		_screenRedrawTime = new Date ( ).getTime ( );
	}

	public Vector<PatternVO> getPatterns ( )
	{
		return _patterns;
	}

	public void setSelectedPattern ( PatternVO patternVO )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			if ( _patterns.get ( n ) == patternVO )
			{
				_selectedPattern = _patterns.get ( n );
			}
		}
	}

	public PatternVO getSelectedPattern ( )
	{
		return _selectedPattern;
	}

	public PatternVO getPatternByID ( UUID id )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			if ( id == _patterns.get ( n ).id )
			{
				return _patterns.get ( n );
			}
		}

		return null;
	}

	public void setTempo ( float bpm )
	{
		_timerThread.setBpm ( bpm );
	}

	public float getTempo ( )
	{
		return _timerThread.getBpm ( );
	}

	public int getPulsesPerQuarterNote ( )
	{
		return _timerThread.getPulsesPerQuarterNote ( );
	}

	public void setPulsesPerQuarterNote ( int ppqn )
	{
		_timerThread.setPulsesPerQuarterNote ( ppqn );
	}

	public int getPulsesSinceStart ( )
	{
		return _timerThread.getPulsesSinceStart ( );
	}

	public float getMillisPerPulse ( )
	{
		return _timerThread.getMillisPerPulse ( );
	}

	public void setPositionNotes ( PatternPositionNote [ ] _positionNotes )
	{
		this._positionNotes = _positionNotes;
	}
	
	public TimerThread getTimerThread()
	{
		return _timerThread;
	}
}
