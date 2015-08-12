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

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;

public class UpdatePatternIsPlayingCommand extends SimpleCommand
{
	/**
	 * Determine if a pattern should play and generate notes.
	 * Sets the patternVO.isPlaying flag.
	 * 
	 * Must be called when a change happens in:
	 * (only for the pattern concerned)
	 * patternVO.mute
	 * patternVO.mutedBySolo
	 * patternVO.solo
	 * patternVO.triggerMidiInEnabled
	 * patternVO.isTriggered
	 * 
	 * (affects all patterns)
	 * midiProxy._isMidiInEnabled
	 * sequencerProxy._isTriggeredByMidiNoteEnabled
	 * 
	 * And must be called also when:
	 * A new single pattern is created.
	 * For each pattern if a project is opened.
	 * 
	 */
	@Override public final void execute ( final INotification notification )
	{
		PatternVO patternVO = ( PatternVO ) notification.getBody ( );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );

		if ( ( patternVO.mute && !patternVO.solo ) || patternVO.mutedBySolo )
		{
			patternVO.isPlaying = false;
			return;
		}

		if ( midiProxy.getMidiInEnabled ( ) )
		{
			if ( sequencerProxy.getTriggeredByMidiNoteEnabled ( ) )
			{
				if ( patternVO.triggerMidiInEnabled )
				{
					if(patternVO.isTriggered)
					{
						patternVO.isPlaying = true;
					}
					else
					{
						patternVO.isPlaying = false;
					}
				}
				else
				{
					patternVO.isPlaying = true;
				}
			}
			else
			{
				patternVO.isPlaying = true;
			}
		}
		else
		{
			patternVO.isPlaying = true;
		}
	}
}
