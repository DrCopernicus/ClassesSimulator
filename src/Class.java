
public class Class {
	public String classid;
	public String type;
	public String instructor;
	public int credit;
	public Time time;
	public String campus;
	
	public Class(String classid, String type, String instructor, String campus, int credit, Time time) {
		this.classid = classid;
		this.type = type;
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
}
