package com.hisschemoller.sequencer.util;

import java.util.prefs.Preferences;

public class EPGPreferences
{
	public static final String WINDOW_WIDTH = "windowWidth";
	public static final String WINDOW_HEIGHT = "windowHeight";
	public static final String WINDOW_X = "windowX";
	public static final String WINDOW_Y = "windowY";
	public static final String LAST_USED_DIR = "lastUsedDirectory";
	public static final String MIDI_OUT_ENABLED = "midiOutEnabled";
	public static final String MIDI_IN_DEVICE = "midiInDevice";
	public static final String MIDI_OUT_DEVICE = "midiOutDevice";
	public static final String OSC_ENABLED = "oscEnabled";
	private static final String PREFERENCES = "preferences";
	private static Preferences _preferences = Preferences.userRoot ( ).node ( PREFERENCES );
	
	public static void putInt ( String key, int value )
	{
		_preferences.putInt ( key, value );
	}
	
	public static int getInt ( String key, int defaultValue )
	{
		return _preferences.getInt ( key, defaultValue );
	}
	
	public static void putBoolean ( String key, boolean value )
	{
		_preferences.putBoolean ( key, value );
	}
	
	public static boolean getBoolean ( String key, boolean defaultValue )
	{
		return _preferences.getBoolean ( key, defaultValue );
	}
	
	public static void put ( String key, String value )
	{
		_preferences.put ( key, value );
	}
	
	public static String get ( String key, String defaultValue )
	{
		return _preferences.get ( key, defaultValue );
	}
}
