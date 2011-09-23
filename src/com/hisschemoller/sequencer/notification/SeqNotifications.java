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

package com.hisschemoller.sequencer.notification;

import org.puremvc.java.multicore.patterns.facade.Facade;

import com.hisschemoller.sequencer.controller.ChangePlaybackCommand;
import com.hisschemoller.sequencer.controller.OpenPatternSettingsCommand;
import com.hisschemoller.sequencer.controller.ShowHelpCommand;
import com.hisschemoller.sequencer.controller.StartupCommand;
import com.hisschemoller.sequencer.controller.UpdatePositionNotesCommand;
import com.hisschemoller.sequencer.controller.UpdateResolutionCommand;
import com.hisschemoller.sequencer.controller.UpdateTempoCommand;
import com.hisschemoller.sequencer.controller.file.NewProjectCommand;
import com.hisschemoller.sequencer.controller.file.OpenProjectCommand;
import com.hisschemoller.sequencer.controller.file.SaveProjectCommand;
import com.hisschemoller.sequencer.controller.midi.EnableMidiOutCommand;
import com.hisschemoller.sequencer.controller.midi.OpenMidiDeviceCommand;
import com.hisschemoller.sequencer.controller.midi.SendMidiAllNotesOffCommand;
import com.hisschemoller.sequencer.controller.midi.SendMidiMessageCommand;
import com.hisschemoller.sequencer.controller.midi.UpdateMidiDevicesCommand;
import com.hisschemoller.sequencer.controller.osc.EnableOscCommand;
import com.hisschemoller.sequencer.controller.osc.SendOscMessageCommand;
import com.hisschemoller.sequencer.controller.osc.UpdateOscPortCommand;
import com.hisschemoller.sequencer.controller.pattern.CreatePatternCommand;
import com.hisschemoller.sequencer.controller.pattern.DeletePatternCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdateMidiSettingsCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdateOscSettingsCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdateOtherSettingsCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdatePatternLocationCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdatePatternNameCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdatePatternQuantizationCommand;
import com.hisschemoller.sequencer.controller.pattern.UpdatePatternSettingsCommand;

public class SeqNotifications
{
	public static final String STARTUP = "SeqNotifications.STARTUP";
	
	public static final String UPDATE_POSITION_NOTES = "SeqNotifications.UPDATE_POSITION_NOTES";

	public static final String CREATE_PATTERN = "SeqNotifications.CREATE_PATTERN";
	public static final String PATTERN_CREATED = "SeqNotifications.PATTERN_CREATED";

	public static final String UPDATE_PATTERN_LOCATION = "SeqNotifications.UPDATE_PATTERN_LOCATION";
	public static final String PATTERN_LOCATION_UPDATED = "SeqNotifications.PATTERN_LOCATION_UPDATED";

	public static final String PATTERN_SEQUENCE_UPDATED = "SeqNotifications.PATTERN_SEQUENCE_UPDATED";

	public static final String DELETE_PATTERN = "SeqNotifications.DELETE_PATTERN";
	public static final String PATTERN_DELETED = "SeqNotifications.PATTERN_DELETED";

	public static final String MUTE_PATTERN = "SeqNotifications.MUTE_PATTERN";
	public static final String PATTERN_MUTED = "SeqNotifications.PATTERN_MUTED";

	public static final String SOLO_PATTERN = "SeqNotifications.MUTE_PATTERN";
	public static final String PATTERN_SOLOED = "PATTERN_SOLOED";

	public static final String SELECT_PATTERN = "SeqNotifications.SELECT_PATTERN";

	public static final String UPDATE_PATTERN_QUANTIZATION = "SeqNotifications.UPDATE_PATTERN_QUANTIZATION";
	public static final String PATTERN_QUANTIZATION_UPDATED = "SeqNotifications.PATTERN_QUANTIZATION_UPDATED";

	public static final String UPDATE_MIDI_SETTINGS = "SeqNotifications.UPDATE_MIDI_SETTINGS";
	public static final String MIDI_SETTINGS_UPDATED = "SeqNotifications.MIDI_SETTINGS_UPDATED";

	public static final String UPDATE_OSC_SETTINGS = "SeqNotifications.UPDATE_OSC_SETTINGS";
	public static final String OSC_SETTINGS_UPDATED = "SeqNotifications.OSC_SETTINGS_UPDATED";

	public static final String UPDATE_PATTERN_SETTINGS = "SeqNotifications.UPDATE_PATTERN_SETTINGS";
	public static final String PATTERN_SETTINGS_UPDATED = "SeqNotifications.PATTERN_SETTINGS_UPDATED";

	public static final String UPDATE_PATTERN_NAME = "SeqNotifications.UPDATE_PATTERN_NAME";
	public static final String PATTERN_NAME_UPDATED = "SeqNotifications.PATTERN_NAME_UPDATED";

	public static final String OPEN_PATTERN_SETTINGS = "SeqNotifications.OPEN_PATTERN_SETTINGS";
	public static final String PATTERN_SETTINGS_OPENED = "SeqNotifications.PATTERN_SETTINGS_OPENED";

	public static final String UPDATE_MIDI_DEVICES = "SeqNotifications.UPDATE_MIDI_DEVICES";
	public static final String MIDI_DEVICES_UPDATED = "SeqNotifications.MIDI_DEVICES_UPDATED";

	public static final String OPEN_MIDI_IN_DEVICE = "SeqNotifications.OPEN_MIDI_IN_DEVICE";
	public static final String MIDI_IN_DEVICE_OPENED = "SeqNotifications.MIDI_IN_DEVICE_OPENED";
	
