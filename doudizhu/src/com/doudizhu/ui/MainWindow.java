package com.doudizhu.ui;

import com.doudizhu.card.Card;
import com.doudizhu.game.GameController;
import com.doudizhu.player.Player;
import com.doudizhu.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 主窗口类 - 管理整个游戏界面
 */
public class MainWindow extends JFrame {
    private GameController controller;
    private GamePanel gamePanel;
    private Timer aiTimer;

    // 叫地主相关
    private JPanel landlordPanel;
    private JButton[] bidButtons;
    private int currentHighestBid = 0;
    private int landlordPosition = -1;

    public MainWindow() {
        controller = new GameController();
        controller.initGame();

        setTitle("斗地主");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showStartPanel();
    }

    public GameController getController() {
        return controller;
    }

    /**
     * 显示开始面板
     */
    public void showStartPanel() {
        if (aiTimer != null && aiTimer.isRunning()) {
            aiTimer.stop();
        }

        getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 标题
        JLabel title = new JLabel("斗 地 主", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 48));
        title.setForeground(UIConstants.HIGHLIGHT_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));

        // 副标题
        JLabel subtitle = new JLabel("Landlord Fight", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitle.setForeground(UIConstants.TEXT_COLOR);

        // 开始按钮
        JButton startButton = new JButton("开始游戏");
        startButton.setFont(UIConstants.BUTTON_FONT);
        startButton.setBackground(UIConstants.BUTTON_COLOR);
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.addActionListener(e -> startGame());

        panel.add(title);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(50));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(200));

        add(panel);
        revalidate();
        repaint();
    }

    /**
     * 开始游戏 - 显示发牌和叫地主
     */
    private void startGame() {
        controller.reset();
        currentHighestBid = 0;
        landlordPosition = -1;
        controller.startBidding();

        getContentPane().removeAll();

        landlordPanel = new JPanel();
        landlordPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        landlordPanel.setLayout(new BorderLayout());

        // 顶部 - 显示三个玩家的发牌情况
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 农民甲
        JPanel leftPlayer = createPlayerInfoPanel("农民甲");
        // 中间 - 叫地主区域
        JPanel centerPanel = createBiddingCenter();
        // 农民乙
        JPanel rightPlayer = createPlayerInfoPanel("农民乙");

        topPanel.add(leftPlayer);
        topPanel.add(centerPanel);
        topPanel.add(rightPlayer);

        landlordPanel.add(topPanel, BorderLayout.NORTH);

        // 中间显示玩家手牌
        CardPanel playerCards = new CardPanel();
        playerCards.setOpaque(false);
        List<Card> hand = controller.getPlayer(1).getHandCards();
        System.out.println("Player hand size: " + (hand == null ? "null" : hand.size()));
        playerCards.setCards(hand);
        playerCards.setPreferredSize(new Dimension(850, 110));

        JPanel playerArea = new JPanel(new BorderLayout());
        playerArea.setOpaque(false);
        playerArea.add(playerCards, BorderLayout.CENTER);
        playerArea.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        landlordPanel.add(playerArea, BorderLayout.CENTER);

        add(landlordPanel);
        revalidate();
        repaint();

        // 初始化按钮状态
        updateBidButtons();
    }

    private JPanel createPlayerInfoPanel(String name) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(UIConstants.TEXT_COLOR);
        nameLabel.setFont(UIConstants.NORMAL_FONT);
        JLabel countLabel = new JLabel("手牌: 17", SwingConstants.CENTER);
        countLabel.setForeground(UIConstants.TEXT_COLOR);
        countLabel.setFont(UIConstants.NORMAL_FONT);
        panel.add(nameLabel);
        panel.add(countLabel);
        return panel;
    }

    private JPanel createBiddingCenter() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel tipLabel = new JLabel("叫地主阶段", SwingConstants.CENTER);
        tipLabel.setFont(UIConstants.TITLE_FONT);
        tipLabel.setForeground(UIConstants.HIGHLIGHT_COLOR);

        JLabel instruction = new JLabel("你当前是玩家，请叫地主", SwingConstants.CENTER);
        instruction.setFont(UIConstants.NORMAL_FONT);
        instruction.setForeground(UIConstants.TEXT_COLOR);

        // 叫分按钮
        JPanel bidPanel = new JPanel();
        bidPanel.setOpaque(false);

        bidButtons = new JButton[4];
        bidButtons[0] = new JButton("不叫");
        bidButtons[1] = new JButton("1分");
        bidButtons[2] = new JButton("2分");
        bidButtons[3] = new JButton("3分");

        for (int i = 0; i < 4; i++) {
            final int bid = i;
            bidButtons[i].setFont(UIConstants.BUTTON_FONT);
            bidButtons[i].setBackground(UIConstants.BUTTON_COLOR);
            bidButtons[i].setPreferredSize(new Dimension(80, 40));
            bidButtons[i].addActionListener(e -> onBid(bid));
            bidPanel.add(bidButtons[i]);
            if (i < 3) bidPanel.add(Box.createHorizontalStrut(10));
        }

        // 底牌显示
        JLabel bottomLabel = new JLabel("底牌:", SwingConstants.CENTER);
        bottomLabel.setFont(UIConstants.NORMAL_FONT);
        bottomLabel.setForeground(UIConstants.TEXT_COLOR);

        CardPanel bottomCards = new CardPanel();
        bottomCards.setOpaque(false);
        List<Card> bottom = controller.getBottomCards();
        bottomCards.setCards(bottom);
        bottomCards.setPreferredSize(new Dimension(220, 110));

        centerPanel.add(tipLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(instruction);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(bidPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(bottomLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(bottomCards);

        return centerPanel;
    }

    /**
     * 处理叫分
     */
    private void onBid(int bid) {
        // 第一个玩家在不叫状态时不能点不叫
        if (bid == 0 && currentHighestBid == 0) {
            return;
        }

        if (bid > currentHighestBid) {
            currentHighestBid = bid;
            landlordPosition = 1;

            // 同步到 GameController
            controller.playerBid(bid);

            // 更新按钮状态
            for (int i = 0; i <= bid; i++) {
                if (bidButtons[i] != null) {
                    bidButtons[i].setEnabled(false);
                }
            }

            // 如果叫到3分，直接确定地主
            if (bid == 3) {
                Timer timer = new Timer(500, e -> confirmLandlord(1));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            // AI轮流叫分
            Timer timer = new Timer(1000, e -> aiTakeTurn());
            timer.setRepeats(false);
            timer.start();
        } else if (bid == 0 && currentHighestBid > 0) {
            // 玩家选择不叫，AI继续
            Timer timer = new Timer(500, e -> aiTakeTurn());
            timer.setRepeats(false);
            timer.start();
        }
    }

    /**
     * AI叫地主回合
     */
    private void aiTakeTurn() {
        // 计算AI叫分
        int aiBid = calculateAIBid();
        int aiPosition = (currentHighestBid == 0) ? 0 : 2;

        if (aiBid > currentHighestBid && aiBid <= 3) {
            currentHighestBid = aiBid;
            landlordPosition = aiPosition;

            // 同步到 GameController (设置正确的 currentPlayerIndex)
            controller.setCurrentPlayerIndex(aiPosition);
            controller.aiBid();

            String bidText = (aiBid == 0) ? "不叫" : aiBid + "分";
            String aiName = controller.getPlayer(aiPosition).getName();
            JOptionPane.showMessageDialog(this,
                aiName + " 叫了 " + bidText,
                "叫地主", JOptionPane.INFORMATION_MESSAGE);

            if (aiBid == 3) {
                Timer timer = new Timer(500, e -> confirmLandlord(aiPosition));
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }

        // 如果没有人叫分，农民甲也必须叫
        if (currentHighestBid == 0) {
            // 农民甲必须叫1分
            currentHighestBid = 1;
            landlordPosition = 0;
            // 同步到 GameController
            controller.setCurrentPlayerIndex(0);
            controller.aiBid();
            JOptionPane.showMessageDialog(this,
                "农民甲 叫了 1分",
                "叫地主", JOptionPane.INFORMATION_MESSAGE);
            Timer timer = new Timer(500, e -> aiTakeTurn2());
            timer.setRepeats(false);
            timer.start();
            return;
        }

        // 继续玩家叫分
        updateBidButtons();
    }

    /**
     * AI第二个（农民乙）叫分
     */
    private void aiTakeTurn2() {
        int aiPosition = 2;
        int aiBid = calculateAIBid2();

        if (aiBid > currentHighestBid && aiBid <= 3) {
            currentHighestBid = aiBid;
            landlordPosition = aiPosition;

            // 同步到 GameController
            controller.setCurrentPlayerIndex(aiPosition);
            controller.aiBid();

            String bidText = (aiBid == 0) ? "不叫" : aiBid + "分";
            String aiName = controller.getPlayer(aiPosition).getName();
            JOptionPane.showMessageDialog(this,
                aiName + " 叫了 " + bidText,
                "叫地主", JOptionPane.INFORMATION_MESSAGE);

            if (aiBid == 3) {
                Timer timer = new Timer(500, e -> confirmLandlord(aiPosition));
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }

        // 继续玩家
        updateBidButtons();
    }

    /**
     * 计算AI叫分（农民甲）
     */
    private int calculateAIBid() {
        int bigCards = 0;
        List<Card> hand = controller.getPlayer(0).getHandCards();
        for (Card c : hand) {
            if (c.getRank() >= Card.TWO || c.getRank() == Card.BIG_JOKER || c.getRank() == Card.SMALL_JOKER) {
                bigCards++;
            }
        }
        if (bigCards >= 6) return 2;
        if (bigCards >= 3) return 1;
        return 0;
    }

    /**
     * 计算AI叫分（农民乙）
     */
    private int calculateAIBid2() {
        int bigCards = 0;
        List<Card> hand = controller.getPlayer(2).getHandCards();
        for (Card c : hand) {
            if (c.getRank() >= Card.TWO || c.getRank() == Card.BIG_JOKER || c.getRank() == Card.SMALL_JOKER) {
                bigCards++;
            }
        }
        if (bigCards >= 6) return 2;
        if (bigCards >= 3) return 1;
        return 0;
    }

    /**
     * 更新按钮状态
     */
    private void updateBidButtons() {
        for (int i = 0; i < 4; i++) {
            if (bidButtons[i] != null) {
                // 第一个玩家在不叫状态时不能点不叫
                if (i == 0 && currentHighestBid == 0) {
                    bidButtons[i].setEnabled(false);
                } else {
                    bidButtons[i].setEnabled(currentHighestBid < i);
                }
            }
        }
    }

    /**
     * 确认地主
     */
    private void confirmLandlord(int position) {
        controller.setLandlord();
        showPlayingPhase();
    }

    /**
     * 显示出牌阶段界面
     */
    private void showPlayingPhase() {
        if (aiTimer != null && aiTimer.isRunning()) {
            aiTimer.stop();
        }

        getContentPane().removeAll();

        gamePanel = new GamePanel(this);
        add(gamePanel);

        // 强制完整布局
        revalidate();
        repaint();

        // 启动AI出牌定时器
        aiTimer = new Timer(1200, e -> onAITurn());
        aiTimer.start();
    }

    /**
     * AI出牌回合
     */
    private void onAITurn() {
        if (gamePanel == null) return;

        if (controller.isAITurn()) {
            controller.aiPlayCards();
            gamePanel.refreshAll();
        }

        // 检查游戏结束
        int winner = controller.getWinner();
        if (winner >= 0) {
            aiTimer.stop();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}