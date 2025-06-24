package com.sricharan.expensetracker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExpenseChartViewer extends JFrame {

    public ExpenseChartViewer() {
        setTitle("📊 Expense Chart");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> categoryTotals = new HashMap<>();

        try (Scanner scanner = new Scanner(new File("expenses.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 3) {
                    String category = parts[1].trim();
                    int amount = Integer.parseInt(parts[0].trim());
                    categoryTotals.put(category, categoryTotals.getOrDefault(category, 0) + amount);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading expense data.");
            return;
        }

        if (categoryTotals.isEmpty()) {
            JOptionPane.showMessageDialog(this, "📁 No data to display in chart.");
            return;
        }

        for (Map.Entry<String, Integer> entry : categoryTotals.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Spending by Category",
                dataset,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(580, 350));
        setContentPane(chartPanel);
        setVisible(true);
    }
}
