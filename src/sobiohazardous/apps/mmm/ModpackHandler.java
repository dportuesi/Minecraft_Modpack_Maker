package sobiohazardous.apps.mmm;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class ModpackHandler 
{
	static String minecraftDirString = System.getenv("APPDATA")+"\\.minecraft\\";
	static String modpackInfoDir = System.getProperty("user.dir") + "\\modpack\\";
	
	public static ImageIcon getLogo()
	{
		File imgFile = new File(modpackInfoDir + "logo.png");
		BufferedImage img = null;
		try
		{
			if(imgFile.exists())
			{
				img = ImageIO.read(imgFile);
			}
			else
			{
				img = ImageIO.read(ModpackHandler.class.getResource("/nologo.png"));
			}
			
		}catch(IOException e)
		{
			System.out.println("Error getting logo");
		}
		return new ImageIcon(img);
	}
		
	public static String getCurrentModpackVersion()
	{
		String line = "";
		String version = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("version="))
					{
						version = line.substring(8);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return version;
	}
	
	public static String getModpackName()
	{
		String line = "";
		String modpackName = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("modpackname="))
					{
						modpackName = line.substring(12);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return modpackName;
	}
	
	public static String getInstallerName()
	{
		String line = "";
		String installerName = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("installername="))
					{
						installerName = line.substring(14);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		System.out.println("Modpack Name: " + installerName);
		return installerName;
	}
	
	public static String getOnlineTexfileLink()
	{
		String line = "";
		String link = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("onlinetextfile="))
					{
						link = line.substring(15);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return link;
	}
	
	public static String getOnlineModpackZipLink()
	{
		String line = "";
		String link = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("onlinemodpackzip="))
					{
						link = line.substring(17);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return link;
	}
	
	public static String getForgeVersion()
	{
		String line = "";
		String link = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("forgeversion="))
					{
						link = line.substring(13);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return link;
	}
	
	public static String getMinecraftVersion()
	{
		String line = "";
		String link = "";
		
		File file = new File(modpackInfoDir + "modpack.txt");
		
		FileInputStream fstream = null;
		try 
		{
			fstream = new FileInputStream(file);
		} catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		DataInputStream data = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(data));
		
		if(file.exists())
		{
			try 
			{
				while((line = br.readLine()) !=null)
				{
					if (line.startsWith("mcversion="))
					{
						link = line.substring(10);
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!file.exists())
		{
			System.out.println("modpack.text not found. creating!");
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e2)
			{
				System.err.println("File could not be created!");
				e2.printStackTrace();
			}
		}
		return link;
	}
	public static String getNewestVersion() throws MalformedURLException, IOException
	{
		String version = "";
		InputStream in = new URL(getOnlineTexfileLink()).openStream();
		try 
		{
			InputStreamReader inR = new InputStreamReader( in );
			BufferedReader buf = new BufferedReader( inR );
			String line;
			while ( ( line = buf.readLine() ) != null ) 
			{
				version = line.substring(8);
			}
		 } finally 
		 {
			 in.close();
		 }
		return version;
	}
	
	public static void openWebpage(URI url) 
	{
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) 
	    {
	        try 
	        {
	            desktop.browse(url);
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static void createLauncherProfile()
	{
		File launcherProfiles = new File(minecraftDirString + "launcher_profiles.json");
		JdomParser parser = new JdomParser();
		JsonRootNode jsonProfileData = null;

        try
        {
            jsonProfileData = parser.parse(Files.newReader(launcherProfiles, Charsets.UTF_8));
        }
        catch (InvalidSyntaxException e)
        {
            JOptionPane.showMessageDialog(null, "The launcher profile file is corrupted. Re-run the minecraft launcher to fix it!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e)
        {
            throw Throwables.propagate(e);
        }
        
			JsonField[] fields = new JsonField[] 
			{
	           JsonNodeFactories.field("name", JsonNodeFactories.string(getModpackName())),
	           JsonNodeFactories.field("lastVersionId", JsonNodeFactories.string(getMinecraftVersion() + "-Forge" + getForgeVersion())),
	           JsonNodeFactories.field("gameDir", JsonNodeFactories.string(minecraftDirString + getModpackName()))
			};

	        HashMap<JsonStringNode, JsonNode> profileCopy = Maps.newHashMap(jsonProfileData.getNode("profiles").getFields());
	        HashMap<JsonStringNode, JsonNode> rootCopy = Maps.newHashMap(jsonProfileData.getFields());
	        profileCopy.put(JsonNodeFactories.string(getModpackName()), JsonNodeFactories.object(fields));
	        JsonRootNode profileJsonCopy = JsonNodeFactories.object(profileCopy);

	        rootCopy.put(JsonNodeFactories.string("profiles"), profileJsonCopy);

	        jsonProfileData = JsonNodeFactories.object(rootCopy);

	        try
	        {
	            BufferedWriter newWriter = Files.newWriter(launcherProfiles, Charsets.UTF_8);
	            PrettyJsonFormatter.fieldOrderPreservingPrettyJsonFormatter().format(jsonProfileData,newWriter);
	            newWriter.close();
	        }
	        catch (Exception e)
	        {
	            JOptionPane.showMessageDialog(null, "There was a problem writing the launch profile,  is it write protected?", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	}
	
	/**
	 * returns progress
	 * @param link
	 * @return
	 * @throws IOException 
	 */
	public static int downloadModpack(String link, String dest) throws IOException
	{
		URL dl = null;
        File fl = null;
        OutputStream os = null;
        InputStream is = null;
        int downloaded = 0;
        try 
        {
            fl = new File(dest);
            dl = new URL(link);
            os = new FileOutputStream(fl);
            is = dl.openStream();

            DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
            dcount.setListener(new ActionListener() 
    		{
    			public void actionPerformed(ActionEvent e) 
    			{
    				
    			}
    		});				    								    		

            // this line give you the total length of source stream as a String.
            // you may want to convert to integer and store this value to
            // calculate percentage of the progression.
            //dl.openConnection().getHeaderField("Content-Length");
            
            downloaded = Integer.parseInt(dl.openConnection().getHeaderField("Content-Length"));

            // begin transfer by writing to dcount, not os.
            IOUtils.copy(is, dcount);
        } 
        catch (Exception e1) 
        {
            System.out.println(e1);
        } 
        finally 
        {
            if (os != null) 
            { 
                os.close(); 
            }
            if (is != null) 
            { 
                is.close(); 
            }
        }
        return downloaded;
	}
}
