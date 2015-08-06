package de.clzserver.homebox.budgetcalc.gui;

public enum UserEnum {
	Marc("Marc","ce8"), Janine("Janine", "ce9");
	
	private String name = null;
	private String style = null;
	
	private UserEnum(String name, String style) {
		this.name = name;
		this.style = style;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String toString() {
		return getName();
	}

	public static UserEnum get(String person){
	UserEnum[] userArray = UserEnum.values();
	for (int i = 0; i< userArray.length; i++)
	{
		if(userArray[i].toString().equals(person))
			return userArray[i];
		
	}
		System.out.println("UserEnum : Dieser User ist nicht verfügbar " + person);
		return null;
		
	}
}
