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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.util.PromptComboBoxRenderer;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class Preferences implements ActionListener
{
	private JCheckBox _midiOutCheckBox;
	private JComboBox _midiOutComboBox;
	private JCheckBox _midiInCheckBox;
	private JComboBox _midiInComboBox;
	private JCheckBox _midiInClockSyncCheckBox;
	private JCheckBox _midiInTriggerByNoteCheckBox;
	private JCheckBox _displayMidiNoteNamesCheckBox;
	private JCheckBox _reloadLastProjectCheckBox;
	private JTextField _oscOutTextField;
	private JCheckBox _oscOutCheckBox;
	private Vector < IViewEventListener > _viewEventListeners = new Vector < IViewEventListener > ( );

	public Preferences ( EPGSwingEngine swingEngine )
	{
		/** Initialize frame from preferences. */
		final JDialog jDialog = ( JDialog ) swingEngine.find ( EPGSwingEngine.PREFERENCES_DIALOG );
		jDialog.addWindowListener ( new WindowAdapter ( )
		{
			public void windowClosing ( WindowEvent windowEvent )
			{
				Preferences.this.dispatchViewEvent ( Preferences.this, ViewEvent.CLOSE_PREFERENCES );
				jDialog.dispose ( );
			}
		} );

		_midiInCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_IN_CHECKBOX" );
		_midiInCheckBox.addActionListener ( this );

		_midiInComboBox = ( JComboBox ) swingEngine.find ( "MIDI_IN_COMBOBOX" );
		_midiInComboBox.setRenderer ( new PromptComboBoxRenderer ( "Select..." ) );
		_midiInComboBox.addActionListener ( this );

		_midiOutCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_OUT_CHECKBOX" );
		_midiOutCheckBox.addActionListener ( this );
		
		_midiInClockSyncCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_IN_CLOCK_CHECKBOX" );
		_midiInClockSyncCheckBox.addActionListener ( this );
		
		_midiInTriggerByNoteCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_IN_NOTE_TRIGGER_CHECKBOX" );
		_midiInTriggerByNoteCheckBox.addActionListener ( this );

		_midiOutComboBox = ( JComboBox ) swingEngine.find ( "MIDI_OUT_COMBOBOX" );
		_midiOutComboBox.setRenderer ( new PromptComboBoxRenderer ( "Select..." ) );
		_midiOutComboBox.addActionListener ( this );

		_oscOutCheckBox = ( JCheckBox ) swingEngine.find ( "OSC_OUT_CHECKBOX" );
		_oscOutCheckBox.addActionListener ( this );

		_oscOutTextField = ( JTextField ) swingEngine.find ( "OSC_OUT_TEXTFIELD" );
		_oscOutTextField.addActionListener ( this );
		
		_displayMidiNoteNamesCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_NOTE_NAMES_CHECKBOX" );
		_displayMidiNoteNamesCheckBox.addActionListener ( this );
		
		_reloadLastProjectCheckBox = ( JCheckBox ) swingEngine.find ( "RELOAD_LAST_PROJECT_CHECKBOX" );
		_reloadLastProjectCheckBox.addActionListener ( this );
	}

	/**
	 * Fill MIDI devices comboboxes.
	 */
	public void updateMidiDevices ( MidiDevice.Info[] midiDeviceInfo )
	{
		_midiInComboBox.removeAllItems ( );
		_midiOutComboBox.removeAllItems ( );

		try
		{
			int n = midiDeviceInfo.length;
			for ( int i = 0; i < n; i++ )
			{
				MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo[ i ] );

				if ( midiDevice.getMaxTransmitters ( ) != 0 )
				{
					_midiInComboBox.addItem ( midiDeviceInfo[ i ] );
				}
				if ( midiDevice.getMaxReceivers ( ) != 0 )
				{
					_midiOutComboBox.addItem ( midiDeviceInfo[ i ] );
				}
			}

			_midiInComboBox.setSelectedIndex ( -1 );
			_midiOutComboBox.setSelectedIndex ( -1 );
		}
		catch ( MidiUnavailableException exception )
		{
			System.out.println ( "MIDI device unavailable: " + exception.getMessage ( ) );
		}
	}

	public void setMidiInDevice ( MidiDevice.Info midiOutDeviceInfo )
	{
		int n = _midiInComboBox.getItemCount ( );
		for ( int i = 0; i < n; i++ )
		{
			if ( ( ( MidiDevice.Info ) _midiInComboBox.getItemAt ( i ) ) == midiOutDeviceInfo )
			{
				_midiInComboBox.setSelectedIndex ( i );
			}
		}
	}

	public void setMidiOutDevice ( MidiDevice.Info midiOutDeviceInfo )
	{
		int n = _midiOutComboBox.getItemCount ( );
		for ( int i = 0; i < n; i++ )
		{
			if ( ( ( MidiDevice.Info ) _midiOutComboBox.getItemAt ( i ) ) == midiOutDeviceInfo )
			{
				_midiOutComboBox.setSelectedIndex ( i );
			}
		}
	}

	public void updateMidiInEnabled ( boolean isEnabled )
	{
		_midiInCheckBox.setSelected ( isEnabled );
		_midiInComboBox.setEnabled ( isEnabled );
		_midiInClockSyncCheckBox.setEnabled ( isEnabled );
		_midiInTriggerByNoteCheckBox.setEnabled ( isEnabled );
	}

	public void setMidiInClockSyncSelected ( boolean isSelected )
	{
		_midiInClockSyncCheckBox.setSelected ( isSelected );
	}

	public void setTriggeredByNoteSelected ( boolean isSelected )
	{
		_midiInTriggerByNoteCheckBox.setSelected ( isSelected );
	}

	public void setDisplayMidiNoteNamesSelected ( boolean isSelected )
	{
		_displayMidiNoteNamesCheckBox.setSelected ( isSelected );
	}

	public void setReloadLastOpenedProjectSelected ( boolean isSelected )
	{
		_reloadLastProjectCheckBox.setSelected ( isSelected );
	}

	public void updateMidiOutEnabled ( boolean isEnabled )
	{
		_midiOutCheckBox.setSelected ( isEnabled );
		_midiOutComboBox.setEnabled ( isEnabled );
	}

	public void updateOscOutEnabled ( boolean isEnabled )
	{
		_oscOutCheckBox.setSelected ( isEnabled );
		_oscOutTextField.setEnabled ( isEnabled );
	}

	public void updateOscOutPort ( int port )
	{
		_oscOutTextField.setText ( String.valueOf ( port ) );
	}

	public MidiDevice.Info getMidiInSelectedItem ( )
	{
		return ( MidiDevice.Info ) _midiInComboBox.getSelectedItem ( );
	}

	public MidiDevice.Info getMidiOutSelectedItem ( )
	{
		return ( MidiDevice.Info ) _midiOutComboBox.getSelectedItem ( );
	}

	public void actionPerformed ( ActionEvent event )
	{
		if ( event.getSource ( ) == _midiInCheckBox )
		{
			dispatchViewEvent ( _midiInCheckBox, ViewEvent.MIDI_IN_CHECKBOX_SELECT );
		}
		else if ( event.getSource ( ) == _midiInComboBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiInComboBox, ViewEvent.MIDI_IN_DEVICE_SELECT );
		}
		else if ( event.getSource ( ) == _midiInClockSyncCheckBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiInClockSyncCheckBox, ViewEvent.MIDI_IN_CLOCK_SYNC_SELECT );
		}
		else if ( event.getSource ( ) == _midiInTriggerByNoteCheckBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiInTriggerByNoteCheckBox, ViewEvent.MIDI_IN_TRIGGER_BY_NOTE_SELECT );
		}
		else if ( event.getSource ( ) == _midiOutCheckBox )
		{
			dispatchViewEvent ( _midiOutCheckBox, ViewEvent.MIDI_OUT_CHECKBOX_SELECT );
		}
		else if ( event.getSource ( ) == _midiOutComboBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiOutComboBox, ViewEvent.MIDI_OUT_DEVICE_SELECT );
		}
		else if ( event.getSource ( ) == _oscOutCheckBox )
		{
			dispatchViewEvent ( _oscOutCheckBox, ViewEvent.OSC_OUT_CHECKBOX_SELECT );
		}
		else if ( event.getSource ( ) == _oscOutTextField )
		{
			dispatchViewEvent ( _oscOutTextField, ViewEvent.OSC_OUT_PORT_CHANGE );
		}
		else if ( event.getSource ( ) == _displayMidiNoteNamesCheckBox )
		{
			dispatchViewEvent ( _displayMidiNoteNamesCheckBox, ViewEvent.USE_MIDI_NOTE_NAMES_SELECT );
		}
		else if ( event.getSource ( ) == _reloadLastProjectCheckBox )
		{
			dispatchViewEvent ( _reloadLastProjectCheckBox, ViewEvent.RELOAD_PROJECT_CHECKBOX_SELECT );
		}
	}

	public synchronized void addViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.addElement ( listener );
	}

	public synchronized void removeViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.removeElement ( listener );
	}

	protected void dispatchViewEvent ( Object source, int id )
	{
		ViewEvent viewEvent = new ViewEvent ( source, id );
		for ( int i = 0; i < _viewEventListeners.size ( ); i++ )
		{
			( ( IViewEventListener ) _viewEventListeners.elementAt ( i ) ).viewEventHandler ( viewEvent );
		}
	}
}
