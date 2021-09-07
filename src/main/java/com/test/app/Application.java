package com.test.app;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class Application extends JPanel {
    private CacheModel cache;
    private DBModel db;
    private JTree cacheTree, dbTree;

    private Application() {
        setLayout(null);

        cacheTree = new JTree(cache = new CacheModel());
        cacheTree.setBounds(10, 10, 200, 500);
        cacheTree.setRootVisible(false);
        add(cacheTree);

        dbTree = new JTree(db = new DBModel());
        dbTree.setBounds(300, 10, 200, 500);
        expandAll(dbTree);
        add(dbTree);
        
        JButton b = new JButton("<<");
        b.addActionListener(e -> doWithSelectedNode(dbTree, node -> {
            cache.push(new Node(node));
            expandAll(cacheTree);
        }));
        b.setBounds(230, 200, 50, 25);
        add(b);

        b = new JButton("+");
        b.setToolTipText("Add node");
        b.addActionListener(e -> doWithSelectedNode(cacheTree, parent -> {
            String value = JOptionPane.showInputDialog(this, "Enter node's value", "Add Node", JOptionPane.INFORMATION_MESSAGE);
            if (value != null) {
                cache.createNode(parent, value);
                expandAll(cacheTree);
            }
        }));
        b.setBounds(10, 520, 50, 25);
        add(b);

        b = new JButton("-");
        b.setToolTipText("Delete node");
        b.addActionListener(e -> doWithSelectedNode(cacheTree, node -> {
            cache.deleteNode(node);
            expandAll(cacheTree);
        }));
        b.setBounds(80, 520, 50, 25);
        add(b);

        b = new JButton("a");
        b.setToolTipText("Rename node");
        b.addActionListener(e -> doWithSelectedNode(cacheTree, node -> {
            String newValue = JOptionPane.showInputDialog(this, "Enter new value", node.getValue());
            if (newValue != null) {
                cache.setNodeValue(node, newValue);
                expandAll(cacheTree);
            }
        }));
        b.setBounds(150, 520, 50, 25);
        add(b);

        b = new JButton("Apply");
        b.setToolTipText("Apply changes");
        b.addActionListener(e -> {
            db.update(cache.makeCopy());
            expandAll(dbTree);
        });
        b.setBounds(300, 520, 80, 25);
        add(b);

        b = new JButton("Reset");
        b.setToolTipText("Reset to init state");
        b.addActionListener(e -> {
            cache.reset();
            db.reset();
            expandAll(dbTree);
        });
        b.setBounds(400, 520, 80, 25);
        add(b);
    }

    private void doWithSelectedNode(JTree tree, Consumer<Node> consumer) {
        Node node = (Node)tree.getLastSelectedPathComponent();
        if (node == null) {
            JOptionPane.showMessageDialog(this, "Select a node");
        } else {
            if (node.isDeleted()) {
                JOptionPane.showMessageDialog(this, "Selected node was deleted");
            } else {
                consumer.accept(node);
            }
        }
    }

    private void expandAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public static void main(String... args) throws Exception {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new Application());
        frame.setSize(530, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
