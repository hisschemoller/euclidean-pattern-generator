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

import javax.sound.midi.MidiDevice;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.view.components.Preferences;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class PreferencesMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = PreferencesMediator.class.getName ( );

	public PreferencesMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 11 ];
		interests[ 0 ] = SeqNotifications.MIDI_DEVICES_UPDATED;
		interests[ 1 ] = SeqNotifications.MIDI_IN_DEVICE_ENABLED;
		interests[ 2 ] = SeqNotifications.MIDI_IN_DEVICE_OPENED;
		interests[ 3 ] = SeqNotifications.SYNC_TO_MIDI_IN_ENABLED_UPDATED;
		interests[ 4 ] = SeqNotifications.TRIGGER_BY_NOTE_UPDATED;
		interests[ 5 ] = SeqNotifications.MIDI_OUT_DEVICE_ENABLED;
		interests[ 6 ] = SeqNotifications.MIDI_OUT_DEVICE_OPENED;
		interests[ 7 ] = SeqNotifications.OSC_OUT_DEVICE_ENABLED;
		interests[ 8 ] = SeqNotifications.OSC_OUT_PORT_UPDATED;
		interests[ 9 ] = SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED;
		interests[ 10 ] = SeqNotifications.RELOAD_LAST_PROJECT_ENABLED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		String name = note.getName ( );
		if ( name == SeqNotifications.MIDI_DEVICES_UPDATED )
		{
			getView ( ).updateMidiDevices ( ( MidiDevice.Info[] ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_IN_DEVICE_ENABLED )
		{
			getView ( ).updateMidiInEnabled ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.SYNC_TO_MIDI_IN_ENABLED_UPDATED )
		{
			getView ( ).setMidiInClockSyncSelected ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.TRIGGER_BY_NOTE_UPDATED )
		{
			getView ( ).setTriggeredByNoteSelected ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_IN_DEVICE_OPENED )
		{
			getView ( ).setMidiInDevice ( ( MidiDevice.Info ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_OUT_DEVICE_ENABLED )
		{
			getView ( ).updateMidiOutEnabled ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_OUT_DEVICE_OPENED )
		{
			getView ( ).setMidiOutDevice ( ( MidiDevice.Info ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.OSC_OUT_DEVICE_ENABLED )
		{
			getView ( ).updateOscOutEnabled ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.OSC_OUT_PORT_UPDATED )
		{
			getView ( ).updateOscOutPort ( ( Integer ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED )
		{
			getView ( ).setDisplayMidiNoteNamesSelected ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.RELOAD_LAST_PROJECT_ENABLED )
		{
			getView ( ).setReloadLastOpenedProjectSelected ( ( Boolean ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );

		EPGSwingEngine swingEngine = ( EPGSwingEngine ) viewComponent;
		Preferences preferences = new Preferences ( swingEngine );
		preferences.addViewEventListener ( this );
		setViewComponent ( preferences );
	}

	public void viewEventHandler ( ViewEvent event )
	{
		switch ( event.getID ( ) )
		{
		case ViewEvent.MIDI_IN_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_MIDI_IN_DEVICE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.MIDI_IN_DEVICE_SELECT:
			sendNotification ( SeqNotifications.OPEN_MIDI_IN_DEVICE, getView ( ).getMidiInSelectedItem ( ) );
			break;

		case ViewEvent.MIDI_IN_CLOCK_SYNC_SELECT:
			sendNotification ( SeqNotifications.UPDATE_SYNC_TO_MIDI_IN_ENABLED, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.MIDI_IN_TRIGGER_BY_NOTE_SELECT:
			sendNotification ( SeqNotifications.UPDATE_TRIGGER_BY_NOTE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.MIDI_OUT_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_MIDI_OUT_DEVICE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.MIDI_OUT_DEVICE_SELECT:
			sendNotification ( SeqNotifications.OPEN_MIDI_OUT_DEVICE, getView ( ).getMidiOutSelectedItem ( ) );
			break;

		case ViewEvent.OSC_OUT_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_OSC_OUT_DEVICE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.OSC_OUT_PORT_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_OSC_OUT_PORT, Integer.parseInt ( ( ( JTextField ) event.getSource ( ) ).getText ( ) ) );
			break;

		case ViewEvent.USE_MIDI_NOTE_NAMES_SELECT:
			sendNotification ( SeqNotifications.ENABLE_DISPLAY_MIDI_NOTE_NAMES, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.RELOAD_PROJECT_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_RELOAD_LAST_PROJECT, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.CLOSE_PREFERENCES:
			sendNotification ( SeqNotifications.HIDE_PREFERENCES );
			break;

		default:
			System.out.println ( "PreferencesMediator - Unhandled ViewEvent with id: " + event.getID ( ) );
			break;
		}
	}

	private Preferences getView ( )
	{
		return ( Preferences ) viewComponent;
	}
}
