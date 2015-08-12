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

package com.hisschemoller.epg.model;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import oscP5.OscMessage;

import com.hisschemoller.epg.model.data.EPGEnums;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.EPGEnums.ClockSourceType;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.notification.note.PatternPositionNote;
import com.hisschemoller.epg.notification.note.PatternSequenceNote;
import com.hisschemoller.epg.util.IClockSource;
import com.hisschemoller.epg.util.ISequenceable;
import com.hisschemoller.epg.util.MidiInNoteSource;
import com.hisschemoller.epg.util.NoteOffThread;

public class SequencerProxy extends Proxy implements ISequenceable
{
	public static final String NAME = SequencerProxy.class.getName ( );
	private final int PULSES_PER_QUARTER_NOTE = 24;
	private IClockSource _clockSource;
	private ClockSourceType _clockSourceType;
	private MidiInNoteSource _midiInNoteSource;
	private Vector < PatternVO > _patterns = new Vector < PatternVO > ( );
	private PatternVO _selectedPattern;
	private long _screenRedrawInterval = 30l;
	private long _screenRedrawTime;
	private NoteOffThread [ ][ ] _noteMatrix = new NoteOffThread[ 16 ][ 128 ];
	private PatternPositionNote [ ] _positionNotes = new PatternPositionNote[ 0 ];
	private boolean _isMidiOutEnabled = true;
	private boolean _isOscOutEnabled = true;
	private boolean _isTriggeredByMidiNoteEnabled;
	private float _millisPerPulse;
	private float _beatsPerMinute;

	public SequencerProxy ( )
	{
		super ( NAME );
	}

	@Override public final void onRemove ( )
	{
		_clockSource.dispose ( );
	}

