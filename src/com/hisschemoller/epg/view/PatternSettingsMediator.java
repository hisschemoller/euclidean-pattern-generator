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

package com.hisschemoller.epg.view;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.view.components.PatternSettings;
import com.hisschemoller.epg.view.components.SettingsPanels;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class PatternSettingsMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = PatternSettingsMediator.class.getName ( );

	public PatternSettingsMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 10 ];
		interests[ 0 ] = SeqNotifications.PATTERN_SETTINGS_UPDATED;
		interests[ 1 ] = SeqNotifications.MIDI_OUT_SETTINGS_UPDATED;
		interests[ 2 ] = SeqNotifications.SELECT_PATTERN;
		interests[ 3 ] = SeqNotifications.PATTERN_QUANTIZATION_UPDATED;
		interests[ 4 ] = SeqNotifications.PATTERN_SOLOED;
		interests[ 5 ] = SeqNotifications.PATTERN_MUTED;
		interests[ 6 ] = SeqNotifications.PATTERN_NAME_UPDATED;
		interests[ 7 ] = SeqNotifications.OSC_SETTINGS_UPDATED;
		interests[ 8 ] = SeqNotifications.MIDI_IN_SETTINGS_UPDATED;
		interests[ 9 ] = SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		if ( note.getName ( ) == SeqNotifications.PATTERN_SETTINGS_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.MIDI_OUT_SETTINGS_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.SELECT_PATTERN )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_QUANTIZATION_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_SOLOED )
		{
			getView ( ).soloPattern ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_MUTED )
		{
			getView ( ).mutePattern ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_NAME_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.OSC_SETTINGS_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.MIDI_IN_SETTINGS_UPDATED )
		{
			getView ( ).updateSettings ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED )
		{
			getView ( ).updateDisplayMidiNoteNamesEnabled ( ( Boolean ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );

		EPGSwingEngine swingEngine = ( EPGSwingEngine ) viewComponent;
		PatternSettings settings = new PatternSettings ( swingEngine );
		settings.addViewEventListener ( this );
		setViewComponent ( settings );

		new SettingsPanels ( swingEngine );
	}

	public void viewEventHandler ( ViewEvent event )
	{
		switch ( event.getID ( ) )
		{
		case ViewEvent.PATTERN_SETTINGS_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_PATTERN_SETTINGS, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.OSC_OUT_ADDRESS_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_OSC_SETTINGS, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.MIDI_OUT_SETTINGS_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_MIDI_OUT_SETTINGS, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.MIDI_IN_SETTINGS_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_MIDI_IN_SETTINGS, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.QUANTIZATION:
			sendNotification ( SeqNotifications.UPDATE_PATTERN_QUANTIZATION, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.DELETE_PATTERN:
			sendNotification ( SeqNotifications.DELETE_PATTERN, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.MUTE_PATTERN:
			sendNotification ( SeqNotifications.MUTE_PATTERN, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.SOLO_PATTERN:
			sendNotification ( SeqNotifications.SOLO_PATTERN, getView ( ).getSettings ( ) );
			break;

		case ViewEvent.NAME_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_PATTERN_NAME, getView ( ).getSettings ( ) );
			break;

		default:
			System.out.println ( "ControlsMediator - Unhandled ViewEvent with id: " + event.getID ( ) );
			break;
		}

	}

	private PatternSettings getView ( )
	{
		return ( PatternSettings ) viewComponent;
	}
}
