/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.command;

import java.util.Collection;
import java.util.Vector;

import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.observer.Notifier;


/**
 * A base <code>ICommand</code> implementation that executes other
 * <code>ICommand</code>s.
 *
 * <P>
 * A <code>MacroCommand</code> maintains an list of <code>ICommand</code>
 * Class references called <i>SubCommands</i>.
 * </P>
 *
 * <P>
 * When <code>execute</code> is called, the <code>MacroCommand</code>
 * instantiates and calls <code>execute</code> on each of its <i>SubCommands</i>
 * turn. Each <i>SubCommand</i> will be passed a reference to the original
 * <code>INotification</code> that was passed to the <code>MacroCommand</code>'s
 * <code>execute</code> method.
 * </P>
 *
 * <P>
 * Unlike <code>SimpleCommand</code>, your subclass should not override
 * <code>execute</code>, but instead, should override the
 * <code>initializeMacroCommand</code> method, calling
 * <code>addSubCommand</code> once for each <i>SubCommand</i> to be executed.
 * </P>
 *
 * <P>
 *
 * @see org.puremvc.java.core.controller.Controller Controller
 * @see org.puremvc.java.patterns.observer.Notification Notification
 * @see org.puremvc.java.patterns.command.SimpleCommand SimpleCommand
 */
public class MacroCommand extends Notifier implements ICommand {

	private Collection<ICommand> subCommands = null;

	/**
	 * Constructor.
	 *
	 * <P>
	 * You should not need to define a constructor, instead, override the
	 * <code>initializeMacroCommand</code> method.
	 * </P>
	 *
	 * <P>
	 * If your subclass does define a constructor, be sure to call
	 * <code>super()</code>.
	 * </P>
	 */
	public MacroCommand() {
		this.subCommands = new Vector<ICommand>();
		initializeMacroCommand();
	}

	/**
	 * Initialize the <code>MacroCommand</code>.
	 *
	 * <P>
	 * In your subclass, override this method to initialize the
	 * <code>MacroCommand</code>'s <i>SubCommand</i> list with
	 * <code>ICommand</code> class references like this:
	 * </P>
	 *
	 * <listing> // Initialize MyMacroCommand override protected function
	 * initializeMacroCommand( ) : void { addSubCommand(
	 * com.me.myapp.controller.FirstCommand ); addSubCommand(
	 * com.me.myapp.controller.SecondCommand ); addSubCommand(
	 * com.me.myapp.controller.ThirdCommand ); } </listing>
	 *
	 * <P>
	 * Note that <i>SubCommand</i>s may be any <code>ICommand</code>
	 * implementor, <code>MacroCommand</code>s or <code>SimpleCommands</code>
	 * are both acceptable.
	 */
	protected void initializeMacroCommand() {
	}

	/**
	 * Add a <i>SubCommand</i>.
	 *
	 * <P>
	 * The <i>SubCommands</i> will be called in First In/First Out (FIFO)
	 * order.
	 * </P>
	 *
	 * @param commandClassRef
	 *            a reference to the <code>Class</code> of the
	 *            <code>ICommand</code>.
	 */
	protected void addSubCommand(ICommand commandClassRef) {
		this.subCommands.add(commandClassRef);
	}

	/**
	 * Execute this <code>MacroCommand</code>'s <i>SubCommands</i>.
	 *
	 * <P>
	 * The <i>SubCommands</i> will be called in First In/First Out (FIFO)
	 * order.
	 *
	 * @param notification
	 *            the <code>INotification</code> object to be passsed to each
	 *            <i>SubCommand</i>.
	 */
	public void execute(INotification notification) {
		for (ICommand command : subCommands) { 
			command.initializeNotifier( multitonKey );
			command.execute( notification );
		}
	}
}
