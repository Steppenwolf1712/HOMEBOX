package de.clzserver.homebox.budgetcalc.ods;

import de.clzserver.homebox.budgetcalc.interfaces.IBudget;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class Budget implements IBudget {

	private String verwendung = null;
	private float betrag = 0;
	private long datum = 0;
	private String user = null;
	
	Budget(String verwendung, float betrag, long datum, String user) {
		this.verwendung = verwendung;
		this.betrag = betrag;
		this.datum = datum;
		this.user = user;
	}
	
	Budget(String verwendung, float betrag, String datum, String user) {
		this(verwendung, betrag, convertDatum(datum), user);
	}
	
	@Override
	public String getVerwendung() {
		return this.verwendung;
	}

	@Override
	public float getBetrag() {
		return this.betrag;
	}

	@Override
	public String getBetragString() {
		DecimalFormat deci = new DecimalFormat("0.00");
		String erg = deci.format( this.betrag).replace(".", ",");
		erg += " \u20ac";
		return erg;
	}

	@Override
	public long getDatum() {
		return this.datum;
	}

	@Override
	public String getDatumString() {
		SimpleDateFormat smdft = new SimpleDateFormat("dd.MM.yy");
		return smdft.format(new Date(this.datum));
	}
	
	public String getStandardDatumString() {
		SimpleDateFormat smdft = new SimpleDateFormat("yyyy-MM-dd");
		return smdft.format(new Date(this.datum));
	}
	
	private static long convertDatum(String date) {
		String[] parts = date.trim().split("\\.");
		Calendar c = Calendar.getInstance();
		c.set(2000+Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1, Integer.parseInt(parts[0]));
		return c.getTime().getTime();
	}

	@Override
	public String getUser() {
		return this.user;
	}

}
