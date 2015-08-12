package com.hisschemoller.epg.util;

import org.swixml.SwingEngine;

public class EPGSwingEngine extends SwingEngine
{
	public static final String MAIN_FRAME = "MAIN_FRAME";
	public static final String PREFERENCES_DIALOG = "PREFERENCES_DIALOG";

	public EPGSwingEngine ( Object client )
	{
		super ( client );
	}
}
