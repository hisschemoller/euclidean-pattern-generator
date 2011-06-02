package com.hisschemoller.sequencer.controller;

import javax.swing.JFrame;

import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.FileProxy;
import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.view.ControlsMediator;
import com.hisschemoller.sequencer.view.MenuBarMediator;
import com.hisschemoller.sequencer.view.PatternEditorMediator;
import com.hisschemoller.sequencer.view.PatternSettingsMediator;

public class StartupCommand extends SimpleCommand implements ICommand
{
	@Override public final void execute ( INotification notification )
	{
		JFrame jFrame = (JFrame) notification.getBody ( );

		getFacade ( ).registerProxy ( new SequencerProxy ( ) );
		getFacade ( ).registerProxy ( new MidiProxy ( ) );
		getFacade ( ).registerProxy ( new FileProxy ( jFrame ) );

		getFacade ( ).registerMediator ( new MenuBarMediator ( null, jFrame ) );
		getFacade ( ).registerMediator ( new ControlsMediator ( null, jFrame ) );
		getFacade ( ).registerMediator ( new PatternEditorMediator ( null, jFrame ) );
		getFacade ( ).registerMediator ( new PatternSettingsMediator ( null, null ) );

		sendNotification ( SeqNotifications.UPDATE_MIDI_DEVICES );
		sendNotification ( SeqNotifications.NEW_PROJECT );
	}
}
