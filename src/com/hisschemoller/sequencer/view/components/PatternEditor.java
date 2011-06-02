package com.hisschemoller.sequencer.view.components;

import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.hisschemoller.sequencer.model.vo.PatternVO;
import com.hisschemoller.sequencer.notification.note.PatternPositionNote;
import com.hisschemoller.sequencer.notification.note.PatternSequenceNote;
import com.hisschemoller.sequencer.util.SequencerEnums;
import com.hisschemoller.sequencer.view.events.IViewEventListener;
import com.hisschemoller.sequencer.view.events.ViewEvent;

public class PatternEditor extends JPanelRoundedCorners implements MouseListener, MouseMotionListener, ComponentListener, ActionListener
{
	public static final long serialVersionUID = -1L;
	private Vector<IViewEventListener> _viewEventListeners = new Vector<IViewEventListener> ( );
	private Vector<Pattern> _patterns = new Vector<Pattern> ( );
	private Pattern _patternUnderMouse;
	private PatternVO _patternChangesVO;
	private JPanel _settingsPanel;
	private Timer _timer;
	private float _bpm;
	private Point _dragOffset = new Point ( 0, 0 );

	public PatternEditor ( )
	{
		this ( null );
	}

	public PatternEditor ( LayoutManager layoutManager )
	{
		super ( layoutManager );
	}

	public void addPattern ( PatternVO patternVO, Boolean isAnimated )
	{
		Pattern pattern = new Pattern ( patternVO, isAnimated );
		add ( pattern );
		_patterns.add ( pattern );
	}

