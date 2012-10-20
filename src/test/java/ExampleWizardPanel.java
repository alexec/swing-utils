import com.alexecollins.swingutils.FormPanel;
import com.alexecollins.swingutils.ProgressPanel;
import com.alexecollins.swingutils.WizardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class ExampleWizardPanel extends WizardPanel {

	private final ProgressPanel page1 = new ProgressPanel("Long running task.", "Explanatory text about why I need to complete some long running task now.");
	private final ProgressPanel page3 = new ProgressPanel("Long running task.", "Explanatory text about why I need to complete some long running task now.");
	private SwingWorker<Void, Void> page1Worker;
	private SwingWorker<Void, Void> page3Worker;

	public ExampleWizardPanel() {
		super("Example Wizard Panel", createIcon());
		// initial details
		{
			final JPanel p = new JPanel(new BorderLayout(0, 20));
			p.add(new JLabel("This is some explanatory text about this page. "), BorderLayout.NORTH);
			{
				final JPanel q = new FormPanel();
				q.add(new JLabel("Label 1:"));
				q.add(new JLabel("Label 2:"));
				q.add(new JTextField("You need to set the size..."));
				q.add(new JLabel("Long label 3:"));
				q.add(new JTextField("A text area."));
				q.add(new JButton("Button 1"));
				q.add(new JLabel());
				q.add(new JCheckBox("Checkbox 1"));
				q.add(new JCheckBox("Checkbox 2"));
				q.add(new JCheckBox("Checkbox 3"));
				p.add(q, BorderLayout.CENTER);
			}
			add(p);
		}
		// an intermediate page
		{
			add(page1);
		}
		// more information page
		{
			final JPanel p = new JPanel(new BorderLayout(20, 20));
			p.add(new JLabel("Some explanatory text about how we need more info."), BorderLayout.NORTH);
			final FormPanel q = new FormPanel();
			q.add(new JLabel("More info please:"));
			final ButtonGroup g = new ButtonGroup();
			q.add(new JRadioButton("Yes") {{g.add(this);setSelected(true);}});
			q.add(new JRadioButton("No") {{g.add(this);}});
			p.add(q, BorderLayout.CENTER);
			add(p);
		}
		// an final progress page
		{
			add(page3);

		}
		// final panel
		{
			final JPanel p = new JPanel(new BorderLayout(20, 20));
			p.add(new JLabel("Some explanatory text about how the task was done successfully.") {{setHorizontalAlignment(CENTER);}}, BorderLayout.CENTER);
			add(p);
		}
	}

	private static ImageIcon createIcon() {
		final ImageIcon icon = new ImageIcon(ExampleWizardPanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif"));

		BufferedImage bi = new BufferedImage(64,64, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = bi.createGraphics();
		g.drawImage(icon.getImage(), (64 - icon.getIconWidth()) / 2, (64 - icon.getIconHeight()) / 2, null);
		g.dispose();
		return new ImageIcon(bi);
	}

	@Override
	protected void help() {
		JOptionPane.showConfirmDialog(this, "Help goes here");
	}

	@Override
	protected void next() {
		super.next();

		if (get() == page1) {
			getPrevious().setEnabled(false);
			getNext().setEnabled(false);
			page1Worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					for (int i = 0; !isCancelled() && i < 5000; i+=50) {
						Thread.sleep(50);
					}
					return null;
				}

				@Override
				protected void done() {
					if (!isCancelled()) {
						next();
					} else {
						previous();
					}
				}
			};
			page1Worker.execute();
		} else if (get() == page3) {
			getPrevious().setEnabled(false);
			getNext().setEnabled(false);
			page3Worker = new SwingWorker<Void, Void>() {
				private String note;

				@Override
				protected Void doInBackground() throws Exception {
					for (int i = 0; !isCancelled() && i < 30000; i+=30) {
						setProgress(i*100/30000);
						Thread.sleep(30);
						setNote("Done " + i * 5 / 30000 + "/5");

					}
					return null;
				}

				@Override
				protected void done() {
					if (!isCancelled()) {
						next();
					} else {
						previous();
					}
				}

				public void setNote(String note) {
					firePropertyChange("note", this.note, this.note = note);
				}
			};
			page3Worker.addPropertyChangeListener(
					new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							if ("progress".equals(evt.getPropertyName())) {
								page3.setProgress((Integer) evt.getNewValue());
							}     else if ("note".equals(evt.getPropertyName()))  {
								page3.setNote(evt.getNewValue().toString());
							}
						}
					});
			page3Worker.execute();
		}
	}

	@Override
	protected void previous() {
		super.previous();
		// skip intermediate working pages
		if (get() == page1 || get() == page3) {
			super.previous();
		}
	}

	@Override
	protected void cancel() {
		if (get() == page1 && !page1Worker.isDone()) {
			page1Worker.cancel(false);
			return;
		}
		if (get() == page3 && !page3Worker.isDone()) {
			page3Worker.cancel(false);
			return;
		}
		System.exit(0);
	}

	@Override
	protected void finish() {
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		final JFrame f = new JFrame();
		f.setSize(600, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new ExampleWizardPanel());
		f.setVisible(true);
	}




}
