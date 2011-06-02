package com.hisschemoller.sequencer.util;


public class TimerThread extends Thread
{
	private ISequenceable _sequencer;
	private boolean _isRunning = false;
	private boolean _isActive = true;
	private long _previousTime;
	private float _beatsPerMinute;
	private int _pulsesPerQuarterNote;
	private int _pulsesSinceStart;
	private float _millisPerPulse;
	private double _interval;
	private double _totalTimePassedReal;
	private double _totalTimePassedIdeal;
	private double _timingError;

	public TimerThread ( ISequenceable sequencer, float newBpm )
	{
		_sequencer = sequencer;
		_previousTime = System.nanoTime ( );
		updateTime ( newBpm );
	}

	public void run ( )
	{
		long restPeriod = 0;

//		DecimalFormat decimalFormat = new DecimalFormat ( "###.##" );
//		double intervalNano = _interval / 1.0e-6;
//		long timeBeforeWork;
//		long timeAfterWork;
//		long timeAfterSleep;
//		long timeAfterLoop;

		while ( _isActive )
		{
			if ( _isRunning )
			{
				// timeBeforeWork = System.nanoTime ( );

				/** The actual work is done here... */
				_sequencer.onPulse ( );
				_pulsesSinceStart++;

				// timeAfterWork = System.nanoTime ( );

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

				// timeAfterSleep = System.nanoTime ( );

				// Wake up a little early and watch the alarm clock.
				while ( timePassed < ( _interval - _timingError ) )
				{
					timePassed = ( System.nanoTime ( ) - _previousTime ) * 1.0e-6;
				}

				// timeAfterLoop = System.nanoTime ( );

				_previousTime = System.nanoTime ( );

				_totalTimePassedReal += timePassed;
				_totalTimePassedIdeal += _interval;

				// If more time has passed between notes than should have
				// passed, then slow down things a little.
				_timingError = _totalTimePassedReal - _totalTimePassedIdeal;

//				if ( _pulsesSinceStart % 100 == 0 )
//				{
//					String work = decimalFormat.format ( ( timeAfterWork - timeBeforeWork ) / intervalNano );
//					String sleep = decimalFormat.format ( ( timeAfterSleep - timeAfterWork ) / intervalNano );
//					String loop = decimalFormat.format ( ( timeAfterLoop - timeAfterSleep ) / intervalNano );
//					System.out.println ( "work: " + work + ", sleep: " + sleep + ", loop: " + loop );
//				}
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

	public void stopTimer ( )
	{
		_isRunning = false;
	}

	public void startTimer ( )
	{
		_pulsesSinceStart = 0;
		_totalTimePassedReal = 0;
		_totalTimePassedIdeal = 0;
		_previousTime = System.nanoTime ( );
		_isRunning = true;
	}

	public void setBpm ( float newBpm )
	{
		_totalTimePassedReal = 0;
		_totalTimePassedIdeal = 0;
		updateTime ( newBpm );
	}

	public float getBpm ( )
	{
		return _beatsPerMinute;
	}

	public void setPulsesPerQuarterNote ( int pulsesPerQuarterNote )
	{
		_pulsesPerQuarterNote = pulsesPerQuarterNote;
	}

	public int getPulsesPerQuarterNote ( )
	{
		return _pulsesPerQuarterNote;
	}

	public int getPulsesSinceStart ( )
	{
		return _pulsesSinceStart;
	}

	public float getMillisPerPulse ( )
	{
		return _millisPerPulse;
	}

	public boolean getIsRunning ( )
	{
		return _isRunning;
	}

	private void updateTime ( float bpm )
	{
		_beatsPerMinute = bpm;
		_interval = ( 1000.0 / ( _beatsPerMinute / 60 ) ) / getPulsesPerQuarterNote ( );
		_millisPerPulse = new Double ( 1000 / ( getPulsesPerQuarterNote ( ) * ( _beatsPerMinute / 60 ) ) ).floatValue ( );
	}
}
