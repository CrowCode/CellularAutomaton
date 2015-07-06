package com.hassanpours.cellularautomata;

import java.io.Serializable;

public class SubRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int howManySign;
	private int howManyNom;
	private int howManyNomOnPos;
	private String onType;

	public SubRule(int id) {
		this.id = id;
		this.howManySign = 0;
		this.howManyNom = 0;
		this.howManyNomOnPos = 0;
		this.onType = "in whole neighborhood";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHowManySign() {
		return howManySign;
	}

	public void setHowManySign(int howManySign) {
		this.howManySign = howManySign;
	}

	public int getHowManyNom() {
		return howManyNom;
	}

	public void setHowManyNom(int howManyNom) {
		this.howManyNom = howManyNom;
	}

	public int getOnPos() {
		return howManyNomOnPos;
	}

	public void setOnPos(int onPos) {
		this.howManyNomOnPos = onPos;
	}

	public String getOnType() {
		return onType;
	}

	public void setOnType(String onType) {
		this.onType = onType;
	}

	public String toString() {
		String s = String.format(
				"* SubRuleId: %d, Hsign: %d, H: %d, On: %s, Hon: %d", id,
				howManySign, howManyNom, onType, howManyNomOnPos);
		return s;
	}

	public void coppy(SubRule sur) {

		this.id = sur.id;
		this.howManySign = sur.howManySign;
		this.howManyNom = sur.howManyNom;
		this.howManyNomOnPos = sur.howManySign;
		this.onType = sur.onType;
	}
}
