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

package com.hisschemoller.epg.controller.project;

import java.io.File;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.PreferencesProxy;
import com.hisschemoller.epg.notification.SeqNotifications;

public class StartupProjectCommand extends SimpleCommand
{
	/**
	 * On startup load a project that was saved in the preferences or start a
	 * new empty project.
	 */
	@Override public final void execute ( final INotification notification )
	{
		// No pathname string provided. Start new project.
		if ( notification.getBody ( ) instanceof String == false )
		{
			sendNotification ( SeqNotifications.NEW_PROJECT );
			return;
		}
		
		// Project reloading disabled. Start new project.
		PreferencesProxy preferencesProxy = ( PreferencesProxy ) getFacade ( ).retrieveProxy ( PreferencesProxy.NAME );
		if ( preferencesProxy.getIsReloadLastProjectEnabled ( ) == false )
		{
			sendNotification ( SeqNotifications.NEW_PROJECT );
			return;
		}

		String filePath = ( String ) notification.getBody ( );

		try
		{
			File file = new File ( filePath );
			sendNotification ( SeqNotifications.OPEN_PROJECT, file );
		}
		catch ( NullPointerException exception )
		{
			System.out.println ( "StartupProjectCommand.execute() Unable to open project file: " + exception.getMessage ( ) );
			sendNotification ( SeqNotifications.NEW_PROJECT );
		}
	}
}
