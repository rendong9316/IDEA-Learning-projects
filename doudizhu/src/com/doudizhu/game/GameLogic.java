package com.doudizhu.game;

import com.doudizhu.card.Card;

import java.util.*;

/**
 * 游戏逻辑类 - 处理出牌规则和判断
 */
public class GameLogic {

    // 牌型常量
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_PAIR = 2;
    public static final int TYPE_TRIPLE = 3;
    public static final int TYPE_STRAIGHT = 4;
    public static final int TYPE_STRAIGHT_PAIR = 5;
    public static final int TYPE_TRIPLE_ONE = 6;
    public static final int TYPE_BOMB = 7;
    public static final int TYPE_KING_BOMB = 8;
    public static final int TYPE_INVALID = 0;

    /**
     * 获取牌型名称
     */
    public String getCardTypeName(List<Card> cards) {
        int type = getCardType(cards);
        switch (type) {
            case TYPE_SINGLE: return "单张";
            case TYPE_PAIR: return "对子";
            case TYPE_TRIPLE: return "三张";
            case TYPE_STRAIGHT: return "顺子";
            case TYPE_STRAIGHT_PAIR: return "连对";
            case TYPE_TRIPLE_ONE: return "三带一";
            case TYPE_BOMB: return "炸弹";
            case TYPE_KING_BOMB: return "王炸";
            default: return "无效";
        }
    }

    /**
     * 判断牌型
     */
    public int getCardType(List<Card> cards) {
        if (cards == null || cards.isEmpty()) return TYPE_INVALID;
        if (cards.size() == 1) return TYPE_SINGLE;

        int size = cards.size();

        // 王炸
        if (size == 2) {
            Card c1 = cards.get(0);
            Card c2 = cards.get(1);
            if ((c1.getRank() == Card.BIG_JOKER && c2.getRank() == Card.SMALL_JOKER) ||
                (c1.getRank() == Card.SMALL_JOKER && c2.getRank() == Card.BIG_JOKER)) {
                return TYPE_KING_BOMB;
            }
        }

        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        // 炸弹
        if (size == 4) {
            for (int count : rankCount.values()) {
                if (count == 4) return TYPE_BOMB;
            }
        }

        List<Integer> counts = new ArrayList<>(rankCount.values());
        Collections.sort(counts);

        if (counts.size() == 1 && size == 2) return TYPE_PAIR;
        if (counts.size() == 1 && size == 3) return TYPE_TRIPLE;

        // 三带一
        if (size == 4) {
            if (counts.get(0) == 1 && counts.get(1) == 3) return TYPE_TRIPLE_ONE;
        }

        // 顺子
        if (size >= 5 && counts.size() == size) {
            List<Integer> ranks = new ArrayList<>(rankCount.keySet());
            Collections.sort(ranks);
            for (int rank : ranks) {
                if (rank >= Card.TWO) return TYPE_INVALID;
            }
            for (int i = 0; i < ranks.size() - 1; i++) {
                if (ranks.get(i + 1) - ranks.get(i) != 1) return TYPE_INVALID;
            }
            return TYPE_STRAIGHT;
        }

        // 连对
        if (size >= 6 && counts.size() == size / 2) {
            boolean allPairs = true;
            for (int count : counts) {
                if (count != 2) {
                    allPairs = false;
                    break;
                }
            }
            if (allPairs) {
                List<Integer> ranks = new ArrayList<>(rankCount.keySet());
                Collections.sort(ranks);
                for (int rank : ranks) {
                    if (rank >= Card.TWO) return TYPE_INVALID;
                }
                for (int i = 0; i < ranks.size() - 1; i++) {
                    if (ranks.get(i + 1) - ranks.get(i) != 1) return TYPE_INVALID;
                }
                return TYPE_STRAIGHT_PAIR;
            }
        }

        return TYPE_INVALID;
    }

