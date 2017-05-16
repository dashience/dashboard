/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deeta1
 */
import java.io.IOException;
import java.util.StringTokenizer;

public class InToPost {

    private final Stack theStack;
    private String input;
    private final String[] tokenArray;
    private String output = "";

    public InToPost(String in) {
        input = in;
        input = input.replaceAll("\\(", "\\( ");
        input = input.replaceAll("\\)", " \\)");
        input = input.replaceAll("-", " - ");
        input = input.replaceAll("\\+", " + ");
        input = input.replaceAll("\\*", " * ");
        input = input.replaceAll("/", " / ");
        System.out.println("input ---->" + input);
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        tokenArray = new String[tokenizer.countTokens()];
        int i = -1;
        while (tokenizer.hasMoreTokens()) {
            String token = (String) tokenizer.nextToken();
            System.out.println("token name ---> " + token);
            tokenArray[++i] = token;
        }
        int stackSize = tokenArray.length;
        System.out.println("stacksize ---> " + stackSize);
        theStack = new Stack(stackSize);
        System.out.println("theStack ---> " + theStack);
    }

    public String doTrans() {
        for (int j = 0; j < tokenArray.length; j++) {
            System.out.println("ch --> " + tokenArray[j]);
            switch (tokenArray[j].trim()) {
                case "+":
                case "-":
                    gotOper(tokenArray[j], 1);
                    break;
                case "*":
                case "/":
                    gotOper(tokenArray[j], 2);
                    break;
                case "(":
                    theStack.push(tokenArray[j]);
                    break;
                case ")":
                    gotParen(tokenArray[j]);
                    break;
                default:
                    if (output.equalsIgnoreCase("") || output == null || output.isEmpty()) {
                        output = output + tokenArray[j];
                    } else {
                        output = output + " " + tokenArray[j];
                    }
                    break;
            }
        }
        while (!theStack.isEmpty()) {
            if (output.equalsIgnoreCase("") || output == null || output.isEmpty()) {
                output = output + theStack.pop();
            } else {
                output = output + " " + theStack.pop();
            }
        }
        System.out.println(output);
        return output;
    }

    public void gotOper(String opThis, int prec1) {
        while (!theStack.isEmpty()) {
            String opTop = theStack.pop();
            System.out.println("opTop ---> " + opTop);
            if (opTop.equalsIgnoreCase("(")) {
                theStack.push(opTop);
                break;
            } else {
                int prec2;
                if (opTop.equalsIgnoreCase("+") || opTop.equalsIgnoreCase("-")) {
                    prec2 = 1;
                } else {
                    prec2 = 2;
                }
                System.out.println("prec1 --> " + prec1);
                System.out.println("prec2 --> " + prec2);
                if (prec2 < prec1) {
                    theStack.push(opTop);
                    break;
                } else {
                    if (output.equalsIgnoreCase("") || output == null || output.isEmpty()) {
                        output = output + opTop;
                    } else {
                        output = output + " " + opTop;
                    }
                }
            }
        }
        theStack.push(opThis);
    }

    public void gotParen(String ch) {
        while (!theStack.isEmpty()) {
            String chx = theStack.pop();
            if (chx.equalsIgnoreCase("(")) {
                break;
            } else {
                if (output.equalsIgnoreCase("") || output == null || output.isEmpty()) {
                    output = output + chx;
                } else {
                    output = output + " " + chx;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String input = "(418*419)/2";
        String output;
        InToPost theTrans = new InToPost(input);
        output = theTrans.doTrans();
        System.out.println("Postfix is " + output + '\n');
    }

    class Stack {

        private int maxSize;
        private String[] stackArray;
        private int top;

        public Stack(int max) {
            maxSize = max;
            System.out.println("maxSize ---> " + maxSize);
            stackArray = new String[maxSize];
            top = -1;
        }

        public void push(String j) {
            stackArray[++top] = j;
        }

        public String pop() {
            return stackArray[top--];
        }

        public String peek() {
            return stackArray[top];
        }

        public boolean isEmpty() {
            return (top == -1);
        }
    }
}
