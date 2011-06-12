/**
 * Copyright 2011 Wouter Hisschemöller
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

package com.hisschemoller.sequencer.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

import com.hisschemoller.sequencer.model.FileProxy;

public class ShowHelpCommand extends SimpleCommand
{
	/**
	 * Delete a pattern.
	 */
	@Override public final void execute ( final INotification notification )
	{
		FileProxy fileProxy = ( FileProxy ) getFacade ( ).retrieveProxy ( FileProxy.NAME );
		JFrame frame = fileProxy.getHelpFrame ( );

		if ( frame == null )
		{
			String string = "<font face='sans-serif'><b>Euclidean Pattern Sequencer v1.0</b><br /><br />";
			string += "<b>Overview</b><br />";
			string += "This sequencer lets you generate rhythm patterns and sends those patterns as MIDI notes to connected MIDI soft- or hardware.<br /><br /><br />";
			string += "<b>Patterns</b><br />";
			string += "The patterns are generated using a simple mathematical formula called the Bjorklund algorithm. This algorithm takes two values:<br />";
			string += "<ul><li>The length the pattern should have. Measured in steps of 16th notes in this software.</li>";
			string += "<li>How many notes are played in the pattern.</li></ul>";
			string += "The algorithm then spreads the played notes as evenly as possible along the length of the pattern.<br /><br />";
			string += "An example: If the pattern is eight steps in length and has two notes, the resulting pattern would be <font face='monospace'>x...x...</font> (where ‘x’ is a played note and ‘.’ is a silent step).<br />";
			string += "And as a slightly less obvious example: A length of eight and three notes produces this pattern: <font face='monospace'>x..x..x.</font><br /><br />";
			string += "It’s much clearer if you try it out yourself.<br /><br /><br />";
			string += "<b>Creating a pattern</b><br />";
			string += "In the main empty area of the application double click on the background. This creates a new default pattern.<br /><br />";
			string += "A default pattern is sixteen steps in length and has four played notes. On screen that’s represented as the ring of sixteen circles of which four are filled. <br /><br />";
			string += "Click the <b>Play</b> button and you’ll see the pattern play: The pointer rotates to indicate the position and played steps jump out when they play.<br /><br />";
			string += "Drag the pattern’s center circle to move it around the screen.<br />";
			string += "If there’s more patterns on screen a click on the center selects that pattern for editing.<br /><br /><br />";
			string += "<b>Modifying a pattern</b><br />";
			string += "Once a pattern is created or selected, it’s settings are shown in the <b>Settings</b> panel on the right:<br />";
			string += "<ul><li><b>Steps</b> is the length of the pattern.</li>";
			string += "<li><b>Fills</b> is the number of played notes. <b>Steps</b> and <b>Fills</b> are the basic pattern settings. Each time one of them changes the algorithm is recalculated.</li>";
			string += "<li><b>Rotate</b> rotates the pattern. As you’ll note when you play with the <b>Steps</b> and <b>Fills</b> sliders, a basic pattern always starts with a note on the first step. To overcome this limitation you can rotate the pattern. The small zero at the top moves along with the pattern to indicate the original start point and a little flag appears on top as extra indication the pattern is rotated.</li></ul><br />";
			string += "<b>MIDI settings</b><br />";
			string += "This application generates MIDI notes. To hear actual sounds you’ll have to connect it to some soft- or hardware that receives MIDI and translates it to audio.<br /><br />";
			string += "Use the <b>MIDI Out</b> combobox next to the <b>Play</b> and <b>Tempo</b> controls to select the appropriate MIDI port. Which one you choose depends on how you connected your software (probably through an internal MIDI port like MIDI Yoke on Windows or the IAC Bus on OSX) or hardware (likely with a USB to MIDI interface).<br /><br />";
			string += "Each pattern has it’s own MIDI settings:";
			string += "<ul><li><b>Channel</b> is one of the sixteen channels a MIDI port has.</li>";
			string += "<li><b>Pitch</b> is one of the 128 note numbers in MIDI. For melodic sounds it’s mostly used to transfer its pitch (in the note scale), for percussive sounds each note number is often a different percussion instrument sound.</li>";
			string += "<li><b>Velocity</b> is a number from 0 to 127 that usually indicates the volume at which the note plays.</li>";
			string += "<li><b>Length</b> is the amount of time until the note is stopped.This is for now a number between 0 and 96, where 96 is one full measure (16 steps). Using 96 for one measure has to do with the internal sequencer resolution which is 24 PPQN.</li></ul><br />";
			string += "<b>Projects</b><br />";
			string += "You can save your patterns with their position and settings as a project file, which is in fact a simple XML text file. A project includes the tempo in BPM as well.<br /><br />";
			string += "To manage projects use the <b>File</b> menu. There you’ll find the familiar <i>New</i>, <i>Open</i>, <i>Save</i> and <i>Save As</i> options with their standard keyboard shortcuts.<br /><br />";
			string += "<b>External references</b><br />";
			string += "<ul><li><a href=\"http://www.hisschemoller.com\">http://www.hisschemoller.com</a> - My website, check it for updates or other interesting projects I work on.</li>";
			string += "<li><a href=\"http://cgm.cs.mcgill.ca/~godfried/publications/banff.pdf\">http://cgm.cs.mcgill.ca/~godfried/publications/banff.pdf</a> - The original article about using the Bjorklund algorithm for musical rhythms by Godfried Toussaint of McGill University (Montreal, Canada).</li>";
			string += "<li><a href=\"http://ruinwesen.com/blog?id=216\">http://ruinwesen.com/blog?id=216</a> - Interesting article on Ruin & Wesen’s website with LISP example.</li></ul>";
			string += "</font>";
			
			JTextPane textPane = new JTextPane ( );
			textPane.setPreferredSize ( new Dimension ( 500, 600 ) );
			textPane.setSize ( 500, 600 );
			textPane.setEditable ( false );
			textPane.setMargin ( new Insets ( 10, 10, 10, 10 ) );
			textPane.setContentType ( "text/html" );
			textPane.setText ( string );
			textPane.setCaretPosition ( 0 );

			JScrollPane scrollPane = new JScrollPane ( );
			scrollPane.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			scrollPane.setViewportView ( textPane );

			frame = new JFrame ( "Euclidean Sequencer Help" );
			frame.setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );
			frame.setBackground ( new Color ( 0xFFFFFF ) );
			frame.setLocation ( 200, 50 );
			frame.add ( scrollPane );
			frame.pack ( );
			frame.setVisible ( true );

			fileProxy.setHelpFrame ( frame );
		}
		else
		{
			frame.setVisible ( true );
		}
	}
}
