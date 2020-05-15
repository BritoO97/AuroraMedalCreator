import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConditionReader {
	
	private static String conditionFileName = "conditions.config";

	public static ArrayList<String> readConditions()
	{
		ArrayList<String> conditions = new ArrayList<String>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(conditionFileName)))
		{
			String line = reader.readLine();
			while (line != null)
			{
				String condition = line.split("=")[1];
				conditions.add(condition);
				line = reader.readLine();
			}
		}
		catch(FileNotFoundException file)
		{
			// TODO: Dialog - condition.config is missing somehow
			
			file.printStackTrace();
			return null;
		}
		catch(IOException io)
		{
			io.printStackTrace();
			return null;
		}

		return conditions;
	}
	
}
