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

import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.FileProxy;
import com.hisschemoller.epg.model.MidiProxy;
import com.hisschemoller.epg.model.OscProxy;
import com.hisschemoller.epg.model.PreferencesProxy;
import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.model.WindowProxy;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;
import com.hisschemoller.epg.view.ControlsMediator;
import com.hisschemoller.epg.view.MainWindowMediator;
import com.hisschemoller.epg.view.MenuBarMediator;
import com.hisschemoller.epg.view.PatternCanvasMediator;
import com.hisschemoller.epg.view.PatternSettingsMediator;

public class StartupCommand extends SimpleCommand implements ICommand
{
	@Override public final void execute ( INotification notification )
	{
		getFacade ( ).registerProxy ( new WindowProxy ( ) );
		getFacade ( ).registerProxy ( new SequencerProxy ( ) );
		getFacade ( ).registerProxy ( new MidiProxy ( ) );
		getFacade ( ).registerProxy ( new OscProxy ( ) );
		getFacade ( ).registerProxy ( new FileProxy ( ) );
		getFacade ( ).registerProxy ( new PreferencesProxy ( ) );

		WindowProxy windowProxy = ( WindowProxy ) getFacade ( ).retrieveProxy ( WindowProxy.NAME );

		getFacade ( ).registerMediator ( new MainWindowMediator ( windowProxy.getMainFrame ( ) ) );
		getFacade ( ).registerMediator ( new MenuBarMediator ( windowProxy.getMainFrame ( ) ) );
		getFacade ( ).registerMediator ( new ControlsMediator ( windowProxy.getSwingEngine ( ) ) );
		getFacade ( ).registerMediator ( new PatternCanvasMediator ( windowProxy.getSwingEngine ( ) ) );
		getFacade ( ).registerMediator ( new PatternSettingsMediator ( windowProxy.getSwingEngine ( ) ) );

		windowProxy.showMainWindow ( );

		sendNotification ( SeqNotifications.UPDATE_MIDI_DEVICES );
		sendNotification ( SeqNotifications.OPEN_MIDI_IN_DEVICE, EPGPreferences.get ( EPGPreferences.MIDI_IN_DEVICE, null ) );
		sendNotification ( SeqNotifications.OPEN_MIDI_OUT_DEVICE, EPGPreferences.get ( EPGPreferences.MIDI_OUT_DEVICE, null ) );
		sendNotification ( SeqNotifications.ENABLE_MIDI_IN_DEVICE, EPGPreferences.getBoolean ( EPGPreferences.MIDI_IN_ENABLED, true ) );
		sendNotification ( SeqNotifications.ENABLE_MIDI_OUT_DEVICE, EPGPreferences.getBoolean ( EPGPreferences.MIDI_OUT_ENABLED, true ) );
		sendNotification ( SeqNotifications.ENABLE_OSC_OUT_DEVICE, EPGPreferences.getBoolean ( EPGPreferences.OSC_OUT_ENABLED, true ) );
		sendNotification ( SeqNotifications.UPDATE_OSC_OUT_PORT, EPGPreferences.getInt ( EPGPreferences.OSC_OUT_PORT, 9000 ) );
		sendNotification ( SeqNotifications.UPDATE_SYNC_TO_MIDI_IN_ENABLED, EPGPreferences.getBoolean ( EPGPreferences.SYNC_TO_MIDI_IN_CLOCK, false ) );
		sendNotification ( SeqNotifications.UPDATE_TRIGGER_BY_NOTE, EPGPreferences.getBoolean ( EPGPreferences.TRIGGERED_BY_MIDI_NOTE, false ) );
		sendNotification ( SeqNotifications.ENABLE_DISPLAY_MIDI_NOTE_NAMES, EPGPreferences.getBoolean ( EPGPreferences.DISPLAY_MIDI_NOTE_NAMES, false ) );
		sendNotification ( SeqNotifications.ENABLE_RELOAD_LAST_PROJECT, EPGPreferences.getBoolean ( EPGPreferences.RELOAD_LAST_OPENED_PROJECT, false ) );
		sendNotification ( SeqNotifications.STARTUP_PROJECT, EPGPreferences.get ( EPGPreferences.LAST_OPENED_PROJECT, null ) );
	}
}
