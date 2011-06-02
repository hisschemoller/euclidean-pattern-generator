package com.hisschemoller.sequencer.controller.pattern;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.model.vo.SettingsVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdateOtherSettingsCommand extends SimpleCommand
{
	/**
	 * 
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		if ( patternVO.mute != settingsVO.mute )
		{
			patternVO.mute = settingsVO.mute;
			sendNotification ( SeqNotifications.PATTERN_MUTED, patternVO );
		}

		if ( patternVO.solo != settingsVO.solo )
		{
			/** Un-solo all patterns. */
			int n = patterns.size ( );
			while ( --n > -1 )
			{
				PatternVO currPatternVO = patterns.get ( n );
				if ( currPatternVO == patternVO )
				{
					currPatternVO.solo = settingsVO.solo;
					currPatternVO.mutedBySolo = false;
				}
				else
				{
					currPatternVO.solo = false;
					currPatternVO.mutedBySolo = settingsVO.solo;
				}
			}

			sendNotification ( SeqNotifications.PATTERN_SOLOED, patternVO );
		}
	}
}
