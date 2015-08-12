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

package com.hisschemoller.epg.controller.pattern;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.notification.SeqNotifications;

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
