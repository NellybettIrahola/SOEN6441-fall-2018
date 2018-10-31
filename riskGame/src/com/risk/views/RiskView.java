/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.views;

import com.risk.controllers.GameController;
import com.risk.controllers.MenuListener;
import com.risk.controllers.RiskController;
import com.risk.models.RiskModel;
import com.risk.views.attack.ArmiesLeft;
import com.risk.views.attack.AttackView;
import com.risk.views.game.MapPanel;
import com.risk.views.game.PhaseAuxiliar;
import com.risk.views.game.PhasePanel;
import com.risk.views.game.PlayerGameInfoPanel;
import com.risk.views.menu.MenuView;
import com.risk.views.menu.NewGamePanel;
import com.risk.views.menu.StartMenuView;
import com.risk.views.reinforcement.CardExchangeView;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Main View of the game
 *
 * @author n_irahol
 */
public final class RiskView extends javax.swing.JFrame {

    /**
     * Card exchange panel
     */
    private CardExchangeView exchangeView;
    /**
     * menuPanel reference to the menu panel
     */
    private MenuView menuPanel;
    /**
     * mapPanel reference to the view that manages the map
     */
    final private MapPanel mapPanel;
    /**
     * playerPanel reference to the view that manages the player information
     */
    final private PlayerGameInfoPanel playerPanel;
    /**
     * playerHandPanel reference to the view that has the cards of the plater
     */
    final private PhaseAuxiliar phaseAuxiliarPanel;
    
    /**
     * stagePanel reference to the view that manages the information of the
     * current stage
     */
    final private PhasePanel stagePanel;

    /**
     * Constructor of main view
     */
    public RiskView() {
        super("Risk Game");

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);

        this.phaseAuxiliarPanel = new PhaseAuxiliar();
        
        this.stagePanel = new PhasePanel();
        this.playerPanel = new PlayerGameInfoPanel();
        this.mapPanel = new MapPanel();

        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(this.phaseAuxiliarPanel, BorderLayout.SOUTH);
        cp.add(this.stagePanel, BorderLayout.NORTH);
        cp.add(this.playerPanel, BorderLayout.EAST);
        cp.add(this.mapPanel, BorderLayout.CENTER);

        this.addMenuBar();

