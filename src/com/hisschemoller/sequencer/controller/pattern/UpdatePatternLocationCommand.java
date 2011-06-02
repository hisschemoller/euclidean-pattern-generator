package com.hisschemoller.sequencer.controller.pattern;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdatePatternLocationCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		PatternVO tempPatternVO = ( PatternVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

		PatternVO patternVO = null;
		Boolean positionChanged = false;

		/** Search for the pattern in the selected sequence. */
		int n = patterns.size ( );
		while ( --n > -1 )
		{
			if ( tempPatternVO.id == patterns.get ( n ).id )
			{
				patternVO = patterns.get ( n );
				break;
			}
		}

		/** If the pattern wasn't found quit. */
		if ( patternVO == null )
		{
			return;
		}

		if ( tempPatternVO.viewX != patternVO.viewX )
		{
			patternVO.viewX = tempPatternVO.viewX;
			positionChanged = true;
		}

		if ( tempPatternVO.viewY != patternVO.viewY )
		{
			patternVO.viewY = tempPatternVO.viewY;
			positionChanged = true;
		}

		if ( positionChanged )
		{
			sendNotification ( SeqNotifications.PATTERN_LOCATION_UPDATED, patternVO );
		}
	}
}
