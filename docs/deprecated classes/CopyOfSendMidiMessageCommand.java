package com.hisschemoller.sequencer.controller;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.SeqEventVO;

public class CopyOfSendMidiMessageCommand extends SimpleCommand
{
	/**
	 * Send MIDI message.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SeqEventVO seqEventVO = ( SeqEventVO ) notification.getBody ( );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );

		if ( seqEventVO.midiStatus == ShortMessage.NOTE_ON || seqEventVO.midiStatus == ShortMessage.NOTE_OFF )
		{
			SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
			boolean [ ][ ] noteOnlist = sequencerProxy.getNoteOnList ( );

			if ( seqEventVO.midiStatus == ShortMessage.NOTE_ON )
			{
				/** This note already plays, so first send a NOTE_OFF. */
				if ( noteOnlist[ seqEventVO.midiChannel ][ seqEventVO.midiPitch ] == true )
				{
					System.out.println ( "yah" );
					SeqEventVO noteOffSeqEventVO = seqEventVO.clone ( );
					noteOffSeqEventVO.midiStatus = ShortMessage.NOTE_OFF;
					sendMidiMessage ( midiProxy, noteOffSeqEventVO );
				}

				noteOnlist[ seqEventVO.midiChannel ][ seqEventVO.midiPitch ] = true;
				sendMidiMessage ( midiProxy, seqEventVO );
			}
			else
			{
				noteOnlist[ seqEventVO.midiChannel ][ seqEventVO.midiPitch ] = false;
				sendMidiMessage ( midiProxy, seqEventVO );
			}
		}
	}

	private void sendMidiMessage ( MidiProxy midiProxy, SeqEventVO seqEventVO )
	{
		try
		{
			System.out.println ( seqEventVO.midiStatus );
			ShortMessage message = new ShortMessage ( );
			message.setMessage ( seqEventVO.midiStatus, seqEventVO.midiChannel, seqEventVO.midiPitch, seqEventVO.midiVelocity );
			midiProxy.sendShortMessage ( message );
		}
		catch ( InvalidMidiDataException exception )
		{
			System.out.println ( "SendMidiMessageCommand exception - Invalid MIDI data: " + exception.getMessage ( ) );
		}
	}
}
