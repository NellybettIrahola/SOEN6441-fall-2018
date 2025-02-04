/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * It represents a continent
 *
 * @author n_irahol
 */
public class ContinentModel implements Serializable {

    

    /**
     * members territories that are part of the continent
     */
    private LinkedList<TerritoryModel> members;
    /**
     * bonusScore the number of extra armies per continent
     */
    private int bonusScore;
    /**
     * name the name of the continent
     */
    private String name;

    /**
     * Constructor
     *
     * @param name name of the continent
     * @param bonusScore bonus armies receive when a player conquer the
     * continent
     */
    ContinentModel(String name, int bonusScore) {
        this.name = name;
        this.bonusScore = bonusScore;
        this.members = new LinkedList<>();
    }

    /**
     * It removes a member from the continent
     *
     * @param member the member which is removed
     */
    void removeMember(TerritoryModel member) {
        this.members.remove(member);
    }

    /**
     * Getter of the members attribute
     *
     * @return the members
     */
    LinkedList<TerritoryModel> getMembers() {
        return members;
    }

    /**
     * Setter of the members attribute
     *
     * @param members the members to set
     */
    void setMembers(LinkedList<TerritoryModel> members) {
        this.members = members;
    }

    /**
     * Adds a member
     *
     * @param member the member to add to members list
     */
    void addMember(TerritoryModel member) {
        this.members.add(member);
    }

    /**
     * Getter of the bonusScore attribute
     *
     * @return the bonusScore
     */
    public int getBonusScore() {
        return bonusScore;
    }

    /**
     * Setter of the bonusScore attribute
     *
     * @param newBonusScore new value of the bonusScore attribute
     */
    void setBonusScore(int newBonusScore) {
        this.bonusScore = newBonusScore;
    }

    /**
     * Getter of the name attribute
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of the name attribute
     *
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Check that this continent is valid, that is to say that it contains
     * territories and that it is a connected graph
     *
     * @return True if this continent is valid
     */
    public boolean check() {
        if (members.isEmpty()) {
            return false;
        }

        List<String> visitedTerritories = new ArrayList<>(members.size());
        dfsConnected(members.getFirst(), visitedTerritories);
        return visitedTerritories.size() == members.size();
    }

    /**
     * Use Deep First Search algorithm to verify that each territory in the
     * continent is reachable through territories of this continent
     *
     * @param v Territory currently visited
     * @param visitedTerritories List of already visited territories
     */
    private void dfsConnected(TerritoryModel v, List<String> visitedTerritories) {
        visitedTerritories.add(v.getName());

        v.getAdj().stream()
                .filter((c) -> (!(visitedTerritories.contains(c.getName()))))
                .filter((c) -> (c.getContinentName().equals(this.getName())))
                .forEach((c) -> {
                    dfsConnected(c, visitedTerritories);
                });
    }

    
    /**
     * Alias for the equal function in order to test equality between two
     * objects of the same class.
     *
     * @param obj object of the same class we want to compare to this instance.
     * @return boolean to know if the objects are equal or not
     */
    public boolean identical(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContinentModel other = (ContinentModel) obj;
        if (this.bonusScore != other.bonusScore) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }

        //comparing members : list of territories
        if ((this.members != null && other.members != null) && !(this.members.size() == other.members.size())) {
            return false;
        }

        if (this.members != null && other.members != null) {
            LinkedList thisList = new LinkedList<>();
            for (TerritoryModel t : this.members) {
                thisList.add(t.getName());
            }
            for (TerritoryModel t : other.members) {
                if (!thisList.contains(t.getName())) {
                    return false;
                }
            }
        }

        return true;
    }
}
