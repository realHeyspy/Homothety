package schoolFile;

import java.util.ArrayList;

public class HomothetyObject {
	public String groupData;
	public int numberRule;
	public ArrayList<String> CharterOD;

	public HomothetyObject(String groupData, int numberRule) {
		super();
		this.groupData = groupData;
		this.numberRule = numberRule;
		this.CharterOD = new ArrayList<>();
	}
}
