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

package com.hisschemoller.epg.view.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.swixml.XScrollPane;

import com.hisschemoller.epg.util.EPGSwingEngine;

public class SettingsPanels implements ActionListener
{
	private final static String SETTINGS_BUTTON_EXPAND_EUCLID = "SETTINGS_BUTTON_EXPAND_EUCLID";
	private final static String SETTINGS_BUTTON_EXPAND_MIDI_OSC_OUT = "SETTINGS_BUTTON_EXPAND_MIDI_OSC_OUT";
	private final static String SETTINGS_BUTTON_EXPAND_MIDI_OSC_IN = "SETTINGS_BUTTON_EXPAND_MIDI_OSC_IN";
	private final static String SETTINGS_BUTTON_EXPAND_OTHER = "SETTINGS_BUTTON_EXPAND_OTHER";
	private final static String SETTINGS_CONTENT_PANEL_EUCLID = "SETTINGS_CONTENT_PANEL_EUCLID";
	private final static String SETTINGS_CONTENT_PANEL_MIDI_OSC_OUT = "SETTINGS_CONTENT_PANEL_MIDI_OSC_OUT";
	private final static String SETTINGS_CONTENT_PANEL_MIDI_OSC_IN = "SETTINGS_CONTENT_PANEL_MIDI_OSC_IN";
	private final static String SETTINGS_CONTENT_PANEL_OTHER = "SETTINGS_CONTENT_PANEL_OTHER";
	private final static String SETTINGS_SCROLLPANE = "SETTINGS_SCROLLPANE";
	private JPanel _contentPanelEuclid;
	private JPanel _contentPanelMidiOscOut;
	private JPanel _contentPanelMidiOscIn;
	private JPanel _contentPanelOther;
	private JToggleButtonForPanel _buttonEuclid;
	private JToggleButtonForPanel _buttonMidiOscOut;
	private JToggleButtonForPanel _buttonMidiOscIn;
	private JToggleButtonForPanel _buttonOther;
	private boolean _euclidExpanded = true;
	private boolean _midiOscOutExpanded = true;
	private boolean _midiOscInExpanded = true;
	private boolean _otherExpanded = true;
	
	public SettingsPanels ( EPGSwingEngine swingEngine )
	{
		_buttonEuclid = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_EUCLID );
		_buttonEuclid.addActionListener ( this );
		_buttonMidiOscOut = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_MIDI_OSC_OUT );
		_buttonMidiOscOut.addActionListener ( this );
		_buttonMidiOscIn = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_MIDI_OSC_IN );
		_buttonMidiOscIn.addActionListener ( this );
		_buttonOther = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_OTHER );
		_buttonOther.addActionListener ( this );
		
		_contentPanelEuclid = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_EUCLID );
		_contentPanelMidiOscOut = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_MIDI_OSC_OUT );
		_contentPanelMidiOscIn = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_MIDI_OSC_IN );
		_contentPanelOther = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_OTHER );
		
		/** Scroll settings panel to top. */
		final XScrollPane scrollPane = ( XScrollPane ) swingEngine.find ( SETTINGS_SCROLLPANE );
		SwingUtilities.invokeLater ( new Runnable ( )
		{
			public void run ()
			{
				scrollPane.getVerticalScrollBar ( ).setValue ( 0 );
			}
		} );
	}

	public void actionPerformed ( ActionEvent event )
	{
		String command = event.getActionCommand ( );
		
		if ( SETTINGS_BUTTON_EXPAND_EUCLID.equals ( command ) )
		{
			_euclidExpanded = !_euclidExpanded;
			_contentPanelEuclid.setVisible ( _euclidExpanded );
		}
		else if ( SETTINGS_BUTTON_EXPAND_MIDI_OSC_OUT.equals ( command ) )
		{
			_midiOscOutExpanded = !_midiOscOutExpanded;
			_contentPanelMidiOscOut.setVisible ( _midiOscOutExpanded );
		}
		else if ( SETTINGS_BUTTON_EXPAND_MIDI_OSC_IN.equals ( command ) )
		{
			_midiOscInExpanded = !_midiOscInExpanded;
			_contentPanelMidiOscIn.setVisible ( _midiOscInExpanded );
		}
		else if ( SETTINGS_BUTTON_EXPAND_OTHER.equals ( command ) )
		{
			_otherExpanded = !_otherExpanded;
			_contentPanelOther.setVisible ( _otherExpanded );
		}
	}
}
