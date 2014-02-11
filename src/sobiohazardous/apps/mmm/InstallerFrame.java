package sobiohazardous.apps.mmm;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class InstallerFrame extends JFrame
{
	private JPanel contentPane;
	private JProgressBar pbar;
	
	private static InstallerFrame instance = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) 
		{
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					int option = JOptionPane.showConfirmDialog(null, "Do you have forge installed? Download the installer for modpack version!", "Forge Download", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(option == 0)
					{
						instance = new InstallerFrame();
						instance.setVisible(true);
					}				
					if(option == 1)
					{
						ModpackHandler.openWebpage(new URI("http://files.minecraftforge.net/"));
						System.exit(0);
					}					
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static InstallerFrame getInstance()
	{
		return instance;
	}
	
	public static void setInstance(InstallerFrame instanceName)
	{
		instance = instanceName;
	}
	
	public InstallerFrame()
	{
		setResizable(false);
		setTitle(ModpackHandler.getInstallerName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 200, 1073, 628);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		final JPanel texturePane = new JPanel();
		texturePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		texturePane.setLayout(null);
		
		final JEditorPane tane = new JEditorPane();
		tane.setEditable(false);
		tane.setBounds(126, 218, 921, 166);
		texturePane.add(tane);

		final JLabel version = new JLabel("Current Version: " + ModpackHandler.getCurrentModpackVersion());
		version.setBounds(480, 565, 411, 21);
		contentPane.add(version);
		
		final JLabel modpackName = new JLabel("Modpack Name: " + ModpackHandler.getModpackName());
		modpackName.setBounds(480, 580, 411, 21);
		contentPane.add(modpackName);
		
		String newestVersionString = "Unknown";
		try 
		{
			if(ModpackHandler.getNewestVersion() != null)
			{
				newestVersionString = ModpackHandler.getNewestVersion();
			}			
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
		final JLabel newestVersionLabel = new JLabel("Newest Version: " + newestVersionString);
		newestVersionLabel.setBounds(480, 550, 411, 21);
		contentPane.add(newestVersionLabel);
		
		final JLabel logo = new JLabel("");
		logo.setBounds(225, 28, 628, 166);
		contentPane.add(logo);
		logo.setIcon(ModpackHandler.getLogo());		
				
		JButton install = new JButton("Install Current");
		install.setToolTipText("Install mods on local directory");
		install.setBounds(424,280, 225, 45);
		install.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					File dir = new File(ModpackHandler.minecraftDirString + ModpackHandler.getModpackName());
					if(!dir.exists())
					{
						dir.mkdir();
					}
					if(!dir.isDirectory())
					{
						dir.mkdirs();
					}
					if(dir.exists())
					{
						dir.delete();
					}
					ZipFile zip = new ZipFile(ModpackHandler.modpackInfoDir + "modpack.zip");
					zip.extractAll(ModpackHandler.minecraftDirString + ModpackHandler.getModpackName());
					ModpackHandler.createLauncherProfile();
					JOptionPane.showMessageDialog(InstallerFrame.getInstance(), "Successfully Installed!");
				}
				catch(Exception e2)
				{
					JOptionPane.showMessageDialog(InstallerFrame.getInstance(), "Operation Failed.");
					e2.printStackTrace();
				}
			}
		});
		contentPane.add(install);
		
		final JButton newestVersion = new JButton("Download and Install Newest");
		newestVersion.setToolTipText("Check online for the newest version");
		newestVersion.setBounds(424,220, 225, 45);
		newestVersion.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
		        int downloaded = 1;
		        
				try 
				{
					if(!ModpackHandler.getCurrentModpackVersion().equals(ModpackHandler.getNewestVersion()))
					{
						int option = JOptionPane.showConfirmDialog(InstallerFrame.getInstance(), "Newer Version of Modpack Detected! Would you like to install?", "Modpack Updater", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						//yes
						if(option == 0)
						{
							try
							{
								URL modpackUrl = new URL(ModpackHandler.getOnlineModpackZipLink());					
								File dir = new File(ModpackHandler.minecraftDirString + ModpackHandler.getModpackName());
								if(!dir.exists())
								{
									dir.mkdir();
								}
								if(!dir.isDirectory())
								{
									dir.mkdirs();
								}													
								
								//FileUtils.copyURLToFile(modpackUrl, new File(ModpackHandler.modpackInfoDir + "modpack.zip"));				        

				        		pbar = new JProgressBar(0, 100);
				                pbar.setValue(downloaded);
				                pbar.setStringPainted(true);
				                pbar.setBounds(500, 250, 200, 25);
				                contentPane.add(pbar);
				                
				        		downloaded = ModpackHandler.downloadModpack(ModpackHandler.getOnlineModpackZipLink(), ModpackHandler.modpackInfoDir + "modpack.zip");
				        					                
				                /*
						        ProgressMonitor progressMonitor = new ProgressMonitor(InstallerFrame.getInstance(), "Running a Long Task", "", 0, downloaded);
						        String message = String.format("Completed %d%%.\n", downloaded);
						        progressMonitor.setNote(message);
						        progressMonitor.setProgress(downloaded);	
						        */			                
				        		
				        		/*
								DownloadFrame dframe = new DownloadFrame();
								dframe.setVisible(true);
								*/
								
								if(dir.exists())
								{
									dir.delete();
								}
								ZipFile zip = new ZipFile(ModpackHandler.modpackInfoDir + "modpack.zip");
								zip.extractAll(ModpackHandler.minecraftDirString + ModpackHandler.getModpackName());
								FileUtils.copyURLToFile(new URL(ModpackHandler.getOnlineTexfileLink()), new File(ModpackHandler.modpackInfoDir + "modpack.txt"));
								version.setText("Current Version: " + ModpackHandler.getCurrentModpackVersion());
								ModpackHandler.createLauncherProfile();
								JOptionPane.showMessageDialog(InstallerFrame.getInstance(), "Successfully Installed!");
							}
							catch(Exception e1)
							{
								JOptionPane.showMessageDialog(InstallerFrame.getInstance(), "Operation Failed.");
								e1.printStackTrace();
							}
						}
					}
					else if(ModpackHandler.getCurrentModpackVersion().equals(ModpackHandler.getNewestVersion())) 
					{
						JOptionPane.showMessageDialog(InstallerFrame.getInstance(), "Your modpack is up to date!");
					}
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				} 
			}
		});
		contentPane.add(newestVersion);
						
		final JTextField directoryField = new JTextField();
		directoryField.setBounds(366,480,310, 30);
		directoryField.setEditable(false);
		directoryField.setText(ModpackHandler.minecraftDirString);
		contentPane.add(directoryField);
		
		final JFileChooser fc = new JFileChooser(new File(ModpackHandler.minecraftDirString));
		
		JButton directoryButton = new JButton("...");
		directoryButton.setBounds(676,480,60, 30);
		directoryButton.setToolTipText("Set your minecraft directory.");
		directoryButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = fc.showOpenDialog(InstallerFrame.getInstance());
				ModpackHandler.minecraftDirString = fc.getSelectedFile().getAbsolutePath() + "\\";
				directoryField.setText(fc.getSelectedFile().getAbsolutePath());
				fc.updateUI();
			}
		});
		contentPane.add(directoryButton);
		
		fc.setApproveButtonText("Select");
		fc.setDialogTitle("Select Your Default Minecraft Directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setSelectedFile(new File(ModpackHandler.minecraftDirString));
		fc.updateUI();
		//TODO Tidy up, ready for testing.
	}
}
