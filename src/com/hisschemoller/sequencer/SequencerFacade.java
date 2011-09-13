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

package com.hisschemoller.sequencer;

import org.puremvc.java.multicore.patterns.facade.Facade;

import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.EPGSwingEngine;

public class SequencerFacade extends Facade
{
	public static final String NAME = "ApplicationFacade";
	private static SequencerFacade instance = null;

	public static SequencerFacade getInstance ( )
	{
		if ( instance == null )
		{
			instance = new SequencerFacade ( );
		}

		return instance;
	}

	protected SequencerFacade ( )
	{
		super ( NAME );
	}

	@Override protected void initializeController ( )
	{
		super.initializeController ( );

		SeqNotifications.registerCommands ( this );
	}

	public void startup ( EPGSwingEngine swingEngine )
	{
		sendNotification ( SeqNotifications.STARTUP, swingEngine );
	}
}
