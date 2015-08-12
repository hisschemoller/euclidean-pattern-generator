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

public class InternalClockThread extends Thread implements IClockSource
{
	private ISequenceable _sequencer;
	private boolean _isRunning = false;
	private boolean _isActive = true;
	private long _previousTime;
	private int _pulsesSinceStart;
	private double _interval;
	private double _totalTimePassedReal;
	private double _totalTimePassedIdeal;
	
	public InternalClockThread ( ISequenceable sequencer, float newBpm, int pulsesPerQuarterNote )
	{
		_sequencer = sequencer;
		_previousTime = System.nanoTime ( );
		setTempoInBPM ( newBpm, pulsesPerQuarterNote );
		start();
	}

	public void run ( )
	{
		long restPeriod = 0;
		double timingError = 0;
		
		while ( _isActive )
		{
			if ( _isRunning )
			{
				/** The actual work is done here... */
				_sequencer.onClock ( );
				_pulsesSinceStart++;

				// Time difference since last beat & wait if necessary.
				double timePassed = ( System.nanoTime ( ) - _previousTime ) * 1.0e-6;

				// Sleep for a while.
				restPeriod = ( long ) ( _interval * .96d );
				try
				{
					if ( ( restPeriod > 1 ) && ( timePassed < restPeriod ) ) Thread.sleep ( restPeriod );
				}
				catch ( InterruptedException error )
				{
					System.out.println ( "force quit..." );
				}

				// Wake up a little early and watch the alarm clock.
				while ( timePassed < ( _interval - timingError ) )
				{
					timePassed = ( System.nanoTime ( ) - _previousTime ) * 1.0e-6;
				}

				_previousTime = System.nanoTime ( );

				_totalTimePassedReal += timePassed;
				_totalTimePassedIdeal += _interval;

				// If more time has passed between notes than should have
				// passed, then slow down things a little.
				timingError = _totalTimePassedReal - _totalTimePassedIdeal;
			}
			else
			{
				try
				{
					Thread.sleep ( 100 );
				}
				catch ( InterruptedException e )
				{
					break;
				}
			}
		}
	}

	public void startClock ( )
	{
		_pulsesSinceStart = 0;
		_totalTimePassedReal = 0;
		_totalTimePassedIdeal = 0;
		_previousTime = System.nanoTime ( );
		_isRunning = true;
	}

	public void stopClock ( )
	{
		_isRunning = false;
	}

	public void dispose ( )
	{
		_isActive = false;
	}

	public void setTempoInBPM ( float bpm, int pulsesPerQuarterNote )
	{
		_totalTimePassedReal = 0;
		_totalTimePassedIdeal = 0;
		_interval = ( 1000.0 / ( bpm / 60 ) ) / pulsesPerQuarterNote;
	}

	public void setSequencer ( ISequenceable sequencer )
	{
		_sequencer = sequencer;
	}

	public int getPulsesSinceStart ( )
	{
		return _pulsesSinceStart;
	}
}
