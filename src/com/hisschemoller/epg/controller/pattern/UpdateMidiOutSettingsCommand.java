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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.notification.SeqNotifications;

public class UpdateMidiOutSettingsCommand extends SimpleCommand
{
	/**
	 * A MIDI Out settings panel control changed.
	 */
	@Override public final void execute ( final INotification notification )
	{
		SettingsVO settingsVO = ( SettingsVO ) notification.getBody ( );

		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		PatternVO patternVO = sequencerProxy.getPatternByID ( settingsVO.patternID );

		patternVO.midiOutChannel = settingsVO.midiOutChannel;
		patternVO.midiOutPitch = settingsVO.midiOutPitch;
		patternVO.midiOutVelocity = settingsVO.midiOutVelocity;
		patternVO.noteLength = settingsVO.noteLength;

		/** Update the pattern events. */
		for ( int i = 0; i < patternVO.fills; i++ )
		{
			MidiEvent midiEvent = patternVO.events.get ( i );
			ShortMessage message = ( ShortMessage ) midiEvent.getMessage ( );

			try
			{
				message.setMessage ( ShortMessage.NOTE_ON, settingsVO.midiOutChannel, settingsVO.midiOutPitch, settingsVO.midiOutVelocity );
			}
			catch ( InvalidMidiDataException exception )
			{
				System.out.println ( "UpdateMidiOutSettingsCommand.execute() InvalidMidiDataException: " + exception.getMessage ( ) );
			}
		}

		sendNotification ( SeqNotifications.MIDI_OUT_SETTINGS_UPDATED, patternVO );
	}
}
