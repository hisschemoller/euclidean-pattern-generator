package com.hisschemoller.sequencer.notification.note;

import java.util.UUID;

public class PatternPositionNote
{
	public UUID patternID;
	public float position;

	public PatternPositionNote ( )
	{
	}

	public PatternPositionNote ( UUID patternID, float position )
	{
		this.patternID = patternID;
		this.position = position;
	}
}
