package AppProject;

import java.io.*;
import java.util.*;

public class QuestionGame {

    public static final String QUESTION_FILE = "emotionalapptreequestions.txt";

    private QuestionNode overallRoot; // referrence to the first node
    private Scanner console;          // referrence to a scanner object

    // post: constructs a new QuestionsGame object with a single
    //       answer (leaf) representing the object "computer"
    public QuestionGame() {
        overallRoot = new QuestionNode("computer");
        console = new Scanner(System.in);

        try {
            read(new Scanner(new File("C:\\Users\\gavin\\GAVIN_STASH\\JavaTestEnvironment\\src\\AppProject\\emotionalapptreequestions.txt")));
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public String getRootQ() {
        return overallRoot.data;
    }

    // post: replaces the current tree with a new tree
    //       based on the information from a passed file
    public void read(Scanner input) {
        overallRoot = readHelper(input);
    }

    // post: returns the referrence to a new tree
    //       using the information from a passed file
    private QuestionNode readHelper(Scanner input) {
        String type = input.nextLine().trim();
        String data = input.nextLine().trim();

        QuestionNode root = new QuestionNode(data);

        if (type.equals("Q:")) {
            root.left = readHelper(input);
            root.right = readHelper(input);
        }

        return root;
    }

    // post: writes the current questions tree to an output file
    public void write(PrintStream output) {
        write(overallRoot, output);
    }

    // post: takes in a referrence to the current tree and writes
    //       the current questions tree to the passed output file
    private void write(QuestionNode root, PrintStream output) {
        if (root != null) {
            String type = "";
            if (root.left != null && root.right != null) {
                type = "Q:";
            } else if (root.left == null && root.right == null) {
                type = "A:";
            }

            output.println(type + "\n" + root.data);
            write(root.left, output);
            write(root.right, output);
        }
    }

    public String nextQuestion(String prev, String response) {
        return nextQuestionHelper(prev, response, overallRoot);
    }

    private String nextQuestionHelper(String prev, String response, QuestionNode root) {
        if (root == null) {
            return "";
        }

        if (prev.equals(root.data)) {

            if (root.right == null && root.left == null) {
                return root.data;
            }

            if (root.left != null && root.right != null) {
                if (response.equals("y")) {
                    return root.left.data;
                } else {
                    return root.right.data;
                }
            }
        }

        String left = nextQuestionHelper(prev, response, root.left);
        String right = nextQuestionHelper(prev, response, root.right);

        if (left.equals("")) {
            return right;
        }

        return left;
    }

    // post:  Uses the current question tree to play one complete
    //        guessing game with the user, asking yes/no questions
    //        until reaching an answer.
    public void askQuestions() {
        overallRoot = askQuestionsHelper(overallRoot);
    }

    // post:  Takes a root node and uses the current question tree
    //        to play one complete guessing game with the user,
    //        asking yes/no questions until reaching an answer object.
    //        If user enters no for the answer object, then asks user for
    //        the answer object they were thinking of as well as a question
    //        to distiguish between the old answer and the new answer.
    private QuestionNode askQuestionsHelper(QuestionNode root) {
        if (root.left == null && root.right == null) {
            if (yesTo("Would your object happen to be " + root.data + "?")) {
                System.out.println("Great, I got it right!");
            } else {
                QuestionNode temp = root;
                QuestionNode answer = new QuestionNode(answerHelper());
                QuestionNode question = new QuestionNode(questionHelper());
                root = question;

                if (yesTo("And what is the answer for your object?")) {
                    root.left = answer;
                    root.right = temp;
                } else {
                    root.left = temp;
                    root.right = answer;
                }
            }
        } else if (root.left != null && root.right != null) {
            if (yesTo(root.data)) {
                root.left = askQuestionsHelper(root.left);
            } else {
                root.right = askQuestionsHelper(root.right);
            }
        }
        return root;
    }

    // post: Keeps asking user for an answer until user
    //       enters a valid answer
    private String answerHelper() {
        System.out.print("What is the name of your object? ");
        String response = console.nextLine();

        if (response.contains("?")) {
            System.out.println("That is not an answer.");
            return answerHelper();
        }

        return response;
    }

    // post: Keeps asking user for a question until user
    //       enters a valid question
    private String questionHelper() {
        System.out.println("Please give me a yes/no question that");
        System.out.println("distinguishes between your object");
        System.out.print("and mine--> ");
        String response = console.nextLine();

        if (!response.contains("?") || response.length() <= 1) {
            System.out.println("That is not a question.");
            return questionHelper();
        }

        return response;
    }

    // post: asks the user a question, forcing an answer of "y" or "n";
    //       returns true if the answer was yes, returns false otherwise
    public boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
    }

    // Class that represents a single node in the tree
    private static class QuestionNode {
        public String data;        // stores the data of this node
        public QuestionNode left;  // reference to left subtree
        public QuestionNode right; // reference to right subtree

        // post: constructs a leaf node with the given data
        public QuestionNode(String data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }
}