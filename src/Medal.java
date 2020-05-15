import java.util.ArrayList;

public class Medal {

	private String name;
	private String abbreviation;
	private String score;
	private String multiple;
	private String description;
	private String imgName;
	private ArrayList<Integer> conditions;
	
	public Medal(String name, String abbreviation, String score, String multiple, String description, String imgName, ArrayList<Integer> conditions) {
		super();
		this.name = name;
		this.abbreviation = abbreviation;
		this.score = score;
		this.multiple = multiple;
		this.description = description;
		this.imgName = imgName;
		this.conditions = conditions;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getScore() {
		return score;
	}

	public String getMultiple() {
		return multiple;
	}

	public String getDescription() {
		return description;
	}

	public String getImgName() {
		return imgName;
	}

	public ArrayList<Integer> getConditions() {
		return conditions;
	}
	
	public void addCondition(int condID)
	{
		conditions.add(new Integer(condID));
	}
	
	public void removeCondition(int condID)
	{
		conditions.remove(new Integer(condID));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
