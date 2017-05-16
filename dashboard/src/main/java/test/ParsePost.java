/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.StringTokenizer;

/**
 *
 * @author deeta1
 */
public class ParsePost {

    private StackX theStack;
    private final String[] input;
//--------------------------------------------------------------

    public ParsePost(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, " ");
        String[] tokenArray = new String[tokenizer.countTokens()];
        int i = -1;
        while (tokenizer.hasMoreTokens()) {
            String token = (String) tokenizer.nextToken();
            System.out.println("token name ---> " + token);
            tokenArray[++i] = token;
        }
        input = tokenArray;
    }
//--------------------------------------------------------------

    public float doParse() {
        theStack = new StackX(20);             // make new stack
        int j;
        float num1, num2, interAns;

        for (j = 0; j < input.length; j++) // for each char,
        {

            // read from input
            theStack.displayStack("" + input[j] + " ");  // *diagnostic*
            if (!input[j].equalsIgnoreCase("+") && !input[j].equalsIgnoreCase("-") && !input[j].equalsIgnoreCase("*") && !input[j].equalsIgnoreCase("/")) {
                if (Integer.parseInt(input[j]) >= 0) // if it's a number
                {
                    theStack.push(Float.parseFloat(input[j])); //   push it
                }
            } else // it's an operator
            {
                num2 = theStack.pop();          // pop operands
                num1 = theStack.pop();
                switch (input[j]) // do arithmetic
                {
                    case "+":
                        interAns = num1 + num2;
                        break;
                    case "-":
                        interAns = num1 - num2;
                        break;
                    case "*":
                        interAns = num1 * num2;
                        break;
                    case "/":
                        interAns = num1 / num2;
                        break;
                    default:
                        interAns = 0;
                }  // end switch
                theStack.push(interAns);        // push result
            }  // end else
        }  // end for
        interAns = theStack.pop();            // get answer
        return interAns;
    }  // end doParse()

}  // end class ParsePost
////////////////////////////////////////////////////////////////