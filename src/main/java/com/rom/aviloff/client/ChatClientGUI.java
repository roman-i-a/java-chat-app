package com.rom.aviloff.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;
    private JButton exitButton;

    public ChatClientGUI() {
        super("Chat App");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Styling variables
        Color backgroundColor = new Color(240, 240, 240);
        Color buttonColor = new Color(75, 75, 75);
        Color textColor = new Color(50, 50, 50);
        Font textFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);

        // Apply styles to the message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backgroundColor);
        messageArea.setForeground(textColor);
        messageArea.setFont(textFont);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name);

        textField = new JTextField();
        textField.setBackground(backgroundColor);
        textField.setForeground(textColor);
        textField.setFont(textFont);
        textField.addActionListener(e -> {
            String formattedDate = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String message = String.format("[%s]%s: %s", formattedDate, name, textField.getText());
            client.sendMessage(message);
            textField.setText("");
        });
        
        exitButton = new JButton("Exit");
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(e -> {
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            System.exit(0);
        });
        JPanel bottomJPanel = new JPanel(new BorderLayout());
        bottomJPanel.setBackground(backgroundColor);
        bottomJPanel.add(exitButton, BorderLayout.EAST);
        bottomJPanel.add(textField, BorderLayout.CENTER);

        add(bottomJPanel, BorderLayout.SOUTH);

        try {
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();
            String formattedDate = new SimpleDateFormat("HH:mm:ss").format(new Date());
            client.sendMessage("[" + formattedDate + "]" + name + " join the chat.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server", "Communication error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
