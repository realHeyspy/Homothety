package schoolFile;

import java.util.ArrayList;
import java.util.Stack;

public class Homothety {
	static Stack<HomothetyObject> storage = new Stack<HomothetyObject>();
	static Stack<HomothetyObject> v = new Stack<HomothetyObject>();
	static Stack<HomothetyObject> memory = new Stack<HomothetyObject>();
	static Stack<HomothetyObject> Remove = new Stack<HomothetyObject>();
	static Stack<HomothetyObject> THOA = new Stack<HomothetyObject>();
	static Stack<Integer> WayToGoal = new Stack<Integer>();
	static Stack<String> ListPartsTarget = new Stack<>();
	static int currentNumber;
	static int sizeStore;
	static boolean CheckIfGoal = false;
	static StringBuilder builder;

	public static HomothetyObject dequeue(Stack<HomothetyObject> res) {
		memory = (Stack<HomothetyObject>) res.clone();
		memory.remove(0);
		if (Remove.isEmpty()) {
			while (!res.isEmpty()) {
				Remove.push(res.pop());
			}
		}
		res.addAll(memory);
		HomothetyObject datapopup = Remove.pop();
		Remove.clear();
		memory.clear();
		return datapopup;
	}

	public static void StepProcess(ArrayList<String> DataInput, String needProve) {
		HomothetyObject resultTHOA = null, currentV = null, current = null;
		String result = "";
		NormalizationData.Normalization(DataInput);
		for (int i = 0; i < DataInput.size(); i++) {
			storage.add(new HomothetyObject(DataInput.get(i), i + 1));
		}
		currentNumber = storage.size() + 1;
		THOA.add(new HomothetyObject(needProve, currentNumber));
		// first line
		System.out.printf("%-22s%-22s%-22s%-22s\n", "", "", "",
				"|" + THOA.get(0).groupData + "(" + THOA.get(0).numberRule + ")");
		while (THOA.size() != 0) {
			ListPartsTarget.clear();
			if (currentV != null && current != null) {
				WayToGoal.add(currentV.numberRule);
				WayToGoal.add(current.numberRule);
			}
			System.out.println("-----------------------------------------------------------------------------");
			current = dequeue(THOA);
			findVMatch(current);
			// use this to reduce we need to multiplication parts that make result bigger
			for (String parts : ListPartsTarget) {
				currentV = dequeue(v);
				result = resultUV(parts, currentV.groupData, current.groupData);
				if (result.length() == 0) {
					resultTHOA = new HomothetyObject(result, currentNumber + 1);
					DisplayDataObject(current, currentV, resultTHOA);
					WayToGoal.add(currentV.numberRule);
					WayToGoal.add(current.numberRule);
					CheckIfGoal = true;
					break;
				}
				resultTHOA = new HomothetyObject(result, currentNumber + 1);
				THOA.add(0, resultTHOA);
				currentNumber++;
				DisplayDataObject(current, currentV, resultTHOA);
			}
			if (result.length() == 0) {
				break;
			}
		}
		DisplayWayToGoal();
	}

	// find what in storage match to push in v can use to reduce current
	public static void findVMatch(HomothetyObject current) {
		sizeStore = storage.size();
		String[] parts = current.groupData.split("v");
		int i = 0;
		while (i < sizeStore) {
			for (String s : parts) {
				int decisition = s.indexOf("!");
				if (decisition == -1) {
					if (storage.get(i).groupData.indexOf("!" + s.charAt(0)) != -1) {
						ListPartsTarget.add(s);
						v.add(replaceDifferentCharter(storage.get(i), s));
					}
				} else {
					if (decisition != -1) {
						builder = new StringBuilder(s);
						builder.deleteCharAt(0);
						String checkData = builder.toString();
						// checking if there have
						if (storage.get(i).groupData.indexOf(checkData.charAt(0)) != -1) {
							if (storage.get(i).groupData.indexOf("!" + checkData.charAt(0)) == -1) {
								ListPartsTarget.add(s);
								v.add(replaceDifferentCharter(storage.get(i), checkData));
							}
						}
					}
				}
			}
			i++;
		}
	}

	// this function split and put variable with order with even are target and odd
	// are origin variable
	public static HomothetyObject replaceDifferentCharter(HomothetyObject input, String targetReplace) {
		String[] partsData = {};
		String Datainput = input.groupData;
		String[] splitData = Datainput.split("v");
		// this reduce the string into only variable and split them to parts
		for (int i = 0; i < splitData.length; i++) {
			if (splitData[i].charAt(0) == '!') {
				if (splitData[i].charAt(1) == targetReplace.charAt(0)) {
					partsData = splitData[i].substring(splitData[i].indexOf("(") + 1, splitData[i].lastIndexOf(")"))
							.split(",");
				}
			} else {
				if (splitData[i].charAt(0) == targetReplace.charAt(0)) {
					partsData = splitData[i].substring(splitData[i].indexOf("(") + 1, splitData[i].lastIndexOf(")"))
							.split(",");
				}
			}
		}
		String[] partsTarget = targetReplace.substring(targetReplace.indexOf("(") + 1, targetReplace.lastIndexOf(")"))
				.split(",");
		int PartsSize = partsTarget.length;
		HomothetyObject result = new HomothetyObject(Datainput, input.numberRule);
		for (int i = 0; i < PartsSize; i++) {
			if (partsData[i].compareTo(partsTarget[i]) != 0) {
				// push target first and replace target laster
				result.CharterOD.add(partsTarget[i]);
				result.CharterOD.add(partsData[i]);
			}
		}
		if (result.CharterOD.size() > 2) {
			for (int i = 0; i < result.CharterOD.size(); i += 2) {
				result.groupData = Datainput.replaceAll(result.CharterOD.get(i + 1), result.CharterOD.get(i));
			}
		} else {
			result.groupData = Datainput.replaceAll(result.CharterOD.get(1), result.CharterOD.get(0));
		}
		return result;
	}

