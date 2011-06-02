package com.hisschemoller.sequencer.view.components;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar
{
	public static final long serialVersionUID = -1L;
	public static final String NEW = "MenuBar.NEW";
	public static final String OPEN = "MenuBar.OPEN";
	public static final String SAVE = "MenuBar.SAVE";
	public static final String SAVE_AS = "MenuBar.SAVE_AS";
	public static final String HELP = "MenuBar.HELP";
	private JMenu _fileMenu;
	private JMenu _helpMenu;

	public MenuBar ( ActionListener actionListener )
	{
		int shortcut = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
		
		_fileMenu = new JMenu ( "File" );
		add ( _fileMenu );

		JMenuItem menuItem = new JMenuItem ( "New Project" );
		menuItem.addActionListener ( actionListener );
		menuItem.setActionCommand ( NEW );
		menuItem.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_N, shortcut ) );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Open Project..." );
		menuItem.addActionListener ( actionListener );
		menuItem.setActionCommand ( OPEN );
		menuItem.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_O, shortcut ) );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Save Project" );
		menuItem.addActionListener ( actionListener );
		menuItem.setActionCommand ( SAVE );
		menuItem.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_S, shortcut ) );
		_fileMenu.add ( menuItem );
		menuItem = new JMenuItem ( "Save Project As..." );
		menuItem.addActionListener ( actionListener );
		menuItem.setActionCommand ( SAVE_AS );
		menuItem.setAccelerator ( KeyStroke.getKeyStroke ( KeyEvent.VK_S, shortcut | InputEvent.SHIFT_DOWN_MASK ) );
		_fileMenu.add ( menuItem );

		_helpMenu = new JMenu ( "Help" );
		add ( _helpMenu );

		menuItem = new JMenuItem ( "Help Contents" );
		menuItem.addActionListener ( actionListener );
		menuItem.setActionCommand ( HELP );
		_helpMenu.add ( menuItem );
	}
}
