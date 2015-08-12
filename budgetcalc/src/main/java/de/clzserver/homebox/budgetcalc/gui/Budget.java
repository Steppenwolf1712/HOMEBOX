package de.clzserver.homebox.budgetcalc.gui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.clzserver.homebox.budgetcalc.ods.IBudget;

class Budget implements IBudget{

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

	@Override
	public String getUser() {
		return this.user;
	}

}
