package com.doudizhu.game;

import com.doudizhu.card.Card;
import com.doudizhu.card.Deck;
import com.doudizhu.player.AIPlayer;
import com.doudizhu.player.Player;

import java.util.*;

/**
 * 游戏控制类 - 管理整个游戏流程
 */
public class GameController {
    private Player[] players;  // 三个玩家
    private Deck deck;        // 牌堆
    private List<Card> bottomCards;  // 底牌
    private int currentPlayerIndex;  // 当前出牌玩家索引
    private List<Card> lastPlay;     // 上一次出的牌
    private int lastPlayerIndex;    // 上一次出牌的玩家索引
    private GameLogic gameLogic;    // 游戏逻辑

    // 叫分相关
    private int[] bids;  // 三个玩家的叫分
    private int landlordBid = 0;  // 当前最高叫分
    private int landlordPosition = -1;  // 地主位置

    public static final int LANDLORD_POSITION = -1;

    public GameController() {
        players = new Player[3];
        deck = new Deck();
        bottomCards = new ArrayList<>();
        gameLogic = new GameLogic();
        bids = new int[3];
    }

    /**
     * 初始化游戏
     */
    public void initGame() {
        // 创建玩家
        players[0] = new AIPlayer("农民甲", 0);
        players[1] = new Player("玩家", 1);
        players[2] = new AIPlayer("农民乙", 2);

        // 重置游戏状态
        Arrays.fill(bids, 0);
        landlordBid = 0;
        landlordPosition = -1;
        lastPlay = null;
        lastPlayerIndex = -1;
        currentPlayerIndex = 0;

        // 洗牌
        deck.shuffle();

        // 发牌
        for (Player player : players) {
            List<Card> hand = deck.deal(17);
            player.setHandCards(hand);
            player.setLandlord(false);
        }

        // 底牌
        bottomCards = deck.getBottomCards(3);
    }

    /**
     * 开始叫地主流程
     */
    public int[] startBidding() {
        // 从第一个玩家（玩家）开始叫地主
        currentPlayerIndex = 1;  // 玩家先叫

        // 返回三个玩家的叫分
        return bids;
    }

    /**
     * 玩家叫分
     */
    public boolean playerBid(int bidAmount) {
        if (bidAmount <= landlordBid && landlordBid > 0) {
            return false;  // 叫分必须高于当前最高
        }
        if (bidAmount > 3 || bidAmount < 1) {
            return false;
        }
        if (bidAmount == 0 && landlordBid == 0) {
            return false;  // 第一个必须叫分
        }

        bids[1] = bidAmount;
        landlordBid = bidAmount;
        landlordPosition = 1;

        return true;
    }

    /**
     * AI叫分
     */
    public void aiBid() {
        // AI叫分逻辑
        AIPlayer ai = (AIPlayer) players[currentPlayerIndex];
        int bidAmount = 0;

        if (landlordBid == 0) {
            // 还没人叫，看手牌决定
            if (ai.wantLandlord(null, 0, 0)) {
                bidAmount = 1;
            }
        } else if (landlordBid < 3) {
            // 可以抢
            if (ai.wantLandlord(null, landlordBid, bids[currentPlayerIndex])) {
                bidAmount = landlordBid + 1;
            }
        }

        if (bidAmount > 0) {
            bids[currentPlayerIndex] = bidAmount;
            landlordBid = bidAmount;
            landlordPosition = currentPlayerIndex;
        }
    }

    /**
     * 继续叫地主流程
     * 返回下一个需要叫分的玩家索引，-1表示结束
     */
    public int continueBidding() {
        // 叫地主最多3轮
        int totalBids = 0;
        for (int bid : bids) {
            if (bid > 0) totalBids++;
        }

        if (totalBids >= 3 && landlordBid > 0) {
            // 叫地主结束
            return -1;
        }

        if (landlordBid == 3) {
            // 叫到3分，结束
            return -1;
        }

        // 轮到下一个
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % 3;
        } while (currentPlayerIndex == landlordPosition && bids[currentPlayerIndex] == landlordBid);

