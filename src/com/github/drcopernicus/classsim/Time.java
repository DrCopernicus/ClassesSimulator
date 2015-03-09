package com.github.drcopernicus.classsim;

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
		switch (stringToEnum(day)) {
		case O:
			start = new int[]{-1,-1,-1,-1,-1};
			end = new int[]{-1,-1,-1,-1,-1};
			break;
        case M:
			start = new int[]{a,-1,-1,-1,-1};
			end = new int[]{b,-1,-1,-1,-1};
			break;
		case T:
			start = new int[]{-1,a,-1,-1,-1};
			end = new int[]{-1,b,-1,-1,-1};
			break;
		case W:
			start = new int[]{-1,-1,a,-1,-1};
			end = new int[]{-1,-1,b,-1,-1};
			break;
		case TH:
			start = new int[]{-1,-1,-1,a,-1};
			end = new int[]{-1,-1,-1,b,-1};
			break;
		case F:
			start = new int[]{-1,-1,-1,-1,a};
			end = new int[]{-1,-1,-1,-1,b};
			break;
		case MW:
			start = new int[]{a,-1,a,-1,-1};
			end = new int[]{b,-1,b,-1,-1};
			break;
		case TTH:
			start = new int[]{-1,a,-1,a,-1};
			end = new int[]{-1,b,-1,b,-1};
			break;
		case WF:
			start = new int[]{-1,-1,a,-1,a};
			end = new int[]{-1,-1,b,-1,b};
			break;
		case MWF:
			start = new int[]{a,-1,a,-1,a};
			end = new int[]{b,-1,b,-1,b};
			break;
		case MTWTH:
			start = new int[]{a,a,a,a,-1};
			end = new int[]{b,b,b,b,-1};
			break;
		case ALL:
			start = new int[]{a,a,a,a,a};
			end = new int[]{b,b,b,b,b};
			break;
		default:
			start = new int[]{0,0,0,0,0};
			end = new int[]{2359,2359,2359,2359,2359};
			System.out.println("Class time improperly set up! Defaulting to ALL DAY EVERY DAY. (" + day + ", " + a + ", " + b + ")");
			break;
		}
	}
	
	public Time(String day, int a, int b, int c, int d) {
		switch (stringToEnum(day)) {
		case MW:
			start = new int[]{a,-1,c,-1,-1};
			end = new int[]{b,-1,d,-1,-1};
			break;
		case TTH:
			start = new int[]{-1,a,-1,c,-1};
			end = new int[]{-1,b,-1,d,-1};
			break;
		default:
			start = new int[]{0,0,0,0,0};
			end = new int[]{2359,2359,2359,2359,2359};
			System.out.println("Class time improperly set up! Defaulting to ALL DAY EVERY DAY.");
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

    //ugly code which should be replaced with simple boolean arrays
    //I really can't stop laughing at how ridiculous this code looks
    //it'd be even funnier if I had every possible combination here.
    //I think I'll keep it.
    private TimeEnum stringToEnum(String day) {
        if (day.equals("o")) return TimeEnum.O;
        else if (day.equals("m")) return TimeEnum.M;
        else if (day.equals("t")) return TimeEnum.T;
        else if (day.equals("w")) return TimeEnum.W;
        else if (day.equals("th")) return TimeEnum.TH;
        else if (day.equals("f")) return TimeEnum.F;
        else if (day.equals("mw")) return TimeEnum.MW;
        else if (day.equals("tth")) return TimeEnum.TTH;
        else if (day.equals("wf")) return TimeEnum.WF;
        else if (day.equals("mtwth")) return TimeEnum.MTWTH;
        else return TimeEnum.ALL;
    }
}
