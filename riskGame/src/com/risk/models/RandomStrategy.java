/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.models;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 *
 * @author Nellybett
 */
public class RandomStrategy implements Strategy{

    @Override
    public void reinforcement(RiskModel rm) {
        rm.aIReinforcement();
        int armiesReinforcement = rm.getCurrentPlayer().getNbArmiesAvailable();
        
        while (armiesReinforcement > 0) {
            TerritoryModel territorySelected=rm.randomTerritory((LinkedList < TerritoryModel >)rm.getCurrentPlayer().getTerritoryOwned());
            rm.reinforcementIntent(territorySelected);
            armiesReinforcement = armiesReinforcement - 1;
        }
        
    }

    @Override
    public void attack(RiskModel playGame) {
        
    }

    @Override
    public void fortification(RiskModel playGame) {
    
    }
    
   @Override
    public void moveArmies(RiskModel rm) {
        rm.getCurrentPlayer().moveArmiesAI();
    }

    @Override
    public boolean exchangeCardsToArmies(RiskModel rm) {
        return rm.getCurrentPlayer().exchangeCardsToArmiesAI();
    }

    @Override
    public void defense(RiskModel rm) {
        rm.getCurrentPlayer().defenseAI();
    }

    @Override
    public void startup(RiskModel rm) {
        TerritoryModel territoryClicked=rm.randomTerritory((LinkedList < TerritoryModel >)rm.getMap().getTerritories().stream()
                                                                                                            .filter(t -> t.getOwner()==null || t.getOwner()==rm.getCurrentPlayer())
                                                                                                            .collect(Collectors.toCollection(LinkedList::new)));
        rm.startupMove(territoryClicked);
    }
    
}