	public static final String OPEN_MIDI_OUT_DEVICE = "SeqNotifications.OPEN_MIDI_OUT_DEVICE";
	public static final String MIDI_OUT_DEVICE_OPENED = "SeqNotifications.MIDI_OUT_DEVICE_OPENED";

	public static final String SEND_MIDI_MESSAGE = "SeqNotifications.SEND_MIDI_MESSAGE";

	public static final String SEND_OSC_MESSAGE = "SeqNotifications.SEND_OSC_MESSAGE";
	
	public static final String SEND_MIDI_ALL_NOTES_OFF = "SeqNotifications.SEND_MIDI_ALL_NOTES_OFF";
	
	public static final String ENABLE_MIDI_OUT_DEVICE = "SeqNotifications.ENABLE_MIDI_OUT_DEVICE";
	public static final String MIDI_OUT_DEVICE_ENABLED = "SeqNotifications.MIDI_OUT_DEVICE_ENABLED";
	
	public static final String ENABLE_OSC_DEVICE = "SeqNotifications.ENABLE_OSC_DEVICE";
	public static final String OSC_DEVICE_ENABLED = "SeqNotifications.OSC_DEVICE_ENABLED";
	
	public static final String UPDATE_OSC_PORT = "SeqNotifications.UPDATE_OSC_PORT";
	public static final String OSC_PORT_UPDATED = "SeqNotifications.OSC_PORT_UPDATED";

	public static final String OPEN_PROJECT = "SeqNotifications.OPEN_PROJECT";
	public static final String PROJECT_OPENED = "SeqNotifications.PROJECT_OPENED";

	public static final String NEW_PROJECT = "SeqNotifications.NEW_PROJECT";

	public static final String SAVE_PROJECT = "SeqNotifications.SAVE_PROJECT";
	public static final String SAVE_PROJECT_AS = "SeqNotifications.SAVE_PROJECT_AS";
	public static final String PROJECT_SAVED = "SeqNotifications.PROJECT_SAVED";

	public static final String UPDATE_TEMPO = "SeqNotifications.UPDATE_TEMPO";
	public static final String TEMPO_UPDATED = "SeqNotifications.TEMPO_UPDATED";

	public static final String UPDATE_RESOLUTION = "SeqNotifications.UPDATE_RESOLUTION";
	public static final String RESOLUTION_UPDATED = "SeqNotifications.RESOLUTION_UPDATED";

	public static final String CHANGE_PLAYBACK = "SeqNotifications.CHANGE_PLAYBACK";
	public static final String PLAYBACK_CHANGED = "SeqNotifications.PLAYBACK_CHANGED";

	public static final String SHOW_HELP = "SeqNotifications.SHOW_HELP";

	public static final String REDRAW = "SeqNotifications.REDRAW";

	public static final void registerCommands ( Facade facade )
	{
		facade.registerCommand ( STARTUP, new StartupCommand ( ) );
		facade.registerCommand ( UPDATE_POSITION_NOTES, new UpdatePositionNotesCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_DEVICES, new UpdateMidiDevicesCommand ( ) );
		facade.registerCommand ( OPEN_MIDI_IN_DEVICE, new OpenMidiDeviceCommand ( ) );
		facade.registerCommand ( OPEN_MIDI_OUT_DEVICE, new OpenMidiDeviceCommand ( ) );
		facade.registerCommand ( SEND_MIDI_MESSAGE, new SendMidiMessageCommand ( ) );
		facade.registerCommand ( SEND_OSC_MESSAGE, new SendOscMessageCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_SETTINGS, new UpdateMidiSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_OSC_SETTINGS, new UpdateOscSettingsCommand ( ) );
		facade.registerCommand ( SEND_MIDI_ALL_NOTES_OFF, new SendMidiAllNotesOffCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_SETTINGS, new UpdateMidiSettingsCommand ( ) );
		facade.registerCommand ( ENABLE_MIDI_OUT_DEVICE, new EnableMidiOutCommand ( ) );
		facade.registerCommand ( ENABLE_OSC_DEVICE, new EnableOscCommand ( ) );
		facade.registerCommand ( UPDATE_OSC_PORT, new UpdateOscPortCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_SETTINGS, new UpdatePatternSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_NAME, new UpdatePatternNameCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_LOCATION, new UpdatePatternLocationCommand ( ) );
		facade.registerCommand ( CREATE_PATTERN, new CreatePatternCommand ( ) );
		facade.registerCommand ( DELETE_PATTERN, new DeletePatternCommand ( ) );
		facade.registerCommand ( MUTE_PATTERN, new UpdateOtherSettingsCommand ( ) );
		facade.registerCommand ( SOLO_PATTERN, new UpdateOtherSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_QUANTIZATION, new UpdatePatternQuantizationCommand ( ) );
		facade.registerCommand ( UPDATE_TEMPO, new UpdateTempoCommand ( ) );
		facade.registerCommand ( UPDATE_RESOLUTION, new UpdateResolutionCommand ( ) );
		facade.registerCommand ( CHANGE_PLAYBACK, new ChangePlaybackCommand ( ) );
		facade.registerCommand ( OPEN_PATTERN_SETTINGS, new OpenPatternSettingsCommand ( ) );
		facade.registerCommand ( NEW_PROJECT, new NewProjectCommand ( ) );
		facade.registerCommand ( OPEN_PROJECT, new OpenProjectCommand ( ) );
		facade.registerCommand ( SAVE_PROJECT, new SaveProjectCommand ( ) );
		facade.registerCommand ( SAVE_PROJECT_AS, new SaveProjectCommand ( ) );
		facade.registerCommand ( SHOW_HELP, new ShowHelpCommand ( ) );
	}
}
