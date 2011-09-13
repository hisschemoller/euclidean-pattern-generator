package com.hisschemoller.sequencer.controller;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.model.vo.SeqEventVO;

public class SendMidiMessageCommand extends SimpleCommand
{
	/**
	 * Send MIDI message.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SeqEventVO seqEventVO = ( SeqEventVO ) notification.getBody ( );
		ShortMessage message = new ShortMessage ( );

		try
		{
			switch ( seqEventVO.midiStatus )
			{
			case ShortMessage.NOTE_ON:
			case ShortMessage.NOTE_OFF:
				message.setMessage ( seqEventVO.midiStatus, seqEventVO.midiChannel, seqEventVO.midiPitch, seqEventVO.midiVelocity );
				MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
				midiProxy.sendShortMessage ( message );
				break;
			}
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SendMidiMessageCommand exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}
	}
}