    /**
     * 比较两组牌的大小
     */
    public int compare(List<Card> a, List<Card> b) {
        if (a == null || a.isEmpty()) return -1;
        if (b == null || b.isEmpty()) return 1;

        int typeA = getCardType(a);
        int typeB = getCardType(b);

        // 王炸最大
        if (typeA == TYPE_KING_BOMB) return 1;
        if (typeB == TYPE_KING_BOMB) return -1;

        // 炸弹
        if (typeA == TYPE_BOMB && typeB != TYPE_BOMB && typeB != TYPE_KING_BOMB) return 1;
        if (typeB == TYPE_BOMB && typeA != TYPE_BOMB && typeA != TYPE_KING_BOMB) return -1;
        if (typeA == TYPE_BOMB && typeB == TYPE_BOMB) {
            return getMainRank(a) - getMainRank(b);
        }

        // 不同类型不能比较
        if (typeA != typeB) return 0;

        return getMainRank(a) - getMainRank(b);
    }

    /**
     * 获取主要牌点的等级
     */
    private int getMainRank(List<Card> cards) {
        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        int maxCount = 0;
        int targetRank = cards.get(0).getRank();

        for (Map.Entry<Integer, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                targetRank = entry.getKey();
            } else if (entry.getValue() == maxCount) {
                if (entry.getKey() > targetRank) {
                    targetRank = entry.getKey();
                }
            }
        }

        if (targetRank == Card.SMALL_JOKER) return Card.SMALL_JOKER;
        if (targetRank == Card.BIG_JOKER) return Card.BIG_JOKER;

