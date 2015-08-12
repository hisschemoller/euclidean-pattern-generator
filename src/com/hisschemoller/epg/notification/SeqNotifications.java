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

package com.hisschemoller.epg.notification;

import org.puremvc.java.multicore.patterns.facade.Facade;

import com.hisschemoller.epg.controller.ChangePlaybackCommand;
import com.hisschemoller.epg.controller.OpenPatternSettingsCommand;
import com.hisschemoller.epg.controller.StartupCommand;
import com.hisschemoller.epg.controller.UpdateClockSourceCommand;
import com.hisschemoller.epg.controller.UpdatePositionNotesCommand;
import com.hisschemoller.epg.controller.UpdateTempoCommand;
import com.hisschemoller.epg.controller.UpdateTriggerByNoteCommand;
import com.hisschemoller.epg.controller.midi.EnableMidiCommand;
import com.hisschemoller.epg.controller.midi.OpenMidiDeviceCommand;
import com.hisschemoller.epg.controller.midi.SendMidiAllNotesOffCommand;
import com.hisschemoller.epg.controller.midi.SendMidiMessageCommand;
import com.hisschemoller.epg.controller.midi.UpdateMidiDevicesCommand;
import com.hisschemoller.epg.controller.osc.EnableOscCommand;
import com.hisschemoller.epg.controller.osc.SendOscMessageCommand;
import com.hisschemoller.epg.controller.osc.UpdateOscPortCommand;
import com.hisschemoller.epg.controller.pattern.CreatePatternCommand;
import com.hisschemoller.epg.controller.pattern.DeletePatternCommand;
import com.hisschemoller.epg.controller.pattern.UpdateMidiInSettingsCommand;
import com.hisschemoller.epg.controller.pattern.UpdateMidiOutSettingsCommand;
import com.hisschemoller.epg.controller.pattern.UpdateOscSettingsCommand;
import com.hisschemoller.epg.controller.pattern.UpdateOtherSettingsCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternIsPlayingCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternLocationCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternNameCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternPointerCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternQuantizationCommand;
import com.hisschemoller.epg.controller.pattern.UpdatePatternSettingsCommand;
import com.hisschemoller.epg.controller.preferences.EnableReloadLastProjectCommand;
import com.hisschemoller.epg.controller.preferences.SyncToMidiClockEnabledCommand;
import com.hisschemoller.epg.controller.preferences.DisplayMidiNoteNamesCommand;
import com.hisschemoller.epg.controller.project.ChooseProjectCommand;
import com.hisschemoller.epg.controller.project.NewProjectCommand;
import com.hisschemoller.epg.controller.project.OpenProjectCommand;
import com.hisschemoller.epg.controller.project.SaveProjectCommand;
import com.hisschemoller.epg.controller.project.StartupProjectCommand;
import com.hisschemoller.epg.controller.window.HidePreferencesCommand;
import com.hisschemoller.epg.controller.window.ShowHelpCommand;
import com.hisschemoller.epg.controller.window.ShowPreferencesCommand;

public class SeqNotifications
{
	public static final String STARTUP = "SeqNotifications.STARTUP";

	public static final String UPDATE_CLOCK_SOURCE = "SeqNotifications.UPDATE_CLOCK_SOURCE";
	public static final String CLOCK_SOURCE_UPDATED = "SeqNotifications.CLOCK_SOURCE_UPDATED";

	public static final String UPDATE_TRIGGER_BY_NOTE = "SeqNotifications.UPDATE_TRIGGER_BY_NOTE";
	public static final String TRIGGER_BY_NOTE_UPDATED = "SeqNotifications.TRIGGER_BY_NOTE_UPDATED";

	public static final String UPDATE_POSITION_NOTES = "SeqNotifications.UPDATE_POSITION_NOTES";

	public static final String CREATE_PATTERN = "SeqNotifications.CREATE_PATTERN";
	public static final String PATTERN_CREATED = "SeqNotifications.PATTERN_CREATED";

	public static final String UPDATE_PATTERN_LOCATION = "SeqNotifications.UPDATE_PATTERN_LOCATION";
	public static final String PATTERN_LOCATION_UPDATED = "SeqNotifications.PATTERN_LOCATION_UPDATED";

	public static final String UPDATE_PATTERN_POINTER = "SeqNotifications.UPDATE_PATTERN_POINTER";
	public static final String PATTERN_POINTER_UPDATED = "SeqNotifications.PATTERN_POINTER_UPDATED";

	public static final String UPDATE_PATTERN_IS_PLAYING = "SeqNotifications.UPDATE_PATTERN_IS_PLAYING";

	public static final String PATTERN_SEQUENCE_UPDATED = "SeqNotifications.PATTERN_SEQUENCE_UPDATED";

