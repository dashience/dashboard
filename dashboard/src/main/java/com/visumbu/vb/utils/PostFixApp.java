/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

/**
 *
 * @author deeta1
 */
import java.io.*;              // for I/O
import java.util.StringTokenizer;
import com.visumbu.vb.utils.ParsePost;
////////////////////////////////////////////////////////////////

class StackX {

    private final int maxSize;
    private final double[] stackArray;
    private int top;
//--------------------------------------------------------------

    public StackX(int size) // constructor
    {
        maxSize = size;
        stackArray = new double[maxSize];
        top = -1;
    }
//--------------------------------------------------------------

    public void push(double j) // put item on top of stack
    {
        stackArray[++top] = j;
    }
//--------------------------------------------------------------

    public double pop() // take item from top of stack
    {
        return stackArray[top--];
    }
//--------------------------------------------------------------

    public double peek() // peek at top of stack
    {
        return stackArray[top];
    }
//--------------------------------------------------------------

    public boolean isEmpty() // true if stack is empty
    {
        return (top == -1);
    }
//--------------------------------------------------------------

    public boolean isFull() // true if stack is full
    {
        return (top == maxSize - 1);
    }
//--------------------------------------------------------------

    public int size() // return size
    {
        return top + 1;
    }
//--------------------------------------------------------------

    public double peekN(int n) // peek at index n
    {
        return stackArray[n];
    }
//--------------------------------------------------------------

    public void displayStack(String s) {
        System.out.print(s);
        System.out.print("Stack (bottom-->top): ");
        for (int j = 0; j < size(); j++) {
            System.out.print(peekN(j));
            System.out.print(' ');
        }
        System.out.println("");
    }
//--------------------------------------------------------------
}  // end class StackX
////////////////////////////////////////////////////////////////


class PostfixApp {

    public static void main(String[] args) throws IOException {
        String input;
        double output;

        while (true) {
            System.out.print("Enter postfix: ");
            System.out.flush();
            input = getString();

            // read a string from kbd
            if (input.equals("")) // quit if [Enter]
            {
                break;
            }
            // make a parser
            ParsePost aParser = new ParsePost(input);
            output = aParser.doParse();  // do the evaluation
            System.out.println("Evaluates to " + output);
        }  // end while
    }  // end main()
//--------------------------------------------------------------

    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }
//--------------------------------------------------------------
}  // end class PostfixApp
////////////////////////////////////////////////////////////////


