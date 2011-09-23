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
import java.awt.Font;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

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
		StringBuilder stringBuilder = new StringBuilder ( );

		if ( frame == null )
		{
			try
			{
				InputStream inputStream = ShowHelpCommand.class.getResourceAsStream ( "/res/help/help.html" );
				final char [ ] buffer = new char[ 0x10000 ];
				Reader in = new InputStreamReader ( inputStream, "UTF-8" );
				int read;
				do
				{
					read = in.read ( buffer, 0, buffer.length );
					if ( read > 0 )
					{
						stringBuilder.append ( buffer, 0, read );
					}
				}
				while ( read >= 0 );
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
			textPane.setSize ( 600, 600 );
			textPane.setPreferredSize ( textPane.getSize ( ) );
			textPane.setEditable ( false );
			textPane.setMargin ( new Insets ( 10, 10, 10, 10 ) );
			textPane.setContentType ( "text/html" );
			textPane.setText ( stringBuilder.toString ( ) );
			textPane.setCaretPosition ( 0 );

			Font font = UIManager.getFont ( "Label.font" );
			String bodyRule = "body { font-family: " + font.getFamily ( ) + "; font-size: " + font.getSize ( ) + "pt; }";
			( ( HTMLDocument ) textPane.getDocument ( ) ).getStyleSheet ( ).addRule ( bodyRule );

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
