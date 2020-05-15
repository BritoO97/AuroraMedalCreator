import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

	public static String defaultImageDirectory = "";
	public static String defaultSaveDirectory = "";
	public static String[] validImageTypes = {"png", "jpg", "jpeg", "gif", "tiff", "tif"};
	
	private static String configFileName = "medalCreator.config";
	
	public static void readConfig()
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(configFileName)))
		{
			String line = reader.readLine();
			while (line != null)
			{
				if (line.trim().length() < 1)
				{
					continue;
				}
				if (line.trim().charAt(0) == '#') 
				{
					line = reader.readLine();
					continue;
				}
				String[] split = line.trim().split("=");
				if (split.length > 1)
					setConfigLine(split[0], split[1]);
				line = reader.readLine();
			}
		}
		catch(FileNotFoundException file)
		{
			// TODO: Dialog - condition.config is missing somehow - Will recreate from defaults
			
			writeConfigFile();
			readConfig();
			file.printStackTrace();
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
	
	// returns if the config has been setup or is default still
	public static boolean configSet()
	{
		if (defaultImageDirectory.equalsIgnoreCase("") || defaultSaveDirectory.equalsIgnoreCase(""))
			return false;
		
		return true;
	}
	
	public static void updateConfigString(String key, String value)
	{
		setConfigLine(key, value);
	}
	
	// this assumes that the config strings are all updated and the current in-memory version is to be written to file
	public static void writeConfigFile()
	{
		try (FileWriter writer = new FileWriter(configFileName))
		{
			writer.write("defaultImageDirectory=" + defaultImageDirectory + System.lineSeparator());
			writer.write("defaultSaveDirectory=" + defaultSaveDirectory + System.lineSeparator());
			String imgTypes = "";
			for (int i = 0; i< validImageTypes.length; i++)
			{
				imgTypes += validImageTypes[i];
				if (i != (validImageTypes.length - 1))
					imgTypes += ";";
			}
			writer.write("validImageTypes=" + imgTypes);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void setConfigLine(String key, String value)
	{
		switch (key)
		{
		case "defaultImageDirectory":
			defaultImageDirectory = value;
			break;
		case "defaultSaveDirectory":
			defaultSaveDirectory = value;
			break;
		case "validImageTypes":
			validImageTypes = value.split(";");
		}
	}
}
