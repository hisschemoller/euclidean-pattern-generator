package com.hisschemoller.epg.controller.pattern;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.notification.SeqNotifications;

public class UpdatePatternNameCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );
		
		if ( !patternVO.name.equals ( settingsVO.name ) )
		{
			patternVO.name = settingsVO.name;
			sendNotification ( SeqNotifications.PATTERN_NAME_UPDATED, patternVO );
		}
	}
}
