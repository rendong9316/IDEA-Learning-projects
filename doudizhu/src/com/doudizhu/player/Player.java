package com.doudizhu.player;

import com.doudizhu.card.Card;
import com.doudizhu.game.GameLogic;

import java.util.*;

/**
 * 玩家类
 */
public class Player {
    protected String name;
    protected List<Card> handCards;
    protected boolean isLandlord;
    protected int position;

    public Player(String name, int position) {
        this.name = name;
        this.position = position;
        this.handCards = new ArrayList<>();
        this.isLandlord = false;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Card> cards) {
        this.handCards = new ArrayList<>(cards);
        sortCards();
    }

    public void addCards(List<Card> cards) {
        this.handCards.addAll(cards);
        sortCards();
    }

    public boolean isLandlord() {
        return isLandlord;
    }

    public void setLandlord(boolean landlord) {
        this.isLandlord = landlord;
    }

    public int getPosition() {
        return position;
    }

    public void sortCards() {
        Collections.sort(handCards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                int rankCompare = Integer.compare(c2.getRank(), c1.getRank());
                if (rankCompare != 0) return rankCompare;
                return Integer.compare(c2.getSuit(), c1.getSuit());
            }
        });
    }

    public void removeCards(List<Card> cardsToRemove) {
        for (Card card : cardsToRemove) {
            Iterator<Card> it = handCards.iterator();
            while (it.hasNext()) {
                Card c = it.next();
                if (c.getRank() == card.getRank() && c.getSuit() == card.getSuit()) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public int getCardCount() {
        return handCards.size();
    }

    public int getCardLevel(Card card) {
        return card.getRank();
    }

    /**
     * 是否想要叫地主
     */
    public boolean wantLandlord(List<Card> threeCards, int currentBid, int myBid) {
        return false;
    }

    /**
     * 出牌策略 - AI玩家会重写这个方法
     */
    public List<Card> playCards(List<Card> lastPlay, GameLogic gameLogic) {
        return new ArrayList<>();
    }
}