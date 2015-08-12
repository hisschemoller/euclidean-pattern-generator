package com.hisschemoller.epg.util;

import java.util.ArrayList;
import java.util.Iterator;

public class BjorklundGenerator3
{
	public static ArrayList < Boolean > generate ( int steps, int pulses )
	{
		int pauses = steps - pulses;
		if ( pulses >= steps )
		{
			return buildPatternListFilledWith ( steps, true );
		}
		else if ( steps == 1 )
		{
			return buildPatternListFilledWith ( steps, pulses == 1 );
		}
		else if ( steps == 0 || pulses == 0 )
		{
			return buildPatternListFilledWith ( steps, false );
		}
		else
		{
			ArrayList < ArrayList < Boolean >> distribution = addTo ( new ArrayList < ArrayList < Boolean >> ( ), pulses, true );
			addTo ( distribution, pauses, false );
			return splitDistributionAndContinue ( distribution, pauses );
		}
	}

	private static ArrayList < Boolean > buildPatternListFilledWith ( int steps, boolean value )
	{
		ArrayList < Boolean > arrayList = new ArrayList < Boolean > ( );
		for ( int i = 0; i < steps; i++ )
		{
			arrayList.add ( value );
		}
		return arrayList;
	}

	private static ArrayList < ArrayList < Boolean >> addTo ( ArrayList < ArrayList < Boolean >> array, int count, boolean value )
	{
		for ( int i = 0; i < count; i++ )
		{
			ArrayList < Boolean > boolArray = new ArrayList < Boolean > ( 1 );
			boolArray.add ( value );
			array.add ( boolArray );
		}
		return array;
	}

	private static ArrayList < Boolean > bjorklund ( ArrayList < ArrayList < Boolean >> distributionArray, ArrayList < ArrayList < Boolean >> remainderArray )
	{

		// Handy for debugging
		// System.out.println(toStringArrayList(distributionArray) + "|" +
		// toStringArrayList(remainderArray));

		if ( remainderArray.size ( ) <= 1 )
		{
			return flattenArrays ( distributionArray, remainderArray );
		}
		else
		{

			int fullRounds = ( int ) Math.floor ( remainderArray.size ( ) / distributionArray.size ( ) );
			int remainder = remainderArray.size ( ) % distributionArray.size ( );
			int newRemainder = remainder == 0 ? 0 : distributionArray.size ( ) - remainder;
			for ( int i = 0; i < fullRounds; i++ )
			{
				for ( int j = 0; j < distributionArray.size ( ) && !remainderArray.isEmpty ( ); j++ )
				{
					distributionArray.get ( j ).addAll ( remainderArray.remove ( 0 ) );
				}
			}
			for ( int i = 0; i < remainder; i++ )
			{
				distributionArray.get ( i ).addAll ( remainderArray.remove ( 0 ) );
			}

			return splitDistributionAndContinue ( distributionArray, newRemainder );
		}

	}

	private static ArrayList < Boolean > splitDistributionAndContinue ( ArrayList < ArrayList < Boolean >> distribution, int remainder )
	{
		ArrayList < ArrayList < Boolean >> newDistribution = new ArrayList < ArrayList < Boolean >> ( );
		ArrayList < ArrayList < Boolean >> newRemainder = new ArrayList < ArrayList < Boolean >> ( );

		if ( remainder == 0 )
		{
			newDistribution = distribution;
		}
		else
		{
			int newDistributionSize = distribution.size ( ) - remainder;
			for ( int i = 0; i < distribution.size ( ); i++ )
			{
				if ( i < newDistributionSize )
				{
					newDistribution.add ( distribution.get ( i ) );
				}
				else
				{
					newRemainder.add ( distribution.get ( i ) );
				}
			}
		}
		return bjorklund ( newDistribution, newRemainder );
	}

	private static ArrayList < Boolean > flattenArrays ( ArrayList < ArrayList < Boolean >> distribution, ArrayList < ArrayList < Boolean >> remainder )
	{
		ArrayList < Boolean > resultList = new ArrayList < Boolean > ( );
		addTo ( distribution, resultList );
		addTo ( remainder, resultList );
		return resultList;
	}

	private static void addTo ( ArrayList < ArrayList < Boolean >> distribution, ArrayList < Boolean > resultList )
	{
		for ( ArrayList < Boolean > values : distribution )
		{
			for ( Boolean value : values )
			{
				resultList.add ( value );
			}
		}
	}

	private static String toStringArrayList ( ArrayList < ArrayList < Boolean >> arrayList )
	{
		StringBuffer buffer = new StringBuffer ( );
		for ( ArrayList < Boolean > values : arrayList )
		{
			buffer.append ( "[" ).append ( toString ( values ) ).append ( "]" );
		}
		return buffer.toString ( );
	}

	private static String toString ( ArrayList < Boolean > rhythm )
	{
		Iterator < Boolean > iterator = rhythm.iterator ( );
		StringBuffer buffer = new StringBuffer ( );
		while ( iterator.hasNext ( ) )
		{
			buffer.append ( iterator.next ( ) ? "x" : "." );
			if ( iterator.hasNext ( ) )
			{
				buffer.append ( " " );
			}
		}
		return buffer.toString ( );
	}

}
