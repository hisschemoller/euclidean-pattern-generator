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

import com.hisschemoller.epg.model.data.EPGEnums;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.notification.note.PatternPositionNote;
import com.hisschemoller.epg.notification.note.PatternSequenceNote;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.view.components.Pattern;
import com.hisschemoller.epg.view.components.PatternCanvas;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class PatternCanvasMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = PatternCanvasMediator.class.getName ( );

	public PatternCanvasMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 10 ];
		interests[ 0 ] = SeqNotifications.REDRAW;
		interests[ 1 ] = SeqNotifications.PATTERN_SEQUENCE_UPDATED;
		interests[ 2 ] = SeqNotifications.PATTERN_SETTINGS_UPDATED;
		interests[ 3 ] = SeqNotifications.PATTERN_LOCATION_UPDATED;
		interests[ 4 ] = SeqNotifications.SELECT_PATTERN;
		interests[ 5 ] = SeqNotifications.PATTERN_CREATED;
		interests[ 6 ] = SeqNotifications.PATTERN_DELETED;
		interests[ 7 ] = SeqNotifications.PATTERN_NAME_UPDATED;
		interests[ 8 ] = SeqNotifications.PLAYBACK_CHANGED;
		interests[ 9 ] = SeqNotifications.PATTERN_POINTER_UPDATED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		if ( note.getName ( ) == SeqNotifications.REDRAW )
		{
			getView ( ).updatePatternPositions ( ( PatternPositionNote [ ] ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_SEQUENCE_UPDATED )
		{
			getView ( ).updatePatternSequence ( ( PatternSequenceNote ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_SETTINGS_UPDATED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.CHANGE );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_LOCATION_UPDATED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.LOCATION );
		}
		else if ( note.getName ( ) == SeqNotifications.SELECT_PATTERN )
		{
			getView ( ).selectPattern ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_CREATED )
		{
			getView ( ).addPattern ( ( PatternVO ) note.getBody ( ), Boolean.parseBoolean ( note.getType ( ) ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_DELETED )
		{
			getView ( ).deletePattern ( ( PatternVO ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_NAME_UPDATED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.NAME );
		}
		else if ( note.getName ( ) == SeqNotifications.PLAYBACK_CHANGED )
		{
			getView ( ).updatePlayback ( ( EPGEnums.Playback ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_POINTER_UPDATED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.POINTER );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );
		
		EPGSwingEngine swingEngine = ( EPGSwingEngine ) viewComponent;
		PatternCanvas editor = new PatternCanvas ( swingEngine );
		editor.addViewEventListener ( this );
		setViewComponent ( editor );
	}

	public void viewEventHandler ( ViewEvent event )
	{
		switch ( event.getID ( ) )
		{
		case ViewEvent.PANEL_CLICK:
			sendNotification ( SeqNotifications.CREATE_PATTERN, getView ( ).getMouseClickPosition ( ) );
			break;

		case ViewEvent.PATTERN_LOCATION_CHANGE:
			sendNotification ( SeqNotifications.UPDATE_PATTERN_LOCATION, getView ( ).getPatternChangesVO ( ) );
			break;

		case ViewEvent.PATTERN_CENTER_PRESS:
			sendNotification ( SeqNotifications.OPEN_PATTERN_SETTINGS, getView ( ).getPatternUnderMouseID ( ) );
			break;
		}
	}

	private PatternCanvas getView ( )
	{
		return ( PatternCanvas ) viewComponent;
	}
}
