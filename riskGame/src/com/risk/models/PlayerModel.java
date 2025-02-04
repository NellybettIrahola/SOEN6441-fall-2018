/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risk.models;

import java.awt.Color;
import java.io.Serializable;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * It represents a Player in the game It is the parent of HumanPlayerModel and
 * AIPlayerModel
 *
 * @author n_irahol
 */
public class PlayerModel extends Observable implements Serializable {

    /**
     * name the name of the player color the color of the player contriesOwned
     */
    private String name;

    
    /**
     * color of the player
     */
    private Color color;
    /**
     * cardsOwned cards owned by a player
     */
    public List<TerritoryModel> territoryOwned;
    /**
     * continents owned by a player
     */
    public List<ContinentModel> continentsOwned;
    /**
     * hand of the player
     */
    private HandModel hand;
    /**
     * The number of armies available to place returnedCards
     */
    private int numArmiesAvailable;
    /**
     * the number of cards that have been returned game the game in which this
     */
    private int returnedCards;
    /**
     * the model of the game
     */
    protected RiskModel game;
    /**
     * the current movement in the fortification phase
     */
    private FortificationMove currentFortificationMove;
    /**
     * The current attack in the attack phase
     */
    private AttackMove currentAttack;
    /**
     * the current player
     */
    private boolean currentPlayer;

    /**
     * If a player conquer a territory
     */
    private boolean conquered;

    /**
     * Strategy of the player
     */
    private Strategy strategy;
    /**
     * If a card exchange is offered
     */
    private boolean cardExchangeOffered;

    /**
     * Constructor
     *
     * @param name name of a player
     * @param color color of a player
     */
    public PlayerModel(String name, Color color) {
        this.name = name;
        this.color = color;
        this.territoryOwned = new LinkedList<>();
        this.continentsOwned = new LinkedList<>();
        this.hand = new HandModel();
        this.hand.setOwner(this);
        this.numArmiesAvailable = 0;
        this.returnedCards = 0;
        this.currentAttack = null;
        this.currentPlayer = false;
    }

    /**
     * Definition of the reinforcement phase. Called at the beginning of the
     * phase. Depending on the type of player it will either initialize and
     * update the UI for the human player to play or execute the action with the
     * artificial intelligence
     *
     * @param playGame GameController reference used to access game informations
     * and methods
     */
    public void reinforcement(RiskModel playGame) {
        addNewLogEvent(String.format(
                "%s starts its reinforcement phase",
                getName()
        ));
        this.setCardExchangeOffered(false);
        strategy.reinforcement(playGame);
    }

    /**
     * Startup movement
     *
     * @param playGame risk model
     */
    public void startup(RiskModel playGame) {
        addNewLogEvent(String.format(
                "%s startup move",
                getName()
        ));
        strategy.startup(playGame);
    }

    /**
     * Definition of the fortification phase. Called at the beginning of the
     * phase. Depending on the type of player it will either initialize and
     * update the UI for the human player to play or execute the action with the
     * artificial intelligence
     *
     * @param playGame GameController reference used to access game informations
     * and methods
     */
    public void fortification(RiskModel playGame) {
        addNewLogEvent(String.format(
                "%s starts its fortification phase",
                getName()
        ));
        strategy.fortification(playGame);
    }

    /**
     * @param playGame GameController reference used to access game informations
     * and methods
     */
    public void attack(RiskModel playGame) {
        strategy.attack(playGame);
    }

    /**
     * @return the strategy
     */
    public Strategy getStrategy() {
        return strategy;
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
        final PlayerModel other = (PlayerModel) obj;
        if (this.numArmiesAvailable != other.numArmiesAvailable) {
            return false;
        }
        if (this.returnedCards != other.returnedCards) {
            return false;
        }
        if (this.currentPlayer != other.currentPlayer) {
            return false;
        }
        if (this.conquered != other.conquered) {
            return false;
        }
        if (this.cardExchangeOffered != other.cardExchangeOffered) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }

        //comparing territoryOwned : list of territories
        if ((this.territoryOwned != null && other.territoryOwned != null) && !(this.territoryOwned.size() == other.territoryOwned.size())) {
            return false;
        }

