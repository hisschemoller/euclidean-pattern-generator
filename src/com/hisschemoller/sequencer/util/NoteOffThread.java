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

package com.hisschemoller.sequencer.util;

import javax.sound.midi.MidiEvent;

import com.hisschemoller.sequencer.model.vo.PatternVO;

public class NoteOffThread extends Thread
{
	private ISequenceable _sequenceable;
	private PatternVO _patternVO;
	private MidiEvent _midiEvent;
	private long _sleepDuration;

	public NoteOffThread ( ISequenceable sequenceable, PatternVO patternVO, MidiEvent midiEvent, long sleepDuration )
	{
		_sequenceable = sequenceable;
		_patternVO = patternVO;
		_midiEvent = midiEvent;
		_sleepDuration = sleepDuration;
	}

	public void run ( )
	{
		try
		{
			Thread.sleep ( _sleepDuration );
			_sequenceable.onNoteEnd ( _patternVO, _midiEvent );
		}
		catch ( InterruptedException exception )
		{
			System.out.println ( "NoteOffThread: force quit..." );
		}
	}

	public PatternVO getPatternVO ( )
	{
		return _patternVO;
	}

	public MidiEvent getMidiEvent ( )
	{
		return _midiEvent;
	}
}
