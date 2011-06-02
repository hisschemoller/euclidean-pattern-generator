package com.hisschemoller.sequencer.util;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class PromptComboBoxRenderer extends BasicComboBoxRenderer
{
	public static final long serialVersionUID = -1L;
	private String prompt;

	/*
	 * Set the text to display when no item has been selected.
	 */
	public PromptComboBoxRenderer ( String prompt )
	{
		this.prompt = prompt;
	}

	/*
	 * Custom rendering to display the prompt text when no item is selected
	 */
	public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
	{
		super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );

		if ( value == null ) setText ( prompt );

		return this;
	}
	
}
