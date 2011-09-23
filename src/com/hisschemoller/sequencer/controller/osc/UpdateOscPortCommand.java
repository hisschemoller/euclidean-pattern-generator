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

package com.hisschemoller.sequencer.controller.osc;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.OscProxy;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.EPGPreferences;

public class UpdateOscPortCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		int port = ( Integer ) notification.getBody ( );
		OscProxy oscProxy = ( OscProxy ) getFacade ( ).retrieveProxy ( OscProxy.NAME );

		if ( oscProxy.getPort ( ) != port )
		{
			oscProxy.setNetAddress ( port );
			sendNotification ( SeqNotifications.OSC_PORT_UPDATED, oscProxy.getPort ( ) );
			EPGPreferences.putInt ( EPGPreferences.OSC_PORT, port );
		}
	}
}
