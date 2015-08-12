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

package com.hisschemoller.epg.controller.preferences;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.PreferencesProxy;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;

public class DisplayMidiNoteNamesCommand extends SimpleCommand
{
	/**
	 * Set if note names like C3 or C#4 should be used for MIDI pitch values.
	 * This is set by a checkbox in the preferences panel.
	 * It affects the values next to the MIDI pitch sliders in the settings
	 * panels at the right of the application.
	 */
	@Override public final void execute ( final INotification notification )
	{
		boolean isEnabled = ( Boolean ) notification.getBody ( );

		PreferencesProxy preferencesProxy = ( PreferencesProxy ) getFacade ( ).retrieveProxy ( PreferencesProxy.NAME );
		preferencesProxy.setDisplayMidiNoteNamesEnabled ( isEnabled );
		
		EPGPreferences.putBoolean ( EPGPreferences.DISPLAY_MIDI_NOTE_NAMES, isEnabled );
		
		sendNotification ( SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED, isEnabled );
	}
}
