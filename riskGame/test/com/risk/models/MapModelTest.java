/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test methods in the map model
 *
 * @author hantoine
 */
public class MapModelTest {

    /**
     * mapModel instance of the map model
     */
    MapModel mapModel;

    /**
     * Constructor
     */
    public MapModelTest() {
    }

    /**
     * Create a valid MapModel that will be the base for all test cases
     */
    @Before
    public void setUp() {
        mapModel = new MapModel();

        HashMap<String, ContinentModel> graphContinents = new HashMap<>();
        String name;
        name = "ContinentA";
        graphContinents.put(name, new ContinentModel(name, 3));
        name = "ContinentB";
        graphContinents.put(name, new ContinentModel(name, 2));

        HashMap<String, TerritoryModel> graphTerritories = new HashMap<>();
        name = "TerritoryA";
        graphTerritories.put(name, new TerritoryModel(name, 50, 50));
        name = "TerritoryB";
        graphTerritories.put(name, new TerritoryModel(name, 100, 50));
        name = "TerritoryC";
        graphTerritories.put(name, new TerritoryModel(name, 50, 100));
        name = "TerritoryD";
        graphTerritories.put(name, new TerritoryModel(name, 200, 200));
        name = "TerritoryE";
        graphTerritories.put(name, new TerritoryModel(name, 250, 250));

        LinkedList<TerritoryModel> memberTerritories = new LinkedList();
        graphTerritories.get("TerritoryA").setContinentName("ContinentA");
        memberTerritories.add(graphTerritories.get("TerritoryA"));
        graphTerritories.get("TerritoryB").setContinentName("ContinentA");
        memberTerritories.add(graphTerritories.get("TerritoryB"));
        graphTerritories.get("TerritoryC").setContinentName("ContinentA");
        memberTerritories.add(graphTerritories.get("TerritoryC"));
        graphContinents.get("ContinentA").setMembers(memberTerritories);
        memberTerritories = new LinkedList();
        graphTerritories.get("TerritoryD").setContinentName("ContinentB");
        memberTerritories.add(graphTerritories.get("TerritoryD"));
        graphTerritories.get("TerritoryE").setContinentName("ContinentB");
        memberTerritories.add(graphTerritories.get("TerritoryE"));
        graphContinents.get("ContinentB").setMembers(memberTerritories);

        LinkedList<TerritoryModel> adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryB"));
        adjacentTerritories.add(graphTerritories.get("TerritoryC"));
        graphTerritories.get("TerritoryA").setAdj(adjacentTerritories);

        adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryB"));
        adjacentTerritories.add(graphTerritories.get("TerritoryC"));
        graphTerritories.get("TerritoryA").setAdj(adjacentTerritories);

        adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryA"));
        adjacentTerritories.add(graphTerritories.get("TerritoryC"));
        graphTerritories.get("TerritoryB").setAdj(adjacentTerritories);

        adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryA"));
        adjacentTerritories.add(graphTerritories.get("TerritoryB"));
        adjacentTerritories.add(graphTerritories.get("TerritoryD"));
        graphTerritories.get("TerritoryC").setAdj(adjacentTerritories);

        adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryE"));
        adjacentTerritories.add(graphTerritories.get("TerritoryC"));
        graphTerritories.get("TerritoryD").setAdj(adjacentTerritories);

        adjacentTerritories = new LinkedList<>();
        adjacentTerritories.add(graphTerritories.get("TerritoryD"));
        graphTerritories.get("TerritoryE").setAdj(adjacentTerritories);

        mapModel.setGraphTerritories(graphTerritories);
        mapModel.setGraphContinents(graphContinents);
    }

    /**
     * After the execution assigns null to the instance
     */
    @After
    public void tearDown() {
        mapModel = null;
    }

    /**
     * Test of isValid method, of class MapModel. Test that the method returns
     * true with a simple valid map
     */
    @Test
    public void testIsValidonValidMap() {
        boolean expResult = true;
        boolean result = mapModel.isValid();
        assertEquals(expResult, result);
    }
    
    /**
     * Test if the new name does not already exists.
     * I.e. that the new name is really "new".
     */
    @Test
    public void testGetNewName(){
        String newTerritoryName = this.mapModel.getNewName(false);
        List<String> names = this.mapModel.getTerritoryList();
        assertFalse(names.contains(newTerritoryName));
        
        String newContinentName = this.mapModel.getNewName(true);
        names = this.mapModel.getContinentList();
        assertFalse(names.contains(newContinentName));
    }

    /**
     * Test of isValid method, of class MapModel. Test that the method returns
     * false with a map whose graph of territories is not connected
     */
    @Test
    public void testIsValidonNotConnectedMap() {
        mapModel.removeLink("TerritoryD", "TerritoryC");

        boolean expResult = false;
        boolean result = mapModel.isValid();
        assertEquals(expResult, result);
    }

    /**
     * Test of isValid method, of class MapModel. Test that the method returns
     * false with a map containing a continent whose subgraph of territories is
     * not connected
     */
    @Test
    public void testIsValidonContinentNotConnectedMap() {
        // Create a new continent ContinentC which is not connected
        mapModel.addContinent("ContinentC", 3);
        mapModel.getGraphTerritories()
                .get("TerritoryA").setContinentName("ContinentC");
        mapModel.getGraphTerritories()
                .get("TerritoryE").setContinentName("ContinentC");

        boolean expResult = false;
        boolean result = mapModel.isValid();
        assertEquals(expResult, result);
    }

    /**
     * Test of isValid method, of class MapModel. Test that the method returns
     * false with a map containing an empty continent
     */
    @Test
    public void testIsValidonEmptyContinentMap() {
        // Create a new continent without adding any territories in it
        mapModel.addContinent("ContinentC", 3);

        boolean expResult = false;
        boolean result = mapModel.isValid();
        assertEquals(expResult, result);
    }
}
