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

public class UpdateOtherSettingsCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector < PatternVO > patterns = sequencerProxy.getPatterns ( );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		if ( patternVO.mute != settingsVO.mute )
		{
			patternVO.mute = settingsVO.mute;
			sendNotification ( SeqNotifications.PATTERN_MUTED, patternVO );
			sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patternVO );
			sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patternVO );
		}

		if ( patternVO.solo != settingsVO.solo )
		{
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
					/** Un-solo all other patterns. */
					currPatternVO.solo = false;
					currPatternVO.mutedBySolo = settingsVO.solo;
				}
				
				sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, currPatternVO );
				sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, currPatternVO );
			}
			
			sendNotification ( SeqNotifications.PATTERN_SOLOED, patternVO );
		}
	}
}
