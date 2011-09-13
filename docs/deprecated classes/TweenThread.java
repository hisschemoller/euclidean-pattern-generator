package com.hisschemoller.sequencer.util;

import javax.swing.JComponent;

public class TweenThread extends Thread
{
	public static final int EASE_IN = 1;
	public static final int EASE_OUT = 2;
	private JComponent _target;
	private int _delay;
	private int _time;
	private int _duration;
	private int _ease;
	private float _startValue;
	private float _endValue;
	private float _value;
	private Boolean _isRunning;

	public TweenThread ( JComponent target, float duration, int fps, float startValue, float endValue, int ease )
	{
		_target = target;
		_delay = ( fps > 0 ) ? ( 1000 / fps ) : 100;
		_duration = (int) ( fps * duration );
		_startValue = startValue;
		_endValue = endValue;
		_ease = ease;
		_isRunning = true;

		start ( );
	}

	public void run ()
	{
		while (Thread.currentThread ( ) == this)
		{
			if ( _isRunning )
			{
				long currentTimeMillis = System.currentTimeMillis ( );
	
				switch (_ease)
				{
				case EASE_IN:
					_value = easeIn ( _time, _startValue, _endValue, _duration );
					break;
				case EASE_OUT:
					_value = easeOut ( _time, _startValue, _endValue, _duration );
					break;
				}
	
				_time++;
	
				if ( _time >= _duration )
				{
					_value = _endValue;
					_isRunning = false;
				}
	
				_target.repaint ( );
				
				try
				{
					currentTimeMillis += _delay;
					Thread.sleep ( Math.max ( 0, currentTimeMillis - System.currentTimeMillis ( ) ) );
				}
				catch (InterruptedException error)
				{
					break;
				}
			}
		}
	}
	
	public void endThread()
	{
		_isRunning = false;
		_target = null;
	}
	
	public float getValue()
	{
		return _value;
	}
	
	public Boolean getIsRunning()
	{
		return _isRunning;
	}

	private float easeIn ( float t, float b, float c, float d )
	{
		return -c * ( t /= d ) * ( t - 2 ) + b;
	}

	private float easeOut ( float t, float b, float c, float d )
	{
		return -c * ( t /= d ) * ( t - 2 ) + b;
	}
}
