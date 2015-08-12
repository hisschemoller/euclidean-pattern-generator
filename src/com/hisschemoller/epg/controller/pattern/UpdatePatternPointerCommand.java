package com.hisschemoller.epg.controller.pattern;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.EPGEnums.PatternState;
import com.hisschemoller.epg.notification.SeqNotifications;

public class UpdatePatternPointerCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		PatternVO patternVO = ( PatternVO ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		boolean isTriggerEnabled = midiProxy.getMidiInEnabled ( ) && sequencerProxy.getTriggeredByMidiNoteEnabled ( ) && patternVO.triggerMidiInEnabled;
		
		if ( patternVO.solo )
		{
			if ( isTriggerEnabled )
			{
				patternVO.patternState = patternVO.isTriggered ? PatternState.TRIGGERED_SOLO : PatternState.UNTRIGGERED_SOLO;
			}
			else
			{
				patternVO.patternState = PatternState.SOLO;
			}
		}
		else if ( patternVO.mute || patternVO.mutedBySolo )
		{
			patternVO.patternState = PatternState.MUTE;
		}
		else
		{
			if ( isTriggerEnabled )
			{
				patternVO.patternState = patternVO.isTriggered ? PatternState.TRIGGERED : PatternState.UNTRIGGERED;
			}
			else
			{
				patternVO.patternState = PatternState.DEFAULT;
			}
		}
		
		sendNotification ( SeqNotifications.PATTERN_POINTER_UPDATED, patternVO );
	}
}
