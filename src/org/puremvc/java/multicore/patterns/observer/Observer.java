/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.observer;

import org.puremvc.java.multicore.interfaces.IFunction;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.interfaces.IObserver;

/**
 * A base <code>IObserver</code> implementation.
 *
 * <P>
 * An <code>Observer</code> is an object that encapsulates information about
 * an interested object with a method that should be called when a particular
 * <code>INotification</code> is broadcast.
 * </P>
 *
 * <P>
 * In PureMVC, the <code>Observer</code> class assumes these responsibilities:
 * <UL>
 * <LI>Encapsulate the notification (callback) method of the interested object.</LI>
 * <LI>Encapsulate the notification context (this) of the interested object.</LI>
 * <LI>Provide methods for setting the notification method and context.</LI>
 * <LI>Provide a method for notifying the interested object.</LI>
 * </UL>
 *
 * @see org.puremvc.java.core.view.View View
 * @see org.puremvc.java.patterns.observer.Notification Notification
 */
public class Observer implements IObserver {

	private Object context;

	private IFunction notify;

	/**
	 * Constructor.
	 *
	 * <P>
	 * The notification method on the interested object should take one
	 * parameter of type <code>INotification</code>
	 * </P>
	 *
	 * @param notify
	 *            the notification method of the interested object
	 * @param context
	 *            the notification context of the interested object
	 */
	public Observer(IFunction notify, Object context) {
		this.setNotifyContext(context);
		this.setNotifyMethod(notify);
	}

	/**
	 * Compare an object to the notification context.
	 *
	 * @param object
	 *            the object to compare
	 * @return boolean indicating if the object and the notification context are
	 *         the same
	 */
	public boolean compareNotifyContext(Object object) {
		return this.context == object;
	}

	/**
	 * Notify the interested object.
	 *
	 * @param notification
	 *            the <code>INotification</code> to pass to the interested
	 *            object's notification method.
	 */
	public void notifyObserver(INotification notification) {
		this.getNotifyMethod().onNotification( notification );
	}

	/**
	 * Set the notification context.
	 *
	 * @param notifyContext
	 *            the notification context (this) of the interested object.
	 */
	public void setNotifyContext(Object notifyContext) {
		this.context = notifyContext;
	}

	/**
	 * Set the notification method.
	 *
	 * <P>
	 * The notification method should take one parameter of type
	 * <code>INotification</code>.
	 * </P>
	 *
	 * @param notifyMethod
	 *            the notification (callback) method of the interested object.
	 */
	public void setNotifyMethod(IFunction notifyMethod) {
		this.notify = notifyMethod;
	}

	/**
	 * Get the notification method.
	 *
	 * @return the notification (callback) method of the interested object.
	 */
	public IFunction getNotifyMethod() {
		return this.notify;
	}

	/**
	 * Get the notification context.
	 *
	 * @return the notification context (<code>this</code>) of the
	 *         interested object.
	 */
	public Object getNotifyContext() {
		return this.context;
	}

}
