package com.hisschemoller.epg.controller.pattern;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.notification.SeqNotifications;

public class UpdateMidiInSettingsCommand extends SimpleCommand
{
	/**
	 * A MIDI In settings panel control changed.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		boolean updatePointer = patternVO.triggerMidiInEnabled != settingsVO.midiInTriggerEnabled;

		patternVO.triggerMidiInEnabled = settingsVO.midiInTriggerEnabled;
		patternVO.triggerMidiInChannel = settingsVO.midiInChannel;
		patternVO.triggerMidiInPitch = settingsVO.midiInPitch;

		sendNotification ( SeqNotifications.MIDI_IN_SETTINGS_UPDATED, patternVO );

		if ( updatePointer )
		{
			sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patternVO );
			sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patternVO );
		}
	}
}
