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
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hisschemoller.epg.model.data.PatternVO;
import com.hisschemoller.epg.model.data.SettingsVO;
import com.hisschemoller.epg.model.data.EPGEnums.Quantization;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.util.MidiUtils;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class PatternSettings implements ActionListener, ChangeListener
{
	public static final long serialVersionUID = -1L;
	private UUID _patternID;
	private JSlider _stepsSlider;
	private JLabel _stepsLabel;
	private JSlider _fillsSlider;
	private JLabel _fillsLabel;
	private JSlider _rotationSlider;
	private JLabel _rotationLabel;
	private JSlider _channelOutSlider;
	private JLabel _channelOutLabel;
	private JSlider _pitchOutSlider;
	private JLabel _pitchOutLabel;
	private JSlider _velocityOutSlider;
	private JLabel _velocityOutLabel;
	private JCheckBox _triggerInEnabledCheckbox;
	private JSlider _channelInSlider;
	private JLabel _channelInLabel;
	private JSlider _pitchInSlider;
	private JLabel _pitchInLabel;
	private JSlider _lengthSlider;
	private JLabel _lengthLabel;
	private JTextField _addressOutTextField;
	private JCheckBox _muteCheckBox;
	private JCheckBox _soloCheckBox;
	private JButton _deleteButton;
	private ButtonGroup _stepTimeButtonGroup;
	private JTextField _nameTextField;
	private Vector < IViewEventListener > _viewEventListeners = new Vector < IViewEventListener > ( );
	private Boolean _isUpdating = false;
	private Boolean _isEnabled = true;
	private Boolean _isDisplayMidiNoteNamesEnabled = false;

	public PatternSettings ( EPGSwingEngine swingEngine )
	{
		_stepsSlider = ( JSlider ) swingEngine.find ( "SETTINGS_EUCLID_LENGTH_SLIDER" );
		_stepsSlider.addChangeListener ( this );
		_stepsLabel = ( JLabel ) swingEngine.find ( "SETTINGS_EUCLID_LENGTH_TEXT" );

		_fillsSlider = ( JSlider ) swingEngine.find ( "SETTINGS_EUCLID_NOTES_SLIDER" );
		_fillsSlider.addChangeListener ( this );
		_fillsLabel = ( JLabel ) swingEngine.find ( "SETTINGS_EUCLID_NOTES_TEXT" );

		_rotationSlider = ( JSlider ) swingEngine.find ( "SETTINGS_EUCLID_ROTATE_SLIDER" );
		_rotationSlider.addChangeListener ( this );
		_rotationLabel = ( JLabel ) swingEngine.find ( "SETTINGS_EUCLID_ROTATE_TEXT" );

		_channelOutSlider = ( JSlider ) swingEngine.find ( "SETTINGS_MIDI_OUT_CHANNEL_SLIDER" );
		_channelOutSlider.addChangeListener ( this );
		_channelOutLabel = ( JLabel ) swingEngine.find ( "SETTINGS_MIDI_OUT_CHANNEL_TEXT" );

		_addressOutTextField = ( JTextField ) swingEngine.find ( "SETTINGS_OSC_OUT_ADDRESS" );
		_addressOutTextField.getDocument ( ).addDocumentListener ( new DocumentListener ( )
		{
			public void removeUpdate ( DocumentEvent event )
			{
				PatternSettings.this.dispatchViewEventLater ( _addressOutTextField, ViewEvent.OSC_OUT_ADDRESS_CHANGE );
			}

			public void insertUpdate ( DocumentEvent event )
			{
				PatternSettings.this.dispatchViewEventLater ( _addressOutTextField, ViewEvent.OSC_OUT_ADDRESS_CHANGE );
			}

			public void changedUpdate ( DocumentEvent event )
			{
			}
		} );

		_pitchOutSlider = ( JSlider ) swingEngine.find ( "SETTINGS_MIDI_OUT_PITCH_SLIDER" );
		_pitchOutSlider.addChangeListener ( this );
		_pitchOutLabel = ( JLabel ) swingEngine.find ( "SETTINGS_MIDI_OUT_PITCH_TEXT" );

		_velocityOutSlider = ( JSlider ) swingEngine.find ( "SETTINGS_MIDI_OUT_VELOCITY_SLIDER" );
		_velocityOutSlider.addChangeListener ( this );
		_velocityOutLabel = ( JLabel ) swingEngine.find ( "SETTINGS_MIDI_OUT_VELOCITY_TEXT" );

		_triggerInEnabledCheckbox = ( JCheckBox ) swingEngine.find ( "SETTINGS_MIDI_IN_TRIGGER_ENABLE_CHECKBOX" );
		_triggerInEnabledCheckbox.addActionListener ( this );

		_channelInSlider = ( JSlider ) swingEngine.find ( "SETTINGS_MIDI_IN_CHANNEL_SLIDER" );
		_channelInSlider.addChangeListener ( this );
		_channelInLabel = ( JLabel ) swingEngine.find ( "SETTINGS_MIDI_IN_CHANNEL_TEXT" );

		_pitchInSlider = ( JSlider ) swingEngine.find ( "SETTINGS_MIDI_IN_PITCH_SLIDER" );
		_pitchInSlider.addChangeListener ( this );
		_pitchInLabel = ( JLabel ) swingEngine.find ( "SETTINGS_MIDI_IN_PITCH_TEXT" );

		_lengthSlider = ( JSlider ) swingEngine.find ( "SETTINGS_NOTE_LENGTH_SLIDER" );
		_lengthSlider.addChangeListener ( this );
		_lengthLabel = ( JLabel ) swingEngine.find ( "SETTINGS_NOTE_LENGTH_TEXT" );

		_nameTextField = ( JTextField ) swingEngine.find ( "SETTINGS_PATTERN_NAME" );
		_nameTextField.getDocument ( ).addDocumentListener ( new DocumentListener ( )
		{
			public void removeUpdate ( DocumentEvent event )
			{
				PatternSettings.this.dispatchViewEventLater ( _nameTextField, ViewEvent.NAME_CHANGE );
			}

			public void insertUpdate ( DocumentEvent event )
			{
				PatternSettings.this.dispatchViewEventLater ( _nameTextField, ViewEvent.NAME_CHANGE );
			}

			public void changedUpdate ( DocumentEvent event )
			{
			}
		} );

		_muteCheckBox = ( JCheckBox ) swingEngine.find ( "SETTINGS_MUTE_CHECKBOX" );
		_muteCheckBox.addActionListener ( this );

		_soloCheckBox = ( JCheckBox ) swingEngine.find ( "SETTINGS_SOLO_CHECKBOX" );
		_soloCheckBox.addActionListener ( this );

		_deleteButton = ( JButton ) swingEngine.find ( "SETTINGS_DELETE_BUTTON" );
		_deleteButton.addActionListener ( this );

		_stepTimeButtonGroup = new ButtonGroup ( );

		JRadioButton radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1" );
		radioButton.setActionCommand ( Quantization.Q1.name ( ) );
		radioButton.addActionListener ( this );
		_stepTimeButtonGroup.add ( radioButton );
		radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1_2" );
		radioButton.setActionCommand ( Quantization.Q2.name ( ) );
		_stepTimeButtonGroup.add ( radioButton );
		radioButton.addActionListener ( this );
		radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1_4" );
		radioButton.setActionCommand ( Quantization.Q4.name ( ) );
		radioButton.addActionListener ( this );
		_stepTimeButtonGroup.add ( radioButton );
		radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1_8" );
		radioButton.setActionCommand ( Quantization.Q8.name ( ) );
		radioButton.addActionListener ( this );
		_stepTimeButtonGroup.add ( radioButton );
		radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1_16" );
		radioButton.setActionCommand ( Quantization.Q16.name ( ) );
		radioButton.addActionListener ( this );
		_stepTimeButtonGroup.add ( radioButton );
		radioButton = ( JRadioButton ) swingEngine.find ( "SETTINGS_STEP_LENGTH_1_32" );
		radioButton.setActionCommand ( Quantization.Q32.name ( ) );
		radioButton.addActionListener ( this );
		_stepTimeButtonGroup.add ( radioButton );
	}

	public void updatePattern ( PatternVO patternVO )
	{
		if ( patternVO != null )
		{
			setSettingsEnabled ( true );
			if ( patternVO.id != _patternID )
			{
				_patternID = patternVO.id;
				setValues ( patternVO );
			}
		}
		else
		{
			setSettingsEnabled ( false );
			_patternID = null;
		}
	}

	public void updateSettings ( PatternVO patternVO )
	{
		if ( patternVO.id == _patternID )
		{
			setValues ( patternVO );
		}
	}

	public void updateQuantization ( PatternVO patternVO )
	{
		if ( patternVO.id == _patternID )
		{
			setValues ( patternVO );
		}
	}

	public void soloPattern ( PatternVO patternVO )
	{
		_muteCheckBox.setEnabled ( !patternVO.mutedBySolo && !patternVO.solo );
	}

	public void mutePattern ( PatternVO patternVO )
	{

	}
	
	public void updateDisplayMidiNoteNamesEnabled(boolean isDisplayMidiNoteNamesEnabled)
	{
		_isDisplayMidiNoteNamesEnabled = isDisplayMidiNoteNamesEnabled;
		updateMidiPitchLabels ( );
	}

	private void setValues ( PatternVO patternVO )
	{
		_isUpdating = true;

		_stepsSlider.setValue ( patternVO.steps );
		_stepsLabel.setText ( Integer.toString ( patternVO.steps ) );
		_fillsSlider.setMaximum ( patternVO.steps );
		_fillsSlider.setValue ( patternVO.fills );
		_fillsLabel.setText ( Integer.toString ( patternVO.fills ) );
		_rotationSlider.setMaximum ( patternVO.steps - 1 );
		_rotationSlider.setValue ( patternVO.rotation );
		_rotationLabel.setText ( Integer.toString ( patternVO.rotation ) );
		_channelOutSlider.setValue ( patternVO.midiOutChannel + 1 );
		_channelOutLabel.setText ( Integer.toString ( patternVO.midiOutChannel + 1 ) );
		_pitchOutSlider.setValue ( patternVO.midiOutPitch );
		_velocityOutSlider.setValue ( patternVO.midiOutVelocity );
		_velocityOutLabel.setText ( Integer.toString ( patternVO.midiOutVelocity ) );
		_triggerInEnabledCheckbox.setSelected ( patternVO.triggerMidiInEnabled );
		_channelInSlider.setValue ( patternVO.triggerMidiInChannel + 1 );
		_channelInLabel.setText ( Integer.toString ( patternVO.triggerMidiInChannel + 1 ) );
		_pitchInSlider.setValue ( patternVO.triggerMidiInPitch );
		_lengthSlider.setValue ( patternVO.noteLength );
		_lengthSlider.setMaximum ( 24 * 4 );
		_addressOutTextField.setText ( patternVO.oscOutAddress );
		_lengthLabel.setText ( Integer.toString ( patternVO.noteLength ) );
		_muteCheckBox.setEnabled ( !patternVO.mutedBySolo && !patternVO.solo );
		_muteCheckBox.setSelected ( patternVO.mute );
		_soloCheckBox.setSelected ( patternVO.solo );
		_nameTextField.setText ( patternVO.name );
		
		updateMidiPitchLabels ( );

		Enumeration < AbstractButton > radioButtons = _stepTimeButtonGroup.getElements ( );
		while ( radioButtons.hasMoreElements ( ) )
		{
			JRadioButton radioButton = ( JRadioButton ) radioButtons.nextElement ( );
			String quantizationName = radioButton.getActionCommand ( );
			Quantization quantization = Enum.valueOf ( Quantization.class, quantizationName );

			if ( patternVO.quantization == quantization.getValue ( ) )
			{
				_stepTimeButtonGroup.setSelected ( radioButton.getModel ( ), true );
			}
		}

		_isUpdating = false;
	}

	public void actionPerformed ( ActionEvent event )
	{
		if ( event.getSource ( ) == _deleteButton )
		{
			dispatchViewEvent ( _deleteButton, ViewEvent.DELETE_PATTERN );
		}
		else if ( event.getSource ( ) == _muteCheckBox )
		{
			dispatchViewEvent ( _deleteButton, ViewEvent.MUTE_PATTERN );
		}
		else if ( event.getSource ( ) == _soloCheckBox )
		{
			dispatchViewEvent ( _deleteButton, ViewEvent.SOLO_PATTERN );
		}
		else if ( event.getSource ( ) == _addressOutTextField )
		{
			dispatchViewEvent ( _addressOutTextField, ViewEvent.OSC_OUT_ADDRESS_CHANGE );
		}
		else if ( event.getSource ( ).getClass ( ) == JRadioButton.class )
		{
			dispatchViewEvent ( _stepTimeButtonGroup, ViewEvent.QUANTIZATION );
		}
		else if ( event.getSource ( ) == _triggerInEnabledCheckbox )
		{
			dispatchViewEvent ( _triggerInEnabledCheckbox, ViewEvent.MIDI_IN_SETTINGS_CHANGE );
		}
	}

	public void stateChanged ( ChangeEvent event )
	{
		if ( event.getSource ( ) == _stepsSlider )
		{
			dispatchViewEvent ( _stepsSlider, ViewEvent.PATTERN_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _fillsSlider )
		{
			dispatchViewEvent ( _fillsSlider, ViewEvent.PATTERN_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _rotationSlider )
		{
			dispatchViewEvent ( _rotationSlider, ViewEvent.PATTERN_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _channelOutSlider )
		{
			dispatchViewEvent ( _channelOutSlider, ViewEvent.MIDI_OUT_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _pitchOutSlider )
		{
			dispatchViewEvent ( _pitchOutSlider, ViewEvent.MIDI_OUT_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _velocityOutSlider )
		{
			dispatchViewEvent ( _velocityOutSlider, ViewEvent.MIDI_OUT_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _channelInSlider )
		{
			dispatchViewEvent ( _channelInSlider, ViewEvent.MIDI_IN_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _pitchInSlider )
		{
			dispatchViewEvent ( _pitchInSlider, ViewEvent.MIDI_IN_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _lengthSlider )
		{
			dispatchViewEvent ( _lengthSlider, ViewEvent.MIDI_OUT_SETTINGS_CHANGE );
		}
	}

	public SettingsVO getSettings ( )
	{
		SettingsVO settingsVO = new SettingsVO ( );
		settingsVO.patternID = _patternID;
		settingsVO.midiOutChannel = _channelOutSlider.getValue ( ) - 1;
		settingsVO.midiOutPitch = _pitchOutSlider.getValue ( );
		settingsVO.midiOutVelocity = _velocityOutSlider.getValue ( );
		settingsVO.noteLength = _lengthSlider.getValue ( );
		settingsVO.oscOutAddress = _addressOutTextField.getText ( );
		settingsVO.midiInTriggerEnabled = _triggerInEnabledCheckbox.isSelected ( );
		settingsVO.midiInChannel = _channelInSlider.getValue ( ) - 1;
		settingsVO.midiInPitch = _pitchInSlider.getValue ( );
		settingsVO.steps = _stepsSlider.getValue ( );
		settingsVO.fills = _fillsSlider.getValue ( );
		settingsVO.rotation = _rotationSlider.getValue ( );
		settingsVO.mute = _muteCheckBox.isSelected ( );
		settingsVO.solo = _soloCheckBox.isSelected ( );
		settingsVO.name = _nameTextField.getText ( );

		String quantizationName = _stepTimeButtonGroup.getSelection ( ).getActionCommand ( );
		Quantization quantization = Enum.valueOf ( Quantization.class, quantizationName );
		settingsVO.quantization = quantization.getValue ( );

		return settingsVO;
	}

	public synchronized void addViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.addElement ( listener );
	}

	public synchronized void removeViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.removeElement ( listener );
	}

	/**
	 * Dispatch custom ViewEvent .
	 */
	protected void dispatchViewEventLater ( Object inSource, int inId )
	{
		final Object source = inSource;
		final int id = inId;

		SwingUtilities.invokeLater ( new Runnable ( )
		{
			public void run ( )
			{
				dispatchViewEvent ( source, id );
			}
		} );
	}

	/**
	 * Dispatch custom ViewEvent.
	 */
	protected void dispatchViewEvent ( Object source, int id )
	{
		if ( _isUpdating )
		{
			return;
		}

		ViewEvent viewEvent = new ViewEvent ( source, id );
		for ( int i = 0; i < _viewEventListeners.size ( ); i++ )
		{
			( ( IViewEventListener ) _viewEventListeners.elementAt ( i ) ).viewEventHandler ( viewEvent );
		}
	}

	private void setSettingsEnabled ( boolean enabled )
	{
		if ( enabled != _isEnabled )
		{
			_isEnabled = enabled;
			_stepsSlider.setEnabled ( _isEnabled );
			_fillsSlider.setEnabled ( _isEnabled );
			_rotationSlider.setEnabled ( _isEnabled );
			_channelOutSlider.setEnabled ( _isEnabled );
			_pitchOutSlider.setEnabled ( _isEnabled );
			_velocityOutSlider.setEnabled ( _isEnabled );
			_addressOutTextField.setEnabled ( _isEnabled );
			_triggerInEnabledCheckbox.setEnabled ( _isEnabled );
			_channelInSlider.setEnabled ( _isEnabled );
			_pitchInSlider.setEnabled ( _isEnabled );
			_lengthSlider.setEnabled ( _isEnabled );
			_muteCheckBox.setEnabled ( _isEnabled );
			_soloCheckBox.setEnabled ( _isEnabled );
			_nameTextField.setEnabled ( _isEnabled );
			_deleteButton.setEnabled ( _isEnabled );

			Enumeration < AbstractButton > radioButtons = _stepTimeButtonGroup.getElements ( );
			while ( radioButtons.hasMoreElements ( ) )
			{
				radioButtons.nextElement ( ).setEnabled ( _isEnabled );
			}
		}
	}
	
	private void updateMidiPitchLabels()
	{
		_pitchOutLabel.setText ( _isDisplayMidiNoteNamesEnabled ? MidiUtils.getPitchName ( _pitchOutSlider.getValue ( ) ) : String.valueOf ( _pitchOutSlider.getValue ( ) ) );
		_pitchInLabel.setText ( _isDisplayMidiNoteNamesEnabled ? MidiUtils.getPitchName ( _pitchInSlider.getValue ( ) ) : String.valueOf ( _pitchInSlider.getValue ( ) ) );
	}
}
