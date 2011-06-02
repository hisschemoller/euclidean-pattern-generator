/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.core.controller;

import java.util.HashMap;
import java.util.Map;

import org.puremvc.java.multicore.core.view.View;
import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.IController;
import org.puremvc.java.multicore.interfaces.IFunction;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.observer.Observer;

/**
 * A Multiton <code>IController</code> implementation.
 *
 * <P>
 * In PureMVC, the <code>Controller</code> class follows the
 * 'Command and Controller' strategy, and assumes these
 * responsibilities:
 * <UL>
 * <LI> Remembering which <code>ICommand</code>
 * are intended to handle which <code>INotifications</code>.</LI>
 * <LI> Registering itself as an <code>IObserver</code> with
 * the <code>View</code> for each <code>INotification</code>
 * that it has an <code>ICommand</code> mapping for.</LI>
 * <LI> Creating a new instance of the proper <code>ICommand</code>
 * to handle a given <code>INotification</code> when notified by the <code>View</code>.</LI>
 * <LI> Calling the <code>ICommand</code>'s <code>execute</code>
 * method, passing in the <code>INotification</code>.</LI>
 * </UL>
 *
 * <P>
 * Your application must register <code>ICommands</code> with the
 * Controller.
 * <P>
	 * The simplest way is to subclass </code>Facade</code>,
 * and use its <code>initializeController</code> method to add your
 * registrations.
 *
 * @see org.puremvc.java.multicore.core.View View
 * @see org.puremvc.java.multicore.patterns.observer.Observer Observer
 * @see org.puremvc.java.multicore.patterns.observer.Notification Notification
 * @see org.puremvc.java.multicore.patterns.command.SimpleCommand SimpleCommand
 * @see org.puremvc.java.multicore.patterns.command.MacroCommand MacroCommand
 */
public class Controller implements IController {

	/**
	 * Mapping of Notification names to Command Class references
	 */
	protected Map<String, ICommand> commandMap;

	/**
	 * Local reference to View
	 */
	protected View view;

	/**
	 * 	 The Multiton Key for this Core
	 */
	protected String multitonKey;

	protected static Map<String, Controller> instanceMap = new HashMap<String, Controller>();

	/**
	 * Constructor.
	 *
	 * <P>
	 * This <code>IController</code> implementation is a Multiton,
	 * so you should not call the constructor
	 * directly, but instead call the static Factory method,
	 * passing the unique key for this instance
	 * <code>Controller.getInstance( multitonKey )</code>
	 *
	 * @throws Error Error if instance for this Multiton key has already been constructed
	 *
	 */
	protected Controller(String key) {
		multitonKey = key;
		instanceMap.put(multitonKey, this);
		this.commandMap = new HashMap<String, ICommand>();
		initializeController();
	}

	/**
	 * Initialize the Multiton <code>Controller</code> instance.
	 *
	 * <P>Called automatically by the constructor.</P>
	 *
	 * <P>Note that if you are using a subclass of <code>View</code>
	 * in your application, you should <i>also</i> subclass <code>Controller</code>
	 * and override the <code>initializeController</code> method in the 
	 * following way:</P>
	 *
	 * <listing>
	 *		// ensure that the Controller is talking to my IView implementation
	 *		override public function initializeController(  ) : void 
	 *		{
	 *			view = MyView.getInstance();
	 *		}
	 * </listing>
	 *
	 * @return void
	 */
	protected void initializeController() {
		this.view = View.getInstance(multitonKey);
	}

	/**
	 * <code>Controller</code> Multiton Factory method.
	 *
	 * @return the Multiton instance of <code>Controller</code>
	 */
	public synchronized static Controller getInstance(String key) {
		if (instanceMap.get(key) == null) {
			new Controller(key);
		}
		return instanceMap.get(key);
	}

	/**
	 * If an <code>ICommand</code> has previously been registered to handle a
	 * the given <code>INotification</code>, then it is executed.
	 *
	 * @param note
	 *            an <code>INotification</code>
	 */
	public void executeCommand(INotification note) {
		//No reflexion in GWT
		//ICommand commandInstance = (ICommand) commandClassRef.newInstance();
		ICommand commandInstance = (ICommand) this.commandMap.get(note.getName());
		if(commandInstance!=null){
			commandInstance.initializeNotifier(multitonKey);
			commandInstance.execute(note);
		}
	}

	/**
	 * Register a particular <code>ICommand</code> class as the handler for a
	 * particular <code>INotification</code>.
	 *
	 * <P>
	 * If an <code>ICommand</code> has already been registered to handle
	 * <code>INotification</code>s with this name, it is no longer used, the
	 * new <code>ICommand</code> is used instead.
	 * </P>
	 *
	 * The Observer for the new ICommand is only created if this the
	 * first time an ICommand has been regisered for this Notification name.
	 *
	 * @param notificationName
	 *            the name of the <code>INotification</code>
	 * @param command
	 *            an instance of <code>ICommand</code>
	 */
	public void registerCommand(String notificationName, ICommand command) {
		if (null != this.commandMap.put(notificationName, command)) return;
		this.view.registerObserver(notificationName, new Observer(new IFunction() {
			public void onNotification(INotification notification) {
				executeCommand(notification);
			}
		}, this ) );
	}

	/**
	 * Remove a previously registered <code>ICommand</code> to
	 * <code>INotification</code> mapping.
	 *
	 * @param notificationName
	 *            the name of the <code>INotification</code> to remove the
	 *            <code>ICommand</code> mapping for
	 */
	public void removeCommand(String notificationName) {
		// if the Command is registered...
		if (hasCommand(notificationName)) {
			// remove the observer
			view.removeObserver(notificationName, this);
			this.commandMap.remove(notificationName);
		}
	}

	/**
	 * Remove an IController instance
	 *
	 * @param multitonKey of IController instance to remove
	 */
	public synchronized static void removeController(String key) {
		instanceMap.remove(key);
	}

	/**
	 * Check if a Command is registered for a given Notification
	 *
	 * @param notificationName
	 * @return whether a Command is currently registered for the given <code>notificationName</code>.
	 */
	public boolean hasCommand(String notificationName) {
		return commandMap.containsKey(notificationName);
	}
}
