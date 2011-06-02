package com.hisschemoller.sequencer.view.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Vector;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hisschemoller.sequencer.util.PromptComboBoxRenderer;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class Controls extends JPanelRoundedCorners implements ActionListener, ChangeListener
{
	public static final long serialVersionUID = -1L;
	private Vector<IViewEventListener> _viewEventListeners = new Vector<IViewEventListener> ( );
	private JToggleButton _playButton;
	private JSlider _tempoSlider;
	private JFormattedTextField _tempoInput;
	private JComboBox _midiInBox;
	private JComboBox _midiOutBox;
	private float _tempoValue;

	public Controls ( )
	{
		this ( new GridBagLayout ( ) );
	}

	public Controls ( LayoutManager layoutManager )
	{
		super ( layoutManager );
	}

	/**
	 * Fill MIDI devices comboboxes.
	 */
	public void updateMidiDevices ( MidiDevice.Info[] midiDeviceInfo )
	{
		_midiInBox.removeAllItems ( );
		_midiOutBox.removeAllItems ( );

		try
		{
			int n = midiDeviceInfo.length;
			for ( int i = 0; i < n; i++ )
			{
				MidiDevice midiDevice = MidiSystem.getMidiDevice ( midiDeviceInfo[ i ] );

				if ( midiDevice.getMaxTransmitters ( ) != 0 )
				{
					_midiInBox.addItem ( midiDeviceInfo[ i ] );
				}
				if ( midiDevice.getMaxReceivers ( ) != 0 )
				{
					_midiOutBox.addItem ( midiDeviceInfo[ i ] );
				}
			}

			_midiInBox.setSelectedIndex ( -1 );
			_midiOutBox.setSelectedIndex ( -1 );
		}
		catch ( MidiUnavailableException exception )
		{
			System.out.println ( "MIDI device unavailable: " + exception.getMessage ( ) );
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
	
	public void updateResolution( int resolution )
	{
		
	}

	public float getTempoValue ( )
	{
		return _tempoValue;
	}

	public MidiDevice.Info getMidiInSelectedItem ( )
	{
		return ( MidiDevice.Info ) _midiInBox.getSelectedItem ( );
	}

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
		else if ( event.getSource ( ) == _midiInBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiInBox, ViewEvent.MIDI_IN_DEVICE_SELECT );
		}
		else if ( event.getSource ( ) == _midiOutBox && event.getModifiers ( ) != 0 )
		{
			dispatchViewEvent ( _midiOutBox, ViewEvent.MIDI_OUT_DEVICE_SELECT );
		}
	}

	public void stateChanged ( ChangeEvent event )
	{
		if ( event.getSource ( ) == _tempoSlider )
		{
			_tempoValue =  _tempoSlider.getValue ( ) / 10f;
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

	protected void setup ( )
	{
		super.setup ( );

		GridBagConstraints constraints = new GridBagConstraints ( );

		_playButton = new JToggleButton ( "Play" );
		_playButton.addActionListener ( this );
		constraints.gridx = 0;
		constraints.gridy = 0;
		add ( _playButton, constraints );

		_tempoSlider = new JSlider ( SwingConstants.HORIZONTAL, 300, 1700, 1200 );
		_tempoSlider.addChangeListener ( this );
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.LINE_START;
		add ( _tempoSlider, constraints );

		JLabel label = new JLabel ( "Tempo in BPM" );
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.insets = new Insets ( 0, 10, 0, 0 );
		add ( label, constraints );

		_tempoInput = new JFormattedTextField ( NumberFormat.getInstance ( ) );
		_tempoInput.setHorizontalAlignment ( JTextField.RIGHT );
		_tempoInput.addActionListener ( this );
		_tempoInput.setPreferredSize ( new Dimension ( 60, _tempoInput.getPreferredSize ( ).height ) );
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.insets = new Insets ( 0, 0, 0, 10 );
		constraints.anchor = GridBagConstraints.LINE_END;
		add ( _tempoInput, constraints );

		constraints.insets = new Insets ( 0, 0, 0, 0 );

		label = new JLabel ( "MIDI in" );
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_END;
		add ( label, constraints );

		label = new JLabel ( "MIDI out" );
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		add ( label, constraints );

		_midiInBox = new JComboBox ( );
		_midiInBox.setRenderer ( new PromptComboBoxRenderer ( "Select..." ) );
		_midiInBox.addActionListener ( this );
		_midiInBox.setPreferredSize ( new Dimension ( 100, _midiInBox.getPreferredSize ( ).height ) );
		_midiInBox.setMaximumSize ( new Dimension ( 100, _midiInBox.getPreferredSize ( ).height ) );
		_midiInBox.setEnabled ( false );
		constraints.gridx = 4;
		constraints.gridy = 0;
		add ( _midiInBox, constraints );

		_midiOutBox = new JComboBox ( );
		_midiOutBox.setRenderer ( new PromptComboBoxRenderer ( "Select..." ) );
		_midiOutBox.addActionListener ( this );
		_midiOutBox.setPreferredSize ( new Dimension ( 100, _midiOutBox.getPreferredSize ( ).height ) );
		_midiOutBox.setMaximumSize ( new Dimension ( 100, _midiOutBox.getPreferredSize ( ).height ) );
		constraints.gridx = 4;
		constraints.gridy = 1;
		add ( _midiOutBox, constraints );
	}
}
