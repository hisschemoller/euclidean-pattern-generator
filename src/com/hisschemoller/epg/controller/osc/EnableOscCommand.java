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

package com.hisschemoller.epg.controller.osc;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.SequencerProxy;
import com.hisschemoller.epg.notification.SeqNotifications;
import com.hisschemoller.epg.util.EPGPreferences;

public class EnableOscCommand extends SimpleCommand
{
	@Override public final void execute ( final INotification notification )
	{
		boolean isEnabled = ( Boolean ) notification.getBody ( );
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );

		if ( sequencerProxy.getOscOutEnabled ( ) != isEnabled )
		{
			sequencerProxy.setOscOutEnabled ( isEnabled );
			sendNotification ( SeqNotifications.OSC_OUT_DEVICE_ENABLED, isEnabled );
			EPGPreferences.putBoolean ( EPGPreferences.OSC_OUT_ENABLED, isEnabled );
		}
	}
}
