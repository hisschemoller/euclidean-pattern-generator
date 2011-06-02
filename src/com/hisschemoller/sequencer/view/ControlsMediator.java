package com.hisschemoller.sequencer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.sound.midi.MidiDevice;
import javax.swing.JFrame;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.components.Controls;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class ControlsMediator extends Mediator implements IViewEventListener
{
	public static final String NAME = "ControlsMediator";

	public ControlsMediator ( String mediatorName, Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 4 ];
		interests[ 0 ] = SeqNotifications.MIDI_DEVICES_UPDATED;
		interests[ 1 ] = SeqNotifications.TEMPO_UPDATED;
		interests[ 2 ] = SeqNotifications.PLAYBACK_CHANGED;
		interests[ 3 ] = SeqNotifications.RESOLUTION_UPDATED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		String name = note.getName ( );
		if ( name == SeqNotifications.MIDI_DEVICES_UPDATED )
		{
			getView ( ).updateMidiDevices ( ( MidiDevice.Info[] ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.TEMPO_UPDATED )
		{
			getView ( ).updateTempo ( ( Float ) note.getBody ( ) );
		}
		else if ( name == SeqNotifications.PLAYBACK_CHANGED )
		{
			getView ( ).updatePlayButtonState ( ( SequencerEnums.Playback ) note.getBody ( ) );
		}
		else if ( note.getName ( ) == SeqNotifications.RESOLUTION_UPDATED )
		{
			getView ( ).updateResolution ( (Integer) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );

		JFrame jFrame = ( JFrame ) viewComponent;

		GridBagConstraints constraints = new GridBagConstraints ( );
		Controls controls = new Controls ( );
		controls.addViewEventListener ( this );
		controls.setBackground ( new Color ( 0xFFCCCC ) );
		controls.setPreferredSize ( new Dimension ( 500, 100 ) );
		controls.setMinimumSize ( new Dimension ( 500, 100 ) );
		controls.setMaximumSize ( new Dimension ( 500, 100 ) );
		// constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets ( 6, 6, 3, 3 );
		jFrame.add ( controls, constraints );
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

		case ViewEvent.MIDI_IN_DEVICE_SELECT:
			sendNotification ( SeqNotifications.OPEN_MIDI_IN_DEVICE, getView ( ).getMidiInSelectedItem ( ) );
			break;

		case ViewEvent.MIDI_OUT_DEVICE_SELECT:
			sendNotification ( SeqNotifications.OPEN_MIDI_OUT_DEVICE, getView ( ).getMidiOutSelectedItem ( ) );
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
