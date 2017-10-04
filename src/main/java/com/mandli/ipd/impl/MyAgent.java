package com.mandli.ipd.impl;

import java.util.ArrayList;
import java.util.List;

import com.mandli.ipd.Action;
import com.mandli.ipd.ActionProcessor;
import com.mandli.ipd.Agent;
import com.mandli.ipd.Result;

/**
 * My Agent
 *
 * @author ericwallat on 10/4/17
 */
public class MyAgent implements Agent {

	
	private List<Action> prevActions;
	Result result;
	Action lastAction, lastActionTemp;
	int counter;
	double defects, coops;
	
	public MyAgent() {
		prevActions = new ArrayList<>();
		lastAction = Action.COOPERATE;
		counter = 0;
		defects = 0;
		coops = 0;
	}

	@Override
	public void performAction(ActionProcessor actionProcessor) {
		result = actionProcessor.submitAction(checkActions());
		countTitTat();
		lastAction = lastActionTemp;
		sumActions();
		prevActions.add(result.getOpponentAction());
	}

	/**
	 * Checks several conditions based on opponents previous moves
	 * to determine the action for the current round.
	 * 
	 * @return the Action object to be used for this round
	 */
	private Action checkActions() {
		if (counter > 500) { //checks if opponent is using tit-tat method
			lastActionTemp = Action.COOPERATE;
		}
		else if (prevActions.size() < 2) { //Cooperates for the first two rounds
			lastActionTemp = Action.COOPERATE;
		}
		else if (!prevActions.contains(Action.DEFECT) || 
				!prevActions.contains(Action.COOPERATE)) { //checks if opponent is defector or cooperator
			lastActionTemp = Action.DEFECT;
		}
		else if (defects/coops > 0.8 && defects/coops < 1.2) { //checks if opponent is random
			lastActionTemp = Action.DEFECT;
		}
		else {
			lastActionTemp = Action.COOPERATE;
		}
		return lastActionTemp;
	}
	
	/**
	 * Checks to see if opponent is copying moves. If so, a counter will be incremented.
	 * If not, the counter is reset to avoid false positives.
	 */
	private void countTitTat() {
		if (result.getOpponentAction() == lastAction) {
			counter += 1;
		}
		else{
			counter = 0;
		}
	}

	/**
	 * Keeps track of how many of each action type the opponent uses.
	 */
	private void sumActions() {
		if (result.getOpponentAction() == Action.DEFECT) { 
			defects += 1;
		}
		else {
			coops += 1;
		}
	}
	
}
