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
import com.hisschemoller.sequencer.util.SequencerEnums.Quantization;

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
		Vector < PatternVO > patterns = sequencerProxy.getPatterns ( );

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
		patternVO.quantization = Quantization.Q16.getValue ( );
		patternVO.stepLength = ( sequencerProxy.getPulsesPerQuarterNote ( ) * 4 ) / patternVO.quantization;
		patternVO.patternLength = patternVO.steps * patternVO.stepLength;
		patternVO.position = sequencerProxy.getPulsesSinceStart ( ) % patternVO.patternLength;
		patternVO.midiChannel = 0;
		patternVO.midiPitch = 60;
		patternVO.midiVelocity = 10;
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
				message.setMessage ( ShortMessage.NOTE_ON, patternVO.midiChannel, patternVO.midiPitch, patternVO.midiVelocity );
				MidiEvent midiEvent = new MidiEvent ( message, i * 4 * patternVO.stepLength );

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
