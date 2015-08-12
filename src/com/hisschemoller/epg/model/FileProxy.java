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

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.io.FilenameUtils;
import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.hisschemoller.epg.model.data.EPGConstants;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;

public class FileProxy extends Proxy
{
	public static final String NAME = FileProxy.class.getName ( );
	private JFileChooser _fileChooser;
	private File _file;

	public FileProxy ( )
	{
		super ( NAME );
	}

	@Override public final void onRegister ( )
	{
		File lastUsedDirectory = new File ( EPGPreferences.get ( EPGPreferences.LAST_USED_DIR, System.getProperty ( "user.home" ) ) );
		_fileChooser = new JFileChooser ( lastUsedDirectory );
	}

	@Override public final void onRemove ( )
	{
		_fileChooser = null;
	}

	public JFileChooser getFileChooser ( )
	{
		return _fileChooser;
	}

	public void setFile ( File file )
	{
		_file = file;

		// TODO: Put the following in it's own command.
		/** Store directory path in preferences. */
		if ( _file != null )
		{
			EPGPreferences.put ( EPGPreferences.LAST_USED_DIR, _file.getParent ( ) );
		}

		/** Store last opened file in preferences. */
		if ( _file != null )
		{
			EPGPreferences.put ( EPGPreferences.LAST_OPENED_PROJECT, _file.getPath ( ) );
		}

		/** Update title in main frame title bar. */
		String filename = "Untitled";
		if ( _file != null )
		{
			filename = FilenameUtils.removeExtension ( _file.getName ( ) );
		}
		sendNotification ( SeqNotifications.PROJECT_FILE_UPDATED, EPGConstants.APP_TITLE + " - " + filename );
	}

	public File getFile ( )
	{
		return _file;
	}
}
