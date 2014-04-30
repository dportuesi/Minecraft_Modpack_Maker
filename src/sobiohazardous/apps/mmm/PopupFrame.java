package sobiohazardous.apps.mmm;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PopupFrame extends JFrame
{
	private JPanel contents;
	private String dialogContent = "null";
	
	public PopupFrame(String dialog)
	{
		super("Downloader");
		this.dialogContent = dialog;
		loadWindow();
	}
	
	public void loadWindow()
	{
		setResizable(true);
		setTitle("Downloader");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds((1073 / 2) + 250 + 75, (628 / 2) + 75 + 35, 250, 75);
		contents = new JPanel();
		contents.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contents);
		contents.setLayout(null);
		
		final JLabel dialog = new JLabel(this.dialogContent);
		dialog.setBounds(5, 2, 100, 45);
		contents.add(dialog);
	}
}
