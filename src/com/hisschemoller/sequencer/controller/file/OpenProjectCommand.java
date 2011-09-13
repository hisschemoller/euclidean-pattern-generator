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

package com.hisschemoller.sequencer.controller.file;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hisschemoller.sequencer.model.FileProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;
import com.hisschemoller.sequencer.util.BjorklundGenerator2;

public class OpenProjectCommand extends SimpleCommand
{
	/**
	 * Choose and open a project.
	 */
	@Override public final void execute ( final INotification notification )
	{
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );
		JFileChooser fileChooser = fileProxy.getFileChooser ( );
		JFrame frame = fileProxy.getDialogParent ( );
		fileChooser.resetChoosableFileFilters ( );
		FileNameExtensionFilter filter = new FileNameExtensionFilter ( "Project XML Files", "xml" );
		fileChooser.setFileFilter ( filter );
		int returnValue = fileChooser.showOpenDialog ( frame );

		if ( returnValue == JFileChooser.APPROVE_OPTION )
		{
			fileProxy.setFile ( fileChooser.getSelectedFile ( ) );
			System.out.println ( "OpenProjectCommand.execute() File opened: " + fileProxy.getFile ( ).getName ( ) );
		}
		else
		{
			return;
		}

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance ( );

		try
		{
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder ( );
			parseXML ( docBuilder, fileProxy.getFile ( ) );
		}
		catch ( ParserConfigurationException exception )
		{
			showMessage ( "OpenProjectCommand.execute() ParserConfigurationException: " + exception.getMessage ( ) );
		}

	}

	/**
	 * Parse XML found in opened file.
	 */
	private void parseXML ( DocumentBuilder docBuilder, File file )
	{
		try
		{
			Document document = docBuilder.parse ( file );
			document.getDocumentElement ( ).normalize ( );
			readXML ( document );
		}
		catch ( Exception exception )
		{
			showMessage ( "OpenProjectCommand.parseXML: IOException, SAXException or IllegalArgumentException: " + exception.getMessage ( ) );
		}
	}

	/**
	 * Create objects based on XML.
	 */
	private void readXML ( Document document )
	{
		Element documentElement = document.getDocumentElement ( );

		if ( documentElement.getNodeName ( ) != "project" )
		{
			showMessage ( "OpenProjectCommand.readXML: Wrong document element. This is not a Project XML file." );
			return;
		}

		NodeList patternNodes = document.getElementsByTagName ( "pattern" );
		if ( patternNodes.getLength ( ) == 0 )
		{
			showMessage ( "OpenProjectCommand.readXML: 	No pattern nodes. This is not a Project XML file." );
			return;
		}

		/** Clear all old data from the sequencer. */
		SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
		sequencerProxy.clear ( );

		int ppqn = Integer.parseInt ( documentElement.getAttribute ( "ppqn" ) );
		sendNotification ( SeqNotifications.UPDATE_RESOLUTION, ( ppqn > 0 ) ? ppqn : 24 );

		float tempo = Float.parseFloat ( documentElement.getAttribute ( "tempo" ) );
		sendNotification ( SeqNotifications.UPDATE_TEMPO, ( tempo > 0 ) ? tempo : 120 );

		Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

		/** Loop through the patterns. */
		for ( int j = 0; j < patternNodes.getLength ( ); j++ )
		{

			Node patternNode = patternNodes.item ( j );
			if ( patternNode.getNodeType ( ) == Node.ELEMENT_NODE )
			{
				Element pattern = ( Element ) patternNode;
				patterns.add ( createPattern ( pattern, ppqn / 4 ) );
			}
		}

		/** Set first pattern as the selected one. */
		sequencerProxy.setSelectedPattern ( patterns.get ( 0 ) );

		/** Notify so patterns will be drawn. */
		Vector<PatternVO> allPatterns = sequencerProxy.getPatterns ( );
		for ( int i = 0; i < allPatterns.size ( ); i++ )
		{
			sendNotification ( SeqNotifications.PATTERN_CREATED, allPatterns.get ( i ), Boolean.toString ( false ) );
			showMessage ( allPatterns.get ( i ).toString ( ) );
		}

		/** Select first pattern. */
		sendNotification ( SeqNotifications.SELECT_PATTERN, allPatterns.get ( 0 ) );

		/** Create position notes array of new length. */
		sendNotification ( SeqNotifications.UPDATE_POSITION_NOTES );
	}

	/**
	 * Create pattern VO from XML.
	 */
	private PatternVO createPattern ( Element pattern, int quantization )
	{
		PatternVO patternVO = new PatternVO ( );

		try
		{
			patternVO.id = UUID.fromString ( pattern.getAttribute ( "id" ) );
		}
		catch ( IllegalArgumentException exception )
		{
			patternVO.id = UUID.randomUUID ( );
		}

		NodeList eventsList = pattern.getElementsByTagName ( "events" );
		if ( eventsList.getLength ( ) > 0 )
		{
			Element events = ( Element ) eventsList.item ( 0 );
			if ( events.getNodeType ( ) == Node.ELEMENT_NODE )
			{
				patternVO.steps = Integer.parseInt ( events.getAttribute ( "steps" ) );
				patternVO.fills = Integer.parseInt ( events.getAttribute ( "notes" ) );
				patternVO.rotation = Integer.parseInt ( events.getAttribute ( "rotation" ) );
			}
		}

		NodeList midiList = pattern.getElementsByTagName ( "midi" );
		if ( midiList.getLength ( ) > 0 )
		{
			Element midi = ( Element ) midiList.item ( 0 );
			if ( midi.getNodeType ( ) == Node.ELEMENT_NODE )
			{
				patternVO.midiChannel = Integer.parseInt ( midi.getAttribute ( "channel" ) );
				patternVO.midiPitch = Integer.parseInt ( midi.getAttribute ( "pitch" ) );
				patternVO.midiVelocity = Integer.parseInt ( midi.getAttribute ( "velocity" ) );
				patternVO.noteLength = Integer.parseInt ( midi.getAttribute ( "notelength" ) );
			}
		}

        NodeList oscList = pattern.getElementsByTagName ( "osc" );
        if ( oscList.getLength ( ) > 0 )
        {
            Element osc = ( Element ) oscList.item ( 0 );
            if ( osc.getNodeType ( ) == Node.ELEMENT_NODE )
            {
                patternVO.address = osc.getAttribute ( "address" );
            }
        }

		NodeList settingsList = pattern.getElementsByTagName ( "settings" );
		if ( settingsList.getLength ( ) > 0 )
		{
			Element settings = ( Element ) settingsList.item ( 0 );
			if ( settings.getNodeType ( ) == Node.ELEMENT_NODE )
			{
				patternVO.solo = Boolean.parseBoolean ( settings.getAttribute ( "solo" ) );
				patternVO.mute = Boolean.parseBoolean ( settings.getAttribute ( "mute" ) );
			}
		}

		NodeList locationList = pattern.getElementsByTagName ( "location" );
		if ( locationList.getLength ( ) > 0 )
		{
			Element location = ( Element ) locationList.item ( 0 );
			if ( location.getNodeType ( ) == Node.ELEMENT_NODE )
			{
				patternVO.viewX = Integer.parseInt ( location.getAttribute ( "x" ) );
				patternVO.viewY = Integer.parseInt ( location.getAttribute ( "y" ) );
			}
		}

		patternVO.quantization = quantization;
		patternVO.length = patternVO.steps * quantization;
		patternVO.position = 0;

		/** Generate the Euclid / Bjorklund pattern. */
		ArrayList<Boolean> bjorklund = BjorklundGenerator2.generate ( patternVO.steps, patternVO.fills );

		/** Add events to the pattern. */
		for ( int i = 0; i < patternVO.steps; i++ )
		{
			if ( bjorklund.get ( i ) == true )
			{
				try
				{
					ShortMessage message = new ShortMessage ( );
					message.setMessage ( ShortMessage.NOTE_ON, patternVO.midiChannel, patternVO.midiPitch, patternVO.midiVelocity );
					MidiEvent midiEvent = new MidiEvent ( message, i * patternVO.quantization );

					patternVO.events.add ( midiEvent );
				}
				catch ( InvalidMidiDataException exception )
				{
					System.out.println ( "CreatePatternCommand.execute() InvalidMidiDataException: " + exception.getMessage ( ) );
				}
			}
		}

		return patternVO;
	}

	private void showMessage ( String message )
	{
		System.out.println ( message );
	}
}