	public void updatePattern ( PatternVO patternVO, Pattern.Operation operation )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			Pattern pattern = _patterns.get ( n );
			if ( pattern.getID ( ) == patternVO.id )
			{
				pattern.updatePattern ( patternVO, operation );
				break;
			}
		}
	}

	public void updateAllPatterns ( PatternVO patternVO, Pattern.Operation operation )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			_patterns.get ( n ).updatePattern ( patternVO, operation );
		}
	}

	public void deletePattern ( PatternVO patternVO )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			Pattern pattern = _patterns.get ( n );
			if ( pattern.getID ( ) == patternVO.id )
			{
				remove ( pattern );
				pattern.dispose ( );
				_patterns.remove ( n );
				repaint ( pattern.getBounds ( ) );
				break;
			}
		}
	}

	public void selectPattern ( PatternVO patternVO )
	{
		if ( patternVO != null )
		{
			int n = _patterns.size ( );
			while ( --n > -1 )
			{
				Pattern pattern = _patterns.get ( n );
				pattern.select ( pattern.getID ( ) == patternVO.id );
			}
		}
	}

	/**
	 * Update pointer position on all patterns.
	 */
	public void updatePatternPositions ( PatternPositionNote [ ] positionNotes )
	{
		int m = positionNotes.length;
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			Pattern pattern = _patterns.get ( n );
			while ( --m > -1 )
			{
				if ( pattern.getID ( ) == positionNotes[ m ].patternID )
				{
					pattern.updatePosition ( positionNotes[ m ].position );
					break;
				}
			}
		}
	}

	/**
	 * Show the MIDI note that is played.
	 */
	public void updatePatternSequence ( PatternSequenceNote note )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			Pattern pattern = _patterns.get ( n );
			if ( pattern.getID ( ) == note.patternID )
			{
				pattern.updateSequence ( note );
			}
		}
	}

	public void updateTempo ( float bpm )
	{
		_bpm = bpm;
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			_patterns.get ( n ).updateTempo ( _bpm );
		}
	}

	public void updatePlayback ( SequencerEnums.Playback playback )
	{
		if ( playback == SequencerEnums.Playback.STOP )
		{
			updateTempo ( 0 );
		}
	}

	public void addSettingsToPanel ( JPanel settingsPanel )
	{
		_settingsPanel = settingsPanel;
		add ( _settingsPanel );
		_settingsPanel.setLocation ( 500, 20 );
		_settingsPanel.repaint ( _settingsPanel.getBounds ( ) );
	}

	public Point getMouseClickPosition ( )
	{
		return new Point ( getMousePosition ( ).x - Pattern.PANEL_SIZE / 2, getMousePosition ( ).y - Pattern.PANEL_SIZE / 2 );
	}

	public PatternVO getPatternChangesVO ( )
	{
		return _patternChangesVO;
	}

	public UUID getPatternUnderMouseID ( )
	{
		return _patternUnderMouse.getID ( );
	}

	public synchronized void addViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.addElement ( listener );
	}

	public synchronized void removeViewEventListener ( IViewEventListener listener )
	{
		_viewEventListeners.removeElement ( listener );
	}

	@Override public void actionPerformed ( ActionEvent event )
	{
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			_patterns.get ( n ).updateDraw ( );
			repaint ( _patterns.get ( n ).getBounds ( ) );
		}
	}

	public void mouseReleased ( MouseEvent event )
	{
		if ( _patternUnderMouse != null )
		{
			_patternChangesVO = new PatternVO ( );
			_patternChangesVO.id = _patternUnderMouse.getID ( );
			_patternChangesVO.viewX = _patternUnderMouse.getX ( );
			_patternChangesVO.viewY = _patternUnderMouse.getY ( );

			dispatchViewEvent ( ViewEvent.PATTERN_LOCATION_CHANGE );
		}
	}

	public void mouseEntered ( MouseEvent event )
	{
	}

	public void mouseExited ( MouseEvent event )
	{
		_patternUnderMouse = null;
	}

	public void mousePressed ( MouseEvent event )
	{
		/** Check if a pattern is clicked. */
		_patternUnderMouse = null;
		int n = _patterns.size ( );
		while ( --n > -1 )
		{
			Pattern pattern = _patterns.get ( n );
			int w = event.getX ( ) - pattern.getX ( ) - Pattern.PANEL_SIZE / 2;
			int h = event.getY ( ) - pattern.getY ( ) - Pattern.PANEL_SIZE / 2;
			double distance = Math.sqrt ( w * w + h * h );

			if ( distance < 20 )
			{
				_dragOffset.x = w;
				_dragOffset.y = h;
				_patternUnderMouse = pattern;
				dispatchViewEvent ( ViewEvent.PATTERN_CENTER_PRESS );
				break;
			}
		}
	}

	public void mouseClicked ( MouseEvent event )
	{
		if ( contains ( event.getPoint ( ) ) && event.getClickCount ( ) == 2 )
		{
			if ( _patternUnderMouse == null )
			{
				dispatchViewEvent ( ViewEvent.PANEL_CLICK );
			}
			// else
			// {
			// dispatchViewEvent ( ViewEvent.PATTERN_CENTER_CLICK );
			// }
		}
	}

	public void mouseDragged ( MouseEvent event )
	{
		if ( _patternUnderMouse != null )
		{
			_patternUnderMouse.setLocation ( ( event.getX ( ) - Pattern.PANEL_SIZE / 2 ) - _dragOffset.x, ( event.getY ( ) - Pattern.PANEL_SIZE / 2 ) - _dragOffset.y );
		}
	}

	public void mouseMoved ( MouseEvent event )
	{
	}

	public void componentHidden ( ComponentEvent event )
	{
	}

	public void componentMoved ( ComponentEvent event )
	{
	}

	public void componentResized ( ComponentEvent event )
	{
		_settingsPanel.setLocation ( getWidth ( ) - _settingsPanel.getSize ( ).width - 20, 20 );
	}

	public void componentShown ( ComponentEvent event )
	{
	}

	protected void dispatchViewEvent ( int id )
	{
		ViewEvent viewEvent = new ViewEvent ( this, id );
		for ( int i = 0; i < _viewEventListeners.size ( ); i++ )
		{
			( ( IViewEventListener ) _viewEventListeners.elementAt ( i ) ).viewEventHandler ( viewEvent );
		}
	}

	@Override protected void setup ( )
	{
		super.setup ( );
		addMouseListener ( this );
		addMouseMotionListener ( this );
		addComponentListener ( this );

		_timer = new Timer ( 35, this );
		_timer.start ( );
	}
}
