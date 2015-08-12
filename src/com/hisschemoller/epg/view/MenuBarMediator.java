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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.view.components.MenuBar;

public class MenuBarMediator extends Mediator implements ActionListener
{
	public static final String NAME = MenuBarMediator.class.getName ( );

	public MenuBarMediator ( Object viewComponent )
	{
		super ( NAME, viewComponent );
	}

	@Override public final void onRegister ( )
	{
		super.onRegister ( );
		
		JFrame jFrame = ( JFrame ) viewComponent;
		MenuBar menuBar = new MenuBar ( this );
		jFrame.setJMenuBar ( menuBar );
		setViewComponent ( menuBar );
	}

	public void actionPerformed ( ActionEvent event )
	{
		String command = event.getActionCommand ( );

		if ( command == MenuBar.NEW )
		{
			sendNotification ( SeqNotifications.NEW_PROJECT );
		}
		else if ( command == MenuBar.OPEN )
		{
			sendNotification ( SeqNotifications.CHOOSE_PROJECT );
		}
		else if ( command == MenuBar.SAVE )
		{
			sendNotification ( SeqNotifications.SAVE_PROJECT );
		}
		else if ( command == MenuBar.SAVE_AS )
		{
			sendNotification ( SeqNotifications.SAVE_PROJECT_AS );
		}
		else if ( command == MenuBar.HELP )
		{
			sendNotification ( SeqNotifications.SHOW_HELP );
		}
		else if ( command == MenuBar.PREFERENCES )
		{
			sendNotification ( SeqNotifications.SHOW_PREFERENCES );
		}
	}
}
