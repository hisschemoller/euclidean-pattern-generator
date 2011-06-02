package com.hisschemoller.sequencer.controller.pattern;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.model.vo.SettingsVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdateMidiSettingsCommand extends SimpleCommand
{
	/**
	 * 
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		patternVO.midiChannel = settingsVO.midiChannel;
		patternVO.midiPitch = settingsVO.midiPitch;
		patternVO.midiVelocity = settingsVO.midiVelocity;
		patternVO.noteLength = settingsVO.noteLength;

		/** Update the pattern events. */
		for ( int i = 0; i < patternVO.fills; i++ )
		{
			MidiEvent midiEvent = patternVO.events.get ( i );
			ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );

			try
			{
				message.setMessage ( ShortMessage.NOTE_ON, settingsVO.midiChannel, settingsVO.midiPitch, settingsVO.midiVelocity );
			}
			catch ( InvalidMidiDataException exception )
			{
				System.out.println ( "UpdateMidiSettingsCommand.execute() InvalidMidiDataException: " + exception.getMessage ( ) );
			}
		}

		sendNotification ( SeqNotifications.MIDI_SETTINGS_UPDATED, patternVO );
	}
}
