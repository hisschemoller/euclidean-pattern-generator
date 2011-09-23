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

package com.hisschemoller.sequencer.view;

import javax.sound.midi.MidiDevice;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.EPGSwingEngine;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.components.Controls;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class ControlsMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = ControlsMediator.class.getName ( );

	public ControlsMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 8 ];
		interests[ 0 ] = SeqNotifications.TEMPO_UPDATED;
		interests[ 1 ] = SeqNotifications.PLAYBACK_CHANGED;
		interests[ 2 ] = SeqNotifications.RESOLUTION_UPDATED;
		interests[ 3 ] = SeqNotifications.MIDI_DEVICES_UPDATED;
		interests[ 4 ] = SeqNotifications.MIDI_OUT_DEVICE_OPENED;
		interests[ 5 ] = SeqNotifications.MIDI_OUT_DEVICE_ENABLED;
		interests[ 6 ] = SeqNotifications.OSC_DEVICE_ENABLED;
		interests[ 7 ] = SeqNotifications.OSC_PORT_UPDATED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		String name = note.getName ( );
		if ( name == SeqNotifications.TEMPO_UPDATED )
		{
			getView ( ).updateTempo ( ( Float ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.PLAYBACK_CHANGED )
		{
			getView ( ).updatePlayButtonState ( ( SequencerEnums.Playback ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.RESOLUTION_UPDATED )
		{
			getView ( ).updateResolution ( ( Integer ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_DEVICES_UPDATED )
		{
			getView ( ).updateMidiDevices ( ( MidiDevice.Info[] ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_OUT_DEVICE_OPENED )
		{
			getView ( ).setMidiOutDevice ( ( MidiDevice.Info ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.MIDI_OUT_DEVICE_ENABLED )
		{
			getView ( ).updateMidiOutEnabled ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.OSC_DEVICE_ENABLED )
		{
			getView ( ).updateOscEnabled ( ( Boolean ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.OSC_PORT_UPDATED )
		{
			getView ( ).updateOscPort ( ( Integer ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );

		EPGSwingEngine swingEngine = ( EPGSwingEngine ) viewComponent;
		Controls controls = new Controls ( swingEngine );
		controls.addViewEventListener ( this );
		setViewComponent ( controls );
	}

	public void viewEventHandler ( ViewEvent event )
	{
		switch ( event.getID ( ) )
		{
		case ViewEvent.TEMPO_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_TEMPO, getView ( ).getTempoValue ( ) );
			break;

		case ViewEvent.PLAYBACK_CHANGE:
			sendNotification ( SeqNotifications.CHANGE_PLAYBACK, getView ( ).getPlayButtonState ( ) );
			break;

		case ViewEvent.ALL_NOTES_OFF:
			sendNotification ( SeqNotifications.SEND_MIDI_ALL_NOTES_OFF );
			break;

		// case ViewEvent.MIDI_IN_DEVICE_SELECT:
		// sendNotification ( SeqNotifications.OPEN_MIDI_IN_DEVICE, getView (
		// ).getMidiInSelectedItem ( ) );
		// break;

		case ViewEvent.MIDI_OUT_DEVICE_SELECT:
			sendNotification ( SeqNotifications.OPEN_MIDI_OUT_DEVICE, getView ( ).getMidiOutSelectedItem ( ) );
			break;

		case ViewEvent.MIDI_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_MIDI_OUT_DEVICE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.OSC_CHECKBOX_SELECT:
			sendNotification ( SeqNotifications.ENABLE_OSC_DEVICE, ( ( JCheckBox ) event.getSource ( ) ).isSelected ( ) );
			break;

		case ViewEvent.OSC_PORT_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_OSC_PORT, Integer.parseInt ( ( ( JTextField ) event.getSource ( ) ).getText ( ) ) );
			break;

		default:
			System.out.println ( "ControlsMediator - Unhandled ViewEvent with id: " + event.getID ( ) );
			break;
		}
	}

	private Controls getView ( )
	{
		return ( Controls ) viewComponent;
	}
}
