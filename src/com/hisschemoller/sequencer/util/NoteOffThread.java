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
