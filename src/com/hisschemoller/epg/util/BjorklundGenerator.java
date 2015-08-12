/**
 * Copyright 2011 Wouter Hisschemšller
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

package com.hisschemoller.epg.util;

import java.util.Vector;

public class BjorklundGenerator
{
	public static Vector<Boolean> generate ( int steps, int pulses, boolean q )
	{
		return bjorklund ( steps, pulses );
	}
	
	/**
	 * From original Max patch by Robin Price:
	 * http://registeringdomainnamesismorefunthandoingrealwork.com/blogs/?p=189
	 */
	private static Vector<Boolean> bjorklund ( int steps, int pulses )
	{
		System.out.println ( "START" );
		Vector<Boolean> pattern = new Vector<Boolean> ( );
		if ( pulses >= steps || steps == 1 || pulses == 0 )
		{
			// test for input for sanity
			if ( pulses >= steps )
			{
				/** Fill every steps with a pulse. */
				for ( int i = 0; i < steps; i++ )
				{
					pattern.add ( true );
				}
			}
			else if ( steps == 1 )
			{
				pattern.add ( pulses == 1 );
			}
			else
			{
				/** Fill every steps with a silence. */
				for ( int i = 0; i < steps; i++ )
				{
					pattern.add ( false );
				}
			}
		}
		else
		{
			// sane input
			int pauses = steps - pulses;
			
			if ( pauses >= pulses )
			{ 
				// first case more pauses than pulses
				int per_pulse = ( int ) Math.floor ( pauses / pulses );
				int remainder = pauses % pulses;
				for ( int i = 0; i < pulses; i++ )
				{
					pattern.add ( true );
					for ( int j = 0; j < per_pulse; j++ )
					{
						pattern.add ( false );
					}
					if ( i < remainder )
					{
						pattern.add ( false );
					}
					printPattern ( pattern );
				}
			}
			else
			{ 
				// second case more pulses than pauses
				int per_pause = ( int ) Math.floor ( ( pulses - pauses ) / pauses );
				int remainder = ( pulses - pauses ) % pauses;
				for ( int i = 0; i < pauses; i++ )
				{
					pattern.add ( true );
					pattern.add ( false );
					for ( int j = 0; j < per_pause; j++ )
					{
						pattern.add ( true );
					}
					if ( i < remainder )
					{
						pattern.add ( true );
					}
				}
			}
		}

		printPattern ( pattern );
		return pattern;
	}

	/**
	 * Euclid's algorithm
	 */
//	private int euclid ( int total, int hits )
//	{
//		if ( hits == 0 )
//		{
//			return total;
//		}
//		else
//		{
//			return euclid ( hits, total % hits );
//		}
//	}

	/**
	 * Print to console.
	 */
	private static void printPattern ( Vector<Boolean> pattern )
	{
		String str = "";
		int n = pattern.size ( );
		for ( int i = 0; i < n; i++ )
		{
			str += ( pattern.get ( i ) == true ) ? "x" : ".";
		}
		System.out.println ( "BjorklundGenerator pattern: " + str );
	}
}
