package com.doudizhu.card;

import java.util.*;

/**
 * 卡牌实体类
 */
public class Card {
    // 花色
    public static final int SPADE = 0;    // 黑桃
    public static final int HEART = 1;     // 红桃
    public static final int CLUB = 2;      // 梅花
    public static final int DIAMOND = 3;   // 方块
    public static final int JOKER = 4;     // 王

    // 点数
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;
    public static final int ACE = 14;
    public static final int TWO = 15;
    public static final int SMALL_JOKER = 16;  // 小王
    public static final int BIG_JOKER = 17;     // 大王

    private int suit;  // 花色
    private int rank;  // 点数

    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    /**
     * 获取牌的显示名称
     */
    public String getName() {
        String rankStr;
        switch (rank) {
            case THREE: rankStr = "3"; break;
            case FOUR: rankStr = "4"; break;
            case FIVE: rankStr = "5"; break;
            case SIX: rankStr = "6"; break;
            case SEVEN: rankStr = "7"; break;
            case EIGHT: rankStr = "8"; break;
            case NINE: rankStr = "9"; break;
            case TEN: rankStr = "T"; break;
            case JACK: rankStr = "J"; break;
            case QUEEN: rankStr = "Q"; break;
            case KING: rankStr = "K"; break;
            case ACE: rankStr = "A"; break;
            case TWO: rankStr = "2"; break;
            case SMALL_JOKER: rankStr = "小王"; break;
            case BIG_JOKER: rankStr = "大王"; break;
            default: rankStr = "?";
        }

        String suitStr;
        if (rank == SMALL_JOKER || rank == BIG_JOKER) {
            suitStr = "";
        } else {
            switch (suit) {
                case SPADE: suitStr = "♠"; break;
                case HEART: suitStr = "♥"; break;
                case CLUB: suitStr = "♣"; break;
                case DIAMOND: suitStr = "♦"; break;
                default: suitStr = "?";
            }
        }
        return rankStr + suitStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return getName();
    }
}