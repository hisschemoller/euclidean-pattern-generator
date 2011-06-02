package com.hisschemoller.sequencer.model.vo;

import java.util.UUID;

public class SettingsVO
{
	public UUID patternID;
	public int steps;
	public int fills;
	public int rotation;
	public int midiChannel;
	public int midiPitch;
	public int midiVelocity;
	public int noteLength;
	public boolean mute;
	public boolean solo;
}