package com.doudizhu.ui;

import com.doudizhu.card.Card;
import com.doudizhu.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 卡牌面板 - 用于显示手牌
 */
public class CardPanel extends JPanel {
    private List<Card> cards;
    private int selectedIndex = -1;
    private boolean isSelectable = true;

    public CardPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        this.selectedIndex = -1;
        repaint();
    }

    public void setSelectable(boolean selectable) {
        this.isSelectable = selectable;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int index) {
        if (cards != null && index >= 0 && index < cards.size()) {
            this.selectedIndex = index;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (cards == null || cards.isEmpty()) {
            return;
        }

        int cardWidth = UIConstants.CARD_WIDTH;
        int cardHeight = UIConstants.CARD_HEIGHT;
        int totalWidth = cards.size() * (cardWidth + 5) - 5;
        int startX = (getWidth() - totalWidth) / 2;

        for (int i = 0; i < cards.size(); i++) {
            int x = startX + i * (cardWidth + 5);
            int y = (getHeight() - cardHeight) / 2;

            // 选中状态上移
            if (i == selectedIndex && isSelectable) {
                y -= 15;
            }

            drawCard(g, cards.get(i), x, y, i == selectedIndex && isSelectable);
        }
    }

    private void drawCard(Graphics g, Card card, int x, int y, boolean selected) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 卡片背景
        g2d.setColor(selected ? new Color(255, 255, 150) : Color.WHITE);
        g2d.fillRoundRect(x, y, UIConstants.CARD_WIDTH, UIConstants.CARD_HEIGHT, 8, 8);

        // 边框
        g2d.setColor(Color.GRAY);
        g2d.drawRoundRect(x, y, UIConstants.CARD_WIDTH, UIConstants.CARD_HEIGHT, 8, 8);

        // 卡面内容
        drawCardContent(g2d, card, x, y);
    }

    private void drawCardContent(Graphics2D g2d, Card card, int x, int y) {
        String name = card.getName();
        boolean isRed = card.getSuit() == Card.HEART || card.getSuit() == Card.DIAMOND ||
                        card.getRank() == Card.BIG_JOKER || card.getRank() == Card.SMALL_JOKER;

        Color textColor = isRed ? Color.RED : Color.BLACK;

        // 牌面点数 - 左上角
        g2d.setColor(textColor);
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
        g2d.drawString(name, x + 8, y + 25);

        // 牌面点数 - 右下角（倒着显示）
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
        String reversed = new StringBuilder(name).reverse().toString();
        g2d.drawString(reversed, x + UIConstants.CARD_WIDTH - 25, y + UIConstants.CARD_HEIGHT - 8);

        // 花色符号 - 中间
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        String suitSymbol = getSuitSymbol(card);
        int strWidth = g2d.getFontMetrics().stringWidth(suitSymbol);
        g2d.drawString(suitSymbol, x + (UIConstants.CARD_WIDTH - strWidth) / 2, y + 60);
    }

    private String getSuitSymbol(Card card) {
        if (card.getRank() == Card.BIG_JOKER) return "👑";
        if (card.getRank() == Card.SMALL_JOKER) return "🃏";

        switch (card.getSuit()) {
            case Card.SPADE: return "♠";
            case Card.HEART: return "♥";
            case Card.CLUB: return "♣";
            case Card.DIAMOND: return "♦";
            default: return "?";
        }
    }
}