/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.controllers;

import com.risk.models.MapFileManagement;
import com.risk.models.MapPath;
import com.risk.models.Strategy;
import com.risk.models.TournamentModel;
import com.risk.views.LogViewer;
import com.risk.views.TournamentResultsView;
import com.risk.views.menu.MapPathListPanel.MapPathListPanelListener;
import com.risk.views.menu.StrategyListPanel.StrategyListPanelListener;
import com.risk.views.menu.TournamentMenuView;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.Timer;

/**
 * Tournament mode controller
 *
 * @author hantoine
 */
public class TournamentController implements StrategyListPanelListener, MapPathListPanelListener, TournamentSaverInterface {

    /**
     * View of tournament mode
     */
    TournamentMenuView tmv;
    /**
     * Attribute of tournament mode
     */
    TournamentModel tm;
    /**
     * Show the tournament result view
     */
    TournamentResultsView trv;
    /**
     * Period between to savings in player per map per max turn per nbGamePerMap
     */
    static final float SAVING_PERIOD = 0.01f;
    /**
     * Number of attempt since last saving
     */
    int nbSaveAttempts;

    /**
     * Constructor
     *
     * @param tm model of tournament mode
     * @param tmv view of tournament mode
     */
    public TournamentController(TournamentModel tm, TournamentMenuView tmv) {
        this.tm = tm;
        this.tmv = tmv;
        nbSaveAttempts = 0;
    }

    /**
     * Add strategy to tournament players
     *
     * @param strategyType strategy of player
     */
    @Override
    public void strategyAdded(Strategy.Type strategyType) {
        tm.addPlayerStategies(strategyType);
    }

    /**
     * Remove strategy from tournament players
     *
     * @param strategyType strategy of player
     */
    @Override
    public void strategyRemoved(Strategy.Type strategyType) {
        tm.removePlayerStategies(strategyType);
    }

    /**
     * Choose maps that players will play
     *
     * @param mapPath map path
     */
    @Override
    public void mapAdded(MapPath mapPath) {
        try {
            tm.addMapsPath(mapPath);
        } catch (IllegalStateException e) {
            this.tmv.showError(e.getMessage());
        }
    }

    /**
     * Remove maps
     *
     * @param mapPath map path
     */
    @Override
    public void mapRemoved(MapPath mapPath) {
        tm.removeMapsPath(mapPath);
    }

    /**
     * Play the tournament
     */
    public void playTournament() {
        tm.setTournamentSaver(this);
        try {
            if (!tm.playTournament()) {
                return;
            }
            displayResultWhenFinished();

        } catch (MapFileManagement.MapFileManagementException ex) {
            tmv.showError(ex.getMessage());
        }
    }

    /**
     * Check if tournament is finished every 300 milliseconds if finished, show
     * the result view
     */
    private void displayResultWhenFinished() {
        if (tm.isTournamentFinished()) {
            trv = new TournamentResultsView(tm);
            tmv.setVisible(false);
            tmv.dispose();
            trv.setController(this);
        } else {
            Timer timer = new Timer(300, (ActionEvent ae) -> {
                this.displayResultWhenFinished();
            });
            timer.setRepeats(false); // Only execute once
            timer.start();
        }
    }

    /**
     * Set maximum number of turn in a game
     *
     * @param value number of turn per map
     */
    public void nbMaximumTurnPerGameChanged(int value) {
        tm.setMaximumTurnPerGame(value);
    }

    /**
     * Set number of game for each map
     *
     * @param value number of game per map
     */
    public void nbGamePerMapChanged(int value) {
        tm.setNbGamePerMap(value);
    }

    /**
     * click the result cell and check the log of the game
     *
     * @param i row number
     * @param j column number
     */
    public void clickResultCell(int i, int j) {
        if (i == 0 || j == 0) {
            return;
        }

        //create view from file
        List<String> logs;
        try {
            logs = Files.readAllLines(
                    Paths.get("logs", tm.getLogFile(j - 1, i - 1))
            );
            LogViewer lv = new LogViewer(logs);
        } catch (IOException ex) {
            this.tmv.showError("Failed to open log file");
        }
    }

    /**
     * Save the tournament to a file
     */
    @Override
    public void saveTournament() {
        if (!this.tm.isSavable()) {
            return;
        }

        nbSaveAttempts++;
        int totalSaveEvery = (int) (SAVING_PERIOD
                * this.tm.getMapsPaths().size()
                * this.tm.getNbGamePerMap()
                * this.tm.getPlayerStategies().size()
                * this.tm.getMaximumTurnPerGame());
        if (nbSaveAttempts % totalSaveEvery != 0) {
            return;
        }

        String path = Paths.get(
                "savedTournaments",
                String.format(
                        "%s-%d.ser",
                        this.tm.getFileIdentifier(),
                        nbSaveAttempts / totalSaveEvery
                )
        ).toString();
        try (FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this.tm);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Load the tournament and continue it
     *
     * @param path Path of the file containing the tournament to load
     */
    public void loadTournament(String path) {
        try (FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            this.tm = (TournamentModel) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            return;
        }

        tm.setTournamentSaver(this);
        this.displayResultWhenFinished();
        if (!this.tm.isTournamentFinished()) {
            this.tm.resume();
        }
    }

}
