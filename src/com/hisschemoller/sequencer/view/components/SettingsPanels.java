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

package com.hisschemoller.sequencer.view.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.swixml.XScrollPane;

import com.hisschemoller.sequencer.util.EPGSwingEngine;

public class SettingsPanels implements ActionListener
{
	private final static String SETTINGS_BUTTON_EXPAND_EUCLID = "SETTINGS_BUTTON_EXPAND_EUCLID";
	private final static String SETTINGS_BUTTON_EXPAND_MIDI_OSC = "SETTINGS_BUTTON_EXPAND_MIDI_OSC";
	private final static String SETTINGS_BUTTON_EXPAND_OTHER = "SETTINGS_BUTTON_EXPAND_OTHER";
	private final static String SETTINGS_CONTENT_PANEL_EUCLID = "SETTINGS_CONTENT_PANEL_EUCLID";
	private final static String SETTINGS_CONTENT_PANEL_MIDI_OSC = "SETTINGS_CONTENT_PANEL_MIDI_OSC";
	private final static String SETTINGS_CONTENT_PANEL_OTHER = "SETTINGS_CONTENT_PANEL_OTHER";
	private final static String SETTINGS_SCROLLPANE = "SETTINGS_SCROLLPANE";
	private JPanel _contentPanelEuclid;
	private JPanel _contentPanelMidiOsc;
	private JPanel _contentPanelOther;
	private JToggleButtonForPanel _buttonEuclid;
	private JToggleButtonForPanel _buttonMidiOsc;
	private JToggleButtonForPanel _buttonOther;
	private boolean _euclidExpanded = true;
	private boolean _midiOscExpanded = true;
	private boolean _otherExpanded = true;
	
	public SettingsPanels ( EPGSwingEngine swingEngine )
	{
		_buttonEuclid = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_EUCLID );
		_buttonEuclid.addActionListener ( this );
		_buttonMidiOsc = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_MIDI_OSC );
		_buttonMidiOsc.addActionListener ( this );
		_buttonOther = ( JToggleButtonForPanel ) swingEngine.find ( SETTINGS_BUTTON_EXPAND_OTHER );
		_buttonOther.addActionListener ( this );
		
		_contentPanelEuclid = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_EUCLID );
		_contentPanelMidiOsc = ( JPanel ) swingEngine.find ( SETTINGS_CONTENT_PANEL_MIDI_OSC );
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
		else if ( SETTINGS_BUTTON_EXPAND_MIDI_OSC.equals ( command ) )
		{
			_midiOscExpanded = !_midiOscExpanded;
			_contentPanelMidiOsc.setVisible ( _midiOscExpanded );
		}
		else if ( SETTINGS_BUTTON_EXPAND_OTHER.equals ( command ) )
		{
			_otherExpanded = !_otherExpanded;
			_contentPanelOther.setVisible ( _otherExpanded );
		}
	}
}
