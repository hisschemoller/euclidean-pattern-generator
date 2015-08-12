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
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;

public class OpenMidiDeviceCommand extends SimpleCommand
{
	/**
	 * Select MIDI in or out device.
	 */
	@Override public final void execute ( final INotification notification )
	{
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		MidiDevice.Info midiDeviceInfo = getMIDIDeviceInfo ( notification, midiProxy );

		if ( midiDeviceInfo == null )
		{
			return;
		}

		try
		{
			/** Get the MIDI device. */
			MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo );
			System.out.println ( "OpenMidiDeviceCommand - MIDI device: " + midiDeviceInfo.getDescription ( ) + ", notification: " + notification.getName ( ) );

			if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_IN_DEVICE )
			{
				/** Set the MIDI IN device. */
				midiProxy.setMidiInDevice ( midiDevice );
				midiProxy.setMidiInDeviceInfo ( midiDeviceInfo );

				/** Close previous transmitter if it is open. */
				Transmitter transmitter = midiProxy.getClockTransmitter ( );
				if ( transmitter != null )
				{
					transmitter.close ( );
				}
				transmitter = midiProxy.getNoteTransmitter ( );
				if ( transmitter != null )
				{
					transmitter.close ( );
				}

				/** Open the new transmitter. */
				midiProxy.setClockTransmitter ( midiDevice.getTransmitter ( ) );
				midiProxy.setNoteTransmitter ( midiDevice.getTransmitter ( ) );

				/** Store device name in preferences. */
				EPGPreferences.put ( EPGPreferences.MIDI_IN_DEVICE, midiDevice.getDeviceInfo ( ).getName ( ) );

				sendNotification ( SeqNotifications.MIDI_IN_DEVICE_OPENED, midiDeviceInfo );
			}
			else if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_OUT_DEVICE )
			{
				/** Set the MIDI OUT device. */
				midiProxy.setMidiOutDevice ( midiDevice );
				midiProxy.setMidiOutDeviceInfo ( midiDeviceInfo );

				/** Close previous receiver if it is open. */
				Receiver receiver = midiProxy.getReceiver ( );
				if ( receiver != null )
				{
					receiver.close ( );
				}

				/** Open the new receiver. */
				midiProxy.setReceiver ( midiDevice.getReceiver ( ) );

				/** Store device name in preferences. */
				EPGPreferences.put ( EPGPreferences.MIDI_OUT_DEVICE, midiDevice.getDeviceInfo ( ).getName ( ) );

				sendNotification ( SeqNotifications.MIDI_OUT_DEVICE_OPENED, midiDeviceInfo );
			}

			/** Open the device. */
			if ( !midiDevice.isOpen ( ) )
			{
				try
				{
					midiDevice.open ( );
				}
				catch ( MidiUnavailableException exception )
				{
					System.out.println ( "OpenMidiDeviceCommand exception - MIDI device can't be opened: " + exception.getMessage ( ) );
				}
			}
		}
		catch ( MidiUnavailableException exception )
		{
			System.out.println ( "OpenMidiDeviceCommand exception - MIDI device unavailable: " + exception.getMessage ( ) );
		}
	}

	/**
	 * Get MIDI device either from notification or preferences.
	 */
	private MidiDevice.Info getMIDIDeviceInfo ( INotification notification, MidiProxy midiProxy )
	{
		if ( notification.getBody ( ) == null )
		{
			return null;
		}
		else if ( notification.getBody ( ) instanceof MidiDevice.Info )
		{
			// MidiDevice.Info when a MIDI port is chosen in the preferences window MIDI combobox.
			return ( MidiDevice.Info ) notification.getBody ( );
		}
		else if ( notification.getBody ( ) instanceof String )
		{
			// String when MIDI port name is retrieved from system preferences (StartupCommand).
			try
			{
				String midiDeviceName = ( String ) notification.getBody ( );
				MidiDevice.Info[] midiDevicesInfo = midiProxy.getMidiDevicesInfo ( );
				for ( int i = 0; i < midiDevicesInfo.length; i++ )
				{
					if ( midiDevicesInfo[ i ].getName ( ).equals ( midiDeviceName ) )
					{
						MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDevicesInfo[ i ] );

						if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_IN_DEVICE && midiDevice.getMaxTransmitters ( ) != 0 )
						{
							return midiDevicesInfo[ i ];
						}

						if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_OUT_DEVICE && midiDevice.getMaxReceivers ( ) != 0 )
						{
							return midiDevicesInfo[ i ];
						}
					}
				}
			}
			catch ( MidiUnavailableException exception )
			{
				System.out.println ( "OpenMidiDeviceCommand exception - MIDI device unavailable: " + exception.getMessage ( ) );
				return null;
			}
		}

		return null;
	}
}