        setSize(1000, 563);
        this.centerWindow();
    }

    /**
     * Update the information displayed in the view to match the ones in the
     * model
     *
     * @param rm Model of the game
     */
    public void updateView(RiskModel rm) {
        this.getStagePanel().updateView(rm);
        this.getMapPanel().updateView(rm, false);
        this.getPlayerPanel().updateView(rm);

    }
    
    /**
     * Updates the view in the button
     * @param countrySource country with the attack
     * @param countryDest country attacked
     * @param gc game controller
     * @param armies max of dices or armies to be moved
     * @param panel the panel to show
     */
    public void updateAuxiliarPhasePanel(String countrySource, String countryDest,GameController gc, int armies,int panel){
        switch(panel){
            case 0:
                if(this.phaseAuxiliarPanel.getAttackPanel()==null){
                    AttackView attackPanel=new AttackView(countrySource,countryDest,gc);
                    this.phaseAuxiliarPanel.setAttackPanel(attackPanel);
                }
                this.phaseAuxiliarPanel.updatePanel((JPanel) phaseAuxiliarPanel.getAttackPanel());
                this.phaseAuxiliarPanel.getAttackPanel().update(countrySource,countryDest, armies);
                break;
            case 1:
                if(this.phaseAuxiliarPanel.getArmiesLeft()==null){
                    ArmiesLeft moveArmiesPanel=new ArmiesLeft();
                    moveArmiesPanel.setListener(gc);
                    this.phaseAuxiliarPanel.setArmiesLeft(moveArmiesPanel);
                }
                this.phaseAuxiliarPanel.updatePanel(phaseAuxiliarPanel.getArmiesLeft());
                this.phaseAuxiliarPanel.getArmiesLeft().update(countrySource,countryDest,gc,armies);
                break;
            default:
                this.phaseAuxiliarPanel.removeAll();
        }
    }
    
   
    /**
     * Update the information displayed in the view to match the ones in the
     * model Update also the map which can have changed (if not changed just
     * overhead)
     *
     * @param rm model of the game
     */
    public void updateViewWithNewMap(RiskModel rm) {
        this.getStagePanel().updateView(rm);
        this.getMapPanel().updateView(rm, true);
        this.getPlayerPanel().updateView(rm);
        //this.getPlayerHandPanel().updateView(rm);

        this.setSize(
                rm.getMap().getMapWidth() + 200,
                rm.getMap().getMapHeight() + 200
        );

        this.centerWindow();
    }

    /**
     * Open a Message Dialog to show message to user
     *
     * @param message Text to be displayed in the message dialog
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Link the view to the controller by setting all required listeners
     *
     * @param rc Controller
     */
    public void setController(RiskController rc) {
        this.getMapPanel().setListener(rc.getCountryListener());
        
        this.getStagePanel().getEndPhase().addActionListener(e -> {
            rc.getPlayGame().finishPhase();
        });

        

        Component c = this.getJMenuBar().getMenu(0).getMenuComponent(0);
        if (c instanceof JMenuItem) {
            JMenuItem j = (JMenuItem) c;
            j.addActionListener(e -> {
                rc.newGameMenuItemPressed();
            });
        }

        this.getMenuPanel().getStartMenu().getNewGamePanel().getOpenMapEditor().addActionListener(e -> {
            rc.openMapEditor();
        });
    }

    /**
     * Initialize the new game menu
     *
     * @param riskModel model of the game
     * @param menuListener listen the events in the menu
     */
    public void initialMenu(RiskModel riskModel, MenuListener menuListener) {

        StartMenuView start = new StartMenuView(riskModel, menuListener);
        MenuView aux = new MenuView(start, this, "New Game");
        this.setMenuPanel(aux);
        aux.add(start);
        aux.setVisible(true);
        aux.setSize(300, 500);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        aux.setLocation(dimension.width / 2 - 300 / 2, dimension.height / 2 - 500 / 2);
    }
    /**
     * Hide the panels
     */
    public void hideAttack(){
        if(this.phaseAuxiliarPanel.getAttackPanel()!=null)
            this.phaseAuxiliarPanel.getAttackPanel().setVisible(false);
        
        if(this.phaseAuxiliarPanel.getArmiesLeft()!=null)
            this.phaseAuxiliarPanel.getArmiesLeft().setVisible(false);
    }
    
    /**
     * Creates the card exchange view
     * @param riskModel the model of the game
     * @param rc the controller of the game
     */
    public void cardExchangeMenu(RiskModel riskModel, RiskController rc) {
        
        this.exchangeView = new CardExchangeView(riskModel);
        this.getExchangeView().setListener(rc.getCardExchangeListener());
        rc.getCardExchangeListener().setPanel(this.getExchangeView());
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        getExchangeView().setLocation(dimension.width / 2 - (this.exchangeView.getWidth()) / 2, dimension.height / 2 - 500 / 2);
        getExchangeView().setVisible(true);
        
    }
    /**
     * Closes the exchange card view
     */
    public void closeExchangeMenu(){
        this.exchangeView.setVisible(false);
        this.remove(this.getExchangeView());
        this.exchangeView=null;
    }

    /**
     * Close menu action
     */
    public void closeMenu() {
        this.menuPanel.setVisible(false);
        this.remove(this.menuPanel);
        this.setMenuPanel(null);
    }

    /**
     * Getter of the new game panel inside the menu panel
     *
     * @return the new panel
     */
    public NewGamePanel getNewGamePanel() {
        return this.getMenuPanel().getStartMenu().getNewGamePanel();
    }

    /**
     * Getter of the player hands panel
     *
     * @return the hands panel of the player
     */
    PhaseAuxiliar getPhaseAuxiliarPanel() {
        return phaseAuxiliarPanel;
    }

    /**
     * Initialize the menu bar of the game
     *
     */
    private void addMenuBar() {
        JMenuBar menuBar;
        JMenu menuFile, menuOption;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //Build the first menu.
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_A);
        menuFile.getAccessibleContext().setAccessibleDescription("File");
        menuBar.add(menuFile, BorderLayout.NORTH);

        //a group of JMenuItems
        menuFile.setLayout(new BoxLayout(menuFile, BoxLayout.Y_AXIS));
        menuItem = new JMenuItem("New Game");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Show New Game");
        menuFile.add(menuItem);

        //Build 2do menu
        menuOption = new JMenu("Options");
        menuOption.setMnemonic(KeyEvent.VK_A);
        menuOption.getAccessibleContext().setAccessibleDescription("Options");
        menuBar.add(menuOption, BorderLayout.NORTH);

        this.setJMenuBar(menuBar);
        this.getJMenuBar().setVisible(true);
    }

    /**
     * Getter of the menuPanel attribute
     *
     * @return the menuPanel
     */
    MenuView getMenuPanel() {
        return menuPanel;
    }

    /**
     * Setter of the menuPanel attribute
     *
     * @param menuPanel the menuPanel to set
     */
    void setMenuPanel(MenuView menuPanel) {
        this.menuPanel = menuPanel;
    }

    /**
     * Getter of the mapPanel attribute
     *
     * @return the mapPanel
     */
    MapPanel getMapPanel() {
        return mapPanel;
    }

    private void centerWindow() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width / 2 - this.getSize().width / 2, dimension.height / 2 - this.getSize().height / 2);
    }

    /**
     * Getter of the playerPanel attribute
     *
     * @return the playerPanel
     */
    PlayerGameInfoPanel getPlayerPanel() {
        return playerPanel;
    }

    /**
     * @return the reinforcementArmies
     */
    PhasePanel getStagePanel() {
        return stagePanel;
    }

    /**
     * @return the exchangeView
     */
    public CardExchangeView getExchangeView() {
        return exchangeView;
    }
}
