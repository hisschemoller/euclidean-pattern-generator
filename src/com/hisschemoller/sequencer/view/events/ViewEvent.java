package com.hisschemoller.sequencer.view.events;

import java.awt.AWTEvent;

public class ViewEvent extends AWTEvent
{
	/** Control */
	public static final int TEMPO_CHANGE = 1;
	public static final int PLAYBACK_CHANGE = 2;
	public static final int MIDI_IN_DEVICE_SELECT = 3;
	public static final int MIDI_OUT_DEVICE_SELECT = 4;

	/** Pattern editor */
	public static final int PANEL_CLICK = 10;
	public static final int PATTERN_LOCATION_CHANGE = 11;
	public static final int PATTERN_CENTER_PRESS = 12;

	/** Settings */
	public static final int MIDI_SETTINGS_CHANGE = 20;
	public static final int PATTERN_SETTINGS_CHANGE = 21;
	public static final int DELETE_PATTERN = 22;
	public static final int MUTE_PATTERN = 23;
	public static final int SOLO_PATTERN = 24;

	public static final long serialVersionUID = -1L;

	public ViewEvent ( Object source, int id )
	{
		super ( source, id );
	}
}
