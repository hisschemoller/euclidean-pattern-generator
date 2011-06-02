package com.hisschemoller.sequencer.view.events;

import java.util.EventListener;

public interface IViewEventListener extends EventListener
{
	public abstract void viewEventHandler( ViewEvent event );
}
