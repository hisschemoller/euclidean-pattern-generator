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

package com.hisschemoller.epg.controller.midi;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.PreferencesProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.EPGEnums.ClockSourceType;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;

public class EnableMidiCommand extends SimpleCommand
{
	/**
	 * Enable MIDI in or out.
	 */
	@Override public final void execute ( final INotification notification )
	{
		boolean isEnabled = ( Boolean ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		PreferencesProxy preferencesProxy = ( PreferencesProxy ) getFacade ( ).retrieveProxy ( PreferencesProxy.NAME );
		
		if ( notification.getName ( ) == SeqNotifications.ENABLE_MIDI_IN_DEVICE && midiProxy.getMidiInEnabled ( ) != isEnabled )
		{
			midiProxy.setMidiInEnabled ( isEnabled );
			sendNotification ( SeqNotifications.MIDI_IN_DEVICE_ENABLED, isEnabled );
			EPGPreferences.putBoolean ( EPGPreferences.MIDI_IN_ENABLED, isEnabled );
			
			// Switch on MIDI In while external clock is selected: disable play controls and switch to external clock.
			if ( isEnabled && preferencesProxy.getIsSyncToMidiInClockEnabled ( ) )
			{ 
				// sendNotification ( SeqNotifications.ENABLE_PLAYBACK_CONTROLS, false );
				sendNotification ( SeqNotifications.UPDATE_CLOCK_SOURCE, ClockSourceType.MIDI_CLOCK_IN );
			}
			
			// Switch off MIDI In while external clock is selected: enable play controls and switch to internal clock.
			if ( !isEnabled && preferencesProxy.getIsSyncToMidiInClockEnabled ( ) )
			{
				// sendNotification ( SeqNotifications.ENABLE_PLAYBACK_CONTROLS, true );
				sendNotification ( SeqNotifications.UPDATE_CLOCK_SOURCE, ClockSourceType.INTERNAL );
			}
			
			// Let all patterns determine if they should play. 
			Vector < PatternVO > patterns = sequencerProxy.getPatterns ( );
			int n = patterns.size ( );
			while ( --n > -1 )
			{
				sendNotification ( SeqNotifications.UPDATE_PATTERN_IS_PLAYING, patterns.get ( n ) );
				sendNotification ( SeqNotifications.UPDATE_PATTERN_POINTER, patterns.get ( n ) );
			}
		}
		
		if ( notification.getName ( ) == SeqNotifications.ENABLE_MIDI_OUT_DEVICE && sequencerProxy.getMidiOutEnabled ( ) != isEnabled )
		{
			sequencerProxy.setMidiOutEnabled ( isEnabled );
			sendNotification ( SeqNotifications.MIDI_OUT_DEVICE_ENABLED, isEnabled );
			EPGPreferences.putBoolean ( EPGPreferences.MIDI_OUT_ENABLED, isEnabled );
		}
	}
}
