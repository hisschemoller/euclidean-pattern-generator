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

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.notification.note.PatternPositionNote;
import com.hisschemoller.sequencer.notification.note.PatternSequenceNote;
import com.hisschemoller.sequencer.util.EPGSwingEngine;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.components.Pattern;
import com.hisschemoller.sequencer.view.components.PatternEditor;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class PatternEditorMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = PatternEditorMediator.class.getName ( );

	public PatternEditorMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 11 ];
		interests[ 0 ] = SeqNotifications.REDRAW;
		interests[ 2 ] = SeqNotifications.PATTERN_SEQUENCE_UPDATED;
		interests[ 1 ] = SeqNotifications.PATTERN_SETTINGS_UPDATED;
		interests[ 5 ] = SeqNotifications.PATTERN_LOCATION_UPDATED;
		interests[ 3 ] = SeqNotifications.SELECT_PATTERN;
		interests[ 4 ] = SeqNotifications.PATTERN_CREATED;
		interests[ 6 ] = SeqNotifications.PATTERN_DELETED;
		interests[ 7 ] = SeqNotifications.PATTERN_MUTED;
		interests[ 8 ] = SeqNotifications.PATTERN_SOLOED;
		interests[ 9 ] = SeqNotifications.PATTERN_NAME_UPDATED;
		interests[ 10 ] = SeqNotifications.PLAYBACK_CHANGED;
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
		else if ( note.getName ( ) == SeqNotifications.PATTERN_MUTED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.MUTE );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_SOLOED )
		{
			getView ( ).updateAllPatterns ( ( PatternVO ) note.getBody ( ), Pattern.Operation.SOLO );
		}
		else if ( note.getName ( ) == SeqNotifications.PATTERN_NAME_UPDATED )
		{
			getView ( ).updatePattern ( ( PatternVO ) note.getBody ( ), Pattern.Operation.NAME );
		}
		else if ( note.getName ( ) == SeqNotifications.PLAYBACK_CHANGED )
		{
			getView ( ).updatePlayback ( ( SequencerEnums.Playback ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );
		
		EPGSwingEngine swingEngine = ( EPGSwingEngine ) viewComponent;
		PatternEditor editor = new PatternEditor ( swingEngine );
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

	private PatternEditor getView ( )
	{
		return ( PatternEditor ) viewComponent;
	}
}
