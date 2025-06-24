package com.sricharan.expensetracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ExpenseTableViewer extends JFrame {

    public ExpenseTableViewer() {
        setTitle("📄 Expense Records");
        setSize(650, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Amount", "Category", "Date"};
        List<String[]> dataList = new ArrayList<>();
        File expenseFile = new File("expenses.txt");

        try (Scanner scanner = new Scanner(expenseFile)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 3) {
                    dataList.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Unable to read expenses.");
            return;
        }

        if (dataList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "📁 No expense data found.");
            return;
        }

        String[][] data = dataList.toArray(new String[0][]);
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);

        // 🔍 Search bar
        JTextField searchField = new JTextField();
        JButton clearSearch = new JButton("Clear");

        searchField.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) return;

            List<String[]> filtered = new ArrayList<>();
            for (String[] row : dataList) {
                if (row[1].toLowerCase().contains(query) || row[2].contains(query)) {
                    filtered.add(row);
                }
            }

            if (filtered.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No results found.");
            } else {
                String[][] newData = filtered.toArray(new String[0][]);
                table.setModel(new DefaultTableModel(newData, columnNames));
            }
        });

        clearSearch.addActionListener(e -> {
            table.setModel(new DefaultTableModel(data, columnNames));
            searchField.setText("");
        });

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("🔍 Search:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearSearch, BorderLayout.EAST);

        // 🗑️ Delete Button
        JButton deleteBtn = new JButton("🗑️ Delete Selected");
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                dataList.remove(selectedRow);
                try (FileWriter writer = new FileWriter(expenseFile)) {
                    for (String[] row : dataList) {
                        writer.write(String.join(",", row) + "\n");
                    }
                    JOptionPane.showMessageDialog(this, "✅ Expense deleted.");
                    dispose();
                    new ExpenseTableViewer(); // reopen updated
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error deleting entry.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please select a row.");
            }
        });

        // ✏️ Edit Button
        JButton editBtn = new JButton("✏️ Edit Selected");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String[] current = dataList.get(row);
                JTextField amountField = new JTextField(current[0]);
                JTextField categoryField = new JTextField(current[1]);
                JTextField dateField = new JTextField(current[2]);

                JPanel editPanel = new JPanel(new GridLayout(3, 2, 5, 5));
                editPanel.add(new JLabel("Amount:"));
                editPanel.add(amountField);
                editPanel.add(new JLabel("Category:"));
                editPanel.add(categoryField);
                editPanel.add(new JLabel("Date (YYYY-MM-DD):"));
                editPanel.add(dateField);

                int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    dataList.set(row, new String[]{
                            amountField.getText().trim(),
                            categoryField.getText().trim(),
                            dateField.getText().trim()
                    });

                    try (FileWriter writer = new FileWriter(expenseFile)) {
                        for (String[] r : dataList) {
                            writer.write(String.join(",", r) + "\n");
                        }
                        JOptionPane.showMessageDialog(this, "✅ Expense updated.");
                        dispose();
                        new ExpenseTableViewer();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "❌ Error saving update.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please select a row to edit.");
            }
        });

        // Buttons Panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(deleteBtn);
        btnPanel.add(editBtn);

        // Assemble Window
        setLayout(new BorderLayout(10, 10));
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
