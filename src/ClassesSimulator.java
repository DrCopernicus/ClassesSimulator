import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class ClassesSimulator {
	public ArrayList<Class> classes; //list of classes generated from readClasses()
	public String[] filters; //filter 0 is the master list, filter 1+ are the student individual filters
	public int[][] compareTo;
	public int[] order;
	public int[] earliestTime;
	public int[] latestTime;
	public String[][] allowedCampuses;
	public boolean[] display;
	public Class[][][] classChoices; //[filter number][class group][class inside group]
	public Class[][] selectedClasses; //[filter number][class picked]
	public static final String SETTINGS = "settings.csv";
	public static final String FILENAME = "classes.csv";
	public static final String OUTPUT = "output.csv";
	public Writer writer;
	public int linesWritten;
	
	public static void main(String[] args) {
		ClassesSimulator c = new ClassesSimulator();
	}
	
	public ClassesSimulator() {
		readClasses();
		readSettings();

		linesWritten = 0;
		
		classChoices = new Class[filters.length][][];
		selectedClasses = new Class[filters.length][];
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < filters.length; i++) { //for every single filter (student, and 0 = master)
			buildLists(i); //build lists (by adding every applicable class to [i])
		}
		
		betterSimulate(0,0);
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(OUTPUT+" written!");
		System.out.println("Wrote "+linesWritten+" lines!");
	}
	
	/**
	 * Builds a class list for a given filter.
	 * @param filterNumber
	 */
	public void buildLists(int filterNumber) {
		int classInList = 0;
		
		String[] lists = filters[filterNumber].split("&");
		selectedClasses[filterNumber] = new Class[lists.length];
		
		classChoices[filterNumber] = new Class[lists.length][];
		for (int i = 0; i < lists.length; i++) {
			String[] sublist = lists[i].split("\\|");
			ArrayList<Class> toPutIn = new ArrayList<Class>();
			
			for (int j = 0; j < sublist.length; j++) {
				for (int k = 0; k < classes.size(); k++) {
					boolean allowed = false;
					Class testingClass = classes.get(k);
					
					if (testingClass.type.equals(sublist[j]) && allowedCampuses[filterNumber]!=null) {
						for (int l = 0; l < allowedCampuses[filterNumber].length; l++) {
							boolean allowedC = allowedCampuses[filterNumber][l].equals(testingClass.campus);
							boolean allowedEarly = testingClass.getEarliestTime()>=earliestTime[filterNumber]||testingClass.getEarliestTime()==-1;
							boolean allowedLate = testingClass.getLatestTime()<=latestTime[filterNumber]||testingClass.getLatestTime()==-1;
							allowed = allowedC&&allowedEarly&&allowedLate;
							if (allowed) break;
						}
					}
					
					if (allowed) {
						toPutIn.add(testingClass);
						classInList++;
					}
				}
			}

			classChoices[filterNumber][i] = new Class[toPutIn.size()];
			for (int j = 0; j < toPutIn.size(); j++) {
				classChoices[filterNumber][i][j] = toPutIn.get(j);
			}
		}

		System.out.println("Filter number " + filterNumber + " has " + classInList + " classes.");
	}
	
	/**
	 * reached end of class list:
	 * 		class list does not collide:
	 * 			reached end of filters:
	 * 				print everything
	 * 			did not reach end of filters:
	 * 				go to next filter
	 * 		class list collides:
	 * 			end this branch
	 * did not reach end of class list:
	 * 		for every class choice in this class group:
	 * 			select the class
	 * 			go to the next class group
	 * @param orderNumber
	 * @param classList
	 */
	public void betterSimulate(int orderNumber, int classList) {
		int filterNumber = order[orderNumber];
		if (classList == classChoices[filterNumber].length) { //end of class list
			boolean collision = false;
			if (collide(filterNumber,filterNumber)) { //check collision against itself
				collision = true;
			}
			for (int i = 0; i < compareTo[filterNumber].length; i++) { //check all collisions against compareTo class schedules
				if (collide(filterNumber,compareTo[filterNumber][i])) {
					collision = true;
					break;
				}
			}
			if (!collision) { //not collision
				if (filterNumber == classChoices.length-1) { //end of filters
					readOut(); //print everything
				} else { //not end of filters
					betterSimulate(orderNumber+1,0); //next filter
				}
			} else { //collision
				//do nothing
			}
		} else { //not end of class list
			for (int i = 0; i < classChoices[filterNumber][classList].length; i++) { //every class choice in the class group
				selectedClasses[filterNumber][classList] = classChoices[filterNumber][classList][i]; //select a choice
				betterSimulate(orderNumber,classList+1); //go to the next class group
			}
		}
	}
	
	public boolean collide(int filterNumber1, int filterNumber2) {
		for (int i = 0; i < selectedClasses[filterNumber1].length; i++) {
			for (int j = 0; j < selectedClasses[filterNumber2].length; j++) {
				if (filterNumber1==filterNumber2 && i==j) {
					//do nothing
				} else {
					if (selectedClasses[filterNumber1][i].collides(selectedClasses[filterNumber2][j])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void readClasses() {
		classes = new ArrayList<Class>();
		ArrayList<ClassNumberPair> printableClasses = new ArrayList<ClassNumberPair>();
		
		try {
			BufferedReader file = new BufferedReader(new FileReader(FILENAME));
			String data = file.readLine();
			while (data != null) {
				String[] array = data.split(",");
				if (!array[0].equals("x")) {
					String classid = array[1].trim();
					String type = array[2].trim();
					String title = array[3].trim();
					String instructor = array[4].trim();
					String campus = array[5].trim();
					int credit = 0;
					try {
						credit = Integer.parseInt(array[6].trim());
					} catch (NumberFormatException e) {
						System.out.println(classid);
						throw new NumberFormatException();
					}
					
					Time time = new Time();
					if (array.length == 10) time = new Time(array[7].trim(),Integer.parseInt(array[8].trim()),Integer.parseInt(array[9].trim()));
					else if (array.length == 12)  time = new Time(array[7].trim(),Integer.parseInt(array[8].trim()),Integer.parseInt(array[9].trim()),Integer.parseInt(array[10].trim()),Integer.parseInt(array[11].trim()));
					
					classes.add(new Class(classid, type, title, instructor, campus, credit, time));
					
					boolean added = false;
					for (int i = 0; i < printableClasses.size(); i++) {
						if (printableClasses.get(i).name.equals(array[2])) {
							printableClasses.get(i).number += 1;
							added = true;
							break;
						}
					}
					if (!added) {
						printableClasses.add(new ClassNumberPair(array[2],1));
					}
				}
				data = file.readLine();
			}
			for (int i = 0; i < printableClasses.size(); i++) {
				if (i!=0&&printableClasses.get(i).name.substring(0, 3).equals(printableClasses.get(i-1).name.substring(0, 3))) {
					System.out.print(", " + printableClasses.get(i).number + "x " + printableClasses.get(i).name.substring(3));
				} else {
					System.out.print("\n" + printableClasses.get(i).name.substring(0, 3) + ": " + printableClasses.get(i).number + "x " + printableClasses.get(i).name.substring(3));
				}
			}
			System.out.println("\n" + classes.size() + " classes loaded!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readSettings() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(SETTINGS));
			String[] filtersData = file.readLine().split(",");
			String[] compareToData = file.readLine().split(",");
			String[] orderData = file.readLine().split(",");
			String[] earliestTimeData = file.readLine().split(",");
			String[] latestTimeData = file.readLine().split(",");
			String[] campusData = file.readLine().split(",");
			String[] displayData = file.readLine().split(",");
			int length = filtersData.length;
			
			filters = filtersData;
			compareTo = new int[length][];
			order = new int[length];
			earliestTime = new int[length];
			latestTime = new int[length];
			allowedCampuses = new String[length][];
			display = new boolean[length];
			
			for (int i = 0; i < length; i++) {
				String[] compareToSubData = compareToData[i].split("&");
				int[] subcompareTo = new int[compareToSubData.length];
				for (int j = 0; j < compareToSubData.length; j++) {
					if (!compareToSubData[j].equals("")) {
						subcompareTo[j] = Integer.parseInt(compareToSubData[j]);
					}
				}
				compareTo[i] = subcompareTo;
				
				order[i] = Integer.parseInt(orderData[i]);
				earliestTime[i] = Integer.parseInt(earliestTimeData[i]);
				latestTime[i] = Integer.parseInt(latestTimeData[i]);
				
				allowedCampuses[i] = campusData[i].split("&");
				
				if (Integer.parseInt(displayData[i])==1) display[i] = true;
				else display[i] = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * prints out class schedule in the format ((filter,credit,,id,class,time) for each class),; for each filter
	 * have fun reading this section of code
	 */
	public void readOut() {
		boolean print = true;
		String sprint = "";
		for (int f = 0; f < selectedClasses.length; f++) {
			String s1 = "";
			String s2 = "";
			
			for (int i = 0; i < compareTo[f].length; i++) {
				if (collide(f,compareTo[f][i])) {
					print = false;
					break;
				}
				for (int j = i; j < compareTo[f].length; j++) {
					if (compareTo[f][i]!=compareTo[f][j]) {
						if (collide(compareTo[f][i],compareTo[f][j])) {
							print = false;
							break;
						}
					}
				}
			}
			
			if (display[f]) {
				int totalCredit = 0;
				for (int i = 0; i < selectedClasses[f].length; i++) {
					totalCredit += selectedClasses[f][i].credit;
				}
				for (int i = 0; i < compareTo[f].length; i++) {
					for (int j = 0; j < selectedClasses[i].length; j++) {
						totalCredit += selectedClasses[i][j].credit;
					}
				}
				boolean[] wd = new boolean[5];
				int[] eT = {2399,2399,2399,2399,2399};
				int[] lT = {0,0,0,0,0};
				for (int i = 0; i < compareTo[f].length; i++) {
					for (int j = 0; j < selectedClasses[i].length; j++) {
						s2+=selectedClasses[i][j].toString()+",";
						boolean[] classDays = selectedClasses[i][j].classDays();
						for (int k = 0; k < classDays.length; k++) {
							if (classDays[k]) {
								wd[k] = true;
								int start = selectedClasses[i][j].time.getStart(k);
								int end = selectedClasses[i][j].time.getEnd(k);
								if (start<eT[k]) eT[k] = start;
								if (end>lT[k]) lT[k] = end;
							}
						}
					}
				}
				for (int i = 0; i < selectedClasses[f].length; i++) {
					s2+=selectedClasses[f][i].toString()+",";
					boolean[] classDays = selectedClasses[f][i].classDays();
					for (int k = 0; k < classDays.length; k++) {
						if (classDays[k]) {
							wd[k] = true;
							int start = selectedClasses[f][i].time.getStart(k);
							int end = selectedClasses[f][i].time.getEnd(k);
							if (start<eT[k]) eT[k] = start;
							if (end>lT[k]) lT[k] = end;
						}
					}
				}
				if (Math.min(eT[0],Math.min(eT[1],Math.min(eT[2],Math.min(eT[3],eT[4]))))<earliestTime[f]||Math.max(lT[0],Math.max(lT[1],Math.max(lT[2],Math.max(lT[3],lT[4]))))>latestTime[f]) {
					print = false;
					break;
				}
				float timeOnCampus = 0;
				for (int i = 0; i < eT.length; i++) {
					if (wd[i]) {
						int eTh = eT[i]/100;
						int lTh = lT[i]/100;
						int eTm = eT[i]-eTh*100;
						int lTm = lT[i]-lTh*100;
						timeOnCampus += lTh-eTh+(float)(lTm-eTm)/60.0;
					}
				}
				s1+=totalCredit+"c ";
				String s1temp = "";
				int daysInClass = 0;
				if (wd[0]) {
					daysInClass++;
					s1temp+="m";
				}
				if (wd[1]) {
					daysInClass++;
					s1temp+="t";
				}
				if (wd[2]) {
					daysInClass++;
					s1temp+="w";
				}
				if (wd[3]) {
					daysInClass++;
					s1temp+="th";
				}
				if (wd[4]) {
					daysInClass++;
					s1temp+="f";
				}	
				s1+=daysInClass+":"+(int)Math.ceil(timeOnCampus)+":"+s1temp;
				s2+=",";
				sprint += s1+",   ,"+s2;
			}
		}
		if (print) {
			try {
				writer.write(sprint+"\n");
				linesWritten++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class ClassNumberPair {
		public String name;
		public int number;
		
		public ClassNumberPair(String name, int number) {
			this.name = name;
			this.number = number;
		}
	}
}
