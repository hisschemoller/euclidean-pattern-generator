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

package com.hisschemoller.epg.controller.window;

import java.awt.Container;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.OscProxy;
import com.hisschemoller.epg.model.PreferencesProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.WindowProxy;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.view.PreferencesMediator;

public class ShowPreferencesCommand extends SimpleCommand
{
	/**
	 * Show preferences dialog.
	 */
	@Override public final void execute ( final INotification notification )
	{
		WindowProxy windowProxy = ( WindowProxy ) getFacade ( ).retrieveProxy ( WindowProxy.NAME );

		if ( windowProxy.getPreferencesOpened ( ) )
		{
			return;
		}

		try
		{
			/** Layout with SwiXML. */
			Container container = windowProxy.getSwingEngine ( ).render ( "res/layout/preferences.xml" );

			/** Create mediator. */
			getFacade ( ).registerMediator ( new PreferencesMediator ( windowProxy.getSwingEngine ( ) ) );

			/** Update view. */
			MidiProxy midiProxy = ( MidiProxy ) getFacade ( ).retrieveProxy ( MidiProxy.NAME );
			OscProxy oscProxy = ( OscProxy ) getFacade ( ).retrieveProxy ( OscProxy.NAME );
			SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
			PreferencesProxy preferencesProxy = ( PreferencesProxy ) getFacade ( ).retrieveProxy ( PreferencesProxy.NAME );

			sendNotification ( SeqNotifications.MIDI_IN_DEVICE_ENABLED, midiProxy.getMidiInEnabled ( ) );
			sendNotification ( SeqNotifications.MIDI_OUT_DEVICE_ENABLED, sequencerProxy.getMidiOutEnabled ( ) );
			sendNotification ( SeqNotifications.MIDI_DEVICES_UPDATED, midiProxy.getMidiDevicesInfo ( ) );
			sendNotification ( SeqNotifications.MIDI_IN_DEVICE_OPENED, midiProxy.getMidiInDeviceInfo ( ) );
			sendNotification ( SeqNotifications.MIDI_OUT_DEVICE_OPENED, midiProxy.getMidiOutDeviceInfo ( ) );
			sendNotification ( SeqNotifications.OSC_OUT_DEVICE_ENABLED, sequencerProxy.getOscOutEnabled ( ) );
			sendNotification ( SeqNotifications.OSC_OUT_PORT_UPDATED, oscProxy.getPort ( ) );
			sendNotification ( SeqNotifications.SYNC_TO_MIDI_IN_ENABLED_UPDATED, preferencesProxy.getIsSyncToMidiInClockEnabled ( ) );
			sendNotification ( SeqNotifications.TRIGGER_BY_NOTE_UPDATED, sequencerProxy.getTriggeredByMidiNoteEnabled ( ) );
			sendNotification ( SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED, preferencesProxy.getDisplayMidiNoteNamesEnabled ( ) );
			sendNotification ( SeqNotifications.RELOAD_LAST_PROJECT_ENABLED, preferencesProxy.getIsReloadLastProjectEnabled ( ) );

			container.setVisible ( true );

			windowProxy.setPreferencesOpened ( true );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace ( );
		}
	}
}
