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

package com.hisschemoller.epg.controller;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.data.EPGEnums;
import com.hisschemoller.epg.model.data.EPGEnums.ClockSourceType;
import com.hisschemoller.epg.model.data.EPGEnums.Playback;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.IClockSource;
import com.hisschemoller.epg.util.InternalClockThread;
import com.hisschemoller.epg.util.MidiInClockSource;

public class UpdateClockSourceCommand extends SimpleCommand
{
	/**
	 * Preferences Sync to MIDI clock checkbox selected or deselected.
	 * Switch between internal clock and MIDI in clock.
	 */
	@Override public final void execute ( INotification notification )
	{
		EPGEnums.ClockSourceType clockSourceType = ( EPGEnums.ClockSourceType ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
		IClockSource clockSource = null;
		
		if ( sequencerProxy.getClockSourceType ( ) == null || sequencerProxy.getClockSourceType ( ) != clockSourceType )
		{
			switch ( clockSourceType )
			{
			case INTERNAL:
				clockSource = new InternalClockThread ( sequencerProxy, sequencerProxy.getBPM ( ), sequencerProxy.getPulsesPerQuarterNote ( ) );
				break;

			case MIDI_CLOCK_IN:
				clockSource = new MidiInClockSource ( sequencerProxy, midiProxy.getClockTransmitter ( ) );
				break;
			}

			if ( clockSource != null )
			{
				sequencerProxy.updateClockSource ( clockSource );
				sequencerProxy.setClockSourceType ( clockSourceType );
				sendNotification ( SeqNotifications.CHANGE_PLAYBACK, Playback.STOP );
				sendNotification ( SeqNotifications.CLOCK_SOURCE_UPDATED, clockSourceType );
				sendNotification ( SeqNotifications.ENABLE_PLAYBACK_CONTROLS, clockSourceType == ClockSourceType.INTERNAL || !midiProxy.getMidiInEnabled ( ) );
			}
		}
	}
}
