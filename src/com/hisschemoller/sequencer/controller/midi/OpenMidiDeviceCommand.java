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

package com.hisschemoller.sequencer.controller.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.EPGPreferences;

public class OpenMidiDeviceCommand extends SimpleCommand
{
	/**
	 * Select MIDI in or out device.
	 */
	@Override public final void execute ( final INotification notification )
	{
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		MidiDevice.Info midiDeviceInfo = getMIDIDevice ( notification, midiProxy );
		
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

				/** Close previous transmitter if it is open. */
				Transmitter transmitter = midiProxy.getTransmitter ( );
				if ( transmitter != null )
				{
					transmitter.close ( );
				}

				/** Open the new transmitter. */
				midiProxy.setTransmitter ( midiDevice.getTransmitter ( ) );

				/** Store device name in preferences. */
				EPGPreferences.put ( EPGPreferences.MIDI_IN_DEVICE, midiDevice.getDeviceInfo ( ).getName ( ) );
				
				sendNotification ( SeqNotifications.MIDI_IN_DEVICE_OPENED, midiDeviceInfo );
			}
			else if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_OUT_DEVICE )
			{
				/** Set the MIDI OUT device. */
				midiProxy.setMidiOutDevice ( midiDevice );

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
	private Info getMIDIDevice ( INotification notification, MidiProxy midiProxy )
	{
		if ( notification.getBody ( ) == null )
		{
			/**
			 * If null try to get the MIDI device from the preferences.
			 */
			String midiDeviceName = null;
			if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_IN_DEVICE )
			{
				midiDeviceName = EPGPreferences.get ( EPGPreferences.MIDI_IN_DEVICE, null );
			}
			else if ( notification.getName ( ) == SeqNotifications.OPEN_MIDI_OUT_DEVICE )
			{
				midiDeviceName = EPGPreferences.get ( EPGPreferences.MIDI_OUT_DEVICE, null );
			}
			
			if ( midiDeviceName == null )
			{
				return null;
			}
			else
			{
				MidiDevice.Info[] midiDevices = midiProxy.getMidiDevicesInfo ( );
				for ( int i = 0; i < midiDevices.length; i++ )
				{
					if ( midiDevices[ i ].getName ( ).equals ( midiDeviceName ) )
					{
						return midiDevices[ i ];
					}
				}
				return null;
			}
		}
		else
		{
			/** Return device specified in notification. */
			return ( MidiDevice.Info ) notification.getBody ( );
		}
	}
}
