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

package com.hisschemoller.epg.model.data;

import java.util.ArrayList;
import java.util.UUID;

import javax.sound.midi.MidiEvent;

import com.hisschemoller.epg.model.data.EPGEnums.PatternState;

public class PatternVO
{
	public UUID id;
	public ArrayList<MidiEvent> events = new ArrayList<MidiEvent> ( );

	/** quantization, length and position in PPQN. */
	public int quantization;
	public int stepLength;
	public int patternLength;
	public int position;

	/** Pattern settings */
	public int steps;
	public int fills;
	public int rotation;

	/** MIDI Out settings */
	public int midiOutChannel;
	public int midiOutPitch;
	public int midiOutVelocity;
	public int noteLength;

	/** MIDI In settings */
	public boolean triggerMidiInEnabled;
	public int triggerMidiInChannel;
	public int triggerMidiInPitch;
	public boolean isTriggered;

	/** OSC settings */
	public String oscOutAddress = "";

	/** Other settings */
	public boolean mute = false;
	public boolean solo = false;
	public boolean mutedBySolo = false;
	public String name = "";

	/** Location. */
	public int viewX;
	public int viewY;
	
	/** Pointer */
	public PatternState patternState = PatternState.DEFAULT;
	
	/** True if the pattern plays and generates notes. */
	public boolean isPlaying;

	public String toString ( )
	{
		String returnStr = "steps: " + steps + ", fills: " + fills + ", rot: " + rotation;
		returnStr += ", x: " + viewX + ", y: " + viewY + ", name: " + name;
		returnStr += ", quant: " + quantization + ", stepLength: " + stepLength + ", patternLength: " + patternLength + ", position: " + position;
		return returnStr;
	}
}