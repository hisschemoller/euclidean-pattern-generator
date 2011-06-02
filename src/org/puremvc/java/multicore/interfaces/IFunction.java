/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.interfaces;

/**
 * This interface must be implemented by all classes that want to be notified of
 * a notification.
 */
public interface IFunction {

	/**
	 * @param notification
	 */
	public void onNotification(INotification notification);
}
