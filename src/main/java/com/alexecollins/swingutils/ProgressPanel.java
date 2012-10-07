package com.alexecollins.swingutils;

import javax.swing.*;
import java.awt.*;

/**
 * Similar to a ProgressMonitor, but in panel form.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @see ProgressMonitor
 */
public class ProgressPanel extends JPanel {

	private final JLabel title = new JLabel();
	private final JLabel note = new JLabel();
	private final JProgressBar progress = new JProgressBar();

	public ProgressPanel(final String title, final String note) {
		super(new BorderLayout(10,10));
		this.title.setText(title);
		add(this.title, BorderLayout.NORTH);
		final JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		this.note.setText(note);
		p.add(this.note, BorderLayout.NORTH);
		this.progress.setIndeterminate(true);
		p.add(this.progress, BorderLayout.CENTER);
		add(p, BorderLayout.CENTER);
	}

	public void setNote(final String note) {
		this.note.setText(note);
	}

	public void setIndeterminate(final boolean indeterminate) {
		this.progress.setIndeterminate(indeterminate);
	}

	public void setProgress(final int progress) {
		this.progress.setIndeterminate(false);
		this.progress.setValue(progress);
	}
}
