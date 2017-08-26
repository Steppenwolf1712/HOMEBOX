package de.clzserver.homebox.budgetcalc.interfaces;

public enum MonthEnum {
	JANUAR("Januar",0), FEBRUAR("Februar",1), MAERZ("März",2), APRIL("April",3),
	MAI("Mai",4), JUNI("Juni",5), JULI("Juli",6), AUGUST("August",7),
	SEPTEMBER("September",8), OKTOBER("Oktober",9), NOVEMBER("November",10), DEZEMBER("Dezember",11), MONTHLY("Monatlich", 12);
	
	private String month = null;
	private int index = 0;
	
	private MonthEnum(String month, int in) {
		this.month = month;
		this.index = in;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String toString() {
		return getMonth();
	}
	
	public static MonthEnum get(int index) {
		switch (index) {
		case 0:
			return JANUAR;
		case 1:
			return FEBRUAR;
		case 2:
			return MAERZ;
		case 3:
			return APRIL;
		case 4:
			return MAI;
		case 5:
			return JUNI;
		case 6:
			return JULI;
		case 7:
			return AUGUST;
		case 8:
			return SEPTEMBER;
		case 9:
			return OKTOBER;
		case 10:
			return NOVEMBER;
		case 11:
			return DEZEMBER;
		case 12:
			return MONTHLY;

		default:
			return null;
		}
	}
}
