/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * It represents a territory in the map
 *
 * @author n_irahol
 */
public class TerritoryModel implements Serializable {

 

    /**
     * adj list of adjacent territories
     */
    private LinkedList<TerritoryModel> adj;
    /**
     * positionX x coordinate of the location of the territory
     */
    private int positionX;
    /**
     * positionY y coordinate of the location of the territory
     */
    private int positionY;
    /**
     * continentName the name of the continent which it belongs
     */
    private String continentName;
    /**
     * numArmies the number of armies in the territory
     */
    private int numArmies;
    /**
     * owner reference to the player that owns the territory
     */
    private PlayerModel owner;
    /**
     * name the name of the this territory
     */
    private String name;

    /**
     * Constructor
     *
     * @param name name of a territory
     * @param positionX position in x in the image
     * @param positionY position in y in the image
     */
    public TerritoryModel(String name, int positionX, int positionY) {
        this.name = name;
        this.adj = new LinkedList();
        this.positionX = positionX;
        this.positionY = positionY;
        this.numArmies = 0;
        this.owner = null;
    }

    /**
     * Constructor
     *
     * @param name name of a territory
     */
    public TerritoryModel(String name) {
        this.name = name;
        this.numArmies = 0;
        this.positionX = -1;
        this.positionY = -1;
        this.owner = null;
        this.adj = new LinkedList();
    }

    /**
     * Add an adjacent territory
     *
     * @param neighbour the neighbour which is gonna be added
     */
    void addNeighbour(TerritoryModel neighbour) {
        this.adj.add(neighbour);
    }

       /**
     * Alias for the equal function in order to test equality between two objects of the same class.
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
        final TerritoryModel other = (TerritoryModel) obj;
        if (this.positionX != other.positionX) {
            return false;
        }
        if (this.positionY != other.positionY) {
            return false;
        }
        if (this.numArmies != other.numArmies) {
            return false;
        }
        if (!Objects.equals(this.continentName, other.continentName)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        
        if ((this.owner != null && other.owner != null) && !this.owner.getName().equals(other.owner.getName())) {
            return false;
        }
        
        //comparing adj : list of territory
        if((this.adj != null && other.adj != null) && !(this.adj.size() == other.adj.size()))
            return false;
        if(this.adj != null && other.adj != null){
            LinkedList<String> thisAdj = new LinkedList<>();
        for(TerritoryModel t:this.adj)
            thisAdj.add(t.getName());
        for(TerritoryModel t:other.getAdj())
            if(!thisAdj.contains(t.getName()))
                return false;
        }
        
        
        return true;
    }
    
    /**
     * Eliminates an adjacent territory
     *
     * @param neighbour the neighbour which is gonna be deleted
     */
    void removeNeighbour(TerritoryModel neighbour) {
        this.adj.remove(neighbour);
    }

    /**
     * Setter for the position of a territory
     *
     * @param positionX the position X of this territory
     * @param positionY the position Y of this territory
     */
    void territorySetter(int positionX, int positionY) {

        this.setPositionX(positionX);
        this.setPositionY(positionY);

    }

    /**
     * Getter for the positionX attribute
     *
     * @return positionX
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Getter for the positionY attribute
     *
     * @return positionY
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Getter for the adj attribute
     *
     * @return the adj
     */
    public List<TerritoryModel> getAdj() {
        return Collections.unmodifiableList(adj);
    }

    /**
     * Add adjacent territory
     *
     * @param adjacentTerritory territory to add as an adjacent territory
     */
    void addAdjacentTerritory(TerritoryModel adjacentTerritory) {
        this.adj.add(adjacentTerritory);
    }

    /**
     * Setter for the adj attribute
     *
     * @param adj the adj to set
     */
    void setAdj(LinkedList<TerritoryModel> adj) {
        this.adj = adj;
    }

    /**
     * Getter for the numArmies attribute
     *
     * @return the numArmies
     */
    public int getNumArmies() {
        return numArmies;
    }

    /**
     * Increase the number of armies on the territory by one
     *
     * @return The new number of armies on this territory
     */
    int incrementNumArmies() {
        return ++numArmies;
    }

    /**
     * Decrease the number of armies on the territory by one
     *
     * @return The new number of armies on this territory
     */
    int decrementNumArmies() throws IllegalStateException {
        if (numArmies == 1) {
            throw new IllegalStateException("Less than one army on a territory not allowed.");
        }
        return --numArmies;
    }

    /**
     * Setter for the numArmies attribute
     *
     * @param numArmies the numArmies to set
     */
    void setNumArmies(int numArmies) {
        this.numArmies = numArmies;
    }

    /**
     * Getter for the owner attribute
     *
     * @return the owner
     */
    public PlayerModel getOwner() {
        return owner;
    }

    /**
     * Setter for the owner attribute
     *
     * @param owner the owner to set
     */
    void setOwner(PlayerModel owner) {
        this.owner = owner;
    }

    /**
     * Getter for the name attribute
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name attribute
     *
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the continentName attribute
     *
     * @return the continentName
     */
    public String getContinentName() {
        return continentName;
    }

    /**
     * Setter for the continentName attribute
     *
     * @param continentName the continentName to set
     */
    void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    /**
     * Setter for the positionX attribute
     *
     * @param positionX the positionX to set
     */
    void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * Setter for the positionY attribute
     *
     * @param positionY the positionY to set
     */
    void setPositionY(int positionY) {
        this.positionY = positionY;
    }

}
