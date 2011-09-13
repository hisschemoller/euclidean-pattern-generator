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

package com.hisschemoller.sequencer.view.events;

import java.awt.AWTEvent;

public class ViewEvent extends AWTEvent
{
	/** Control */
	public static final int TEMPO_CHANGE = 1;
	public static final int PLAYBACK_CHANGE = 2;
	public static final int MIDI_IN_DEVICE_SELECT = 3;
	public static final int MIDI_OUT_DEVICE_SELECT = 4;
	public static final int MIDI_CHECKBOX_SELECT = 5;
	public static final int OSC_CHECKBOX_SELECT = 6;
	public static final int ALL_NOTES_OFF = 7;

	/** Pattern editor */
	public static final int PANEL_CLICK = 10;
	public static final int PATTERN_LOCATION_CHANGE = 11;
	public static final int PATTERN_CENTER_PRESS = 12;

	/** Settings */
	public static final int MIDI_SETTINGS_CHANGE = 20;
	public static final int OSC_SETTINGS_CHANGE = 25;
	public static final int PATTERN_SETTINGS_CHANGE = 21;
	public static final int DELETE_PATTERN = 22;
	public static final int MUTE_PATTERN = 23;
	public static final int SOLO_PATTERN = 24;
	public static final int QUANTIZATION = 25;
	public static final int NAME_CHANGE = 26;

	public static final long serialVersionUID = -1L;

	public ViewEvent ( Object source, int id )
	{
		super ( source, id );
	}
}
