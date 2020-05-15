import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class MedalReader {

	public static ArrayList<Medal> readFromFile(String path)
	{
		ArrayList<Medal> medals = new ArrayList<Medal>();
		
		try(CSVReader reader = new CSVReader(new FileReader(path)))
		{
			String[] line = reader.readNext();
			while (line != null)
			{
				String name = line[0];
				String description = line[1];
				String abbreviation = line[2];
				String score = line[3];
				String multiple = line[4];
				String imgName = line[5];
				
				ArrayList<Integer> conditions = new ArrayList<Integer>();
				
				for (int i = 6; i<line.length; i++)
				{
					conditions.add(new Integer(Integer.parseInt(line[i])));
				}
				
				
				Medal currentMedal = new Medal(name,abbreviation, score, multiple, description, imgName, conditions);
				medals.add(currentMedal);
					
				line = reader.readNext();
			}
			return medals;
		}
		catch(FileNotFoundException file)
		{
			// TODO: Dialog - Given File does not exists
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return medals;
	}
}
