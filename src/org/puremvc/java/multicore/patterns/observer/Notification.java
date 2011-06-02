/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.patterns.observer;

import org.puremvc.java.multicore.interfaces.INotification;

/**
 * A base <code>INotification</code> implementation.
 *
 * <P>
 * PureMVC does not rely upon underlying event models such as the one provided
 * with Flash, and ActionScript 3 does not have an inherent event model.
 * </P>
 *
 * <P>
 * The Observer Pattern as implemented within PureMVC exists to support
 * event-driven communication between the application and the actors of the MVC
 * triad.
 * </P>
 *
 * <P>
 * Notifications are not meant to be a replacement for Events in
 * Flex/Flash/Apollo. Generally, <code>IMediator</code> implementors place
 * event listeners on their view components, which they then handle in the usual
 * way. This may lead to the broadcast of <code>Notification</code>s to
 * trigger <code>ICommand</code>s or to communicate with other
 * <code>IMediators</code>. <code>IProxy</code> and <code>ICommand</code>
 * instances communicate with each other and <code>IMediator</code>s by
 * broadcasting <code>INotification</code>s.
 * </P>
 *
 * <P>
 * A key difference between Flash <code>Event</code>s and PureMVC
 * <code>Notification</code>s is that <code>Event</code>s follow the
 * 'Chain of Responsibility' pattern, 'bubbling' up the display hierarchy until
 * some parent component handles the <code>Event</code>, while PureMVC
 * <code>Notification</code>s follow a 'Publish/Subscribe' pattern. PureMVC
 * classes need not be related to each other in a parent/child relationship in
 * order to communicate with one another using <code>Notification</code>s.
 *
 * @see org.puremvc.java.patterns.observer.Observer Observer
 *
 */
public class Notification implements INotification {

	// the name of the notification instance
	private String name = null;

	// the type of the notification instance
	private String type = null;

	// the body of the notification instance
	private Object body = null;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            name of the <code>Notification</code> instance. (required)
	 * @param body
	 *            the <code>Notification</code> body. (optional)
	 * @param type
	 *            the type of the <code>Notification</code> (optional)
	 */
	public Notification(String name, Object body, String type) {
		this.name = name;
		this.body = body;
		this.type = type;
	}

	/**
	 * Constructor.
	 *
	 * @param name
	 *            name of the <code>Notification</code> instance. (required)
	 */
	public Notification(String name) {
		this.name = name;
		this.body = null;
		this.type = null;
	}

	/**
	 * Constructor.
	 *
	 * @param name
	 *            name of the <code>Notification</code> instance. (required)
	 * @param body
	 *            the <code>Notification</code> body. (optional)
	 */
	public Notification(String name, Object body) {
		this.name = name;
		this.body = body;
		this.type = null;
	}

	/**
	 * Get the body of the <code>Notification</code> instance.
	 *
	 * @return the body object.
	 */
	public Object getBody() {
		return this.body;
	}

	/**
	 * Get the name of the <code>Notification</code> instance.
	 *
	 * @return the name of the <code>Notification</code> instance.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the type of the <code>Notification</code> instance.
	 *
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set the body of the <code>Notification</code> instance.
	 * @param body
	 */
	public void setBody(Object body) {
		this.body = body;
	}

	/**
	 * Set the type of the <code>Notification</code> instance.
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the string representation of the <code>Notification</code>
	 * instance.
	 *
	 * @return the string representation of the <code>Notification</code>
	 *         instance.
	 */
	public String toString() {
		String result = "Notification Name: " + getName() + " Body:";
		if (this.body != null) {
			result += this.body.toString() + " Type:";
		} else {
			result += "null Type:";
		}
		if (this.type != null) {
			result += this.type;
		} else {
			result += "null ";
		}
		return result;
	}
}
