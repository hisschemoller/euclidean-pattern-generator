package com.hisschemoller.sequencer.controller.file;

import java.io.File;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hisschemoller.sequencer.model.FileProxy;
import com.hisschemoller.sequencer.model.SequencerProxy;
import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.SeqNotifications;

public class SaveProjectCommand extends SimpleCommand
{
	/**
	 * Save a project as an XML file.
	 */
	@Override public final void execute ( final INotification notification )
	{
		Document document = createXML ( );
		printXML ( document );
		saveFile ( document, notification.getName ( ) );
	}

	private Document createXML ( )
	{
		try
		{
			/** Create an empty XML document. */
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance ( );
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder ( );
			Document document = docBuilder.newDocument ( );

			SequencerProxy sequencerProxy = ( SequencerProxy ) getFacade ( ).retrieveProxy ( SequencerProxy.NAME );
			// Vector<SequenceVO> seqs = sequencerProxy.getSequences ( );

			/** Create the root element and add it to the document. */
			Element projectNode = document.createElement ( "project" );
			projectNode.setAttribute ( "tempo", Float.toString ( sequencerProxy.getTempo ( ) ) );
			projectNode.setAttribute ( "ppqn", Integer.toString ( sequencerProxy.getPulsesPerQuarterNote ( ) ) );
			document.appendChild ( projectNode );

			Element patternsNode = document.createElement ( "patterns" );
			projectNode.appendChild ( patternsNode );
			Vector<PatternVO> patterns = sequencerProxy.getPatterns ( );

			for ( int j = 0; j < patterns.size ( ); j++ )
			{
				/** Add a pattern. */
				PatternVO patternVO = patterns.get ( j );
				Element patternNode = document.createElement ( "pattern" );
				patternNode.setAttribute ( "id", patternVO.id.toString ( ) );
				patternsNode.appendChild ( patternNode );

				/** Add events element. */
				Element events = document.createElement ( "events" );
				events.setAttribute ( "notes", Integer.toString ( patternVO.fills ) );
				events.setAttribute ( "steps", Integer.toString ( patternVO.steps ) );
				events.setAttribute ( "rotation", Integer.toString ( patternVO.rotation ) );
				patternNode.appendChild ( events );

				/** Add midi element. */
				Element midi = document.createElement ( "midi" );
				midi.setAttribute ( "channel", Integer.toString ( patternVO.midiChannel ) );
				midi.setAttribute ( "pitch", Integer.toString ( patternVO.midiPitch ) );
				midi.setAttribute ( "velocity", Integer.toString ( patternVO.midiVelocity ) );
				midi.setAttribute ( "notelength", Integer.toString ( patternVO.noteLength ) );
				patternNode.appendChild ( midi );

				/** Add settings element. */
				Element settings = document.createElement ( "settings" );
				settings.setAttribute ( "solo", Boolean.toString ( patternVO.solo ) );
				settings.setAttribute ( "mute", Boolean.toString ( patternVO.mute ) );
				patternNode.appendChild ( settings );

				/** Add location element. */
				Element location = document.createElement ( "location" );
				location.setAttribute ( "x", Integer.toString ( patternVO.viewX ) );
				location.setAttribute ( "y", Integer.toString ( patternVO.viewY ) );
				patternNode.appendChild ( location );
			}

			return document;
		}
		catch ( ParserConfigurationException exception )
		{
			showMessage ( "SaveProjectCommand.execute() ParserConfigurationException: " + exception.getMessage ( ) );
		}

		return null;
	}

	private void printXML ( Document document )
	{
		try
		{
			/** Set up a transformer. */
			TransformerFactory transFactory = TransformerFactory.newInstance ( );
			Transformer transformer = transFactory.newTransformer ( );
			transformer.setOutputProperty ( OutputKeys.OMIT_XML_DECLARATION, "yes" );
			transformer.setOutputProperty ( OutputKeys.INDENT, "yes" );

			/** Create string from XML tree. */
			StringWriter stringWriter = new StringWriter ( );
			StreamResult result = new StreamResult ( stringWriter );
			DOMSource source = new DOMSource ( document );
			transformer.transform ( source, result );
			String xmlString = stringWriter.toString ( );

			/** Print XML. */
			System.out.println ( "Here's the xml:\n\n" + xmlString );
		}
		catch ( Exception exception )
		{
			showMessage ( "SaveProjectCommand.printXML() Exception: " + exception.getMessage ( ) );
		}
	}

	private void saveFile ( Document document, String saveType )
	{
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );

		if ( saveType == SeqNotifications.SAVE_PROJECT_AS || ( saveType == SeqNotifications.SAVE_PROJECT && fileProxy.getFile ( ) == null ) )
		{
			JFileChooser fileChooser = fileProxy.getFileChooser ( );
			fileChooser.setDialogTitle ( "Save Project File" );
			fileChooser.setSelectedFile ( new File ( "Euclidean Sequencer Project.xml" ) );
			int returnVal = fileChooser.showSaveDialog ( fileProxy.getDialogParent ( ) );

			if ( returnVal == JFileChooser.APPROVE_OPTION )
			{
				fileProxy.setFile ( fileChooser.getSelectedFile ( ) );
			}
			else
			{
				return;
			}
		}

		System.out.println ( "SaveProjectCommand.saveFile() file.getAbsolutePath: " + fileProxy.getFile ( ).getAbsolutePath ( ) );

		// Prepare the DOM document for writing.
		Source source = new DOMSource ( document );

		// Prepare the output file.
		Result result = new StreamResult ( fileProxy.getFile ( ) );

		try
		{
			/** Write the DOM document to the file. */
			Transformer transformer = TransformerFactory.newInstance ( ).newTransformer ( );
			transformer.setOutputProperty ( OutputKeys.INDENT, "yes" );
			transformer.transform ( source, result );
		}
		catch ( TransformerConfigurationException exception )
		{
			System.out.println ( "SaveProjectCommand.saveFile() TransformerConfigurationException: " + exception.getMessage ( ) );
		}
		catch ( TransformerException exception )
		{
			System.out.println ( "SaveProjectCommand.saveFile() TransformerException: " + exception.getMessage ( ) );
		}
	}

	private void showMessage ( String message )
	{
		System.out.println ( message );
	}
}
