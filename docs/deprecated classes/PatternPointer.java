package com.hisschemoller.sequencer.view.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class PatternPointer extends JPanel
{
	public static final long serialVersionUID = -1L;
	public static final float DOUBLE_PI = ( float ) Math.PI * 2;
	private int _radius;
	private int _mutedShortening = 0;
	private int _pointerLength;
	private AffineTransform _transform;
	private float _rotation = 0;
	private boolean _fill;
	private int _shapeX[] =
	{ -8, 0, 8 };
	private int _shapeY[] =
	{ 16, 50, 16 };

	public PatternPointer ( int radius )
	{
		this ( new FlowLayout ( ), radius );
	}

	public PatternPointer ( LayoutManager layoutManager, int radius )
	{
		super ( layoutManager );
		setup ( radius );
	}

	public void dispose ( )
	{
		// Nothing to do?
	}

	public void updateRotation ( int time, int length )
	{
		_rotation = ( float ) Math.PI + ( DOUBLE_PI * ( ( float ) time / length ) );
		repaint ( );
	}

	public void updateMuteState ( boolean isMuted )
	{
		_mutedShortening = isMuted ? 20 : 0;
		_pointerLength = _radius - _mutedShortening - 4;
		_shapeY[ 1 ] = _pointerLength;
		repaint ( );
	}

	public void updateSoloState ( boolean isSoloed )
	{
		_fill = isSoloed;
		repaint ( );
	}

	public void updateSize ( float ratio )
	{
		_shapeX[ 0 ] = ( int ) ( ratio * -8 );
		_shapeX[ 1 ] = 0;
		_shapeX[ 2 ] = ( int ) ( ratio * 8 );
		_shapeY[ 0 ] = ( int ) ( ratio * 16 );
		_shapeY[ 1 ] = ( int ) ( ratio * 50 );
		_shapeY[ 2 ] = _shapeY[ 0 ];

		repaint ( );
	}

	public void paintComponent ( Graphics graphics )
	{
		super.paintComponent ( graphics );

		_transform = new AffineTransform ( );
		_transform.translate ( _radius, _radius );
		_transform.rotate ( _rotation );

		Graphics2D graphics2 = ( Graphics2D ) graphics;
		graphics2.transform ( _transform );
		graphics2.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		graphics2.setStroke ( new BasicStroke ( 2 ) );

		if ( _fill )
		{
			graphics2.setColor ( new Color ( 0xFF666666 ) );
			graphics2.fillPolygon ( _shapeX, _shapeY, 3 );
		}

		graphics2.setColor ( new Color ( 0xFF333333 ) );
		graphics2.drawPolygon ( _shapeX, _shapeY, 3 );
	}

	private void setup ( int radius )
	{
		_radius = radius;
		_pointerLength = _radius - _mutedShortening - 4;

		setOpaque ( false );
		setSize ( _radius * 2, _radius * 2 );

		_transform = new AffineTransform ( );
	}
}
