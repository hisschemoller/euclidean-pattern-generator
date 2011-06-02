package com.hisschemoller.sequencer;

import javax.swing.JFrame;

import org.puremvc.java.multicore.patterns.facade.Facade;

import com.hisschemoller.sequencer.notification.SeqNotifications;

public class SequencerFacade extends Facade
{
	public static final String NAME = "ApplicationFacade";
	private static SequencerFacade instance = null;

	public static SequencerFacade getInstance()
	{
		if ( instance == null )
		{
			instance = new SequencerFacade ( );
		}

		return instance;
	}
	
	protected SequencerFacade ()
	{
		super ( NAME );
	}

	@Override
	protected void initializeController()
	{
		super.initializeController ( );
		
		SeqNotifications.registerCommands ( this );
	}

	public void startup(JFrame jFrame)
	{
		sendNotification ( SeqNotifications.STARTUP, jFrame );
	}
}
