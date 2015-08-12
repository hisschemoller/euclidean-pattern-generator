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

package com.hisschemoller.epg.view.events;

import java.awt.AWTEvent;

public class ViewEvent extends AWTEvent
{
	/** Control */
	public static final int TEMPO_CHANGE = 1;
	public static final int PLAYBACK_CHANGE = 2;
	public static final int ALL_NOTES_OFF = 3;
	public static final int SHOW_PREFERENCES = 4;

	/** Pattern editor */
	public static final int PANEL_CLICK = 20;
	public static final int PATTERN_LOCATION_CHANGE = 21;
	public static final int PATTERN_CENTER_PRESS = 22;

	/** Settings */
	public static final int OSC_OUT_ADDRESS_CHANGE = 30;
	public static final int MIDI_OUT_SETTINGS_CHANGE = 31;
	public static final int MIDI_IN_SETTINGS_CHANGE = 32;
	public static final int PATTERN_SETTINGS_CHANGE = 33;
	public static final int DELETE_PATTERN = 34;
	public static final int MUTE_PATTERN = 35;
	public static final int SOLO_PATTERN = 36;
	public static final int QUANTIZATION = 37;
	public static final int NAME_CHANGE = 38;
	
	/** Preferences */
	public static final int CLOSE_PREFERENCES = 40;
	public static final int MIDI_IN_CHECKBOX_SELECT = 41;
	public static final int MIDI_IN_DEVICE_SELECT = 42;
	public static final int MIDI_IN_CLOCK_SYNC_SELECT = 43;
	public static final int MIDI_IN_TRIGGER_BY_NOTE_SELECT = 44;
	public static final int MIDI_OUT_CHECKBOX_SELECT = 45;
	public static final int MIDI_OUT_DEVICE_SELECT = 46;
	public static final int OSC_OUT_CHECKBOX_SELECT = 47;
	public static final int OSC_OUT_PORT_CHANGE = 48;
	public static final int USE_MIDI_NOTE_NAMES_SELECT = 49;
	public static final int RELOAD_PROJECT_CHECKBOX_SELECT = 50;

	private static final long serialVersionUID = 3091188804101099429L;

	public ViewEvent ( Object source, int id )
	{
		super ( source, id );
	}
}
