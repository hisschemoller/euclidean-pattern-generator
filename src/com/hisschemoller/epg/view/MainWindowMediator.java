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

package com.hisschemoller.epg.view;

import javax.swing.JFrame;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.view.components.MainWindow;

public class MainWindowMediator extends Mediator
{
	public static final String NAME = MainWindowMediator.class.getName ( );

	public MainWindowMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	public String [ ] listNotificationInterests ( )
	{
		String [ ] interests = new String[ 1 ];
		interests[ 0 ] = SeqNotifications.PROJECT_FILE_UPDATED;
		return interests;
	}

	public void handleNotification ( INotification note )
	{
		String name = note.getName ( );
		if ( name == SeqNotifications.PROJECT_FILE_UPDATED )
		{
			getView ( ).setTitle ( ( String ) note.getBody ( ) );
		}
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );
		
		MainWindow mainWindow = new MainWindow ( (JFrame) viewComponent );
		setViewComponent ( mainWindow );
	}

	private MainWindow getView ( )
	{
		return ( MainWindow ) viewComponent;
	}
}
