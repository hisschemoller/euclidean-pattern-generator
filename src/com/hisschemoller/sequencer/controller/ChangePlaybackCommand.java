package com.hisschemoller.sequencer.controller;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.SequencerEnums;

public class ChangePlaybackCommand extends SimpleCommand
{
	/**
	 * Start or stop the sequencer.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SequencerEnums.Playback playback = (SequencerEnums.Playback) notification.getBody ( );
		SequencerProxy sequencerProxy = (SequencerProxy) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );

		if ( playback == SequencerEnums.Playback.START )
		{
			sequencerProxy.start ( );
		}
		else if ( playback == SequencerEnums.Playback.STOP )
		{
			sequencerProxy.stop ( );
		}

		sendNotification ( SeqNotifications.PLAYBACK_CHANGED, playback );
	}
}
