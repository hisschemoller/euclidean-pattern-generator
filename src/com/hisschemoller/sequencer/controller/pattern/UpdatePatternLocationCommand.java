/**
 * Copyright 2011 Wouter Hisschemšller
 * 
 * This file is part of Euclidean Pattern Generator.
 * 
 * Euclidean Pattern Generator is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 * 
 * Euclidean Pattern Generator is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; without even the 
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Euclidean Pattern Generator.  If not, 
 * see <http://www.gnu.org/licenses/>.
 */

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
