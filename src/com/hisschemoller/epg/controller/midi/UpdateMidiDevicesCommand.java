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

package com.hisschemoller.epg.controller.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.notification.SeqNotifications;

public class UpdateMidiDevicesCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo ( );
		int n = midiDeviceInfo.length;
		for ( int i = 0; i < n; i++ )
		{
			try
			{
				MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo[ i ] );
				System.out.println ( "UpdateMidiDevicesCommand - MIDI device found, name: " + midiDeviceInfo[ i ].getName ( ) + ", description: " + midiDeviceInfo[ i ].getDescription ( ) + " (Max. MIDI IN: " + midiDevice.getMaxTransmitters ( ) + ", max. MIDI OUT: " + midiDevice.getMaxReceivers ( ) + ")" );
			}
			catch ( MidiUnavailableException exception )
			{
				System.out.println ( "UpdateMidiDevicesCommand exception - MIDI device unavailable: " + exception.getMessage ( ) );
			}
		}

		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		midiProxy.setMidiDevicesInfo ( midiDeviceInfo );

		sendNotification ( SeqNotifications.MIDI_DEVICES_UPDATED, midiDeviceInfo );
	}
}
