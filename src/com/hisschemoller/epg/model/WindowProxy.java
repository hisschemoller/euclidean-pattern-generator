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

package com.hisschemoller.epg.model;

import java.awt.Container;

import javax.swing.JFrame;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.util.PromptComboBoxRenderer;
import com.hisschemoller.epg.view.components.JToggleButtonForPanel;

public class WindowProxy extends Proxy
{
	public static String NAME = WindowProxy.class.getName ( );
	private EPGSwingEngine _swingEngine;
	private Container _container;
	private JFrame _mainFrame;
	private JFrame _helpWindow;
	private Boolean _preferencesOpened = false;

	public WindowProxy ( )
	{
		super ( NAME );
	}

	@Override public void onRegister ( )
	{
		try
		{
			/** Layout with SwiXML. */
			_swingEngine = new EPGSwingEngine ( this );
			_swingEngine.getTaglib ( ).registerTag ( "JToggleButtonForPanel", JToggleButtonForPanel.class );
			_swingEngine.getTaglib ( ).registerTag ( "PromptComboBoxRenderer", PromptComboBoxRenderer.class );
			_container = _swingEngine.render ( "res/layout/main.xml" );
			_mainFrame = ( JFrame ) _swingEngine.find ( EPGSwingEngine.MAIN_FRAME );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace ( );
		}
	}
	
	public void showMainWindow()
	{
		_container.setVisible ( true );
	}

	public EPGSwingEngine getSwingEngine ( )
	{
		return _swingEngine;
	}

	public JFrame getMainFrame ( )
	{
		return _mainFrame;
	}

	public void setHelpFrame ( JFrame frame )
	{
		_helpWindow = frame;
	}

	public JFrame getHelpFrame ( )
	{
		return _helpWindow;
	}

	public void setPreferencesOpened ( Boolean preferencesOpened )
	{
		_preferencesOpened = preferencesOpened;
	}

	public Boolean getPreferencesOpened ( )
	{
		return _preferencesOpened;
	}
}
