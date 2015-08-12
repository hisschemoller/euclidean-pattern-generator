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

package com.hisschemoller.epg.model;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

public class PreferencesProxy extends Proxy
{
	public static final String NAME = PreferencesProxy.class.getName ( );
	private boolean _isSyncToMidiInClockEnabled;
	private boolean _displayMidiNoteNamesEnabled;
	private boolean _isReloadLastProjectEnabled;

	public PreferencesProxy ( )
	{
		super ( NAME );
	}

	public void setIsSyncToMidiInClockEnabled ( boolean isSyncToMidiInClockEnabled )
	{
		_isSyncToMidiInClockEnabled = isSyncToMidiInClockEnabled;
	}

	public boolean getIsSyncToMidiInClockEnabled ( )
	{
		return _isSyncToMidiInClockEnabled;
	}

	public void setDisplayMidiNoteNamesEnabled ( boolean displayMidiNoteNamesEnabled )
	{
		_displayMidiNoteNamesEnabled = displayMidiNoteNamesEnabled;
	}

	public boolean getDisplayMidiNoteNamesEnabled ( )
	{
		return _displayMidiNoteNamesEnabled;
	}

	public void setIsReloadLastProjectEnabled ( boolean isReloadLastProjectEnabled )
	{
		_isReloadLastProjectEnabled = isReloadLastProjectEnabled;
	}

	public boolean getIsReloadLastProjectEnabled ( )
	{
		return _isReloadLastProjectEnabled;
	}
}
