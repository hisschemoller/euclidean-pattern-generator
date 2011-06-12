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
