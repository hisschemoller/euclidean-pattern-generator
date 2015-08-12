package com.hisschemoller.epg.util;

public class MidiUtils
{
	private static final String [ ] keyNames =
	{ "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	public static String getPitchName ( int pitch )
	{
		if ( pitch > 127 || pitch < 0 )
		{
			return "illegal value";
		}
		else
		{
			int note = pitch % 12;
			int octave = pitch / 12;
			return keyNames[ note ] + ( octave - 1 );
		}
	}

	public static int get14bitValue ( int nLowerPart, int nHigherPart )
	{
		return ( nLowerPart & 0x7F ) | ( ( nHigherPart & 0x7F ) << 7 );
	}
}
