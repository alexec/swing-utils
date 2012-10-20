package com.alexecollins.swingutils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple wizard.
 * <p/>
 * Creates a simple, multi-page wizard.
 * <pre>
 * +--------------------------------------+
 * |Title                             Icon|
 * +--------------------------------------+
 * |                                      |
 * |                  Page                |
 * |                                      |
 * +--------------------------------------+
 * |   Previous Next  Finish  Cancel  Help|
 * +--------------------------------------+
 * </pre>
 * <p/>
 * Known issues:
 * <ul>
 *     <li>Not really amenable to changing page order after construction.</li>
 * </ul>
 * <p/>
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa511260.aspx
 * http://wiki.eclipse.org/User_Interface_Guidelines#Wizards
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class WizardPanel extends JPanel {

	/**
	 * Used to layout the pages.
	 */
	private final CardLayout pagesLayout = new CardLayout();
	/**
	 * Centered panel for pages.
	 */
	private final JPanel pages = new JPanel(pagesLayout);
	private final JButton next = new JButton("Next >");
	private final JButton prev = new JButton("< Back");
	private final JButton finish = new JButton("Finish");
	private final JButton cancel = new JButton("Cancel");
	private final JButton help = new JButton("Help");


	/**
	 * @param title Title for the wizard.
	 * @param icon An icon for the wizard.
	 */
	public WizardPanel(final String title, final ImageIcon icon) {
		super(new BorderLayout());

		final int pad = 10;
		final Border border = BorderFactory.createEmptyBorder(pad, pad, pad, pad);

		// white box at top
		{
			final JPanel p = new JPanel();
			final CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
							BorderFactory.createMatteBorder(0, 0, 1, 0, p.getBackground().brighter()),
							BorderFactory.createMatteBorder(0, 0, 1, 0, p.getBackground().darker()));
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.setBackground(Color.white);
			p.setBorder(compoundBorder);
			final JLabel l = new JLabel(title);
			l.setBorder(border);
			l.setFont(l.getFont().deriveFont(Font.BOLD, l.getFont().getSize() * 2));
			p.add(l);
			p.add(Box.createHorizontalGlue());
			p.add(new JLabel(icon));
			super.add(p, BorderLayout.NORTH);
		}

		// page
		{
			pages.setBorder(border);
			super.add(pages, BorderLayout.CENTER);
		}

		// buttons at bottom
		{
			final JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createMatteBorder(1, 0, 0, 0, p.getBackground().brighter()),
							BorderFactory.createMatteBorder(1, 0, 0, 0, p.getBackground().darker())),
					border));
			p.add(Box.createHorizontalGlue());
			prev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					previous();
				}
			});
			prev.setMnemonic('p');
			p.add(prev);
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					next();
				}
			});
			next.setMnemonic('n');
			p.add(next);
			p.add(Box.createRigidArea(new Dimension(pad, pad)));
			finish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					finish();
				}
			});
			finish.setMnemonic('f');

			p.add(finish);
			p.add(Box.createRigidArea(new Dimension(pad, pad)));
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					cancel();
				}
			});
			cancel.setMnemonic('c');
			p.add(cancel);
			p.add(Box.createRigidArea(new Dimension(pad, pad))) ;
			help.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					help();
				}
			});
			p.add(help);
			help.setMnemonic('h');
			super.add(p, BorderLayout.SOUTH);
		}
	}

	/**
	 * Display any help.
	 *
	 * By default we assume you'll be providing help. No help?
	 *
	 * <code>
	 *     getHelp().setEnabled(false);
	 * </code>
	 */
	protected abstract void help();

	/**
	 * @param page The new page to add.
	 */
	@Override
	public Component add(Component page) {
		pages.add(page, String.valueOf(pages.getComponentCount()));
		update();
		return page;
	}

	/**
	 * @return The currently visible page.
	 */
	public Component get() {
		for (Component component : pages.getComponents()) {
			if (component.isVisible()) {
				return component;
			}
		}
		throw new IllegalStateException("no visible page");
	}

	/**
	 * Move to the next page. Override this if you want to do any processing.
	 */
	protected void next() {
		pagesLayout.next(pages);
		update();
	}

	/**
	 * Move to the previous page. Override this if you want to do any processing.
	 */
	protected void previous() {
		pagesLayout.previous(pages);
		update();
	}

	/**
	 * Cancel the wizard.
	 *
	 * This hides the wizard by default, override to change this to something else (i.e. cancel the current task).
	 *
	 * Use the fact that the component is hidden to dispose of any containing frame if you need to.
	 */
	protected void cancel() {
		setVisible(false);
	}

	/**
	 * Finish. Override this for custom finish behaviour.
	 */
	protected void finish() {
		setVisible(false);
	}

	private void update() {
		prev.setEnabled(!pages.getComponent(0).isVisible());
		next.setEnabled(!pages.getComponent(pages.getComponentCount() - 1).isVisible());
		finish.setEnabled(!next.isEnabled());
		cancel.setEnabled(!finish.isEnabled());
	}

	public JButton getPrevious() {
		return prev;
	}

	public JButton getNext() {
		return next;
	}

	public JButton getHelp() {
		return help;
	}
}
