package com.hisschemoller.sequencer.controller;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class UpdateResolutionCommand extends SimpleCommand
{
	/**
	 * Change sequencer resolution in PPQN.
	 */
	@Override public final void execute ( final INotification notification )
	{
		String ppqnString = notification.getBody ( ).toString ( );
		int ppqn = Integer.parseInt ( ppqnString );
		SequencerProxy sequencerProxy = (SequencerProxy) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		sequencerProxy.setPulsesPerQuarterNote ( ppqn );
		
		sendNotification ( SeqNotifications.RESOLUTION_UPDATED, ppqn );
	}
}
