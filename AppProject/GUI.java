package AppProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame window;
    private JPanel topPanel;
    private JLabel name;
    private JPanel buttons;
    private QuestionGame qg;

    private String prev;

    GUI() {
        // TODO: upload a tree question file of your choice

        qg = new QuestionGame();
        createWin();
    }

    private void createWin() {
        window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setSize(600, 600);
        window.setTitle("Emotional Support App");

        topPanel = new JPanel(new GridLayout(0,1));

        JPanel label = new JPanel(new FlowLayout(FlowLayout.CENTER));
        name = new JLabel("Welcome to the Emotional Support App");
        label.add(name);
        label.setBackground(Color.orange);
        label.setBorder(new EmptyBorder(50,10,10,10));
        topPanel.add(label);

        createButtons();
        topPanel.add(buttons);

        window.setContentPane(topPanel);

        window.setVisible(true);
    }

    private void createButtons() {
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setBackground(Color.ORANGE);

        JButton next = new JButton("Next");
        next.setBackground(Color.LIGHT_GRAY);
        buttons.add(next);

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        yes.setBackground(Color.LIGHT_GRAY);
        no.setBackground(Color.LIGHT_GRAY);

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    prev = qg.getRootQ();
                    name.setText("Question: " + prev);

                    buttons.remove(next);
                    buttons.add(yes);
                    buttons.add(no);

                    buttons.revalidate();
                    buttons.repaint();

                    topPanel.revalidate();
                    topPanel.repaint();
                }
        });

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String next = qg.nextQuestion(prev, "y");

                if (!next.contains("?")) {
                    JOptionPane.showMessageDialog(null, next, "Answer or Solution", JOptionPane.INFORMATION_MESSAGE);
                    window.dispose();
                    createWin();
                } else {
                    name.setText(next);
                    prev = next;
                }
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String next = qg.nextQuestion(prev, "n");

                if (!next.contains("?")) {
                    JOptionPane.showMessageDialog(null, next, "Answer or Solution", JOptionPane.INFORMATION_MESSAGE);
                    window.dispose();
                    createWin();
                } else {
                    name.setText(next);
                    prev = next;
                }
            }
        });
    }

    public static void main(String[] args) {
        new GUI();
    }
}
