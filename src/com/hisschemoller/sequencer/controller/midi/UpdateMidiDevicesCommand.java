package com.hisschemoller.sequencer.controller.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdateMidiDevicesCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo ( );
		int n = midiDeviceInfo.length;
		for (int i = 0; i < n; i++)
		{
			try
			{
				MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo[i] );
				System.out.println ( "UpdateMidiDevicesCommand - MIDI device found, name: " + midiDeviceInfo[i].getName ( ) + ", description: " + midiDeviceInfo[i].getDescription ( ) + " (Max. MIDI IN: " + midiDevice.getMaxTransmitters ( ) + ", max. MIDI OUT: " + midiDevice.getMaxReceivers ( ) + ")" );
			}
			catch (MidiUnavailableException exception)
			{
				System.out.println ( "UpdateMidiDevicesCommand exception - MIDI device unavailable: " + exception.getMessage ( ) );
			}
		}

		MidiProxy midiProxy = (MidiProxy) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		midiProxy.setMidiDevicesInfo ( midiDeviceInfo );

		sendNotification ( SeqNotifications.MIDI_DEVICES_UPDATED, midiDeviceInfo );
	}
}
