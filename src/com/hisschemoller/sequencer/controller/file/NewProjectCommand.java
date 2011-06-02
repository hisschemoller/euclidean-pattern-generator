package com.hisschemoller.sequencer.controller.file;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.FileProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class NewProjectCommand extends SimpleCommand
{
	/**
	 * Start new project.
	 */
	@Override public final void execute ( final INotification notification )
	{
		/** Clear all old data from the sequencer. */
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		sequencerProxy.clear ( );
		
		/** Clear file reference. */
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );
		fileProxy.setFile ( null );
		
		/** Reset tempo and resolution. */
		sendNotification ( SeqNotifications.UPDATE_RESOLUTION, 24 );
		sendNotification ( SeqNotifications.UPDATE_TEMPO, 120 );
		
		/** No pattern selected. */
		sendNotification ( SeqNotifications.SELECT_PATTERN, null );
	}
}
