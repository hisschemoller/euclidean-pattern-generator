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

package com.hisschemoller.epg.controller.window;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.WindowProxy;
import com.hisschemoller.epg.view.PreferencesMediator;

public class HidePreferencesCommand extends SimpleCommand
{
	/**
	 * Hide preferences dialog.
	 */
	@Override public final void execute ( final INotification notification )
	{
		getFacade ( ).removeMediator ( PreferencesMediator.NAME );
		
		WindowProxy windowProxy = ( WindowProxy ) getFacade ( ).retrieveProxy ( WindowProxy.NAME );
		windowProxy.setPreferencesOpened ( false );
	}
}
