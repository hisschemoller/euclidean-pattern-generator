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

package com.hisschemoller.sequencer.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class SequencerEnums
{
	public enum Playback
	{
		START, STOP;
	}

	public enum MidiDevice
	{
		MIDI_IN, MIDI_OUT;
	}

	public enum Quantization
	{
		Q1 ( 1 ), Q2 ( 2 ), Q4 ( 4 ), Q8 ( 8 ), Q16 ( 16 ), Q32 ( 32 );

		private static final Map < Integer, Quantization > lookup = new HashMap < Integer, Quantization > ( );

		static
		{
			for ( Quantization q : EnumSet.allOf ( Quantization.class ) )
				lookup.put ( q.getValue ( ), q );
		}

		private int value;

		private Quantization ( int value )
		{
			this.value = value;
		}

		public int getValue ( )
		{
			return value;
		}

		public static Quantization get ( int value )
		{
			return lookup.get ( value );
		}
	}
}
