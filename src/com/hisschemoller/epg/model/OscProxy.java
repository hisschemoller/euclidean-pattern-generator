/**
 * Copyright 2011 Wouter Hisschemoller
 * 
 * This file is part of Euclidean Pattern Generator.
 * 
 * Euclidean Pattern Generator is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 * 
 * Euclidean Pattern Generator is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; without even the 
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Euclidean Pattern Generator.  If not, 
 * see <http://www.gnu.org/licenses/>.
 */

package com.hisschemoller.epg.model;

import netP5.NetAddress;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import oscP5.OscMessage;
import oscP5.OscP5;

public final class OscProxy extends Proxy
{
	public static String NAME = OscProxy.class.getName ( );
	private OscP5 _oscP5;
	private NetAddress _localhost;

	public OscProxy ( )
	{
		super ( NAME );
	}

	@Override public void onRegister ( )
	{
		_oscP5 = new OscP5 ( new Object ( ), 9001 );
		// setNetAddress ( 9000 ); Done in StartupCommand.
	}

	@Override public void onRemove ( )
	{
		_oscP5.stop ( );
		_oscP5.dispose ( );
	}

	public void setNetAddress ( int port )
	{
		/** Port can't be 9001. */
		port = ( port == 9001 ) ? 9000 : port;
		_localhost = new NetAddress ( "localhost", port );
	}

	public int getPort ( )
	{
		return ( _localhost != null ) ? _localhost.port ( ) : 0;
	}

	public void send ( final OscMessage oscMessage )
	{
		StringBuilder sb = new StringBuilder ( );
		sb.append ( "sending osc message " );
		sb.append ( oscMessage.addrPattern ( ) );
		sb.append ( " " );
		sb.append ( oscMessage.get ( 0 ).intValue ( ) );
		sb.append ( " " );
		sb.append ( oscMessage.get ( 1 ).intValue ( ) );
		sb.append ( " " );
		sb.append ( oscMessage.get ( 2 ).intValue ( ) );
		sb.append ( " " );
		sb.append ( oscMessage.get ( 3 ).intValue ( ) );
		sb.append ( " to " );
		sb.append ( _localhost.toString ( ) );
		System.out.println ( sb.toString ( ) );
		_oscP5.send ( oscMessage, _localhost );
	}
}
