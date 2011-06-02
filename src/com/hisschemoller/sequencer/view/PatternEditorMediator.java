package com.hisschemoller.sequencer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.notification.note.PatternPositionNote;
import com.hisschemoller.sequencer.notification.note.PatternSequenceNote;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.components.Pattern;
import com.hisschemoller.sequencer.view.components.PatternEditor;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class PatternEditorMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = PatternEditorMediator.class.getName ( );

	public PatternEditorMediator ( String mediatorName, Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 12 ];
		interests[ 0 ] = SeqNotifications.REDRAW;
		interests[ 2 ] = SeqNotifications.PATTERN_SEQUENCE_UPDATED;
		interests[ 1 ] = SeqNotifications.PATTERN_SETTINGS_UPDATED;
		interests[ 5 ] = SeqNotifications.PATTERN_LOCATION_UPDATED;
		interests[ 3 ] = SeqNotifications.SELECT_PATTERN;
		interests[ 4 ] = SeqNotifications.PATTERN_CREATED;
		interests[ 6 ] = SeqNotifications.PATTERN_DELETED;
		interests[ 7 ] = SeqNotifications.PATTERN_MUTED;
		interests[ 8 ] = SeqNotifications.PATTERN_SOLOED;
		interests[ 9 ] = SeqNotifications.TEMPO_UPDATED;
		interests[ 10 ] = SeqNotifications.PLAYBACK_CHANGED;
		interests[ 11 ] = SeqNotifications.ADD_PATTERN_SETTINGS;
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
		else if ( note.getName ( ) == SeqNotifications.TEMPO_UPDATED )
		{
			getView ( ).updateTempo ( ( Float ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.PLAYBACK_CHANGED )
		{
			getView ( ).updatePlayback ( ( SequencerEnums.Playback ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.ADD_PATTERN_SETTINGS )
		{
			getView ( ).addSettingsToPanel ( ( JPanel ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );

		JFrame jFrame = ( JFrame ) viewComponent;

		GridBagConstraints constraints = new GridBagConstraints ( );
		PatternEditor editor = new PatternEditor ( );
		editor.addViewEventListener ( this );
		editor.setPreferredSize ( new Dimension ( 800, 400 ) );
		editor.setMinimumSize ( new Dimension ( 400, 100 ) );
		editor.setBackground ( new Color ( 0xCCCCFF ) );
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.insets = new Insets ( 3, 6, 6, 6 );
		jFrame.add ( editor, constraints );
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
