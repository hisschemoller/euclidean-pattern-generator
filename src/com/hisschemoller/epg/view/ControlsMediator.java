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

import com.hisschemoller.epg.model.data.EPGEnums.Playback;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.view.components.Controls;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class ControlsMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = ControlsMediator.class.getName ( );

	public ControlsMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 3 ];
		interests[ 0 ] = SeqNotifications.TEMPO_UPDATED;
		interests[ 1 ] = SeqNotifications.PLAYBACK_CHANGED;
		interests[ 2 ] = SeqNotifications.ENABLE_PLAYBACK_CONTROLS;
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
			getView ( ).updatePlayButtonState ( ( Playback ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.ENABLE_PLAYBACK_CONTROLS )
		{
			getView ( ).enablePlayBackControls ( ( Boolean ) note.getBody ( ) );
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

		case ViewEvent.SHOW_PREFERENCES:
			sendNotification ( SeqNotifications.SHOW_PREFERENCES );
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
