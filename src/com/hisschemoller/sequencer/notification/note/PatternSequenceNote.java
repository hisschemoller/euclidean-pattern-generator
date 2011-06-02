package com.hisschemoller.sequencer.notification.note;

import java.util.UUID;

public class PatternSequenceNote
{
	public UUID patternID;
	public int stepIndex;
	public int midiStatus;

	public PatternSequenceNote ( int stepIndex, int midiStatus, UUID patternID )
	{
		this.patternID = patternID;
		this.stepIndex = stepIndex;
		this.midiStatus = midiStatus;
	}
}