        return targetRank;
    }

    /**
     * 找出能管上上家的牌
     */
    public List<Card> findValidCards(List<Card> handCards, List<Card> lastPlay) {
        if (lastPlay == null || lastPlay.isEmpty()) {
            return findSmallestSingle(handCards);
        }

        int lastType = getCardType(lastPlay);
        List<List<Card>> allCombos = new ArrayList<>();

        generateAllCombinations(handCards, lastType, lastPlay.size(), allCombos);

        List<Card> best = null;
        for (List<Card> combo : allCombos) {
            if (compare(combo, lastPlay) > 0) {
                if (best == null || compare(combo, best) < 0) {
                    best = combo;
                }
            }
        }

        if (best == null) {
            return findBombOrKingBomb(handCards);
        }

        return best;
    }

    /**
     * 找出最小的单张
     */
    private List<Card> findSmallestSingle(List<Card> handCards) {
        if (handCards.isEmpty()) return new ArrayList<>();

        Card smallest = handCards.get(handCards.size() - 1);
        List<Card> result = new ArrayList<>();
        result.add(smallest);
        return result;
    }

    /**
     * 查找炸弹或王炸
     */
    private List<Card> findBombOrKingBomb(List<Card> handCards) {
        // 王炸
        boolean hasBigJoker = false;
        boolean hasSmallJoker = false;
        for (Card card : handCards) {
            if (card.getRank() == Card.BIG_JOKER) hasBigJoker = true;
            if (card.getRank() == Card.SMALL_JOKER) hasSmallJoker = true;
        }
        if (hasBigJoker && hasSmallJoker) {
            List<Card> result = new ArrayList<>();
            for (Card card : handCards) {
                if (card.getRank() == Card.BIG_JOKER) {
                    result.add(card);
                    break;
                }
            }
            for (Card card : handCards) {
                if (card.getRank() == Card.SMALL_JOKER) {
                    result.add(card);
                    break;
                }
            }
            return result;
        }

        // 普通炸弹
        Map<Integer, List<Card>> rankCards = new HashMap<>();
        for (Card card : handCards) {
            rankCards.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }

        int bombRank = -1;
        for (Map.Entry<Integer, List<Card>> entry : rankCards.entrySet()) {
            if (entry.getValue().size() >= 4 && entry.getKey() != Card.BIG_JOKER && entry.getKey() != Card.SMALL_JOKER) {
                if (bombRank == -1 || entry.getKey() > bombRank) {
                    bombRank = entry.getKey();
                }
            }
        }

        if (bombRank != -1) {
            List<Card> result = new ArrayList<>();
            for (Card card : handCards) {
                if (card.getRank() == bombRank) {
                    result.add(card);
                    if (result.size() == 4) break;
                }
            }
            return result;
        }

        return new ArrayList<>();
    }

    /**
     * 生成所有可能的出牌组合
     */
    private void generateAllCombinations(List<Card> handCards, int lastType, int size, List<List<Card>> result) {
        Map<Integer, List<Card>> rankCardsMap = new HashMap<>();
        for (Card card : handCards) {
            rankCardsMap.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }

        switch (lastType) {
            case TYPE_SINGLE:
                for (Card card : handCards) {
                    List<Card> combo = new ArrayList<>();
                    combo.add(card);
                    result.add(combo);
                }
                break;

            case TYPE_PAIR:
                for (List<Card> cards : rankCardsMap.values()) {
                    if (cards.size() >= 2) {
                        List<Card> combo = new ArrayList<>(cards.subList(0, 2));
                        result.add(combo);
                    }
                }
                break;

            case TYPE_TRIPLE:
                for (List<Card> cards : rankCardsMap.values()) {
                    if (cards.size() >= 3) {
                        List<Card> combo = new ArrayList<>(cards.subList(0, 3));
                        result.add(combo);
                    }
                }
                break;

            case TYPE_STRAIGHT:
                generateStraight(handCards, size, result);
                break;

            case TYPE_TRIPLE_ONE:
                generateTripleWithOne(handCards, result);
                break;

            case TYPE_BOMB:
                for (List<Card> cards : rankCardsMap.values()) {
                    if (cards.size() >= 4) {
                        List<Card> combo = new ArrayList<>(cards.subList(0, 4));
                        result.add(combo);
                    }
                }
                break;
        }
    }

    /**
     * 生成顺子
     */
    private void generateStraight(List<Card> handCards, int length, List<List<Card>> result) {
        Map<Integer, List<Card>> rankCards = new HashMap<>();
        for (Card card : handCards) {
            if (card.getRank() <= Card.ACE) {
                rankCards.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
            }
        }

        List<Integer> availableRanks = new ArrayList<>(rankCards.keySet());
        Collections.sort(availableRanks);

        for (int i = 0; i < availableRanks.size(); i++) {
            if (availableRanks.get(i) >= Card.TWO) continue;

            List<Integer> straight = new ArrayList<>();
            straight.add(availableRanks.get(i));

            for (int j = i + 1; j < availableRanks.size() && straight.size() < length; j++) {
                if (availableRanks.get(j) - straight.get(straight.size() - 1) == 1) {
                    straight.add(availableRanks.get(j));
                }
            }

            if (straight.size() >= length) {
                List<Card> combo = new ArrayList<>();
                for (int k = 0; k < length; k++) {
                    combo.add(rankCards.get(straight.get(k)).get(0));
                }
                result.add(combo);
            }
        }
    }

    /**
     * 生成三带一
     */
    private void generateTripleWithOne(List<Card> handCards, List<List<Card>> result) {
        Map<Integer, List<Card>> rankCards = new HashMap<>();
        for (Card card : handCards) {
            rankCards.computeIfAbsent(card.getRank(), k -> new ArrayList<>()).add(card);
        }

        for (Map.Entry<Integer, List<Card>> entry : rankCards.entrySet()) {
            if (entry.getValue().size() >= 3) {
                List<Card> triple = new ArrayList<>(entry.getValue().subList(0, 3));

                for (Map.Entry<Integer, List<Card>> other : rankCards.entrySet()) {
                    if (other.getKey() != entry.getKey()) {
                        for (Card card : other.getValue()) {
                            List<Card> combo = new ArrayList<>(triple);
                            combo.add(card);
                            result.add(combo);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查是否可以出牌
     */
    public boolean isValidPlay(List<Card> cards) {
        return getCardType(cards) != TYPE_INVALID;
    }
}