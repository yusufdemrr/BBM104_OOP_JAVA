//// Yusuf Demir b2210356074

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}


/**
 * Represents a circular doubly linked list.
 *
 * @param <T> the type of elements stored in the list
 */
class CircularDoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CircularDoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addToHead(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.prev = tail;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode;
            head = newNode;
        }
        size++;
    }

    public void addToTail(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.prev = tail;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            tail.next = newNode;
            head.prev = newNode;
            tail = newNode;
        }
        size++;
    }

    public T removeFromHead() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        T data = head.data;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = tail;
            tail.next = head;
        }
        size--;
        return data;
    }

    public T removeFromTail() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        T data = tail.data;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = head;
            head.prev = tail;
        }
        size--;
        return data;
    }
}

/**
 * Represents a generic stack.
 *
 * @param <T> the type of elements stored in the stack
 */
class GenericStack<T> {
    private CircularDoublyLinkedList<T> list;

    public GenericStack() {
        list = new CircularDoublyLinkedList<>();
    }

    public void push(T item) {
        list.addToHead(item);
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty.");
        }
        return list.removeFromHead();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

class GenericQueue<T> {
    private CircularDoublyLinkedList<T> list;

    public GenericQueue() {
        list = new CircularDoublyLinkedList<>();
    }

    public void enqueue(T item) {
        list.addToTail(item);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return list.removeFromHead();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            String input = args[0];
            String output = args[1];
            File inputFile = new File(input);
            File outputFile = new File(output);
            Scanner scanner = new Scanner(inputFile);
            PrintWriter writer = new PrintWriter(outputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");

                if (parts[0].equals("Convert from Base 10 to Base 2:")) {
                    int number = Integer.parseInt(parts[1].trim());
                    String binary = baseConversion(number);
                    writer.println("Equivalent of " + number + " (base 10) in base 2 is: " + binary);
                } else if (parts[0].equals("Count from 1 up to n in binary:")) {
                    int n = Integer.parseInt(parts[1].trim());
                    writer.print("Counting from 1 up to " + n + " in binary:\t");
                    countBinary(n, writer);
                } else if (parts[0].equals("Check if following is palindrome or not:")) {
                    String word = parts[1].trim();
                    boolean isPalindrome = isPalindrome(word);
                    writer.println("\"" + word + "\" is " + (isPalindrome ? "a palindrome." : "not a palindrome."));
                } else if (parts[0].equals("Check if following expression is valid or not:")) {
                    String expression = parts[1].trim();
                    boolean isValid = isBalanced(expression);
                    writer.println("\"" + expression + "\" is " + (isValid ? "a valid expression." : "not a valid expression."));
                }
            }

            scanner.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a decimal number to binary representation.
     *
     * @param number the decimal number to be converted
     * @return the binary representation of the decimal number
     */
    public static String baseConversion(int number) {
        GenericStack<Integer> stack = new GenericStack<>();

        if (number == 0){
            return "0";
        }

        if (number < 0){
            number = number*-1;
            while (number > 0) {
                int remainder = number % 2;
                stack.push(remainder);
                number /= 2;
            }

            StringBuilder result = new StringBuilder();
            while (!stack.isEmpty()) {
                result.append(stack.pop());
            }
            return "-"+result.toString();
        }

        while (number > 0) {
            int remainder = number % 2;
            stack.push(remainder);
            number /= 2;
        }

        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.toString();
    }

    /**
     * Checks if an expression with parentheses is balanced.
     *
     * @param expression the expression to be checked
     * @return true if the expression is balanced, false otherwise
     */
    public static boolean isBalanced(String expression) {
        GenericStack<Character> stack = new GenericStack<>();

        for (char c : expression.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty()) {
                    return false;
                }

                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                        (c == '}' && top != '{') ||
                        (c == ']' && top != '[')) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    /**
     * Checks if a word is a palindrome.
     *
     * @param word the word to be checked
     * @return true if the word is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String word) {
        GenericStack<Character> stack = new GenericStack<>();
        StringBuilder filteredWord = new StringBuilder();

        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                stack.push(Character.toLowerCase(c));
                filteredWord.append(Character.toLowerCase(c));
            }
        }

        StringBuilder reversedWord = new StringBuilder();
        while (!stack.isEmpty()) {
            reversedWord.append(stack.pop());
        }

        return filteredWord.toString().equals(reversedWord.toString());
    }

    /**
     * Counts from 1 up to n in binary representation.
     *
     * @param n      the upper limit
     * @param writer the PrintWriter used to write the output
     */
    public static void countBinary(int n, PrintWriter writer) {
        GenericQueue<String> queue = new GenericQueue<>();

        queue.enqueue("1");

        while (n > 0) {
            String binary = queue.dequeue();
            writer.print(binary + "\t");
            n--;

            if (n > 0 && n % 5 == 0) {
                writer.println();
            }

            queue.enqueue(binary + "0");
            queue.enqueue(binary + "1");
        }

        writer.println();
    }
}

