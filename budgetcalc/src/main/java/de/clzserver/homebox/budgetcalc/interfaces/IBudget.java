package de.clzserver.homebox.budgetcalc.interfaces;

public interface IBudget {

	interface Regular {
		String getPurpose();

		float getAmount();
		String getAmountS();

		int getDay();
		MonthEnum getMonth();

		long getStart();
		String getStartS();

		long getEnd();
		String getEndS();

		Income getIncome();
		Investments getInvestments();
	}

	interface Singular {
		String getPurpose();

		float getAmount();
		String getAmountS();


	}

	interface Income {
		String getSender();
		Account getTarget();
	}

	interface Investments {
		Account getSender();
		String getTarget();
	}

	interface Account {
		String getUser();

		String getBIC();
		String getIBAN();

		String getName();

		boolean isBudget();
	}

	String getVerwendung();
	
	float getBetrag();
	String getBetragString();
	
	long getDatum();
	String getDatumString();

	String getUser();
	
}
