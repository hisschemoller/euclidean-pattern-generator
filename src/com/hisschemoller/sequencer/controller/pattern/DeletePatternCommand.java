package com.hisschemoller.sequencer.controller.pattern;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.model.vo.SettingsVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class DeletePatternCommand extends SimpleCommand
{
	/**
	 * Delete a pattern.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

		int n = patterns.size ( );
		while ( --n > -1 )
		{
			PatternVO patternVO = patterns.get ( n );
			if ( patternVO.id == settingsVO.patternID )
			{
				/** Delete the pattern. */
				PatternVO removedPatternVO = patterns.remove ( n );
				sendNotification ( SeqNotifications.PATTERN_DELETED, removedPatternVO );

				/** Select the next pattern. */
				PatternVO patternToSelect;
				if ( patterns.size ( ) > 0 )
				{
					patternToSelect = patterns.get ( ( n > 0 ) ? n - 1 : 0 );
				}
				else
				{
					patternToSelect = null;
				}
				
				/** Select pattern. */
				sendNotification ( SeqNotifications.SELECT_PATTERN, patternToSelect );
				
				/** Create position notes array of new length. */
				sendNotification ( SeqNotifications.UPDATE_POSITION_NOTES );
				
				return;
			}
		}

		System.out.println ( "Failed to delete pattern. Pattern not found in sequence." );
	}
}
