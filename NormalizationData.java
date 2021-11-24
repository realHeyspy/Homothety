package schoolFile;

import java.util.ArrayList;

public class NormalizationData {
	// normalization => into ! and v
	public static String ConvertImplied(String DataInput) {
		int numberReplace = DataInput.indexOf("=>");
		DataInput = DataInput.replace("=>", "v");
		StringBuffer str = new StringBuffer(DataInput);
		if (numberReplace == 1) {
			str.insert(numberReplace - 1, "!");
		}
		if (numberReplace != 1) {
			String ReplaceCut = str.substring(0, numberReplace);
			char[] data = ReplaceCut.toCharArray();
			int count = 0;
			for (int i = 0; i < data.length; i++) {
				if (Character.isLetter(data[i]) && Character.isUpperCase(data[i])) {
					str.insert(i + count, "!");
					count++;
				}
				if (data[i] == '^') {
					str.setCharAt(i + count, 'v');
				}
				if (data[i] == 'v') {
					str.setCharAt(i + count, '^');
				}
			}
		}
		return str.toString();
	}

	// normalization <=> into simple !, v and ^
	public static String ConvertEquivalent(String DataInput) {
		int numberReplace = DataInput.indexOf("<=>");
		String firstPath = DataInput.replace("<=>", "v");
		String secondPath = "" + DataInput.substring(0, numberReplace) + "v"
				+ DataInput.substring(numberReplace + 3, DataInput.length());
		DataInput = "(!" + firstPath + ")^(!" + secondPath + ")";
		return DataInput;
	}

	//
	public static String SimplifiedEveryandOnly(String DataInput) {
		return DataInput;
	}
	
	public static String ConvertNegativeOfPC(String Datainput) {
		int numberReplace = Datainput.indexOf("!(");
		StringBuffer str = new StringBuffer(Datainput);
		char[] data = Datainput.toCharArray();
		int count = 0;
		for (int i = 0; i < data.length; i++) {
			if (Character.isLetter(data[i]) && Character.isUpperCase(data[i])) {
				str.insert(i + count, "!");
				count++;
			}
			if (data[i] == '^') {
				str.setCharAt(i + count, 'v');
			}
			if (data[i] == 'v') {
				str.setCharAt(i + count, '^');
			}
		}
		str.deleteCharAt(str.length()-1);
		str.delete(numberReplace, numberReplace+2);
		Datainput = str.toString();
		return Datainput;
	}

	// function to call
	public static void Normalization(ArrayList<String> DataInput) {
		for (int i = 0; i < DataInput.size(); i++) {
			DataInput.set(i, DataInput.get(i).replaceAll("\\s+", ""));
			DataInput.set(i, DataInput.get(i).replaceAll("!!", ""));
			if (DataInput.get(i).indexOf("<=>") != -1) {
				DataInput.set(i, ConvertEquivalent(DataInput.get(i)));
			}
			if (DataInput.get(i).indexOf("=>") != -1) {
				DataInput.set(i, ConvertImplied(DataInput.get(i)));
			}
			if (DataInput.get(i).indexOf("!(") !=-1) {
				DataInput.set(i, ConvertNegativeOfPC(DataInput.get(i)));
			}
		}
	}
}
