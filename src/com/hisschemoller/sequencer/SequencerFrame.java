package com.hisschemoller.sequencer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.hisschemoller.sequencer.view.components.JPanelRoundedCorners;

public class SequencerFrame extends JFrame
{
	public static final long serialVersionUID = -1L;
	private SequencerFacade facade = SequencerFacade.getInstance ( );
	private JMenuBar _menuBar;
	private JMenu _fileMenu;

	public SequencerFrame ( )
	{
		super ( "Euclidean Sequencer" );
		
		System.setProperty("apple.laf.useScreenMenuBar","true");

		setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		setSize ( 800, 600 );
		setBackground ( new Color ( 0xBBBBBB ) );
		setVisible ( true );

		addComponents ( );

		facade.startup ( this );
	}

	public void addMenuBar ( )
	{
		System.setProperty("apple.laf.useScreenMenuBar","true");

		_menuBar = new JMenuBar ( );
		setJMenuBar ( _menuBar );

		_fileMenu = new JMenu ( "File" );
		_menuBar.add ( _fileMenu );

		JMenuItem menuItem = new JMenuItem ( "New Project" );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Open Project..." );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Save Project" );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Save Project As..." );
		_fileMenu.add ( menuItem );
	}

	public void addComponents ( )
	{
		Container pane = getContentPane ( );
		pane.setLayout ( new GridBagLayout ( ) );
		GridBagConstraints constraints = new GridBagConstraints ( );
		
		/** The right top panel. */
		JPanel panel = new JPanelRoundedCorners ( );
		panel.setPreferredSize ( new Dimension ( 300, 100 ) );
		panel.setMinimumSize ( new Dimension ( 100, 100 ) );
		panel.setMaximumSize ( new Dimension ( 400, 100 ) );
		panel.setBackground ( new Color ( 0xCCFFCC ) );
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.insets = new Insets ( 6, 3, 3, 6 );
		pane.add ( panel, constraints );
	}
}
