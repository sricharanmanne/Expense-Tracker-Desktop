package com.sricharan.expensetracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseTrackerUI extends JFrame {

    private final JTextField amountField = new JTextField(15);
    private final JTextField categoryField = new JTextField(15);
    private final JTextField dateField = new JTextField(15);

    public ExpenseTrackerUI() {
        setTitle("💸 Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🔹 Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        // 🔹 Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // 🔹 Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton saveBtn = new JButton("💾 Save");
        JButton viewBtn = new JButton("📄 View");
        JButton chartBtn = new JButton("📊 Pie Chart");
        JButton exitBtn = new JButton("❌ Exit");

        buttonPanel.add(saveBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(chartBtn);
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Button Actions
        saveBtn.addActionListener(e -> saveExpense());
        viewBtn.addActionListener(e -> viewExpenses());
        chartBtn.addActionListener(e -> showPieChart());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void saveExpense() {
        String amount = amountField.getText().trim();
        String category = categoryField.getText().trim();
        String date = dateField.getText().trim();

        if (amount.isEmpty() || category.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields.");
            return;
        }

        try (FileWriter fw = new FileWriter("expenses.txt", true)) {
            fw.write(amount + "," + category + "," + date + "\n");
            JOptionPane.showMessageDialog(this, "✅ Expense saved!");
            amountField.setText("");
            categoryField.setText("");
            dateField.setText(LocalDate.now().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving expense.");
        }
    }

    private void viewExpenses() {
        // use the upgraded viewExpenses() with table, edit/delete/search already provided earlier
        new ExpenseTableViewer();
    }

    private void showPieChart() {
        // use the pie chart code from our previous upgrade
        new ExpenseChartViewer();
    }
}
