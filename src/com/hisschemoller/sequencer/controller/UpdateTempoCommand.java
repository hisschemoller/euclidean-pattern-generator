package com.hisschemoller.sequencer.controller;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdateTempoCommand extends SimpleCommand
{
	/**
	 * Change playback speed.
	 */
	@Override public final void execute ( final INotification notification )
	{
		String bpmString = notification.getBody ( ).toString ( );
		float bpm = Float.parseFloat ( bpmString );
		SequencerProxy sequencerProxy = (SequencerProxy) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		sequencerProxy.setTempo ( bpm );
		
		sendNotification ( SeqNotifications.TEMPO_UPDATED, bpm );
	}
}