	// send back result u*v
	public static String resultUV(String current, String CompareData, String originData) {
		String result = "";
		result = CompareData + "v" + originData;
		int decisition = current.indexOf("!");
		// this remove !a and normal a and clean other left behind
		if (decisition == -1) {
			String currentNeedReplace = current;
			result = result.replace("!" + currentNeedReplace, "");
			result = result.replace(currentNeedReplace, "");
			result = result.replace("vv", "v");
			if (result.length() == 1 && result.equals("v")) {
				result = "";
			} else {
				StringBuffer str = new StringBuffer(result);
				int check = result.lastIndexOf("v");
				if (check == result.length() - 1) {
					str.deleteCharAt(check);
				}
				check = result.indexOf("v");
				if (check == 0) {
					str.deleteCharAt(0);
				}
				result = str.toString();
			}
		} else {
			if (decisition != -1) {
				builder = new StringBuilder(current);
				builder.deleteCharAt(0);
				String currentNeedReplace = builder.toString();
				result = result.replace("!" + currentNeedReplace, "");
				result = result.replace(currentNeedReplace, "");
				result = result.replace("vvv", "v");
				result = result.replace("vv", "v");
				if (result.length() == 1 && result.equals("v")) {
					result = "";
				} else {
					StringBuffer str = new StringBuffer(result);
					int check = result.lastIndexOf("v");
					if (check == result.length() - 1) {
						str.deleteCharAt(check);
					}
					check = result.indexOf("v");
					if (check == 0) {
						str.deleteCharAt(0);
					}
					result = str.toString();
				}
			}
		}
		return result;
	}

	// print process data after one cycle complete
	public static void DisplayDataObject(HomothetyObject u, HomothetyObject v, HomothetyObject res) {
		int sizeTHOA = THOA.size();
		String printThoa = "";
		for (int i = 0; i < sizeTHOA; i++) {
			printThoa += THOA.get(i).groupData + "(" + THOA.get(i).numberRule + "), ";
		}
		if (printThoa.length() != 0) {
			StringBuffer str = new StringBuffer(printThoa);
			str.deleteCharAt(printThoa.length() - 1);
			str.deleteCharAt(printThoa.length() - 2);
			printThoa = str.toString();
		}
		// start process print what variable replacement
		String printv = "";
		for (int j = 0; j < v.CharterOD.size(); j += 2) {
			printv = "[" + v.CharterOD.get(j + 1) + "/" + v.CharterOD.get(j) + ",";
		}
		builder = new StringBuilder(printv);
		builder.deleteCharAt(printv.length() - 1);
		builder.append("]");
		printv = builder.toString();
		// end
		if (res.groupData == "") {
			System.out.printf("%-22s%-22s%-22s%-22s\n", u.groupData + " (" + u.numberRule + ")",
					"|" + v.numberRule + " " + printv + "", "|" + res.groupData, "|" + printThoa);
		} else {
			System.out.printf("%-22s%-22s%-22s%-22s\n", u.groupData + " (" + u.numberRule + ")",
					"|" + v.numberRule + " " + printv + "", "|" + res.groupData + " (" + res.numberRule + ")",
					"|" + printThoa);
		}
	}

	// print the last line are way to get result or printing this can't find it
	public static void DisplayWayToGoal() {
		if (THOA.size() == 0 && CheckIfGoal == false) {
			System.out.println("There is no way to find the goal");
		} else {
			System.out.println();
			System.out.print("Contradiction <-");
			while (WayToGoal.size() > 2) {
				System.out.print("(" + WayToGoal.pop() + "," + WayToGoal.pop() + ")" + " <-");
			}
			System.out.print("(" + WayToGoal.pop() + "," + WayToGoal.pop() + ")");
		}
	}

	// add input G1,G2...Gv into DataInput and Goal are second variable of function
	// StepProcess
	public static void main(String[] arg) {
		ArrayList<String> DataInput = new ArrayList<>();
		DataInput.add("P(x)=>Q(x)");
		DataInput.add("Q(x)=>E(x)");
		DataInput.add("P(x)^E(y)=>F(x)");
		DataInput.add("M(x,y)=>N(y)");
		DataInput.add("N(x)^E(y)=>F(x)");
		DataInput.add("Q(a)");
		DataInput.add("M(a,b)");
		System.out.printf("%-22s%-22s%-22s%-22s\n", "u", "v", "result(u,v)", "THOA");
		StepProcess(DataInput, "!F(b)");
	}
}
