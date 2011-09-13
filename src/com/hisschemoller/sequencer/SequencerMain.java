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

import java.awt.Container;

import javax.swing.SwingUtilities;

import com.hisschemoller.sequencer.util.EPGSwingEngine;
import com.hisschemoller.sequencer.view.components.MainFrame;

/**
 * Program argument: -Dcom.apple.macos.useScreenMenuBar = true
 */

public class SequencerMain
{
	private SequencerFacade _facade = SequencerFacade.getInstance ( );

	public SequencerMain ( )
	{
		try
		{
			/** Layout with SwiXML. */
			EPGSwingEngine swingEngine = new EPGSwingEngine ( this );
			Container container = swingEngine.render ( "res/layout/main.xml" );
			container.setVisible ( true );
			
			/** Frame created here; doesn't need MVC yet. */
			new MainFrame ( swingEngine );

			/** Start PureMVC. */
			_facade.startup ( swingEngine );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace ( );
		}
	}

	public static void main ( String [ ] args )
	{
		SwingUtilities.invokeLater ( new Runnable ( )
		{
			public void run ( )
			{
				new SequencerMain ( );
			}
		} );
	}
}
