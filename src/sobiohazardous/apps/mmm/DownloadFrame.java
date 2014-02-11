package sobiohazardous.apps.mmm;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class DownloadFrame extends JFrame
{	
	private JPanel contentPane;
	int downloaded = 2;
	
	public DownloadFrame()
	{
		initWindow();
	}
	
	private void initWindow()
	{
		setResizable(false);
		setTitle(ModpackHandler.getInstallerName());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(500, 200, 500, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		try 
		{
			downloaded = ModpackHandler.downloadModpack(ModpackHandler.getOnlineModpackZipLink(), ModpackHandler.modpackInfoDir + "modpack.zip");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		JProgressBar pbar = new JProgressBar(0, 100);
        pbar.setValue(downloaded);
        pbar.setStringPainted(true);
        pbar.setBounds(500, 250, 200, 25);
        pbar.updateUI();
	}
}
