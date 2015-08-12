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

import java.util.ArrayList;

public class BjorklundGenerator2
{
	public static ArrayList<Boolean> generate ( int steps, int pulses )
	{
		if ( pulses >= steps || steps == 1 || pulses == 0 )
		{
			ArrayList<Boolean> pattern = new ArrayList<Boolean> ( );
			if ( pulses >= steps )
			{
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
				for ( int i = 0; i < steps; i++ )
				{
					pattern.add ( false );
				}
			}
			return pattern;
		}

		ArrayList<ArrayList<Boolean>> bigArray = new ArrayList<ArrayList<Boolean>> ( );
		for ( int i = 0; i < pulses; i++ )
		{
			ArrayList<Boolean> boolArray = new ArrayList<Boolean> ( 1 );
			boolArray.add ( true );
			bigArray.add ( boolArray );
		}

		ArrayList<ArrayList<Boolean>> smallArray = new ArrayList<ArrayList<Boolean>> ( );
		for ( int i = 0; i < ( steps - pulses ); i++ )
		{
			ArrayList<Boolean> boolArray = new ArrayList<Boolean> ( 1 );
			boolArray.add ( false );
			smallArray.add ( boolArray );
		}

		return bjorklund ( bigArray, smallArray );
	}

	private static ArrayList<Boolean> bjorklund ( ArrayList<ArrayList<Boolean>> bigArray, ArrayList<ArrayList<Boolean>> smallArray )
	{
		/** Done. */
		if ( smallArray.size ( ) <= 1 )
		{
			/** Flatten arrays. */
			ArrayList<Boolean> resultList = new ArrayList<Boolean> ( );
			for ( int i = 0; i < bigArray.size ( ); i++ )
			{
				for ( int j = 0; j < bigArray.get ( i ).size ( ); j++ )
				{
					resultList.add ( bigArray.get ( i ).get ( j ) );
				}
			}
			for ( int i = 0; i < smallArray.size ( ); i++ )
			{
				for ( int j = 0; j < smallArray.get ( i ).size ( ); j++ )
				{
					resultList.add ( smallArray.get ( i ).get ( j ) );
				}
			}
			return resultList;
		}

		int fullRounds = ( int ) Math.floor ( smallArray.size ( ) / bigArray.size ( ) );
		for ( int i = 0; i < fullRounds; i++ )
		{
			for ( int j = 0; j < bigArray.size ( ); j++ )
			{
				ArrayList<Boolean> sourceBoolArray = smallArray.get ( 0 );
				for ( int k = 0; k < sourceBoolArray.size ( ); k++ )
				{
					bigArray.get ( j ).add ( sourceBoolArray.get ( k ) );
				}
				smallArray.remove ( 0 );
			}
		}

		int remainder = smallArray.size ( ) % bigArray.size ( );
		for ( int i = 0; i < remainder; i++ )
		{
			ArrayList<Boolean> sourceBoolArray = smallArray.get ( 0 );
			for ( int k = 0; k < sourceBoolArray.size ( ); k++ )
			{
				bigArray.get ( i ).add ( sourceBoolArray.get ( k ) );
			}
			smallArray.remove ( 0 );
		}

		/** Split result in two new arrays. */
		ArrayList<ArrayList<Boolean>> newBigArray = new ArrayList<ArrayList<Boolean>> ( );
		ArrayList<ArrayList<Boolean>> newSmallArray = new ArrayList<ArrayList<Boolean>> ( );
		for ( int i = 0; i < bigArray.size ( ); i++ )
		{
			if ( remainder == 0 )
			{
				newBigArray.add ( bigArray.get ( i ) );
			}
			else
			{
				if ( i < remainder )
				{
					newBigArray.add ( bigArray.get ( i ) );
				}
				else
				{
					newSmallArray.add ( bigArray.get ( i ) );
				}
			}
		}

		return bjorklund ( newBigArray, newSmallArray );
	}

//	private static void printArrays ( ArrayList<ArrayList<Boolean>> bigArray, ArrayList<ArrayList<Boolean>> smallArray )
//	{
//		String str = new String ( );
//
//		for ( int i = 0; i < bigArray.size ( ); i++ )
//		{
//			ArrayList<Boolean> boolArray = bigArray.get ( i );
//			for ( int j = 0; j < boolArray.size ( ); j++ )
//			{
//				str += ( boolArray.get ( j ) ) ? "x" : ".";
//			}
//		}
//
//		str += " en ";
//
//		for ( int i = 0; i < smallArray.size ( ); i++ )
//		{
//			ArrayList<Boolean> boolArray = smallArray.get ( i );
//			for ( int j = 0; j < boolArray.size ( ); j++ )
//			{
//				str += ( boolArray.get ( j ) ) ? "x" : ".";
//			}
//		}
//
//		System.out.println ( "printArrays: " + str + ", big size: " + bigArray.size ( ) + ", small size: " + smallArray.size ( ) );
//	}
}
