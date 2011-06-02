package com.hisschemoller.sequencer.controller;

import java.util.UUID;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class OpenPatternSettingsCommand extends SimpleCommand
{
	@Override public final void execute ( INotification notification )
	{
		UUID patternID = (UUID) notification.getBody ( );
		SequencerProxy sequencerProxy = (SequencerProxy) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( patternID );
		
		sendNotification ( SeqNotifications.PATTERN_SETTINGS_OPENED, patternVO );
		sendNotification ( SeqNotifications.SELECT_PATTERN, patternVO );
	}
}