	public static final String DELETE_PATTERN = "SeqNotifications.DELETE_PATTERN";
	public static final String PATTERN_DELETED = "SeqNotifications.PATTERN_DELETED";

	public static final String MUTE_PATTERN = "SeqNotifications.MUTE_PATTERN";
	public static final String PATTERN_MUTED = "SeqNotifications.PATTERN_MUTED";

	public static final String SOLO_PATTERN = "SeqNotifications.MUTE_PATTERN";
	public static final String PATTERN_SOLOED = "SeqNotifications.PATTERN_SOLOED";

	public static final String SELECT_PATTERN = "SeqNotifications.SELECT_PATTERN";

	public static final String UPDATE_PATTERN_QUANTIZATION = "SeqNotifications.UPDATE_PATTERN_QUANTIZATION";
	public static final String PATTERN_QUANTIZATION_UPDATED = "SeqNotifications.PATTERN_QUANTIZATION_UPDATED";

	public static final String UPDATE_MIDI_OUT_SETTINGS = "SeqNotifications.UPDATE_MIDI_OUT_SETTINGS";
	public static final String MIDI_OUT_SETTINGS_UPDATED = "SeqNotifications.MIDI_OUT_SETTINGS_UPDATED";

	public static final String UPDATE_MIDI_IN_SETTINGS = "SeqNotifications.UPDATE_MIDI_IN_SETTINGS";
	public static final String MIDI_IN_SETTINGS_UPDATED = "SeqNotifications.MIDI_IN_SETTINGS_UPDATED";

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

	public static final String ENABLE_MIDI_IN_DEVICE = "SeqNotifications.ENABLE_MIDI_IN_DEVICE";
	public static final String MIDI_IN_DEVICE_ENABLED = "SeqNotifications.MIDI_IN_DEVICE_ENABLED";

	public static final String ENABLE_MIDI_OUT_DEVICE = "SeqNotifications.ENABLE_MIDI_OUT_DEVICE";
	public static final String MIDI_OUT_DEVICE_ENABLED = "SeqNotifications.MIDI_OUT_DEVICE_ENABLED";

	public static final String ENABLE_OSC_OUT_DEVICE = "SeqNotifications.ENABLE_OSC_OUT_DEVICE";
	public static final String OSC_OUT_DEVICE_ENABLED = "SeqNotifications.OSC_OUT_DEVICE_ENABLED";

	public static final String UPDATE_OSC_OUT_PORT = "SeqNotifications.UPDATE_OSC_OUT_PORT";
	public static final String OSC_OUT_PORT_UPDATED = "SeqNotifications.OSC_OUT_PORT_UPDATED";

	public static final String STARTUP_PROJECT = "SeqNotifications.STARTUP_PROJECT";

	public static final String CHOOSE_PROJECT = "SeqNotifications.CHOOSE_PROJECT";

	public static final String OPEN_PROJECT = "SeqNotifications.OPEN_PROJECT";

	public static final String NEW_PROJECT = "SeqNotifications.NEW_PROJECT";

	public static final String SAVE_PROJECT = "SeqNotifications.SAVE_PROJECT";
	
	public static final String SAVE_PROJECT_AS = "SeqNotifications.SAVE_PROJECT_AS";

	public static final String UPDATE_TEMPO = "SeqNotifications.UPDATE_TEMPO";
	public static final String TEMPO_UPDATED = "SeqNotifications.TEMPO_UPDATED";

	public static final String CHANGE_PLAYBACK = "SeqNotifications.CHANGE_PLAYBACK";
	public static final String PLAYBACK_CHANGED = "SeqNotifications.PLAYBACK_CHANGED";

	public static final String ENABLE_PLAYBACK_CONTROLS = "SeqNotifications.ENABLE_PLAYBACK_CONTROLS";

	public static final String SHOW_HELP = "SeqNotifications.SHOW_HELP";

	public static final String SHOW_PREFERENCES = "SeqNotifications.SHOW_PREFERENCES";
	public static final String HIDE_PREFERENCES = "SeqNotifications.HIDE_PREFERENCES";

	public static final String UPDATE_SYNC_TO_MIDI_IN_ENABLED = "SeqNotifications.UPDATE_SYNC_TO_MIDI_IN_ENABLED";
	public static final String SYNC_TO_MIDI_IN_ENABLED_UPDATED = "SeqNotifications.SYNC_TO_MIDI_IN_ENABLED_UPDATED";

	public static final String ENABLE_DISPLAY_MIDI_NOTE_NAMES = "SeqNotifications.ENABLE_DISPLAY_MIDI_NOTE_NAMES";
	public static final String DISPLAY_MIDI_NOTE_NAMES_ENABLED = "SeqNotifications.DISPLAY_MIDI_NOTE_NAMES_ENABLED";

