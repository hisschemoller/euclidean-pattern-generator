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

package com.hisschemoller.sequencer.model;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.hisschemoller.sequencer.util.EPGPreferences;

public class FileProxy extends Proxy
{
	public static final String NAME = FileProxy.class.getName ( );
	private JFileChooser _fileChooser;
	private File _file;
	private JFrame _frame;
	private JFrame _helpFrame;

	public FileProxy ( JFrame frame )
	{
		super ( NAME );
		_frame = frame;
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

	public JFrame getDialogParent ( )
	{
		return _frame;
	}

	public void setFile ( File file )
	{
		_file = file;

		/** Store directory path in preferences. */
		if ( _file != null )
		{
			EPGPreferences.put ( EPGPreferences.LAST_USED_DIR, _file.getParent ( ) );
		}
	}

	public File getFile ( )
	{
		return _file;
	}

	public void setHelpFrame ( JFrame frame )
	{
		_helpFrame = frame;
	}

	public JFrame getHelpFrame ( )
	{
		return _helpFrame;
	}
}
