package com.hisschemoller.sequencer.controller.midi;

import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.MidiProxy;

public class SendMidiMessageCommand extends SimpleCommand
{
	/**
	 * Send MIDI message.
	 */
	@Override public final void execute ( final INotification notification )
	{
		ShortMessage shortMessage = ( ShortMessage ) notification.getBody ( );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		midiProxy.sendShortMessage ( shortMessage );
	}
}
