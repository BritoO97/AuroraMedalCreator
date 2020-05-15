import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

public class MedalWriter {
	
	public static void writeToFile(ArrayList<Medal> medalArray, String path)
	{
		
		try(CSVWriter writer = new CSVWriter(new FileWriter(path), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END))
		{
			for (Medal medal : medalArray)
			{
				int lineSize = 6 + medal.getConditions().size();
				String[] nextLine = new String[lineSize];
				
				nextLine[0] = validateString(medal.getName());
				nextLine[1] = validateString(medal.getDescription());
				nextLine[2] = validateString(medal.getAbbreviation());
				nextLine[3] = validateString(medal.getScore());
				nextLine[4] = validateString(medal.getMultiple());
				nextLine[5] = validateString(medal.getImgName());
				
				ArrayList<Integer> conditions = medal.getConditions();
				for (int i = 0; i < conditions.size(); i++)
				{
					nextLine[6+i] = conditions.get(i).toString();
				}
				
				writer.writeNext(nextLine);
			}
		}
		catch(IOException e)
		{
			// TODO: File could not be written to. OOPS
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static String validateString(String data)
	{
		int index = data.indexOf(',');
		if (index < 0)
			return data;
		else
		{
			return "\"" + data + "\"";
		}		
	}
	
}
