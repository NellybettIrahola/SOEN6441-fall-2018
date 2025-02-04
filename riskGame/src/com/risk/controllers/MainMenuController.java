/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.controllers;

import com.risk.models.MapModel;
import com.risk.models.RiskModel;
import com.risk.models.TournamentModel;
import com.risk.views.MainMenuView;
import com.risk.views.RiskView;
import com.risk.views.editor.MapEditorView;
import com.risk.views.menu.TournamentMenuView;

/**
 * Main menu controller
 *
 * @author hantoine
 */
public class MainMenuController {

    /**
     * Main menu view for choosing mode and map editor
     */
    MainMenuView mmv;

    /**
     * Constructor
     *
     * @param mmv Main menu view
     */
    public MainMenuController(MainMenuView mmv) {
        this.mmv = mmv;
        this.mmv.setVisible(true);
    }

    /**
     * Choose single game mode
     */
    public void startSingleGameMode() {
        RiskModel riskModel = new RiskModel();
        RiskView riskView = new RiskView();
        RiskController riskController = new RiskController(riskModel, riskView);
        riskView.setController(riskController);
        this.mmv.close();
    }

    /**
     * Choose tournament mode
     */
    public void startTournamentMode() {
        TournamentModel tm = new TournamentModel();
        TournamentMenuView tmv = new TournamentMenuView();
        TournamentController tc = new TournamentController(tm, tmv);
        tmv.setController(tc);
        tm.addObserver(tmv);
        this.mmv.close();
    }

    /**
     * Choose map editor
     */
    public void startMapEditor() {
        MapModel newMap = new MapModel();
        MapEditorController editorController = new MapEditorController(newMap);
        MapEditorView mev = new MapEditorView(1000, 600, editorController, newMap);
        mev.setVisible(true);
        newMap.addObserver(mev);
        newMap.addObserver(mev.getMapView());
        newMap.addObserver(mev.getContinentListPanel());
        this.mmv.close();
    }

}
