
public class Time {
	private int[] start;
	private int[] end;
	
	public Time() {
		start = new int[]{-1,-1,-1,-1,-1};
		end = new int[]{-1,-1,-1,-1,-1};
	}
	
	public Time(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j) {
		start = new int[]{a,c,e,g,i};
		end = new int[]{b,d,f,h,j};
	}
	
	/**
	 * Possible days strings: o, m, t, w, th, f, mw, tth, mwf, mtwth, mtwthf
	 * @param day
	 * @param a
	 * @param b
	 */
	public Time(String day, int a, int b) {
		switch (day.toLowerCase()) {
		case "o":
			start = new int[]{-1,-1,-1,-1,-1};
			end = new int[]{-1,-1,-1,-1,-1};
			break;
		case "m":
			start = new int[]{a,-1,-1,-1,-1};
			end = new int[]{b,-1,-1,-1,-1};
			break;
		case "t":
			start = new int[]{-1,a,-1,-1,-1};
			end = new int[]{-1,b,-1,-1,-1};
			break;
		case "w":
			start = new int[]{-1,-1,a,-1,-1};
			end = new int[]{-1,-1,b,-1,-1};
			break;
		case "th":
			start = new int[]{-1,-1,-1,a,-1};
			end = new int[]{-1,-1,-1,b,-1};
			break;
		case "f":
			start = new int[]{-1,-1,-1,-1,a};
			end = new int[]{-1,-1,-1,-1,b};
			break;
		case "mw":
			start = new int[]{a,-1,a,-1,-1};
			end = new int[]{b,-1,b,-1,-1};
			break;
		case "tth":
			start = new int[]{-1,a,-1,a,-1};
			end = new int[]{-1,b,-1,b,-1};
			break;
		case "wf":
			start = new int[]{-1,-1,a,-1,a};
			end = new int[]{-1,-1,b,-1,b};
			break;
		case "mwf":
			start = new int[]{a,-1,a,-1,a};
			end = new int[]{b,-1,b,-1,b};
			break;
		case "mtwth":
			start = new int[]{a,a,a,a,-1};
			end = new int[]{b,b,b,b,-1};
			break;
		case "mtwthf":
			start = new int[]{a,a,a,a,a};
			end = new int[]{b,b,b,b,b};
			break;
		default:
			start = new int[]{0,0,0,0,0};
			end = new int[]{2359,2359,2359,2359,2359};
			System.out.println("Class time improperly set up! Defaulting to ALL DAY. (" + day + ", " + a + ", " + b + ")");
			break;
		}
	}
	
	public Time(String day, int a, int b, int c, int d) {
		switch (day) {
		case "mw":
			start = new int[]{a,-1,c,-1,-1};
			end = new int[]{b,-1,d,-1,-1};
			break;
		case "tth":
			start = new int[]{-1,a,-1,c,-1};
			end = new int[]{-1,b,-1,d,-1};
			break;
		default:
			start = new int[]{0,0,0,0,0};
			end = new int[]{2359,2359,2359,2359,2359};
			System.out.println("Class time improperly set up! Defaulting to ALL DAY.");
			break;
		}
	}
	
	public boolean collides(Time t) {
		for (int i = 0; i < 5; i++) {
			int u0 = t.getStart(i);
			int u1 = t.getEnd(i);
			int t0 = start[i];
			int t1 = end[i];
			if (!(u0==-1||t0==-1||u1==-1||t1==-1) && ((u0>=t0&&u0<=t1&&u1>=t0&&u1>=t1)||(u0<=t0&&u0<=t1&&u1>=t0&&u1<=t1))) {
				return true;
			}
		}
		return false;
	}
	
	public int getStart(int day) {
		return start[day];
	}
	
	public int getEnd(int day) {
		return end[day];
	}
	
	public String toString() {
		String s = "";
		if (start[0]!=-1) s += "m"+start[0]+"-"+end[0]+";";
		if (start[1]!=-1) s += "t"+start[1]+"-"+end[1]+";";
		if (start[2]!=-1) s += "w"+start[2]+"-"+end[2]+";";
		if (start[3]!=-1) s += "th"+start[3]+"-"+end[3]+";";
		if (start[4]!=-1) s += "f"+start[4]+"-"+end[4]+";";
		if (s.equals("")) s = "online;";
		return s;
	}
	
	public boolean[] classDays() {
		return new boolean[]{start[0]!=-1,start[1]!=-1,start[2]!=-1,start[3]!=-1,start[4]!=-1};
	}
}