	/**
	 * Called by TimerThread or MidiInClockSource on each PPQN pulse.
	 */
	public void onClock ( )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );

			if ( patternVO.isPlaying )
			{
				/** Check if events take place this pulse. */
				int patternPositionRotationCorrected = ( patternVO.position + ( patternVO.rotation * patternVO.stepLength ) ) % patternVO.patternLength;

				/**
				 * If patterns are triggered by incoming MIDI notes look at the
				 * previous clock tick because a Note On or Off may have
				 * happened since the last tick. This means the patterns play
				 * with one clock tick delay. It's necessary because Note events
				 * that happen on a given tick are sent AFTER the clock tick
				 * event.
				 */
				if ( _isTriggeredByMidiNoteEnabled )
				{
					patternPositionRotationCorrected--;
					if ( patternPositionRotationCorrected < 0 )
					{
						patternPositionRotationCorrected += patternVO.patternLength;
					}
				}

				int m = patternVO.events.size ( );
				while ( --m > -1 )
				{
					MidiEvent midiEvent = patternVO.events.get ( m );
					// if ( midiEvent.getTick ( ) ==
					// patternPositionRotationCorrected && ( patternVO.solo || (
					// !patternVO.mute && !patternVO.mutedBySolo ) ) )
					if ( midiEvent.getTick ( ) == patternPositionRotationCorrected )
					{
						onPatternNoteStart ( patternVO, midiEvent );
					}
					else if ( midiEvent.getTick ( ) < patternPositionRotationCorrected )
					{
						break;
					}
				}
			}

			/** Set position for pattern visual to update. */
			_positionNotes[ n ].position = ( float ) patternVO.position / patternVO.patternLength;

			/** Advance position in pattern. */
			patternVO.position = ( patternVO.position + 1 ) % patternVO.patternLength;
		}

		/** Check if it's time to update the screen. */
		if ( new Date ( ).getTime ( ) >= _screenRedrawTime )
		{
			_screenRedrawTime += _screenRedrawInterval;
			sendNotification ( SeqNotifications.REDRAW, _positionNotes );
		}
	}

	/**
	 * Called by MidiInClockSource.
	 */
	public void onNoteOn ( ShortMessage message )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );
			if ( message.getChannel ( ) == patternVO.triggerMidiInChannel && message.getData1 ( ) == patternVO.triggerMidiInPitch )
			{
				patternVO.isTriggered = true;
				sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patternVO );
				sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patternVO );
			}
		}
	}

	/**
	 * Called by MidiInClockSource.
	 */
	public void onNoteOff ( ShortMessage message )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );
			if ( message.getChannel ( ) == patternVO.triggerMidiInChannel && message.getData1 ( ) == patternVO.triggerMidiInPitch )
			{
				patternVO.isTriggered = false;
				sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patternVO );
				sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patternVO );
			}
		}
	}

	/**
	 * View update notification: On | Off, pattern ID, time Dus: Welk patroon,
	 * welke stap, wat voor soort event (on | off).
	 */
	public void onPatternNoteStart ( PatternVO patternVO, MidiEvent midiEvent )
	{
		ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );
		NoteOffThread oldThread = _noteMatrix[ message.getChannel ( ) ][ message.getData1 ( ) ];
		if ( oldThread != null && oldThread.isAlive ( ) )
		{
			/** Stop running threads for this channel and pitch. */
			oldThread.interrupt ( );

			/** Send MIDI Note Off and pattern update notifications. */
			onPatternNoteEnd ( oldThread.getPatternVO ( ), oldThread.getMidiEvent ( ) );
		}

		/** Start thread for this channel and pitch. */
		long duration = ( long ) ( patternVO.noteLength * _millisPerPulse );
		NoteOffThread noteOffThread = new NoteOffThread ( this, patternVO, midiEvent, duration );
		_noteMatrix[ message.getChannel ( ) ][ message.getData1 ( ) ] = noteOffThread;
		noteOffThread.start ( );

		/** Send MIDI Note On notification. */
		try
		{
			if ( _isMidiOutEnabled )
			{
				ShortMessage shortMessage = new ShortMessage ( );
				shortMessage.setMessage ( ShortMessage.NOTE_ON, message.getChannel ( ), message.getData1 ( ), message.getData2 ( ) );
				sendNotification ( SeqNotifications.SEND_MIDI_MESSAGE, shortMessage );
			}
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SequencerProxy.onNoteStart() exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}

		/** Send OSC Note On notification. */
		if ( _isOscOutEnabled && patternVO.oscOutAddress != null && !patternVO.oscOutAddress.isEmpty ( ) )
		{
			OscMessage oscMessage = new OscMessage ( patternVO.oscOutAddress );
			oscMessage.add ( 1 );
			oscMessage.add ( message.getChannel ( ) );
			oscMessage.add ( message.getData1 ( ) );
			oscMessage.add ( message.getData2 ( ) );
			sendNotification ( SeqNotifications.SEND_OSC_MESSAGE, oscMessage );
		}

		/** Send view update notification. */
		PatternSequenceNote note = new PatternSequenceNote ( ( int ) ( midiEvent.getTick ( ) / patternVO.stepLength ), ShortMessage.NOTE_ON, patternVO.id );
		sendNotification ( SeqNotifications.PATTERN_SEQUENCE_UPDATED, note );
	}

	/**
	 * 
	 */
	public void onPatternNoteEnd ( PatternVO patternVO, MidiEvent midiEvent )
	{
		ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );

		/** Send MIDI Note Off notification. */
		try
		{
			if ( _isMidiOutEnabled )
			{
				ShortMessage shortMessage = new ShortMessage ( );
				shortMessage.setMessage ( ShortMessage.NOTE_OFF, message.getChannel ( ), message.getData1 ( ), message.getData2 ( ) );
				sendNotification ( SeqNotifications.SEND_MIDI_MESSAGE, shortMessage );
			}
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SequencerProxy.onNoteStart() exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}

		/** Send OSC Note Off notification. */
		if ( _isOscOutEnabled && patternVO.oscOutAddress != null && !patternVO.oscOutAddress.isEmpty ( ) )
		{
			OscMessage oscMessage = new OscMessage ( patternVO.oscOutAddress );
			oscMessage.add ( 0 );
			oscMessage.add ( message.getChannel ( ) );
			oscMessage.add ( message.getData1 ( ) );
			oscMessage.add ( message.getData2 ( ) );
			sendNotification ( SeqNotifications.SEND_OSC_MESSAGE, oscMessage );
		}

		/** Send view update notification. */
		PatternSequenceNote note = new PatternSequenceNote ( ( int ) ( midiEvent.getTick ( ) / patternVO.stepLength ), ShortMessage.NOTE_OFF, patternVO.id );
		sendNotification ( SeqNotifications.PATTERN_SEQUENCE_UPDATED, note );
	}

	/**
	 * songPosition: MIDI song position in 16th notes.
	 */
	public void onSongPosition ( int songPosition )
	{
		int positionInPulses = songPosition * ( PULSES_PER_QUARTER_NOTE / 4 );
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = _patterns.get ( n );
			/** Set the new position of the pattern. */
			// patternVO.position = ( positionInPulses + ( patternVO.rotation * patternVO.stepLength ) ) % patternVO.patternLength;
			patternVO.position = positionInPulses % patternVO.patternLength;
			/** Get position for pattern visual to update. */
			_positionNotes[ n ].position = ( float ) patternVO.position / patternVO.patternLength;
		}
		sendNotification ( SeqNotifications.REDRAW, _positionNotes );
	}

	public void start ( )
	{
		_clockSource.startClock ( );
	}

	public void stop ( )
	{
		_clockSource.stopClock ( );
	}

	/**
	 * Clears patterns and sequencer when a new project is started.
	 */
	public void clear ( )
	{
		if ( _clockSource != null )
		{
			stop ( );
			sendNotification ( SeqNotifications.PLAYBACK_CHANGED, EPGEnums.Playback.STOP );
		}

		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO removedPatternVO = _patterns.remove ( n );
			sendNotification ( SeqNotifications.PATTERN_DELETED, removedPatternVO );
		}

		_screenRedrawTime = new Date ( ).getTime ( );
	}

	public void updateClockSource ( IClockSource clockSource )
	{
		if ( _clockSource != null )
		{
			_clockSource.dispose ( );
		}

		_clockSource = clockSource;
	}

	public void setClockSourceType ( ClockSourceType clockSourceType )
	{
		_clockSourceType = clockSourceType;
	}

	public ClockSourceType getClockSourceType ( )
	{
		return _clockSourceType;
	}

	public Vector < PatternVO > getPatterns ( )
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
		_beatsPerMinute = bpm;
		_millisPerPulse = new Double ( 1000 / ( PULSES_PER_QUARTER_NOTE * ( _beatsPerMinute / 60 ) ) ).floatValue ( );
		_clockSource.setTempoInBPM ( _beatsPerMinute, PULSES_PER_QUARTER_NOTE );
	}

	public float getBPM ( )
	{
		return _beatsPerMinute;
	}

	public int getPulsesPerQuarterNote ( )
	{
		return PULSES_PER_QUARTER_NOTE;
	}

	public int getPulsesSinceStart ( )
	{
		return _clockSource.getPulsesSinceStart ( );
	}

	public float getMillisPerPulse ( )
	{
		return _millisPerPulse;
	}

	public void setPositionNotes ( PatternPositionNote [ ] positionNotes )
	{
		_positionNotes = positionNotes;
	}

	public boolean getMidiOutEnabled ( )
	{
		return _isMidiOutEnabled;
	}

	public void setMidiOutEnabled ( boolean isMidiOutEnabled )
	{
		_isMidiOutEnabled = isMidiOutEnabled;
	}

	public boolean getOscOutEnabled ( )
	{
		return _isOscOutEnabled;
	}

	public void setOscOutEnabled ( boolean isOscOutEnabled )
	{
		_isOscOutEnabled = isOscOutEnabled;
	}

	public boolean getTriggeredByMidiNoteEnabled ( )
	{
		return _isTriggeredByMidiNoteEnabled;
	}

	public void setTriggeredByMidiNoteEnabled ( MidiInNoteSource midiInNoteSource )
	{
		_isTriggeredByMidiNoteEnabled = midiInNoteSource != null;

		if ( _midiInNoteSource != null )
		{
			_midiInNoteSource.dispose ( );
		}

		_midiInNoteSource = midiInNoteSource;
	}
}
