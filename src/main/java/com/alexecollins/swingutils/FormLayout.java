package com.alexecollins.swingutils;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;

/**
 * Layout in a form.
 * <p/>
 * Labels are on the left, other components on the right.  If you want to horizontal align the right components (e.g.
 * for checkboxes) put them into a JPanel first.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @see FormPanel
 */
public class FormLayout extends GridBagLayout {
	private final GridBagConstraints c = new GridBagConstraints();

	public FormLayout() {
		c.insets = new Insets(0, 10, 0, 0);
	}

	public GridBagConstraints getConstraints() {
		return c;
	}

	@Override
	public void addLayoutComponent(final Component component, final Object o) {
		if (component instanceof JLabel) {
			c.fill = NONE;
			c.anchor = EAST;
			c.gridx = 0;
			c.gridy = c.gridy++;
		} else {
			c.fill = HORIZONTAL;
			c.anchor = WEST;
			c.gridx = 1;
		}
		super.addLayoutComponent(component, c);
	}
}
