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

package com.hisschemoller.epg.controller;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.notification.note.PatternPositionNote;

public class UpdatePositionNotesCommand extends SimpleCommand
{
	/**
	 * Recreate array with the correct length. When a pattern is created or
	 * deleted. PatternPositionNote objects contain the position within the
	 * rotation of a pattern.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector < PatternVO > patterns = sequencerProxy.getPatterns ( );

		/** Create position notes array of new length. */
		int n = patterns.size ( );
		PatternPositionNote [ ] positionNotes = new PatternPositionNote[ n ];
		for ( int i = 0; i < n; i++ )
		{
			positionNotes[ i ] = new PatternPositionNote ( );
			positionNotes[ i ].patternID = patterns.get ( i ).id;
		}

		sequencerProxy.setPositionNotes ( positionNotes );
	}
}
