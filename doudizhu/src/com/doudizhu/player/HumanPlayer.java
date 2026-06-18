package com.doudizhu.player;

import com.doudizhu.card.Card;
import com.doudizhu.game.GameLogic;

import java.util.*;

/**
 * 人类玩家类
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, int position) {
        super(name, position);
    }

    /**
     * AI叫地主 - 人类玩家不需要
     */
    public boolean wantLandlord(List<Card> threeCards, int currentBid, int myBid) {
        return false;
    }

    /**
     * AI出牌策略 - 人类玩家由用户控制
     */
    public List<Card> playCards(List<Card> lastPlay, GameLogic gameLogic) {
        // 人类玩家由界面控制出牌，这里返回空
        return new ArrayList<>();
    }
}