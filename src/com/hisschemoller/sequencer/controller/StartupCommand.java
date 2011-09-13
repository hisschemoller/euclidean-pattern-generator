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

package com.hisschemoller.sequencer.controller;

import javax.swing.JFrame;

import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.FileProxy;
import com.hisschemoller.sequencer.model.MidiProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.EPGPreferences;
import com.hisschemoller.sequencer.util.EPGSwingEngine;
import com.hisschemoller.sequencer.view.ControlsMediator;
import com.hisschemoller.sequencer.view.MenuBarMediator;
import com.hisschemoller.sequencer.view.PatternEditorMediator;
import com.hisschemoller.sequencer.view.PatternSettingsMediator;

public class StartupCommand extends SimpleCommand implements ICommand
{
	@Override public final void execute ( INotification notification )
	{
		EPGSwingEngine swingEngine = ( EPGSwingEngine ) notification.getBody ( );
		JFrame jFrame = ( JFrame ) swingEngine.find ( EPGSwingEngine.MAIN_FRAME );

		getFacade ( ).registerProxy ( new SequencerProxy ( ) );
		getFacade ( ).registerProxy ( new MidiProxy ( ) );
		getFacade ( ).registerProxy ( new FileProxy ( jFrame ) );
		getFacade ( ).registerMediator ( new MenuBarMediator ( jFrame ) );
		getFacade ( ).registerMediator ( new ControlsMediator ( swingEngine ) );
		getFacade ( ).registerMediator ( new PatternEditorMediator ( swingEngine ) );
		getFacade ( ).registerMediator ( new PatternSettingsMediator ( swingEngine ) );
		
		sendNotification ( SeqNotifications.UPDATE_MIDI_DEVICES );
		sendNotification ( SeqNotifications.ENABLE_MIDI_OUT_DEVICE, EPGPreferences.getBoolean ( EPGPreferences.MIDI_OUT_ENABLED, true ) );
		sendNotification ( SeqNotifications.ENABLE_OSC_DEVICE, EPGPreferences.getBoolean ( EPGPreferences.OSC_ENABLED, true ) );
		sendNotification ( SeqNotifications.NEW_PROJECT );
	}
}
