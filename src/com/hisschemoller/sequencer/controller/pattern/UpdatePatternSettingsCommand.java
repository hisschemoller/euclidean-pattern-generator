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

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.model.vo.SettingsVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.BjorklundGenerator2;

public class UpdatePatternSettingsCommand extends SimpleCommand
{
	/**
	 * 
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );
		Boolean patternChanged = false;

		/** Regenerate the pattern if steps or fills changed. */
		if ( settingsVO.steps != 0 && settingsVO.fills != 0 )
		{
			/** Check if the values have actually changed. */
			if ( settingsVO.steps != patternVO.steps || settingsVO.fills != patternVO.fills )
			{
				patternChanged = true;

				/** Never more fills then there are steps */
				settingsVO.fills = Math.min ( settingsVO.fills, settingsVO.steps );

				/** Adjust length (measured in PPQN). */
				patternVO.length = settingsVO.steps * patternVO.quantization;

				/** Generate the Euclid / Bjorklund pattern. */
				ArrayList<Boolean> pattern = BjorklundGenerator2.generate ( settingsVO.steps, settingsVO.fills );

				/** Take first event in patternVO as 'blueprint'. */
				MidiEvent sourceMidiEvent = patternVO.events.get ( 0 );

				/** Clear all events. */
				patternVO.events = new ArrayList<MidiEvent> ( );

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
							MidiEvent midiEvent = new MidiEvent ( message, i * patternVO.quantization );
							patternVO.events.add ( midiEvent );
						}
					}
				}
				catch ( InvalidMidiDataException exception )
				{
					System.out.println ( "UpdatePatternSettingsCommand.execute() InvalidMidiDataException: " + exception.getMessage ( ) );
				}

				/** Update pattern with values from the settings pattern. */
				if ( settingsVO.steps != 0 )
				{
					patternVO.steps = settingsVO.steps;
					patternChanged = true;
				}

				if ( settingsVO.fills != 0 )
				{
					patternVO.fills = settingsVO.fills;
					patternChanged = true;
				}
			}
		}

		/** Check for rotation change. */
		if ( settingsVO.rotation != patternVO.rotation )
		{
			patternVO.rotation = Math.min ( settingsVO.rotation, patternVO.steps - 1 );
			patternChanged = true;
		}

		if ( patternChanged )
		{
			sendNotification ( SeqNotifications.PATTERN_SETTINGS_UPDATED, patternVO );
		}
	}
}
