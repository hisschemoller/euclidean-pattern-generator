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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.model.vo.SettingsVO;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class PatternSettings extends JPanel implements ActionListener, ChangeListener
{
	public static final long serialVersionUID = -1L;
	private UUID _patternID;
	private JSlider _stepsSlider;
	private JLabel _stepsLabel;
	private JSlider _fillsSlider;
	private JLabel _fillsLabel;
	private JSlider _rotationSlider;
	private JLabel _rotationLabel;
	private JSlider _channelSlider;
	private JLabel _channelLabel;
	private JSlider _pitchSlider;
	private JLabel _pitchLabel;
	private JSlider _velocitySlider;
	private JLabel _velocityLabel;
	private JSlider _lengthSlider;
	private JLabel _lengthLabel;
	private JTextField _addressTextField;
	private JCheckBox _muteCheckBox;
	private JCheckBox _soloCheckBox;
	private JButton _deleteButton;
	private Vector<IViewEventListener> _viewEventListeners = new Vector<IViewEventListener> ( );
	private Boolean _isUpdating = false;
	private Boolean _isEnabled = true;

	public PatternSettings ( )
	{
		this ( new GridBagLayout ( ) );
	}

	public PatternSettings ( LayoutManager layoutManager )
	{
		super ( layoutManager );
		setup ( );
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

	public void soloPattern ( PatternVO patternVO )
	{
		_muteCheckBox.setEnabled ( !patternVO.mutedBySolo && !patternVO.solo );
	}

	public void mutePattern ( PatternVO patternVO )
	{

	}

	public void updateResolution ( int ppqn )
	{
		_isUpdating = true;
		_lengthSlider.setMaximum ( ppqn * 4 );
		_isUpdating = false;
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
		_channelSlider.setValue ( patternVO.midiChannel + 1 );
		_channelLabel.setText ( Integer.toString ( patternVO.midiChannel + 1 ) );
		_pitchSlider.setValue ( patternVO.midiPitch );
		_pitchLabel.setText ( Integer.toString ( patternVO.midiPitch ) );
		_velocitySlider.setValue ( patternVO.midiVelocity );
		_velocityLabel.setText ( Integer.toString ( patternVO.midiVelocity ) );
		_lengthSlider.setValue ( patternVO.noteLength );
		_addressTextField.setText ( patternVO.address );
		_lengthLabel.setText ( Integer.toString ( patternVO.noteLength ) );
		_muteCheckBox.setEnabled ( !patternVO.mutedBySolo && !patternVO.solo );
		_muteCheckBox.setSelected ( patternVO.mute );
		_soloCheckBox.setSelected ( patternVO.solo );

		_isUpdating = false;
	}

	public void paintComponent ( Graphics graphics )
	{
		super.paintComponent ( graphics );

		Graphics2D graphics2 = ( Graphics2D ) graphics;
		graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

		int padding = 2;
		int paddingDouble = padding * 2;
		int radius = 8;
		Color color = new Color ( 0xEEEEEE );
		graphics2.setColor ( new Color ( color.getRed ( ), color.getGreen ( ), color.getBlue ( ), 255 ) );
		graphics2.fillRoundRect ( padding, padding, getWidth ( ) - paddingDouble, getHeight ( ) - paddingDouble, radius, radius );

		color = new Color ( 0x999999 );
		graphics2.setStroke ( new BasicStroke ( 4 ) );
		graphics2.setColor ( new Color ( color.getRed ( ), color.getGreen ( ), color.getBlue ( ), 255 ) );
		graphics2.drawRoundRect ( padding, padding, getWidth ( ) - paddingDouble, getHeight ( ) - paddingDouble, radius, radius );
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
        else if ( event.getSource ( ) == _addressTextField )
        {
            dispatchViewEvent ( _addressTextField, ViewEvent.OSC_SETTINGS_CHANGE );
        }
	}

	public void stateChanged ( ChangeEvent event )
	{
		if ( _isUpdating )
		{
			return;
		}

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
		else if ( event.getSource ( ) == _channelSlider )
		{
			dispatchViewEvent ( _channelSlider, ViewEvent.MIDI_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _pitchSlider )
		{
			dispatchViewEvent ( _pitchSlider, ViewEvent.MIDI_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _velocitySlider )
		{
			dispatchViewEvent ( _velocitySlider, ViewEvent.MIDI_SETTINGS_CHANGE );
		}
		else if ( event.getSource ( ) == _lengthSlider )
		{
			dispatchViewEvent ( _lengthSlider, ViewEvent.MIDI_SETTINGS_CHANGE );
		}
	}

	public SettingsVO getSettings ( )
	{
		SettingsVO settingsVO = new SettingsVO ( );
		settingsVO.patternID = _patternID;
		settingsVO.midiChannel = _channelSlider.getValue ( ) - 1;
		settingsVO.midiPitch = _pitchSlider.getValue ( );
		settingsVO.midiVelocity = _velocitySlider.getValue ( );
		settingsVO.noteLength = _lengthSlider.getValue ( );
		settingsVO.address = _addressTextField.getText ( );
		settingsVO.steps = _stepsSlider.getValue ( );
		settingsVO.fills = _fillsSlider.getValue ( );
		settingsVO.rotation = _rotationSlider.getValue ( );
		settingsVO.mute = _muteCheckBox.isSelected ( );
		settingsVO.solo = _soloCheckBox.isSelected ( );

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
	 * Dispatch custom ViewEvent.
	 */
	protected void dispatchViewEvent ( Object source, int id )
	{
		ViewEvent viewEvent = new ViewEvent ( source, id );
		for ( int i = 0; i < _viewEventListeners.size ( ); i++ )
		{
			( ( IViewEventListener ) _viewEventListeners.elementAt ( i ) ).viewEventHandler ( viewEvent );
		}
	}

	protected void setup ( )
	{
		setOpaque ( false );

		GridBagConstraints constraints = new GridBagConstraints ( );

		JLabel label = new JLabel ( "Pattern Settings" );
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.insets = new Insets ( 15, 0, 5, 0 );
		constraints.anchor = GridBagConstraints.LINE_START;
		add ( label, constraints );

		createLabel ( 1, 0, "Steps", false );
		_stepsSlider = createSlider ( 1, 1, 16, 1 );
		_stepsLabel = createLabel ( 1, 2, "", true );

		createLabel ( 2, 0, "Fills", false );
		_fillsSlider = createSlider ( 2, 1, 16, 1 );
		_fillsLabel = createLabel ( 2, 2, "", true );

		createLabel ( 3, 0, "Rotate", false );
		_rotationSlider = createSlider ( 3, 0, 15, 0 );
		_rotationLabel = createLabel ( 3, 2, "", true );

		label = new JLabel ( "MIDI Settings" );
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 3;
		add ( label, constraints );

		createLabel ( 6, 0, "Channel", false );
		_channelSlider = createSlider ( 6, 1, 16, 1 );
		_channelLabel = createLabel ( 6, 2, "", true );

		createLabel ( 7, 0, "Pitch", false );
		_pitchSlider = createSlider ( 7, 0, 127, 0 );
		_pitchLabel = createLabel ( 7, 2, "", true );

		createLabel ( 8, 0, "Velocity", false );
		_velocitySlider = createSlider ( 8, 0, 127, 0 );
		_velocityLabel = createLabel ( 8, 2, "", true );

		createLabel ( 9, 0, "Length", false );
		_lengthSlider = createSlider ( 9, 1, 1, 1 );
		_lengthLabel = createLabel ( 9, 2, "", true );

		label = new JLabel ( "OSC Settings" );
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 3;
		add ( label, constraints );

		createLabel ( 12, 0, "Address", false );
		_addressTextField = new JTextField ( 24 );
        _addressTextField.setMinimumSize ( new Dimension ( 128, 20 ) );
        _addressTextField.setPreferredSize ( new Dimension ( 128, 20 ) );
		_addressTextField.addActionListener ( this );
		constraints.gridx = 1;
		constraints.gridy = 12;
		constraints.gridwidth = 2;
		Insets oldInsets = constraints.insets;
        constraints.insets = new Insets ( 0, 12, 0, 0 );
		add ( _addressTextField, constraints );
		constraints.insets = oldInsets;

		label = new JLabel ( "Other Settings" );
		constraints.gridx = 0;
		constraints.gridy = 14;
		constraints.gridwidth = 3;
		add ( label, constraints );

		createLabel ( 15, 0, "Mute", false );
		_muteCheckBox = new JCheckBox ( );
		_muteCheckBox.addActionListener ( this );
		constraints.gridx = 1;
		constraints.gridy = 15;
		constraints.gridwidth = 1;
		constraints.insets = new Insets ( 0, 12, 0, 0 );
		add ( _muteCheckBox, constraints );

		createLabel ( 16, 0, "Solo", false );
		_soloCheckBox = new JCheckBox ( );
		_soloCheckBox.addActionListener ( this );
		constraints.gridx = 1;
		constraints.gridy = 16;
		add ( _soloCheckBox, constraints );

		createLabel ( 17, 0, "Delete", false );
		_deleteButton = new JButton ( "Delete pattern" );
		_deleteButton.addActionListener ( this );
		constraints.gridx = 1;
		constraints.gridy = 17;
		constraints.gridwidth = 1;
		add ( _deleteButton, constraints );
	}

	private JSlider createSlider ( int gridRow, int min, int max, int value )
	{
		GridBagConstraints constraints = new GridBagConstraints ( );
		constraints.gridx = 1;
		constraints.gridy = gridRow;
		JSlider slider = new JSlider ( SwingConstants.HORIZONTAL, min, max, value );
		slider.addChangeListener ( this );
        slider.setMinimumSize ( new Dimension ( 128, 20 ) );
		slider.setPreferredSize ( new Dimension ( 128, 20 ) );
        Insets oldInsets = constraints.insets;
        constraints.insets = new Insets ( 0, 12, 0, 0 );
		add ( slider, constraints );
        constraints.insets = oldInsets;
		return slider;
	}

	private JLabel createLabel ( int gridRow, int gridCol, String text, Boolean isValueLabel )
	{
		GridBagConstraints constraints = new GridBagConstraints ( );
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.gridx = gridCol;
		constraints.gridy = gridRow;
		JLabel label = new JLabel ( text, SwingConstants.RIGHT );

		if ( isValueLabel )
		{
			label.setPreferredSize ( new Dimension ( 40, 20 ) );
		}

		add ( label, constraints );
		return label;
	}

	private void setSettingsEnabled ( boolean enabled )
	{
		if ( enabled != _isEnabled )
		{
			_isEnabled = enabled;
			int n = getComponentCount ( );
			while ( --n > -1 )
			{
				getComponent ( n ).setEnabled ( _isEnabled );
			}
		}
	}
}
