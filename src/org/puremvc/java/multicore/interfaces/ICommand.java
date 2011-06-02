/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.interfaces;

/**
 * The interface definition for a PureMVC Command.
 *
 * @see org.puremvc.java.interfaces INotification
 */
public interface ICommand extends INotifier {

	/**
	 * Execute the <code>ICommand</code>'s logic to handle a given
	 * <code>INotification</code>.
	 *
	 * @param notification
	 *            an <code>INotification</code> to handle.
	 */
	public void execute(INotification notification);
}
