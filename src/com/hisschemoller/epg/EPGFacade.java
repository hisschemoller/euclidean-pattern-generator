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

package com.hisschemoller.epg;

import org.puremvc.java.multicore.patterns.facade.Facade;

import com.hisschemoller.epg.notification.SeqNotifications;

public class EPGFacade extends Facade
{
	public static final String NAME = "ApplicationFacade";
	private static EPGFacade instance = null;

	public static EPGFacade getInstance ( )
	{
		if ( instance == null )
		{
			instance = new EPGFacade ( );
		}

		return instance;
	}

	protected EPGFacade ( )
	{
		super ( NAME );
	}

	@Override protected void initializeController ( )
	{
		super.initializeController ( );

		SeqNotifications.registerCommands ( this );
	}

	public void startup ( )
	{
		sendNotification ( SeqNotifications.STARTUP );
	}
}
