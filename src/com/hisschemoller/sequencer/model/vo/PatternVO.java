package com.hisschemoller.sequencer.model.vo;

import java.util.ArrayList;
import java.util.UUID;

import javax.sound.midi.MidiEvent;

public class PatternVO
{
	public UUID id;
	public ArrayList<MidiEvent> events = new ArrayList<MidiEvent> ( );

	/** quantization, length and position in PPQN. */
	public int quantization;
	public int length;
	public int position;

	/** Pattern settings */
	public int steps;
	public int fills;
	public int rotation;

	/** MIDI settings */
	public int midiChannel;
	public int midiPitch;
	public int midiVelocity;
	public int noteLength;

	/** Other settings */
	public boolean mute = false;
	public boolean solo = false;
	public boolean mutedBySolo = false;

	/** Location. */
	public int viewX;
	public int viewY;

	public String toString ( )
	{
		String returnStr = "steps: " + steps + ", fills: " + fills + ", rot: " + rotation;
		returnStr += ", x: " + viewX + ", y: " + viewY;
		returnStr += ", quant: " + quantization + ", length: " + length + ", position: " + position;
		return returnStr;
	}
}