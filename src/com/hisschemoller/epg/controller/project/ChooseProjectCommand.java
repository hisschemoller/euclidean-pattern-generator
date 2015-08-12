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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.epg.model.FileProxy;
import com.hisschemoller.epg.model.WindowProxy;
import com.hisschemoller.epg.notification.SeqNotifications;

public class ChooseProjectCommand extends SimpleCommand
{
	/**
	 * Show file chooser dialog to browse for a project XML file to open.
	 */
	@Override public final void execute ( final INotification notification )
	{
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );
		WindowProxy windowProxy = ( WindowProxy ) getFacade ( ).retrieveProxy ( WindowProxy.NAME );

		FileNameExtensionFilter filter = new FileNameExtensionFilter ( "Project XML Files", "xml" );
		JFrame frame = windowProxy.getMainFrame ( );
		
		JFileChooser fileChooser = fileProxy.getFileChooser ( );
		fileChooser.resetChoosableFileFilters ( );
		fileChooser.setFileFilter ( filter );
		
		int returnValue = fileChooser.showOpenDialog ( frame );

		if ( returnValue == JFileChooser.APPROVE_OPTION )
		{
			System.out.println ( "ChooseProjectCommand() File chosen: " + fileChooser.getSelectedFile ( ) );
			sendNotification ( SeqNotifications.OPEN_PROJECT, fileChooser.getSelectedFile ( ) );
		}
	}
}
