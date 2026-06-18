package com.doudizhu.ui;

import com.doudizhu.card.Card;
import com.doudizhu.game.GameController;
import com.doudizhu.player.Player;
import com.doudizhu.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏主面板 - 游戏的中央界面
 */
public class GamePanel extends JPanel {
    private GameController controller;
    private MainWindow mainWindow;

    // 玩家区域
    private JPanel leftPlayerArea;
    private JPanel rightPlayerArea;

    // 信息显示
    private JLabel infoLabel;
    private JLabel lastPlayLabel;
    private JLabel leftCountLabel;
    private JLabel rightCountLabel;
    private JLabel leftLandlordLabel;
    private JLabel rightLandlordLabel;

    // 按钮
    private JButton playButton;
    private JButton passButton;

    // 卡牌显示
    private CardPanel playerCardPanel;

    // 当前状态
    private List<Card> selectedCards = new ArrayList<>();

    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = mainWindow.getController();
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        initComponents();
    }

    private void initComponents() {
        // 顶部面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 左边玩家
        JPanel leftPlayer = createPlayerPanel(0);
        topPanel.add(leftPlayer, BorderLayout.WEST);

        // 中间玩家信息
        JPanel centerInfo = new JPanel();
        centerInfo.setOpaque(false);
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));

        infoLabel = new JLabel("等待出牌...", SwingConstants.CENTER);
        infoLabel.setForeground(UIConstants.TEXT_COLOR);
        infoLabel.setFont(UIConstants.TITLE_FONT);

        JLabel bottomLabel = new JLabel("底牌:", SwingConstants.CENTER);
        bottomLabel.setForeground(UIConstants.TEXT_COLOR);
        bottomLabel.setFont(UIConstants.NORMAL_FONT);

        CardPanel bottomCards = new CardPanel();
        bottomCards.setOpaque(false);
        bottomCards.setCards(controller.getBottomCards());

        centerInfo.add(Box.createVerticalStrut(20));
        centerInfo.add(infoLabel);
        centerInfo.add(Box.createVerticalStrut(20));
        centerInfo.add(bottomLabel);
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(bottomCards);

        topPanel.add(centerInfo, BorderLayout.CENTER);

        // 右边玩家
        JPanel rightPlayer = createPlayerPanel(2);
        topPanel.add(rightPlayer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 中间 - 上次出牌显示
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        lastPlayLabel = new JLabel("", SwingConstants.CENTER);
        lastPlayLabel.setForeground(UIConstants.HIGHLIGHT_COLOR);
        lastPlayLabel.setFont(UIConstants.TITLE_FONT);

        middlePanel.add(Box.createVerticalStrut(100));
        middlePanel.add(lastPlayLabel);

        add(middlePanel, BorderLayout.CENTER);

        // 底部 - 玩家手牌和按钮
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 玩家手牌区域
        playerCardPanel = new CardPanel();
        playerCardPanel.setPreferredSize(new Dimension(800, 120));
        playerCardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!controller.isAITurn()) {
                    toggleCardSelection(e.getX());
                }
            }
        });

        JPanel playerArea = new JPanel(new BorderLayout());
        playerArea.setOpaque(false);
        playerArea.add(playerCardPanel, BorderLayout.CENTER);

        // 按钮区域
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        playButton = new JButton("出牌");
        playButton.setFont(UIConstants.BUTTON_FONT);
        playButton.setBackground(UIConstants.BUTTON_COLOR);
        playButton.setEnabled(false);
        playButton.addActionListener(e -> onPlayButton());

        passButton = new JButton("不出");
        passButton.setFont(UIConstants.BUTTON_FONT);
        passButton.setBackground(UIConstants.BUTTON_COLOR);
        passButton.setEnabled(false);
        passButton.addActionListener(e -> onPassButton());

        buttonPanel.add(playButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(passButton);

        playerArea.add(buttonPanel, BorderLayout.SOUTH);
        bottomPanel.add(playerArea, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // 初始化时更新状态
        updateGameState();
    }

    private JPanel createPlayerPanel(int playerIndex) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(120, 100));

        Player player = controller.getPlayer(playerIndex);

        JLabel nameLabel = new JLabel(player.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(UIConstants.TEXT_COLOR);
        nameLabel.setFont(UIConstants.NORMAL_FONT);

        JLabel countLabel = new JLabel("手牌: " + controller.getPlayerCardCount(playerIndex), SwingConstants.CENTER);
        countLabel.setForeground(UIConstants.TEXT_COLOR);
        countLabel.setFont(UIConstants.NORMAL_FONT);

        JLabel landlordLabel = new JLabel(player.isLandlord() ? "[地主]" : "", SwingConstants.CENTER);
        landlordLabel.setForeground(Color.YELLOW);
        landlordLabel.setFont(UIConstants.NORMAL_FONT);

        panel.add(nameLabel);
        panel.add(countLabel);
        panel.add(landlordLabel);

        // 保存引用以便后续更新
        if (playerIndex == 0) {
            leftCountLabel = countLabel;
            leftLandlordLabel = landlordLabel;
        } else {
            rightCountLabel = countLabel;
            rightLandlordLabel = landlordLabel;
        }

        return panel;
    }

    private void toggleCardSelection(int x) {
        List<Card> cards = controller.getPlayer(1).getHandCards();
        if (cards == null || cards.isEmpty()) return;

        // 强制验证布局以获取正确的宽度
        playerCardPanel.revalidate();
        int panelWidth = playerCardPanel.getWidth();
        if (panelWidth <= 0) panelWidth = 800; // fallback

        int cardWidth = UIConstants.CARD_WIDTH;
        int totalWidth = cards.size() * (cardWidth + 5) - 5;
        int startX = (panelWidth - totalWidth) / 2;

        int index = (x - startX) / (cardWidth + 5);
        index = Math.min(Math.max(index, 0), cards.size() - 1);

        if (index >= 0 && index < cards.size()) {
            Card clickedCard = cards.get(index);
            if (selectedCards.contains(clickedCard)) {
                selectedCards.remove(clickedCard);
            } else {
                selectedCards.add(clickedCard);
            }
            // 只显示最后一个选中卡片的视觉反馈
            playerCardPanel.setSelectedIndex(selectedCards.isEmpty() ? -1 : cards.indexOf(selectedCards.get(selectedCards.size() - 1)));
            playerCardPanel.repaint();

            // 更新出牌按钮状态
            boolean isPlayerTurn = !controller.isAITurn();
            playButton.setEnabled(isPlayerTurn && !selectedCards.isEmpty());
        }
    }

    private void onPlayButton() {
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要出的牌!");
            return;
        }

        List<Card> result = controller.playerPlayCards(new ArrayList<>(selectedCards));
        if (result == null) {
            JOptionPane.showMessageDialog(this, "无效的出牌，无法管上!");
            return;
        }

        selectedCards.clear();
        updateGameState();
    }

    private void onPassButton() {
        controller.pass();
        updateGameState();
    }

    public void updateGameState() {
        // 强制验证布局
        revalidate();

        // 更新手牌显示
        Player player = controller.getPlayer(1);
        playerCardPanel.setCards(player.getHandCards());
        playerCardPanel.revalidate();
        playerCardPanel.repaint();

        // 更新AI手牌数量和地主标记
        Player leftPlayer = controller.getPlayer(0);
        Player rightPlayer = controller.getPlayer(2);

        if (leftCountLabel != null) {
            leftCountLabel.setText("手牌: " + controller.getPlayerCardCount(0));
        }
        if (leftLandlordLabel != null) {
            leftLandlordLabel.setText(leftPlayer.isLandlord() ? "[地主]" : "");
        }
        if (rightCountLabel != null) {
            rightCountLabel.setText("手牌: " + controller.getPlayerCardCount(2));
        }
        if (rightLandlordLabel != null) {
            rightLandlordLabel.setText(rightPlayer.isLandlord() ? "[地主]" : "");
        }

        // 更新出牌信息
        List<Card> lastPlay = controller.getLastPlay();
        if (lastPlay != null && !lastPlay.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Card c : lastPlay) {
                sb.append(c.getName()).append(" ");
            }
            lastPlayLabel.setText(sb.toString());
        } else {
            lastPlayLabel.setText("");
        }

        // 更新按钮状态
        boolean isPlayerTurn = !controller.isAITurn();
        playButton.setEnabled(isPlayerTurn && !selectedCards.isEmpty());
        passButton.setEnabled(isPlayerTurn && controller.shouldShowPass());

        // 更新信息
        int current = controller.getCurrentPlayerIndex();
        if (current >= 0 && current < 3) {
            String currentPlayerName = controller.getPlayer(current).getName();
            infoLabel.setText(currentPlayerName + " 出牌...");
        }

        // 检查游戏是否结束
        int winner = controller.getWinner();
        if (winner >= 0) {
            String result = controller.getPlayer(winner).isLandlord() ? "地主获胜!" : "农民获胜!";
            JOptionPane.showMessageDialog(this, result, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            mainWindow.showStartPanel();
        }
    }

    public void refreshAll() {
        updateGameState();
    }
}