        return currentPlayerIndex;
    }

    /**
     * 设置地主并发放底牌
     */
    public void setLandlord() {
        if (landlordPosition >= 0 && landlordPosition < 3) {
            players[landlordPosition].setLandlord(true);
            players[landlordPosition].addCards(bottomCards);
        }
    }

    /**
     * 获取地主位置
     */
    public int getLandlordPosition() {
        return landlordPosition;
    }

    /**
     * 获取底牌
     */
    public List<Card> getBottomCards() {
        return bottomCards;
    }

    /**
     * 开始出牌阶段
     */
    public void startPlaying() {
        // 地主先出
        currentPlayerIndex = landlordPosition;
        lastPlayerIndex = -1;
        lastPlay = null;
    }

    /**
     * 玩家出牌
     */
    public List<Card> playerPlayCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            // 不出
            return new ArrayList<>();
        }

        // 验证出牌是否合法
        if (!gameLogic.isValidPlay(cards)) {
            return null;
        }

        // 如果不是先手，需要和上家比较
        if (lastPlay != null && !lastPlay.isEmpty() && lastPlayerIndex != 1) {
            if (gameLogic.compare(cards, lastPlay) <= 0) {
                return null;  // 管不上
            }
        }

        // 验证玩家是否有这些牌
        if (!hasCards(players[1].getHandCards(), cards)) {
            return null;
        }

        // 出牌成功
        players[1].removeCards(cards);
        lastPlay = cards;
        lastPlayerIndex = 1;
        currentPlayerIndex = (currentPlayerIndex + 1) % 3;

        return cards;
    }

    /**
     * AI出牌
     */
    public List<Card> aiPlayCards() {
        Player ai = players[currentPlayerIndex];

        List<Card> play = ai.playCards(lastPlay, gameLogic);

        if (play != null && !play.isEmpty()) {
            ai.removeCards(play);
            lastPlay = play;
            lastPlayerIndex = currentPlayerIndex;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % 3;

        return play;
    }

    /**
     * 检查玩家是否有这些牌
     */
    private boolean hasCards(List<Card> hand, List<Card> cards) {
        List<Card> temp = new ArrayList<>(hand);
        for (Card c : cards) {
            boolean found = false;
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getRank() == c.getRank() && temp.get(i).getSuit() == c.getSuit()) {
                    temp.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    /**
     * 获取当前出牌玩家索引
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * 设置当前出牌玩家索引
     */
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    /**
     * 获取上次出的牌
     */
    public List<Card> getLastPlay() {
        return lastPlay;
    }

    /**
     * 获取玩家
     */
    public Player getPlayer(int index) {
        if (index >= 0 && index < 3) {
            return players[index];
        }
        return null;
    }

    /**
     * 获取玩家手牌数量
     */
    public int getPlayerCardCount(int index) {
        if (index >= 0 && index < 3) {
            return players[index].getCardCount();
        }
        return 0;
    }

    /**
     * 检查游戏是否结束
     */
    public int checkGameEnd() {
        for (int i = 0; i < 3; i++) {
            if (players[i].getCardCount() == 0) {
                return i;  // 返回获胜玩家索引
            }
        }
        return -1;
    }

    /**
     * 判断获胜方
     * 返回：0=农民获胜，1=地主获胜，-1=游戏未结束
     */
    public int getWinner() {
        int winner = checkGameEnd();
        if (winner < 0) return -1;

        if (players[winner].isLandlord()) {
            return 1;  // 地主获胜
        } else {
            return 0;  // 农民获胜
        }
    }

    /**
     * 跳过（不出）
     */
    public void pass() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 3;
    }

    /**
     * 获取下一个需要出牌的玩家索引（跳过不出的）
     */
    public void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 3;
    }

    /**
     * 是否需要显示不出按钮
     */
    public boolean shouldShowPass() {
        return lastPlayerIndex != currentPlayerIndex && lastPlayerIndex != -1;
    }

    /**
     * AI是否需要自动出牌
     */
    public boolean isAITurn() {
        return currentPlayerIndex != 1;
    }

    /**
     * 获取当前最高叫分
     */
    public int getLandlordBid() {
        return landlordBid;
    }

    /**
     * 获取当前地主位置
     */
    public int getLandlordPositionIndex() {
        return landlordPosition;
    }

    /**
     * 重置游戏
     */
    public void reset() {
        deck.reset();
        initGame();
    }
}