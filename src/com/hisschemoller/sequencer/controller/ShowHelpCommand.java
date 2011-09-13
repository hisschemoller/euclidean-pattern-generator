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

package com.hisschemoller.sequencer.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.FileProxy;

public class ShowHelpCommand extends SimpleCommand
{
	/**
	 * Show help frame.
	 */
	@Override public final void execute ( final INotification notification )
	{
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );
		JFrame frame = fileProxy.getHelpFrame ( );
		String txt = "";

		if ( frame == null )
		{
			try
			{
				// Get absolute path.
				File appBase = new File ( "." );
				String path = appBase.getAbsolutePath ( );
				System.out.println ( "path: " + path );
				
				BufferedReader bufferedReader = new BufferedReader ( new FileReader ( "res/help/help.html" ) );
				String str;
				while ( ( str = bufferedReader.readLine ( ) ) != null )
				{
					txt += str;
				}
			}
			catch ( FileNotFoundException exception )
			{
				exception.printStackTrace ( );
			}
			catch ( IOException exception )
			{
				exception.printStackTrace ( );
			}
			
			JTextPane textPane = new JTextPane ( );
			textPane.setPreferredSize ( new Dimension ( 500, 600 ) );
			textPane.setSize ( 500, 600 );
			textPane.setEditable ( false );
			textPane.setMargin ( new Insets ( 10, 10, 10, 10 ) );
			textPane.setContentType ( "text/html" );
			textPane.setText ( txt );
			textPane.setCaretPosition ( 0 );

			JScrollPane scrollPane = new JScrollPane ( );
			scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			scrollPane.setViewportView ( textPane );

			frame = new JFrame ( "Euclidean Sequencer Help" );
			frame.setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );
			frame.setBackground ( new Color ( 0xFFFFFF ) );
			frame.setLocation ( 200, 50 );
			frame.add ( scrollPane );
			frame.pack ( );
			frame.setVisible ( true );

			fileProxy.setHelpFrame ( frame );
		}
		else
		{
			frame.setVisible ( true );
		}
	}
}
