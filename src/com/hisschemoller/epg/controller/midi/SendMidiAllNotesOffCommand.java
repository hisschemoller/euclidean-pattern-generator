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

package com.hisschemoller.epg.controller.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;

public class SendMidiAllNotesOffCommand extends SimpleCommand
{
	/**
	 * Send MIDI All Notes Off message (CC 123).
	 */
	@Override public final void execute ( final INotification notification )
	{
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		ShortMessage shortMessage = new ShortMessage ( );
		try
		{
			for ( int i = 0; i < 16; i++ )
			{
				shortMessage.setMessage ( ShortMessage.CONTROL_CHANGE, i, 123, 0 );
				midiProxy.sendShortMessage ( shortMessage );
			}
		}
		catch ( InvalidMidiDataException exception )
		{
			exception.printStackTrace ( );
		}
	}
}
