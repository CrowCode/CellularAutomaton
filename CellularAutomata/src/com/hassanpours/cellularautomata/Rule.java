package com.hassanpours.cellularautomata;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Rule implements Serializable {

	public int getNomOfSubRules() {
		return nomOfSubRules;
	}

	public void setNomOfSubRules(int nomOfSubRules) {
		this.nomOfSubRules = nomOfSubRules;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int iid;
	private int id;
	private String neighborhoodType;
	private String ruleInputState;
	private String ruleOutputState;
	private int nomOfSubRules;

	private transient ArrayList<SubRuleComponent> subRulesComponentList = new ArrayList<>();

	private ArrayList<SubRule> subRulesList = new ArrayList<>();

	public Rule() {
		this.id = Rule.iid;
		Rule.iid++;
		this.neighborhoodType = "4 pt";
		this.ruleInputState = "Passive";
		this.ruleOutputState = "Passive";
		this.nomOfSubRules = 0;
	}

	public Rule(int i) {

		this.id = i;
		this.neighborhoodType = null;
		this.ruleInputState = null;
		this.ruleOutputState = null;
	}

	public static int getIdd() {
		return iid;
	}

	public static void setIdd(int idd) {
		Rule.iid = idd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNeighborhoodType() {
		return neighborhoodType;
	}

	public void setNeighborhoodType(String neighborhoodType) {
		this.neighborhoodType = neighborhoodType;

	}

	public String getRuleInputState() {
		return ruleInputState;
	}

	public void setRuleInputState(String ruleInputState) {
		this.ruleInputState = ruleInputState;

	}

	public String getRuleOutputState() {
		return ruleOutputState;
	}

	public void setRuleOutputState(String ruleOutputState) {
		this.ruleOutputState = ruleOutputState;

	}

	public SubRuleComponent addSubRuleCompo() throws IOException {
		SubRuleComponent c = new SubRuleComponent(neighborhoodType, id);
		subRulesComponentList.add(c);
		nomOfSubRules++;
		return c;
	}

	public int getSubRulesComponentSize() {
		return subRulesComponentList.size();
	}

	public SubRuleComponent getSubRuleCompo(int i) throws IOException {

		return subRulesComponentList.get(i);
	}

	public ArrayList<SubRuleComponent> getSubRuleCompoList() throws IOException {

		return subRulesComponentList;
	}

	public ArrayList<SubRule> getSubRuleList() throws IOException {

		return subRulesList;
	}

	public String toString() {

		String s = String
				.format("# RuleId: %d, NeighborhoodType: %s, InputState: %s, OutPutState: %s, Hsub: %d",
						id, neighborhoodType, ruleInputState, ruleOutputState,
						nomOfSubRules);

		return s;
	}

	public void prepareBeforeSerializing() {
		for (SubRuleComponent subrC : subRulesComponentList) {

			subRulesList.add(subrC.getSubRule());
		}
	}

	public void prepareAfterSerializing() throws IOException {
		subRulesComponentList = new ArrayList<SubRuleComponent>();
		for (SubRule subr : subRulesList) {
			SubRuleComponent c = new SubRuleComponent(subr,
					this.neighborhoodType);
			subRulesComponentList.add(c);
		}
	}
	
	public void removeSubruleComponent(int i){
		subRulesComponentList.remove(i);
		subRulesList.remove(i);
	}
}
