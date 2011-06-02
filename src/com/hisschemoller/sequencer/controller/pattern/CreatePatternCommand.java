package com.hisschemoller.sequencer.controller.pattern;

import java.awt.Point;
import java.util.UUID;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class CreatePatternCommand extends SimpleCommand
{
	/**
	 * Create a new Euclid Pattern.
	 */
	@Override public final void execute ( final INotification notification )
	{
		Point point = ( Point ) notification.getBody ( );

		if ( point == null )
		{
			return;
		}

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

		/** Check for solo. */
		boolean mutedBySolo = false;
		int n = patterns.size ( );
		while ( --n > -1 )
		{
			if ( patterns.get ( n ).solo )
			{
				mutedBySolo = true;
				break;
			}
		}

		PatternVO patternVO = new PatternVO ( );
		patternVO.id = UUID.randomUUID ( );
		patternVO.steps = 16;
		patternVO.fills = 4;
		patternVO.rotation = 0;
		patternVO.quantization = sequencerProxy.getPulsesPerQuarterNote ( ) / 4;
		patternVO.length = patternVO.steps * patternVO.quantization;
		patternVO.position = sequencerProxy.getPulsesSinceStart ( ) % patternVO.length;
		patternVO.midiChannel = 0;
		patternVO.midiPitch = 60;
		patternVO.midiVelocity = 100;
		patternVO.noteLength = sequencerProxy.getPulsesPerQuarterNote ( ) / 4;
		patternVO.mutedBySolo = mutedBySolo;
		patternVO.viewX = point.x;
		patternVO.viewY = point.y;

		/** Add four events to the pattern. */
		for ( int i = 0; i < patternVO.fills; i++ )
		{
			try
			{
				ShortMessage message = new ShortMessage ( );
				message.setMessage ( ShortMessage.NOTE_ON, 0, 60, 100 );
				MidiEvent midiEvent = new MidiEvent ( message, i * 4 * patternVO.quantization );

				patternVO.events.add ( midiEvent );
			}
			catch ( InvalidMidiDataException exception )
			{
				System.out.println ( "CreatePatternCommand.execute() InvalidMidiDataException: " + exception.getMessage ( ) );
			}
		}

		/** Add pattern to list. */
		patterns.add ( patternVO );
		
		/** Create position notes array of new length. */
		sendNotification ( SeqNotifications.UPDATE_POSITION_NOTES );

		/** Send notification for editor to draw pattern. */
		sendNotification ( SeqNotifications.PATTERN_CREATED, patternVO, Boolean.toString ( true ) );
		sendNotification ( SeqNotifications.SELECT_PATTERN, patternVO );
	}
}