	public static final String ENABLE_RELOAD_LAST_PROJECT = "SeqNotifications.ENABLE_RELOAD_LAST_PROJECT";
	public static final String RELOAD_LAST_PROJECT_ENABLED = "SeqNotifications.RELOAD_LAST_PROJECT_ENABLED";

	public static final String PROJECT_FILE_UPDATED = "SeqNotifications.PROJECT_FILE_UPDATED";

	public static final String REDRAW = "SeqNotifications.REDRAW";

	public static final void registerCommands ( Facade facade )
	{
		facade.registerCommand ( STARTUP, new StartupCommand ( ) );
		facade.registerCommand ( UPDATE_CLOCK_SOURCE, new UpdateClockSourceCommand ( ) );
		facade.registerCommand ( UPDATE_TRIGGER_BY_NOTE, new UpdateTriggerByNoteCommand ( ) );
		facade.registerCommand ( UPDATE_POSITION_NOTES, new UpdatePositionNotesCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_DEVICES, new UpdateMidiDevicesCommand ( ) );
		facade.registerCommand ( OPEN_MIDI_IN_DEVICE, new OpenMidiDeviceCommand ( ) );
		facade.registerCommand ( OPEN_MIDI_OUT_DEVICE, new OpenMidiDeviceCommand ( ) );
		facade.registerCommand ( SEND_MIDI_MESSAGE, new SendMidiMessageCommand ( ) );
		facade.registerCommand ( SEND_OSC_MESSAGE, new SendOscMessageCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_OUT_SETTINGS, new UpdateMidiOutSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_MIDI_IN_SETTINGS, new UpdateMidiInSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_OSC_SETTINGS, new UpdateOscSettingsCommand ( ) );
		facade.registerCommand ( SEND_MIDI_ALL_NOTES_OFF, new SendMidiAllNotesOffCommand ( ) );
		facade.registerCommand ( ENABLE_MIDI_IN_DEVICE, new EnableMidiCommand ( ) );
		facade.registerCommand ( ENABLE_MIDI_OUT_DEVICE, new EnableMidiCommand ( ) );
		facade.registerCommand ( ENABLE_OSC_OUT_DEVICE, new EnableOscCommand ( ) );
		facade.registerCommand ( UPDATE_OSC_OUT_PORT, new UpdateOscPortCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_SETTINGS, new UpdatePatternSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_NAME, new UpdatePatternNameCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_LOCATION, new UpdatePatternLocationCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_POINTER, new UpdatePatternPointerCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_IS_PLAYING, new UpdatePatternIsPlayingCommand ( ) );
		facade.registerCommand ( CREATE_PATTERN, new CreatePatternCommand ( ) );
		facade.registerCommand ( DELETE_PATTERN, new DeletePatternCommand ( ) );
		facade.registerCommand ( MUTE_PATTERN, new UpdateOtherSettingsCommand ( ) );
		facade.registerCommand ( SOLO_PATTERN, new UpdateOtherSettingsCommand ( ) );
		facade.registerCommand ( UPDATE_PATTERN_QUANTIZATION, new UpdatePatternQuantizationCommand ( ) );
		facade.registerCommand ( UPDATE_TEMPO, new UpdateTempoCommand ( ) );
		facade.registerCommand ( CHANGE_PLAYBACK, new ChangePlaybackCommand ( ) );
		facade.registerCommand ( OPEN_PATTERN_SETTINGS, new OpenPatternSettingsCommand ( ) );
		facade.registerCommand ( STARTUP_PROJECT, new StartupProjectCommand ( ) );
		facade.registerCommand ( CHOOSE_PROJECT, new ChooseProjectCommand ( ) );
		facade.registerCommand ( NEW_PROJECT, new NewProjectCommand ( ) );
		facade.registerCommand ( OPEN_PROJECT, new OpenProjectCommand ( ) );
		facade.registerCommand ( SAVE_PROJECT, new SaveProjectCommand ( ) );
		facade.registerCommand ( SAVE_PROJECT_AS, new SaveProjectCommand ( ) );
		facade.registerCommand ( SHOW_HELP, new ShowHelpCommand ( ) );
		facade.registerCommand ( SHOW_PREFERENCES, new ShowPreferencesCommand ( ) );
		facade.registerCommand ( HIDE_PREFERENCES, new HidePreferencesCommand ( ) );
		facade.registerCommand ( UPDATE_SYNC_TO_MIDI_IN_ENABLED, new SyncToMidiClockEnabledCommand ( ) );
		facade.registerCommand ( ENABLE_DISPLAY_MIDI_NOTE_NAMES, new DisplayMidiNoteNamesCommand ( ) );
		facade.registerCommand ( ENABLE_RELOAD_LAST_PROJECT, new EnableReloadLastProjectCommand ( ) );
	}
}
