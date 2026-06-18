package com.doudizhu.player;

import com.doudizhu.card.Card;
import com.doudizhu.game.GameLogic;

import java.util.*;

/**
 * AI玩家类
 */
public class AIPlayer extends Player {

    public AIPlayer(String name, int position) {
        super(name, position);
    }

    /**
     * AI叫地主
     */
    public boolean wantLandlord(List<Card> threeCards, int currentBid, int myBid) {
        if (currentBid >= 3) {
            return false;
        }

        int score = evaluateHand();
        return score >= 3;
    }

    /**
     * 评估手牌质量
     */
    private int evaluateHand() {
        int score = 0;
        Map<Integer, Integer> rankCount = new HashMap<>();

        for (Card card : handCards) {
            int rank = card.getRank();
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);

            if (rank == Card.BIG_JOKER) score += 4;
            else if (rank == Card.SMALL_JOKER) score += 3;
            else if (rank == Card.TWO) score += 2;
            else if (rank == Card.ACE) score += 1;
        }

        for (int count : rankCount.values()) {
            if (count == 4) score += 3;
        }

        score += (17 - handCards.size()) / 2;

        return score;
    }

    /**
     * AI出牌策略
     */
    public List<Card> playCards(List<Card> lastPlay, GameLogic gameLogic) {
        if (handCards.isEmpty()) {
            return new ArrayList<>();
        }

        if (lastPlay == null || lastPlay.isEmpty()) {
            return selectFirstPlay();
        }

        // 找出能管上的牌
        List<Card> result = gameLogic.findValidCards(handCards, lastPlay);
        if (result != null && !result.isEmpty()) {
            return result;
        }

        // 管不上返回空
        return new ArrayList<>();
    }

    /**
     * 选择出牌策略（先手时）
     */
    private List<Card> selectFirstPlay() {
        if (handCards.isEmpty()) return new ArrayList<>();

        // 优先出手中的小单张
        for (Card card : handCards) {
            if (card.getRank() < Card.TWO && card.getRank() != Card.SMALL_JOKER && card.getRank() != Card.BIG_JOKER) {
                List<Card> single = new ArrayList<>();
                single.add(card);
                return single;
            }
        }

        // 没有小单张，出最小的
        Card smallest = handCards.get(handCards.size() - 1);
        List<Card> result = new ArrayList<>();
        result.add(smallest);
        return result;
    }
}