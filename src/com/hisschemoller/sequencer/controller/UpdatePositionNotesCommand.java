package com.hisschemoller.sequencer.controller;

import java.util.Vector;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.note.PatternPositionNote;

public class UpdatePositionNotesCommand extends SimpleCommand
{
	/**
	 * Recreate array with the correct length. When a pattern is created or
	 * deleted. PatternPositionNote objects contain the position within the
	 * rotation of a pattern.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

		/** Create position notes array of new length. */
		int n = patterns.size ( );
		PatternPositionNote [ ] positionNotes = new PatternPositionNote[ n ];
		for ( int i = 0; i < n; i++ )
		{
			positionNotes[ i ] = new PatternPositionNote ( );
			positionNotes[ i ].patternID = patterns.get ( i ).id;
		}
		
		sequencerProxy.setPositionNotes ( positionNotes );
	}
}
