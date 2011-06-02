/* 
 PureMVC Java MultiCore Port by Ima OpenSource <opensource@ima.eu>
 Maintained by Anthony Quinault <anthony.quinault@puremvc.org>
 PureMVC - Copyright(c) 2006-08 Futurescale, Inc., Some rights reserved. 
 Your reuse is governed by the Creative Commons Attribution 3.0 License 
 */
package org.puremvc.java.multicore.interfaces;

/**
 * The interface definition for a PureMVC Facade.
 *
 * <P>
 * The Facade Pattern suggests providing a single class to act as a central
 * point of communication for a subsystem.
 * </P>
 *
 * <P>
 * In PureMVC, the Facade acts as an interface between the core MVC actors
 * (Model, View, Controller) and the rest of your application.
 * </P>
 *
 * @see org.puremvc.java.interfaces.IModel IModel
 * @see org.puremvc.java.interfaces.IView IView
 * @see org.puremvc.java.interfaces.IController IController
 * @see org.puremvc.java.interfaces.ICommand ICommand
 * @see org.puremvc.java.interfaces.INotification INotification
 */
public interface IFacade extends INotifier {
	/**
	 * Register an <code>IProxy</code> with the <code>Model</code> by name.
	 *
	 * @param proxy
	 *            the <code>IProxy</code> to be registered with the
	 *            <code>Model</code>.
	 */
	public void registerProxy(IProxy proxy);

	/**
	 * Retrieve a <code>IProxy</code> from the <code>Model</code> by name.
	 *
	 * @param proxyName
	 *            the name of the <code>IProxy</code> instance to be
	 *            retrieved.
	 * @return the <code>IProxy</code> previously regisetered by
	 *         <code>proxyName</code> with the <code>Model</code>.
	 */
	public IProxy retrieveProxy(String proxyName);

	/**
	 * Remove an <code>IProxy</code> instance from the <code>Model</code> by
	 * name.
	 *
	 * @param proxyName
	 *            the <code>IProxy</code> to remove from the
	 *            <code>Model</code>.
	 */
	public IProxy removeProxy(String proxyName);

	/**
	 * Check if a Proxy is registered.
	 *
	 * @param proxyName
	 * @return whether a Proxy is currently registered with the given <code>proxyName</code>.
	 */
	public boolean hasProxy(String proxyName);

	/**
	 * Register an <code>ICommand</code> with the <code>Controller</code>.
	 *
	 * @param noteName
	 *            the name of the <code>INotification</code> to associate the
	 *            <code>ICommand</code> with.
	 * @param commandClassRef
	 *            a reference to the <code>Class</code> of the
	 *            <code>ICommand</code>.
	 */
	public void registerCommand(String noteName, ICommand commandClassRef);

	/**
	 * Remove a previously registered <code>ICommand</code> to <code>INotification</code> mapping from the Controller.
	 *
	 * @param notificationName the name of the <code>INotification</code> to remove the <code>ICommand</code> mapping for
	 */
	public void removeCommand(String notificationName);

	/**
	 * Check if a Command is registered for a given Notification
	 *
	 * @param notificationName
	 * @return whether a Command is currently registered for the given <code>notificationName</code>.
	 */
	public boolean hasCommand(String notificationName);

	/**
	 * Register an <code>IMediator</code> instance with the <code>View</code>.
	 *
	 * @param mediator
	 *            a reference to the <code>IMediator</code> instance
	 */
	public void registerMediator(IMediator mediator);

	/**
	 * Retrieve an <code>IMediator</code> instance from the <code>View</code>.
	 *
	 * @param mediatorName
	 *            the name of the <code>IMediator</code> instance to retrievve
	 * @return the <code>IMediator</code> previously registered with the given
	 *         <code>mediatorName</code>.
	 */
	public IMediator retrieveMediator(String mediatorName);

	/**
	 * Check if a Mediator is registered or not
	 *
	 * @param mediatorName
	 * @return whether a Mediator is registered with the given <code>mediatorName</code>.
	 */
	public boolean hasMediator(String mediatorName);

	/**
	 * Remove a <code>IMediator</code> instance from the <code>View</code>.
	 *
	 * @param mediatorName
	 *            name of the <code>IMediator</code> instance to be removed.
	 */
	public IMediator removeMediator(String mediatorName);
}
