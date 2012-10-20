package com.alexecollins.swingutils;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;

/**
 * Layout in a form.
 * <p/>
 * Labels are on the left, other components on the right.  If you want to horizontal align the right components (e.g.
 * for checkboxes) put them into a JPanel first.
 * <p/>
 * Know issues:
 * <ul>
 *     <li>Components on the right should wrap with in container size.</li>
 * </ul>
 *
 * http://javatechniques.com/blog/gridbaglayout-example-a-simple-form-layout/
 *
 * @author alexec (alex.e.c@gmail.com)
 * @see FormPanel
 */
public class FormLayout implements LayoutManager2 {
	private final GridBagLayout l = new GridBagLayout();
	private final GridBagConstraints c = new GridBagConstraints();

	public FormLayout() {
		c.insets = new Insets(0, 5, 0, 5);
	}

	public void setInsets(Insets insets) {
		c.insets = insets;
		c.gridwidth = 1;
	}

	public void addLayoutComponent(final Component component, final Object o) {
		updateConstraints(component);
		l.addLayoutComponent(component, c);
	}

	@Override
	public Dimension maximumLayoutSize(Container container) {
		return l.maximumLayoutSize(container);
	}

	@Override
	public float getLayoutAlignmentX(Container container) {
		return l.getLayoutAlignmentX(container);
	}

	@Override
	public float getLayoutAlignmentY(Container container) {
		return l.getLayoutAlignmentX(container);
	}

	@Override
	public void invalidateLayout(Container container) {
		l.invalidateLayout(container);
	}

	private void updateConstraints(Component component) {
		if (component instanceof JLabel || component instanceof Label) {
			c.fill = NONE;
			c.anchor = EAST;
			c.gridx = 0;
			c.gridy = c.gridy++;
		} else {
			c.fill = HORIZONTAL;
			c.anchor = WEST;
			c.gridx = 1;
		}
	}

	@Override
	public void addLayoutComponent(String s, Component component) {
		l.addLayoutComponent(s, component);
	}

	@Override
	public void removeLayoutComponent(Component component) {
		l.removeLayoutComponent(component);
	}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		return l.preferredLayoutSize(container);
	}

	@Override
	public Dimension minimumLayoutSize(Container container) {
		return l.minimumLayoutSize(container);
	}

	@Override
	public void layoutContainer(Container container) {
		l.layoutContainer(container);
	}
}
