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

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;
import com.hisschemoller.epg.util.MidiInNoteSource;

public class UpdateTriggerByNoteCommand extends SimpleCommand
{
	/**
	 * Enable or disable patterns being triggered by an incoming MIDI note.
	 * Switched on or off by a checkbox in the preferences panel.
	 */
	@Override public final void execute ( INotification notification )
	{
		boolean isSelected = ( Boolean ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		
		if ( isSelected != sequencerProxy.getTriggeredByMidiNoteEnabled ( ) )
		{
			MidiInNoteSource midiInNoteSource = null;
			if ( isSelected )
			{
				MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
				midiInNoteSource = new MidiInNoteSource ( sequencerProxy, midiProxy.getNoteTransmitter ( ) );
			}
			sequencerProxy.setTriggeredByMidiNoteEnabled ( midiInNoteSource );

			EPGPreferences.putBoolean ( EPGPreferences.TRIGGERED_BY_MIDI_NOTE, isSelected );
			sendNotification ( SeqNotifications.TRIGGER_BY_NOTE_UPDATED, isSelected );
			
			/** Update all pattern graphics. */
			Vector < PatternVO > patterns = sequencerProxy.getPatterns ( );
			int n = patterns.size ( );
			while ( --n > -1 )
			{
				sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patterns.get ( n ) );
				sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patterns.get ( n ) );
			}
		}
	}
}
