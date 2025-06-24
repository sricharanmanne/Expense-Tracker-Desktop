package com.sricharan.expensetracker;

import com.formdev.flatlaf.FlatLightLaf;
import com.sricharan.expensetracker.ExpenseTrackerUI;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // 🧼 Modern UI
        } catch (Exception e) {
            System.err.println("Failed to apply FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> new ExpenseTrackerUI());
    }
}
