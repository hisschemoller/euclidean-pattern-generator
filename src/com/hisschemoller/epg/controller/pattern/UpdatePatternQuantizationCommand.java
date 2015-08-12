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

package com.hisschemoller.epg.controller.pattern;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.model.data.EPGEnums.Quantization;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.BjorklundGenerator3;

public class UpdatePatternQuantizationCommand extends SimpleCommand
{
	/**
	 * Change pattern quantization (playback speed).
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		if ( patternVO.quantization != settingsVO.quantization )
		{
			Quantization quantization = Quantization.get ( settingsVO.quantization );

			float oldLength = patternVO.patternLength;

			patternVO.quantization = ( quantization != null ) ? quantization.getValue ( ) : Quantization.Q16.getValue ( );
			patternVO.stepLength = ( sequencerProxy.getPulsesPerQuarterNote ( ) * 4 ) / patternVO.quantization;
			patternVO.patternLength = patternVO.stepLength * patternVO.steps;
			patternVO.position = ( int ) ( ( patternVO.position / oldLength ) * patternVO.patternLength );

			/** Generate the Euclid / Bjorklund pattern. */
			ArrayList < Boolean > pattern = BjorklundGenerator3.generate ( settingsVO.steps, settingsVO.fills );

			/** Take first event in patternVO as 'blueprint'. */
			MidiEvent sourceMidiEvent = patternVO.events.get ( 0 );

			/** Clear all events. */
			patternVO.events = new ArrayList < MidiEvent > ( );

			/** Loop through the new euclid pattern */
			try
			{
				int n = pattern.size ( );
				for ( int i = 0; i < n; i++ )
				{
					if ( pattern.get ( i ) == true )
					{
						ShortMessage sourceMessage = ( ShortMessage ) sourceMidiEvent.getMessage ( );
						ShortMessage message = new ShortMessage ( );
						message.setMessage ( sourceMessage.getStatus ( ), sourceMessage.getChannel ( ), sourceMessage.getData1 ( ), sourceMessage.getData2 ( ) );
						MidiEvent midiEvent = new MidiEvent ( message, i * patternVO.stepLength );
						patternVO.events.add ( midiEvent );
					}
				}
			}
			catch ( InvalidMidiDataException exception )
			{
				exception.printStackTrace ( );
			}

			sendNotification ( SeqNotifications.PATTERN_QUANTIZATION_UPDATED, patternVO );
		}
	}
}
