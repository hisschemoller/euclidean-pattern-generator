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

package com.hisschemoller.epg.util;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class MidiInClockSource implements IClockSource, Receiver
{
	private ISequenceable _sequencer;
	private Transmitter _transmitter;
	private int _pulsesSinceStart = 0;
	private double _timestamp = 0;
	private double _interval;

	public MidiInClockSource ( ISequenceable sequencer, Transmitter transmitter )
	{
		_sequencer = sequencer;
		_transmitter = transmitter;
		_transmitter.setReceiver ( this );
	}

	public void startClock ( )
	{
		_pulsesSinceStart = 0;
	}

	public void stopClock ( )
	{
	}

	public void dispose ( )
	{
		close ( );
	}

	public void setTempoInBPM ( float bpm, int pulsesPerQuarterNote )
	{
		// TODO Auto-generated method stub
	}

	public void setSequencer ( ISequenceable sequencer )
	{
		_sequencer = sequencer;
	}

	public int getPulsesSinceStart ( )
	{
		return _pulsesSinceStart;
	}

	public void close ( )
	{
		_transmitter.setReceiver ( null );
	}

	public void send ( MidiMessage message, long timeStamp )
	{
		if ( message instanceof ShortMessage )
		{
			ShortMessage shortMessage = ( ShortMessage ) message;
			switch ( shortMessage.getCommand ( ) )
			{
			case 0xF0:
				switch ( shortMessage.getChannel ( ) )
				{
				case 0x8:
					_sequencer.onClock ( );
					_pulsesSinceStart++;
					_interval = timeStamp - _timestamp;
					_timestamp = timeStamp;
					break;
					
				case 0x2:
					_sequencer.onSongPosition ( MidiUtils.get14bitValue ( shortMessage.getData1 ( ), shortMessage.getData2 ( ) ) );
					break;
					
				case 0xA:
					_sequencer.start ( );
					break;
					
				case 0xC:
					_sequencer.stop ( );
					break;
				}
				break;
			}
		}
	}
}
