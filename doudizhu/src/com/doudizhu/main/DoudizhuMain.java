package com.doudizhu.main;

import com.doudizhu.ui.MainWindow;

import javax.swing.*;

/**
 * 斗地主游戏主程序入口
 */
public class DoudizhuMain {

    public static void main(String[] args) {
        // 设置Swing外观为系统默认
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 在EDT线程上启动GUI
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}