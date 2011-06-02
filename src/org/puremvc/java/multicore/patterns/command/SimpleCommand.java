/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.command;

import org.puremvc.java.multicore.interfaces.ICommand;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.observer.Notifier;

/**
 * A base <code>ICommand</code> implementation.
 *
 * <P>
 * Your subclass should override the <code>execute</code> method where your
 * business logic will handle the <code>INotification</code>.
 * </P>
 *
 * @see org.puremvc.java.Controller Controller
 * @see org.puremvc.java.patterns.observer.Notification Notification
 * @see org.puremvc.java.patterns.command.MacroCommand MacroCommand
 */
public class SimpleCommand extends Notifier implements ICommand {
	/**
	 * Fulfill the use-case initiated by the given <code>INotification</code>.
	 *
	 * <P>
	 * In the Command Pattern, an application use-case typically begins with
	 * some user action, which results in an <code>INotification</code> being
	 * broadcast, which is handled by business logic in the <code>execute</code>
	 * method of an <code>ICommand</code>.
	 * </P>
	 *
	 * @param notification
	 *            the <code>INotification</code> to handle.
	 */
	public void execute(INotification notification) {
	}
}
