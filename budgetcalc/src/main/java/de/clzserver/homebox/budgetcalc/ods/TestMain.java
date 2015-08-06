package de.clzserver.homebox.budgetcalc.ods;

import java.util.ArrayList;

import de.clzserver.homebox.budgetcalc.ods.interfaces.IBudget;
import de.clzserver.homebox.config.Config;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config cfg = Config.getInstance();

		String path = cfg.getValue(cfg.CALCODS_PATH_KEY);
		path+=cfg.getValue(cfg.CALCODS_NAME_KEY);
		
		System.out.println(path);

		CalcTable calc;
		calc = CalcTable.getInstance(path);
		
		ArrayList<IBudget> list = calc.readout_Table("2014_Juni");
		
		for (int i = 0; i<list.size(); i++)
			System.out.println(i+"ter eintrag hat \n\tden Verwendungszweck "+list.get(i).getVerwendung()+
					"\n\tden Betrag "+list.get(i).getBetragString()+
					"\n\tdas Datum "+list.get(i).getDatumString()+
					"\n\tund den Benutzer "+list.get(i).getUser());
	}

}
