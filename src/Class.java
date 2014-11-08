
public class Class {
	public String classid;
	public String type;
	public String title;
	public String instructor;
	public int credit;
	public Time time;
	public String campus;
	
	public Class(String classid, String type, String title, String instructor, String campus, int credit, Time time) {
		this.classid = classid;
		this.type = type;
		this.title = title;
		this.instructor = instructor;
		this.campus = campus;
		this.credit = credit;
		this.time = time;
	}
	
	public boolean collides(Class c) {
		return time.collides(c.time);
	}
	
	public String toString() {
		return type + "," + classid + "," + time.toString();
	}
	
	public boolean[] classDays() {
		return time.classDays();
	}
	
	public int getEarliestTime() {
		int earliest = time.getStart(0);
		for (int i = 1; i < 5; i++) {
			int test = time.getStart(i);
			if (test<earliest) earliest = test;
		}
		return earliest;
	}
	
	public int getLatestTime() {
		int latest = time.getEnd(0);
		for (int i = 1; i < 5; i++) {
			int test = time.getEnd(i);
			if (test>latest) latest = test;
		}
		return latest;
	}
}