        LinkedList<String> thisList;
        if(this.territoryOwned != null && other.territoryOwned != null) {
            thisList = new LinkedList<>();
        for (TerritoryModel t : this.territoryOwned) {
            thisList.add(t.getName());
        }
        for (TerritoryModel t : other.territoryOwned) {
            if (!thisList.contains(t.getName())) {
                return false;
            }
        }
        }
        

        //comparing continentsOwned : list of continents
        if ((this.continentsOwned != null && other.continentsOwned != null) && !(this.continentsOwned.size() == other.continentsOwned.size())) {
            return false;
        }

        if (this.continentsOwned != null && other.continentsOwned != null) {
            thisList = new LinkedList<>();
            for (ContinentModel t : this.continentsOwned) {
                thisList.add(t.getName());
            }
            for (ContinentModel t : other.continentsOwned) {
                if (!thisList.contains(t.getName())) {
                    return false;
                }
            }
        }

        if ((this.hand != null && other.hand != null) && !this.hand.identical(other.hand)) {
            return false;
        }

        if ((this.currentFortificationMove != null && other.currentFortificationMove != null) && !this.currentFortificationMove.identical(other.currentFortificationMove)) {
            return false;
        }
        if ((this.currentAttack != null && other.currentAttack != null) && !this.currentAttack.identical(other.currentAttack)) {
            return false;
        }
        if ((this.strategy != null && other.strategy != null) && !this.strategy.identical(other.strategy)) {
            return false;
        }
        return true;
    }
    
    /**
     * Getter of the cardExchageOffered attribute
     *
     * @return cardExchangeOffered attribute
     */
    public boolean isCardExchangeOffered() {
        return cardExchangeOffered;
    }

    /**
     * Setter of the cardExchangeOffered attribute
     *
     * @param cardExchangeOffered boolean that determines if exchange was
     * offered
     */
    public void setCardExchangeOffered(boolean cardExchangeOffered) {
        this.cardExchangeOffered = cardExchangeOffered;
    }

    /**
     * Setter of the strategy
     *
     * @param strategy the strategy to set
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Exchange cards according strategy
     *
     * @return true if it is success
     */
    boolean exchangeCardsToArmies() {
        return strategy.exchangeCardsToArmies(this.game);
    }

    /**
     * Defense according to strategy
     */
    public void defense() {
        strategy.defense(this.game);
    }

    /**
     * Move armies according to strategy
     */
    public void moveArmies() {
        strategy.moveArmies(this.game);
    }

    /**
     * Getter of the name attribute
     *
     * @return the name of this player
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

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the currentPlayer attribute
     *
     * @return whether this player is the current player or not
     */
    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter of the currentPlayer attribute
     *
     * @param currentPlayer whether this player is the current player or not
     */
    void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.hand.setCurrent(currentPlayer);

        if (currentPlayer) {
            addNewLogEvent(String.format(
                    "%s starts its turn",
                    getName()
            ), true);
        } else {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Getter of the color attribute
     *
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * This method is to get the current game
     *
     * @return the gaame
     */
    RiskModel getGame() {
        return game;
    }

    /**
     * This method is for set the game
     *
     * @param game the game which is gonna be setter
     */
    void setGame(RiskModel game) {
        this.game = game;

        setChanged();
        notifyObservers();
    }

    /**
     * Setter of the color attribute
     *
     * @param color the color to set
     */
    void setColor(Color color) {
        this.color = color;

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the territoryOwned attribute
     *
     * @return the contriesOwned
     */
    public List<TerritoryModel> getTerritoryOwned() {
        return Collections.unmodifiableList(this.territoryOwned);
    }

    public boolean checkOwnTerritory(TerritoryModel territory) {
        return territoryOwned.contains(territory);
    }

    /**
     * Return the number of territories owned by this player
     *
     * @return the number of territories owned by this player
     */
    public int getNbTerritoriesOwned() {
        return territoryOwned.size();
    }

    /**
     * Return the number of territories owned by this player
     *
     * @return the number of territories owned by this player
     */
    public int getNbContinentsOwned() {
        return continentsOwned.size();
    }

    /**
     * Setter of the territoriesOwned attribute
     *
     * @param contriesOwned the contriesOwned to set
     */
    void setContriesOwned(Collection<TerritoryModel> contriesOwned) {

        this.territoryOwned.stream()
                .filter(c -> c.getOwner() != null)
                .forEach((c) -> {
                    c.getOwner().removeTerrOwned(c);
                });
        this.territoryOwned = new LinkedList(contriesOwned);

        this.territoryOwned.stream().forEach((c) -> {
            c.setOwner(this);
        });

        updateContinentsOwned();

        setChanged();
        notifyObservers();
    }

    /**
     * Add a territory to the list of territories owned by this player
     *
     * @param terrOwned the additional territory owned by this player
     */
    void addTerritoryOwned(TerritoryModel terrOwned) {
        if (terrOwned.getOwner() != null) {
            terrOwned.getOwner().removeTerrOwned(terrOwned);
        }
        this.territoryOwned.add(terrOwned);
        terrOwned.setOwner(this);

        updateContinentsOwned();

        setChanged();
        notifyObservers();
    }

    /**
     * Remove a territory from the list of territories owned by this player
     *
     * @param territoryOwned the territory no longer owned by this player
     */
    void removeTerrOwned(TerritoryModel territoryOwned) {
        this.territoryOwned.remove(territoryOwned);
        territoryOwned.setOwner(this);

        updateContinentsOwned();

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the continentsOwned attribute
     *
     * @return the continentsOwned
     */
    public Collection<ContinentModel> getContinentsOwned() {
        return Collections.unmodifiableCollection(this.continentsOwned);
    }

    /**
     * Setter of the continentsOwned attribute
     *
     * @param continentsOwned the continentsOwned to set
     */
    void setContinentsOwned(List<ContinentModel> continentsOwned) {
        this.continentsOwned = continentsOwned;

        setChanged();
        notifyObservers();
    }

    /**
     * Add a continent in the list of continents owned by this player
     *
     * @param newContinentOwned New continent owned by this player
     */
    void addContinentOwned(ContinentModel newContinentOwned) {
        if (this.continentsOwned == null) {
            this.continentsOwned = new LinkedList<>();
        }

        this.continentsOwned.add(newContinentOwned);

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the numArmiesAvailable attribute
     *
     * @return the numArmies
     */
    public int getNbArmiesAvailable() {
        return numArmiesAvailable;
    }

    /**
     * Setter of the numArmiesAvailable attribute
     *
     * @param numArmies the numArmies to set
     */
    void setNumArmiesAvailable(int numArmies) {
        this.numArmiesAvailable = numArmies;

        setChanged();
        notifyObservers();
    }

    /**
     * Decrease by one the number of armies this player has available
     *
     * @return the new number of armies available for this player
     */
    int decrementNumArmiesAvailable() {
        this.numArmiesAvailable--;

        setChanged();
        notifyObservers();

        return this.numArmiesAvailable;
    }

    /**
     * Increase the number of armies this player has available
     *
     * @param i the number of the armies
     */
    void addNumArmiesAvailable(int i) {
        this.setNumArmiesAvailable(this.getNbArmiesAvailable() + i);
    }

    /**
     * Get the number of initial armies players should get depending on the
     * number of players in the game
     *
     * @param nbPlayers number of players in the game
     * @return number of initial armies
     */
    static int getNbInitialArmies(int nbPlayers) {
        switch (nbPlayers) {
            case 2:
                return 40;
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            case 6:
                return 20;
            default:
                throw new IllegalArgumentException("Invalid number of players");
        }
    }

    /**
     * Assign new armies to the player. Called at each reinforcement phase.
     *
     */
    void assignNewArmies() {
        int newArmies = this.armiesAssignation();
        this.setNumArmiesAvailable(newArmies);
        addNewLogEvent(String.format(
                "%s receives %d new armies",
                getName(),
                newArmies
        ));
    }

    /**
     * Getter of the cardsOwned attribute
     *
     * @return the cardsOwned
     */
    public HandModel getHand() {
        return hand;
    }

    /**
     * Setter of the cardsOwned attribute
     *
     * @param hand the cardsOwned to set
     */
    void setHand(HandModel hand) {
        this.hand = hand;

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the returnedCards attribute
     *
     * @return the returnedCards
     */
    public int getReturnedCards() {
        return returnedCards;
    }

    /**
     * Setter of the returnedCards attribute
     *
     * @param returnedCards the returnedCards to set
     */
    void setReturnedCards(int returnedCards) {
        this.returnedCards = returnedCards;

        setChanged();
        notifyObservers();
    }

    /**
     * Return the total number of armies owned by this player
     *
     * @return Total number of armies owned by this player
     */
    public int getNbArmiesOwned() {
        int numArmiesDeployed = this.territoryOwned.stream()
                .mapToInt((territory) -> territory.getNumArmies()).sum();

        return numArmiesDeployed + this.getNbArmiesAvailable();
    }

    /**
     * Assign the armies for reinforcement phase
     *
     * @return number of armies to deploy
     */
    int armiesAssignation() {
        int extraTerritories = (int) floor(this.getNbTerritoriesOwned() / 3);
        int extraContinent = this.continentsOwned.stream()
                .mapToInt((c) -> c.getBonusScore()).sum();

        return max(3, extraContinent + extraTerritories);
    }

    /**
     * Calls the function to add the armies of the handed cards
     */
    void armiesCardAssignation() {
        int numberArmiesCard = this.armiesAssignationCards();
        this.addNumArmiesAvailable(numberArmiesCard);
    }

    /**
     * Assign extra armies depending on handed cards
     *
     * @return number of extra armies according to handed cards
     */
    int armiesAssignationCards() {
        this.setReturnedCards(this.getReturnedCards() + 3);

        switch (this.getReturnedCards()) {
            case 3:
                return 4;
            case 6:
                return 6;
            case 9:
                return 8;
            case 12:
                return 10;
            case 15:
                return 12;
            case 18:
                return 15;
            default:
                return 15 + (((this.getReturnedCards() - 18) / 3) * 5); //after 18 you get 5 more for every 3 cards returned
        }

    }

    /**
     * Function that removes the cards and calls a function that assigns armies
     * depending on the number of cards the player has handed
     *
     * @return true if the cards are equal or different; false in other case
     */
    /**
     * Adds a card to the player's hand from the deck
     */
    void addCardToPlayerHand() {
        HandModel handCurrentPlayer = this.getHand();
        try {
            CardModel card = this.game.getDeck().getLast();
            handCurrentPlayer.getCardsList().add(card);
            this.game.getDeck().removeLast();

            addNewLogEvent(String.format(
                    "%s receives a new card",
                    getName()
            ));
        } catch (NoSuchElementException ex) {
            System.out.println("No card left in deck");
        }
    }

    /**
     * Getter of the currentFortificationMove attribute
     *
     * @return the current movement
     */
    public FortificationMove getCurrentFortificationMove() {
        return currentFortificationMove;
    }

    /**
     * Setter of the currentFortificationMove attribute
     *
     * @param src source territory
     * @param dest destiny of the fortification move
     */
    void setCurrentFortificationMove(TerritoryModel src, TerritoryModel dest) {
        this.currentFortificationMove = new FortificationMove(src, dest);

        setChanged();
        notifyObservers();
    }

    /**
     * Getter of the handed attribute
     *
     * @return the handed
     */
    public boolean isHanded() {
        return hand.isHanded();
    }

    /**
     * Setter of handed attribute
     *
     * @param handed the handed to set
     */
    public void setHanded(boolean handed) {
        this.hand.setHanded(handed);

        setChanged();
        notifyObservers();
    }

    /**
     * @return the currentAttack
     */
    public AttackMove getCurrentAttack() {
        return currentAttack;
    }

    /**
     * @param currentAttack the currentAttack to set
     */
    public void setCurrentAttack(AttackMove currentAttack) {
        this.currentAttack = currentAttack;

        setChanged();
        notifyObservers();
    }

    /**
     * Set the dice number in attack movement
     *
     * @param diceAttack number of dice for attack
     */
    public void setAttackValues(int diceAttack) {

        String diceStr = Integer.toString(diceAttack);
        if (diceAttack == -1) {
            diceStr = "Battle all";
        }

        addNewLogEvent(String.format(
                "%s selects %s dice to attack",
                this.getName(),
                diceStr
        ));
        this.getCurrentAttack().setNbDiceAttack(diceAttack);
    }

    /**
     * Set the dice number in defense movement
     *
     * @param diceAttacked number of dice for defense
     */
    public void setDefenseValues(int diceAttacked) {

        String diceStr = Integer.toString(diceAttacked);
        if (this.getCurrentAttack().getNbDiceAttack() == -1) {
            diceStr = "Battle all";
        }

        addNewLogEvent(String.format(
                "%s selects %s dice as defense",
                this.getCurrentAttack().getDefensePlayer().getName(),
                diceStr
        ));
        this.getCurrentAttack().setNbDiceDefense(diceAttacked);
    }

    /**
     * Perform the current attack of this player with the given number of dice
     * The value -1 correspond to the special mode in which battles are made
     * until one of the territory has no more armies
     *
     * @param diceAttack number of dices selected by the attacker
     * @param diceAttacked number of dices selected by the attacked player
     */
    public void performCurrentAttack(int diceAttack, int diceAttacked) {
        if (this.getCurrentAttack() == null) {
            return;
        }

        this.getCurrentAttack().perform(diceAttack, diceAttacked);

        /*
        when the battle is finished if there is still armies on the attacked
        territory, then this attack is terminated. If not the the attacked
        territory needs to be conquered before the attack terminates
         */
        if (this.currentAttack.getDest().getNumArmies() != 0) {
            this.setCurrentAttack(null);
        } else {
            addNewLogEvent(String.format(
                    "%s conquered the territory %s",
                    getName(),
                    this.currentAttack.getDest().getName()
            ));
        }
    }

    /**
     * Conquer a territory after an attack
     *
     * @param armies number of armies to move to the new conquered territory
     * @return -1 error; 0 success
     */
    public int conquerTerritory(int armies) {
        if (armies < this.getCurrentAttack().getNbDiceAttack() || armies >= this.getCurrentAttack().getSource().getNumArmies()) {
            return -1;
        }

        int newArmies = this.getCurrentAttack().getSource().getNumArmies();
        this.getCurrentAttack().getSource().setNumArmies(newArmies - armies);
        this.getCurrentAttack().getDest().setNumArmies(armies);

        addTerritoryOwned(this.getCurrentAttack().getDest());
        setConquered(true);
        this.setCurrentAttack(null);

        if (game != null) {
            game.checkForDeadPlayers();
        }
        return 0;
    }

    /**
     * It verifies that the current attack is valid
     *
     * @param sourceTerritory source territory of attack
     * @param destTerritory territory attacked
     * @return -1 error;0 success
     */
    public int validateAttack(TerritoryModel sourceTerritory, TerritoryModel destTerritory) {
        if (!sourceTerritory.getAdj().contains(destTerritory)) {
            return -1;
        }
        if (this.getCurrentAttack() != null) {
            return -2;
        }
        if (!this.getTerritoryOwned().contains(sourceTerritory)
                || this.getTerritoryOwned().contains(destTerritory)) {
            return -3;
        }
        if (sourceTerritory.getNumArmies() < 2) {
            return -4;
        }

        return 0;
    }

    /**
     * Set fortification move to null
     */
    void resetCurrentFortificationMove() {
        this.currentFortificationMove = null;

        setChanged();
        notifyObservers();
    }

    /**
     * Verifies that a continent is not owned
     *
     * @param continent continent to be verified
     * @return true id it is owned; false in other case
     */
    boolean checkOwnContinent(ContinentModel continent) {
        return this.continentsOwned.contains(continent);
    }

    /**
     * Calculates the % of territories owned
     *
     * @return % of territories owned
     */
    public int getPercentMapControlled() {
        int nbTerrInMap = this.getGame().getMap().getTerritories().size();
        return (100 * this.getNbTerritoriesOwned()) / nbTerrInMap;
    }

    /**
     * Addition of an event in the log
     *
     * @param logMessage message to add to the log
     */
    void addNewLogEvent(String logMessage) {
        addNewLogEvent(logMessage, false);
    }

    /**
     * Setter of the current attack
     *
     * @param src source territory
     * @param dest territory attacked
     */
    void startAttackMove(TerritoryModel src, TerritoryModel dest) {
        AttackMove attack = new AttackMove(this, src, dest);
        this.setCurrentAttack(attack);
    }

    /**
     * Update the list of continents owned by this players from the list of
     * territories owned by this player
     */
    private void updateContinentsOwned() {
        /* If this player is not associated with a game we cannot update
        owned continents
         */
        if (this.game == null) {
            return;
        }

        List<ContinentModel> newContinentsOwned;
        newContinentsOwned = this.game.getMap().getContinents().stream()
                .filter((c) -> c.getMembers().stream()
                .allMatch((t) -> this.territoryOwned.contains(t)))
                .collect(Collectors.toList());
        this.setContinentsOwned(newContinentsOwned);
    }

    /**
     * @return the conquered
     */
    public boolean isConquered() {
        return conquered;
    }

    /**
     * @param conquered the conquered to set
     */
    public void setConquered(boolean conquered) {
        this.conquered = conquered;
    }

    /**
     * Notify observer of a new event that can be displayed in the logs
     *
     * @param logMessage Message describing this event
     * @param clear true if this event should clear previous log messages
     */
    protected void addNewLogEvent(String logMessage, boolean clear) {
        this.getGame().addNewLogEvent(logMessage, clear);
    }

    /**
     * Add the cards from the killed player to this player's cards
     *
     * @param killedPlayer Player who has been eliminated
     */
    void stealCardsFrom(PlayerModel killedPlayer) {
        killedPlayer.getHand().getCards().forEach((c) -> {
            this.getHand().addCardToPlayerHand(c);
        });
    }

    /**
     * Exchange selected cards
     *
     * @return true success; false error
     */
    public boolean exchangeCardsToArmiesHuman() {
        List<String> selectedCards = this.getHand().getSelectedCards();
        LinkedList<String> typeOfArmie = new LinkedList<>();
        this.getHand().getCards().stream()
                .filter(c -> selectedCards.contains(c.getTerritoryName()))
                .forEach(cs -> typeOfArmie.add(cs.getTypeOfArmie()));

        boolean areEqual = typeOfArmie.stream()
                .allMatch(a -> a.equals(typeOfArmie.getFirst()));
        boolean different = !(typeOfArmie.get(0).equals(typeOfArmie.get(1))) && !(typeOfArmie.get(0).equals(typeOfArmie.get(2))) && !(typeOfArmie.get(2).equals(typeOfArmie.get(1)));

        if (areEqual || different) {
            this.setHanded(true);
            this.getHand().removeCards(selectedCards, this.game.getDeck());
            armiesCardAssignation();
            this.setChanged();
            this.notifyObservers(this.game);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Move armies for conquering a territory
     */
    public void moveArmiesAI() {
        if (this.getCurrentAttack() != null) {
            if (this.getCurrentAttack().getDest().getNumArmies() == 0) {
                int diceAttack = this.getCurrentAttack().getNbDiceAttack();
                this.game.moveArmiesToConqueredTerritory(max(diceAttack, this.currentAttack.getSource().getNumArmies() - 1));
            }
        }
    }

    /**
     * Defense movement for computer players
     */
    public void defenseAI() {
        this.game.getCurrentPlayer().setDefenseValues(2);
        this.game.performAttack(this.game.getCurrentPlayer());
        this.game.getCurrentPlayer().moveArmies();
        this.game.setAttackPhase(true);
        this.game.executeAttack();
    }

    /**
     * Card exchange for computer players
     *
     * @return true success
     */
    public boolean exchangeCardsToArmiesAI() {
        int[] cardDuplicates = this.getHand().getCardDuplicates();

        if (cardDuplicates[0] >= 3) {
            this.getHand().removeCards("infantry", this.game.getDeck());
        } else if (cardDuplicates[1] >= 3) {
            this.getHand().removeCards("cavalry", this.game.getDeck());
        } else if (cardDuplicates[2] >= 3) {
            this.getHand().removeCards("artillery", this.game.getDeck());
        } else {
            this.getHand().removeCards("different", this.game.getDeck());
        }
        armiesCardAssignation();
        return true;
    }
}
