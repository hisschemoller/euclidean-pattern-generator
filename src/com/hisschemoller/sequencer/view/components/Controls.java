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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hisschemoller.sequencer.util.EPGSwingEngine;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class Controls implements ActionListener, ChangeListener
{
	public static final long serialVersionUID = -1L;
	private Vector < IViewEventListener > _viewEventListeners = new Vector < IViewEventListener > ( );
	private JToggleButton _playButton;
	private JButton _allNotesOffButton;
	private JSlider _tempoSlider;
	private JFormattedTextField _tempoInput;
	// private JComboBox _midiInBox;
	private JComboBox _midiOutBox;
	private JCheckBox _midiCheckBox;
	private JTextField _oscTextField;
	private JCheckBox _oscCheckBox;
	private float _tempoValue;

	public Controls ( EPGSwingEngine swingEngine )
	{
		_playButton = ( JToggleButton ) swingEngine.find ( "PLAY_BUTTON" );
		_playButton.addActionListener ( this );

		_allNotesOffButton = ( JButton ) swingEngine.find ( "ALL_NOTES_OFF_BUTTON" );
		_allNotesOffButton.addActionListener ( this );

		_tempoSlider = ( JSlider ) swingEngine.find ( "TEMPO_SLIDER" );
		_tempoSlider.addChangeListener ( this );

		_tempoInput = ( JFormattedTextField ) swingEngine.find ( "TEMPO_INPUT" );
		_tempoInput.setPreferredSize ( new Dimension ( 60, _tempoInput.getPreferredSize ( ).height ) );
		_tempoInput.addActionListener ( this );

		_midiOutBox = ( JComboBox ) swingEngine.find ( "MIDI_OUT_BOX" );
		_midiOutBox.addActionListener ( this );

		_midiCheckBox = ( JCheckBox ) swingEngine.find ( "MIDI_CHECKBOX" );
		_midiCheckBox.addActionListener ( this );

		_oscTextField = ( JTextField ) swingEngine.find ( "OSC_TEXTFIELD" );
		_oscTextField.addActionListener ( this );

		_oscCheckBox = ( JCheckBox ) swingEngine.find ( "OSC_CHECKBOX" );
		_oscCheckBox.addActionListener ( this );
	}

	/**
	 * Fill MIDI devices comboboxes.
	 */
	public void updateMidiDevices ( MidiDevice.Info[] midiDeviceInfo )
	{
		// _midiInBox.removeAllItems ( );
		_midiOutBox.removeAllItems ( );

		try
		{
			int n = midiDeviceInfo.length;
			for ( int i = 0; i < n; i++ )
			{
				MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo[ i ] );

				// if ( midiDevice.getMaxTransmitters ( ) != 0 )
				// {
				// _midiInBox.addItem ( midiDeviceInfo[ i ] );
				// }
				if ( midiDevice.getMaxReceivers ( ) != 0 )
				{
					_midiOutBox.addItem ( midiDeviceInfo[ i ] );
				}
			}

			// _midiInBox.setSelectedIndex ( -1 );
			_midiOutBox.setSelectedIndex ( -1 );
		}
		catch ( MidiUnavailableException exception )
		{
			System.out.println ( "MIDI device unavailable: " + exception.getMessage ( ) );
		}
	}

	public void setMidiOutDevice ( MidiDevice.Info midiOutDeviceInfo )
	{
		int n = _midiOutBox.getItemCount ( );
		for ( int i = 0; i < n; i++ )
		{
			if ( ( ( MidiDevice.Info ) _midiOutBox.getItemAt ( i ) ) == midiOutDeviceInfo )
			{
				_midiOutBox.setSelectedIndex ( i );
			}
		}
	}

	public void updatePlayButtonState ( SequencerEnums.Playback state )
	{
		_playButton.setSelected ( state == SequencerEnums.Playback.START );
	}

	public void updateTempo ( float bpm )
	{
		_tempoInput.setText ( Float.toString ( bpm ) );
		_tempoSlider.setValue ( Math.round ( bpm * 10 ) );
	}

	public void updateResolution ( int resolution )
	{

	}

	public void updateMidiOutEnabled ( boolean isEnabled )
	{
		_midiCheckBox.setSelected ( isEnabled );
		_midiOutBox.setEnabled ( isEnabled );
	}

	public void updateOscEnabled ( boolean isEnabled )
	{
		_oscCheckBox.setSelected ( isEnabled );
		_oscTextField.setEnabled ( isEnabled );
	}

	public void updateOscPort ( int port )
	{
		_oscTextField.setText ( String.valueOf ( port ) );
	}

	public float getTempoValue ( )
	{
		return _tempoValue;
	}

	// public MidiDevice.Info getMidiInSelectedItem ( )
	// {
	// return ( MidiDevice.Info ) _midiInBox.getSelectedItem ( );
	// }

	public MidiDevice.Info getMidiOutSelectedItem ( )
	{
		return ( MidiDevice.Info ) _midiOutBox.getSelectedItem ( );
	}

	public SequencerEnums.Playback getPlayButtonState ( )
	{
		if ( _playButton.isSelected ( ) )
		{
			return SequencerEnums.Playback.START;
		}

		return SequencerEnums.Playback.STOP;
	}

	public void actionPerformed ( ActionEvent event )
	{
		if ( event.getSource ( ) == _tempoInput )
		{
			_tempoValue = Float.parseFloat ( _tempoInput.getText ( ).trim ( ) );
			dispatchViewEvent ( _tempoInput, ViewEvent.TEMPO_CHANGE );
		}
		else if ( event.getSource ( ) == _playButton )
		{
			dispatchViewEvent ( _playButton, ViewEvent.PLAYBACK_CHANGE );
		}
		else if ( event.getSource ( ) == _allNotesOffButton )
		{
			dispatchViewEvent ( _allNotesOffButton, ViewEvent.ALL_NOTES_OFF );
		}
		// else if ( event.getSource ( ) == _midiInBox && event.getModifiers ( )
		// != 0 )
		// {
		// dispatchViewEvent ( _midiInBEvent.MIDI_IN_DEVICE_SELECT );
		// }
		else if ( event.getSource ( ) == _midiOutBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiOutBox, ViewEvent.MIDI_OUT_DEVICE_SELECT );
		}
		else if ( event.getSource ( ) == _midiCheckBox )
		{
			dispatchViewEvent ( _midiCheckBox, ViewEvent.MIDI_CHECKBOX_SELECT );
		}
		else if ( event.getSource ( ) == _oscCheckBox )
		{
			dispatchViewEvent ( _oscCheckBox, ViewEvent.OSC_CHECKBOX_SELECT );
		}
		else if ( event.getSource ( ) == _oscTextField )
		{
			dispatchViewEvent ( _oscTextField, ViewEvent.OSC_PORT_CHANGE );
		}
	}

	public void stateChanged ( ChangeEvent event )
	{
		if ( event.getSource ( ) == _tempoSlider )
		{
			_tempoValue = _tempoSlider.getValue ( ) / 10f;
			dispatchViewEvent ( _tempoSlider, ViewEvent.TEMPO_CHANGE );
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
