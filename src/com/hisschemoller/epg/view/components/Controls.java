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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.hisschemoller.epg.model.data.EPGEnums;
import com.hisschemoller.epg.util.EPGSwingEngine;
import com.hisschemoller.epg.view.events.IViewEventListener;
import com.hisschemoller.epg.view.events.ViewEvent;

public class Controls implements ActionListener, ChangeListener
{
	public static final long serialVersionUID = -1L;
	private Vector < IViewEventListener > _viewEventListeners = new Vector < IViewEventListener > ( );
	private JToggleButton _playButton;
	private JButton _allNotesOffButton;
	private JSlider _tempoSlider;
	private JFormattedTextField _tempoInput;
	private float _tempoValue;
	private JButton _preferencesButton;

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
		
		_preferencesButton = ( JButton ) swingEngine.find ( "PREFERENCES_BUTTON" );
		_preferencesButton.addActionListener ( this );
	}

	public void updatePlayButtonState ( EPGEnums.Playback state )
	{
		_playButton.setSelected ( state == EPGEnums.Playback.START );
	}

	public void enablePlayBackControls ( Boolean isEnabled )
	{
		_playButton.setEnabled ( isEnabled );
		_tempoInput.setEnabled ( isEnabled );
		_tempoSlider.setEnabled ( isEnabled );
	}

	public void updateTempo ( float bpm )
	{
		_tempoInput.setText ( Float.toString ( bpm ) );
		_tempoSlider.setValue ( Math.round ( bpm * 10 ) );
	}

	public float getTempoValue ( )
	{
		return _tempoValue;
	}

	public EPGEnums.Playback getPlayButtonState ( )
	{
		if ( _playButton.isSelected ( ) )
		{
			return EPGEnums.Playback.START;
		}

		return EPGEnums.Playback.STOP;
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
		else if ( event.getSource ( ) == _preferencesButton )
		{
			dispatchViewEvent ( _preferencesButton, ViewEvent.SHOW_PREFERENCES );
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
