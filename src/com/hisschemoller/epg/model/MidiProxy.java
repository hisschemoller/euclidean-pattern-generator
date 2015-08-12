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

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

public class MidiProxy extends Proxy
{
	public static final String NAME = MidiProxy.class.getName ( );
	private MidiDevice.Info[] _midiDevicesInfo;
	private MidiDevice.Info _midiInDeviceInfo;
	private MidiDevice.Info _midiOutDeviceInfo;
	private MidiDevice _midiInDevice;
	private MidiDevice _midiOutDevice;
	private Transmitter _midiInClockTransmitter;
	private Transmitter _midiInNoteTransmitter;
	private Receiver _midiOutReceiver;
	private boolean _isMidiInEnabled = false;
	private boolean _isSyncedToMidiInClock = false;

	public MidiProxy ( )
	{
		super ( NAME );
	}

	@Override public final void onRegister ( )
	{

	}

	@Override public final void onRemove ( )
	{
		if ( _midiInDevice != null && _midiInDevice.isOpen ( ) )
		{
			_midiInDevice.close ( );
		}

		if ( _midiOutDevice != null && _midiOutDevice.isOpen ( ) )
		{
			_midiOutDevice.close ( );
		}

		if ( _midiInClockTransmitter != null )
		{
			_midiInClockTransmitter.close ( );
		}

		if ( _midiInNoteTransmitter != null )
		{
			_midiInNoteTransmitter.close ( );
		}

		if ( _midiOutReceiver != null )
		{
			_midiOutReceiver.close ( );
		}
	}

	public void sendShortMessage ( ShortMessage message )
	{
		if ( _midiOutReceiver != null )
		{
			_midiOutReceiver.send ( message, -1 );
		}
	}

	public void setMidiDevicesInfo ( MidiDevice.Info[] midiDevices )
	{
		_midiDevicesInfo = midiDevices;
	}

	public MidiDevice.Info[] getMidiDevicesInfo ( )
	{
		return _midiDevicesInfo;
	}

	public void setMidiOutDeviceInfo ( MidiDevice.Info midiOutDeviceInfo )
	{
		_midiOutDeviceInfo = midiOutDeviceInfo;
	}

	public MidiDevice.Info getMidiOutDeviceInfo ( )
	{
		return _midiOutDeviceInfo;
	}

	public void setMidiInDeviceInfo ( MidiDevice.Info midiInDeviceInfo )
	{
		_midiInDeviceInfo = midiInDeviceInfo;
	}

	public MidiDevice.Info getMidiInDeviceInfo ( )
	{
		return _midiInDeviceInfo;
	}

	public void setMidiInDevice ( MidiDevice midiDevice )
	{
		_midiInDevice = midiDevice;
	}

	public MidiDevice getMidiInDevice ( )
	{
		return _midiInDevice;
	}

	public void setMidiOutDevice ( MidiDevice midiDevice )
	{
		_midiOutDevice = midiDevice;
	}

	public MidiDevice getMidiOutDevice ( )
	{
		return _midiOutDevice;
	}

	public void setClockTransmitter ( Transmitter transmitter )
	{
		_midiInClockTransmitter = transmitter;
	}

	public Transmitter getClockTransmitter ( )
	{
		return _midiInClockTransmitter;
	}

	public void setNoteTransmitter ( Transmitter transmitter )
	{
		_midiInNoteTransmitter = transmitter;
	}

	public Transmitter getNoteTransmitter ( )
	{
		return _midiInNoteTransmitter;
	}

	public void setReceiver ( Receiver receiver )
	{
		_midiOutReceiver = receiver;
	}

	public Receiver getReceiver ( )
	{
		return _midiOutReceiver;
	}

	public boolean getMidiInEnabled ( )
	{
		return _isMidiInEnabled;
	}

	public void setMidiInEnabled ( boolean isMidiInEnabled )
	{
		_isMidiInEnabled = isMidiInEnabled;
	}

	public boolean getSyncedToMidiInClock ( )
	{
		return _isSyncedToMidiInClock;
	}

	public void setSyncedToMidiInClock ( boolean isSyncedToMidiInClock )
	{
		_isSyncedToMidiInClock = isSyncedToMidiInClock;
	}
}
