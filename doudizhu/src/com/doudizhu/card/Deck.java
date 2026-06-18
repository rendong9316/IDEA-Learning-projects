package com.doudizhu.card;

import java.util.*;

/**
 * 牌堆管理类 - 负责洗牌和发牌
 */
public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        initDeck();
    }

    /**
     * 重新初始化牌堆（重新生成54张牌）
     */
    public void reset() {
        initDeck();
    }

    /**
     * 初始化一副牌
     */
    private void initDeck() {
        cards.clear();
        // 黑桃、红桃、梅花、方块
        for (int suit = Card.SPADE; suit <= Card.DIAMOND; suit++) {
            for (int rank = Card.THREE; rank <= Card.TWO; rank++) {
                cards.add(new Card(suit, rank));
            }
        }
        // 添加大小王
        cards.add(new Card(Card.JOKER, Card.SMALL_JOKER));
        cards.add(new Card(Card.JOKER, Card.BIG_JOKER));
    }

    /**
     * 洗牌
     */
    public void shuffle() {
        Collections.shuffle(cards, new Random());
    }

    /**
     * 发牌
     * @param count 发牌数量
     * @return 发的牌列表
     */
    public List<Card> deal(int count) {
        if (count > cards.size()) {
            throw new IllegalArgumentException("牌不够了: 需要 " + count + " 张，剩余 " + cards.size() + " 张");
        }
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            hand.add(cards.remove(0));
        }
        return hand;
    }

    /**
     * 获取剩余牌数
     */
    public int remaining() {
        return cards.size();
    }

    /**
     * 获取底牌（同时从牌堆中移除）
     */
    public List<Card> getBottomCards(int count) {
        List<Card> bottom = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            bottom.add(cards.remove(cards.size() - 1));
        }
        return bottom;
    }
}