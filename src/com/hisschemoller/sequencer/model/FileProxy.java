package com.hisschemoller.sequencer.model;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

public class FileProxy extends Proxy
{
	public static final String NAME = FileProxy.class.getName ( );
	private JFileChooser _fileChooser;
	private File _file;
	private JFrame _frame;
	private JFrame _helpFrame;

	public FileProxy ( JFrame frame )
	{
		super ( NAME );
		_frame = frame;
	}

	@Override public final void onRegister ( )
	{
		_fileChooser = new JFileChooser ( );
	}

	@Override public final void onRemove ( )
	{
		_fileChooser = null;
	}

	public JFileChooser getFileChooser ( )
	{
		return _fileChooser;
	}

	public JFrame getDialogParent ( )
	{
		return _frame;
	}

	public void setFile ( File _file )
	{
		this._file = _file;
	}

	public File getFile ( )
	{
		return _file;
	}

	public void setHelpFrame ( JFrame frame )
	{
		_helpFrame = frame;
	}

	public JFrame getHelpFrame ( )
	{
		return _helpFrame;
	}
}
