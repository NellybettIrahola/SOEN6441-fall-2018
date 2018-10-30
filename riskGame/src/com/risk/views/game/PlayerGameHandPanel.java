/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.views.game;

import com.risk.models.PlayerModel;
import com.risk.models.RiskModel;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * View of the cards owned by the player
 *
 * @author liyixuan
 */
public final class PlayerGameHandPanel extends JPanel implements Observer {

    /**
     * The view of the cards of the current player
     */
    HashMap<String, JButton> handCards = new HashMap<>();

    /**
     * Constructor
     *
     */
    public PlayerGameHandPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    /**
     * Update the information displayed by the PlayerGameHandPanl according to
     * the information of the current player
     *
     * @param rm The model of the game containing all information about the
     * current player
     */
    public void updateView(RiskModel rm) {
        PlayerModel currentPlayer = rm.getCurrentPlayer();
        this.removeAll();
        currentPlayer.getCardsOwned().getCards().forEach((card) -> {
            addCard(card.getTypeOfArmie(),
                    card.getCountryName(),
                    currentPlayer.getColor()
            );
        });
        this.repaint();
    }

    /**
     * Observer patter implementation
     *
     * @param o the observable
     * @param arg the object
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof RiskModel) {
            this.updateView((RiskModel) arg);
        } else {
            LinkedList<String> receivedObj = (LinkedList<String>) arg;
            addCard(receivedObj.get(2),
                    receivedObj.get(1),
                    ((PlayerModel) o).getColor()
            );
        }
    }

    /**
     * Add a card to be displayed in this panel
     *
     * @param armyType The name of the type of armies associated with the card
     * @param territoryName The name of the territory associated with the card
     * @param bgColor The background color of the card
     */
    public void addCard(String armyType, String territoryName, Color bgColor) {
        ImageIcon cardIcon = new ImageIcon("." + File.separator + "images"
                + File.separator + armyType + ".png");
        Image image = cardIcon.getImage();
        Image newImage = image.getScaledInstance(50, 70, Image.SCALE_SMOOTH);
        cardIcon = new ImageIcon(newImage);
        JButton aux = new JButton();
        aux.setIcon(cardIcon);
        aux.setText("");
        aux.setBackground(bgColor);
        handCards.put(territoryName, aux);
        this.add(aux);
    }
}
