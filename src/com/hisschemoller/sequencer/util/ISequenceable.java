package com.hisschemoller.sequencer.util;

import javax.sound.midi.MidiEvent;

import com.hisschemoller.sequencer.model.vo.PatternVO;

public interface ISequenceable
{
	public void onPulse ( );

	public void onNoteEnd ( PatternVO patternVO, MidiEvent midiEvent );
}
