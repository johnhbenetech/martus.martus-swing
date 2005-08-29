/*

The Martus(tm) free, social justice documentation and
monitoring software. Copyright (C) 2001-2004, Beneficent
Technology, Inc. (Benetech).

Martus is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later
version with the additions and exceptions described in the
accompanying Martus license file entitled "license.txt".

It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, including warranties of fitness of purpose or
merchantability.  See the accompanying Martus License and
GPL license for more details on the required license terms
for this software.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.

*/
package org.martus.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.martus.util.language.LanguageOptions;


public class UiComboBox extends JComboBox
{
	
	public UiComboBox()
	{
		initalize();
	}

	
	public UiComboBox(Object[] items)
	{
		super(items);
		initalize();
	}

	private void initalize()
	{
		setComponentOrienation();
		addKeyListener(new UiComboBoxKeyListener());
	}
	
	public void setUI(ComboBoxUI ui)
	{
		/* UGLY HACK: Under MS Windows, some fonts like Arabic extend beyond their declared bounds,
		 * so we have to pad them. If we do that in combo boxes, another Swing bug 
		 * causes the dropdown arrow to become twice as large.
		 * If we are running under MS Windows, we'll tweak the button size.
		 * But under Linux, this would cause the entire combo border to disappear, and 
		 * when using a padded language the dropdown button is half as wide as it should be */
		if(Utilities.isMSWindows())
			ui = new SlimArrowComboBoxUi();
		super.setUI(ui);
	}
	
	private void setComponentOrienation()
	{
		setComponentOrientation(UiLanguageDirection.getComponentOrientation());
		setRenderer(new UiComboListCellRenderer());
	}
	
	private class UiComboBoxKeyListener extends KeyAdapter
	{
		public void keyReleased(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				if(isPopupVisible())
					hidePopup();
				else
					showPopup();
				e.consume();
				return;
			}
			super.keyReleased(e);
		}
		
	}

	private class UiComboListCellRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			setComponentOrientation(UiLanguageDirection.getComponentOrientation());
			setHorizontalAlignment(UiLanguageDirection.getHorizontalAlignment());
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}
	}
	
	class SlimArrowComboBoxUi extends BasicComboBoxUI
	{
		protected LayoutManager createLayoutManager()
		{
			return new slimArrowLayoutManager();
		}
		
		public JButton getArrowButton()
		{
			return arrowButton;
		}
		
		public class slimArrowLayoutManager extends ComboBoxLayoutManager
		{
	        public void layoutContainer(Container parent) 
	        {
	        	super.layoutContainer(parent);
	        	
		        if(LanguageOptions.needsLanguagePadding()) 
		        {
		        	Rectangle rect = getArrowButton().getBounds();
		        	int slimArrowFactor = 2;
		        	rect.width /= slimArrowFactor;
		        	getArrowButton().setBounds(rect);
		        }
	        }
		}
	}
	
}
