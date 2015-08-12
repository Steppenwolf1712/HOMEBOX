package de.clzserver.homebox.budgetcalc.ods;

public interface IODS_API {

	/**
	 * Gibt zur�ck, ob die ODS datie die zur Speicherung der Rechnugen derzeit von einem User benutzt wird.
	 * 
	 * @return
	 */
	public boolean islocked();
	
	/**
	 * Gibt einen String mit den Informationen zu r�ck, wer zurzeit die ODS Datei benutzt.
	 * 
	 * @return
	 */
	public String getCurrentUser();
	
	/**
	 * Gibt Alle Zeilen eines MOnats zur�ck, die f�r die Abrechnung relevant sind.
	 * 
	 * @param m_enum
	 * @param year
	 * @return
	 */
	public IBudget[] getMonth(MonthEnum m_enum, int year);
	
	/**
	 * Schreibt eine neue Zeile mit den Rechnungsinformationen in die ODS Datei.
	 * Die neue Zeile wird in die Tabelle f�r ein bestimmtes Jahr und ein bestimmten Monat geschrieben.
	 * 
	 * @param calc
	 */
	public void writeBudget(IBudget calc);
}
