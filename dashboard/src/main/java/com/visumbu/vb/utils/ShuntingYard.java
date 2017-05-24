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
import java.util.*;
import java.util.stream.Collectors;

public class ShuntingYard {

    private enum Operator {

        AND(1), OR(1), ADD(16), SUBTRACT(15), MULTIPLY(17), DIVIDE(18), NOTEQUALS(13), EQUALS(14), LESSTHAN(11), GREATERTHAN(12), LESSTHANOREQUAL(9), GREATERTHANOREQUAL(10), EMPTY(8), NOTEMPTY(7), LIKE(6), NOTLIKE(5), NULL(4), NOTNULL(3);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {
        {
            put("+", Operator.ADD);
            put("-", Operator.SUBTRACT);
            put("*", Operator.MULTIPLY);
            put("/", Operator.DIVIDE);
            put("AND", Operator.AND);
            put("OR", Operator.OR);
            put("=", Operator.EQUALS);
            put("!=", Operator.NOTEQUALS);
            put("<", Operator.LESSTHAN);
            put(">", Operator.GREATERTHAN);
            put("<=", Operator.LESSTHANOREQUAL);
            put(">=", Operator.GREATERTHANOREQUAL);
            put("=''", Operator.EMPTY);
            put("!=''", Operator.NOTEMPTY);
            put("LIKE", Operator.LIKE);
            put("NOTLIKE", Operator.NOTLIKE);
            put("NULL", Operator.NULL);
            put("NOTNULL", Operator.NOTNULL);

        }
    };

    private static boolean isHigerPrec(String op, String sub) {
        return (ops.containsKey(sub) && ops.get(sub).precedence >= ops.get(op).precedence);
    }

    public static String postfix(String infix) {
        StringBuilder output = new StringBuilder();
        List<String> outputList = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        String input = infix;
        input = input.replaceAll("\\(", "\\( ");
        input = input.replaceAll("\\)", " \\)");
        input = input.replaceAll("-", " - ");
        input = input.replaceAll("\\+", " + ");
        input = input.replaceAll("\\*", " * ");
        input = input.replaceAll("/", " / ");
        System.out.println("input infix ----> " + input);
        for (String token : input.split("\\s+")) {
            // operator
            System.out.println("token ---> "+token);
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrec(token, stack.peek())) {
                    String popedOperator = stack.pop();
                    output.append(popedOperator).append(' ');
                    outputList.add(popedOperator);
                }
                stack.push(token);

                // left parenthesis
            } else if (token.equals("(")) {
                stack.push(token);

                // right parenthesis
            } else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    String popedOperator = stack.pop();
                    output.append(popedOperator).append(' ');
                    outputList.add(popedOperator);
                }
                stack.pop();

                // digit
            } else {
                output.append(token).append(' ');
                outputList.add(token);
            }
        }

        while (!stack.isEmpty()) {
            String popedOperator = stack.pop();
            output.append(popedOperator).append(' ');
            outputList.add(popedOperator);
        }
        System.out.println(outputList);
        System.out.println("output ---> " + output);
        return output.toString();
    }

    public static void main(String argv[]) {
        if (1 == 1) {
            // System.out.println("'john'".substring(1, "'john'".length() - 1));
            System.out.println(postfix("ga:dayOfWeekName LIKE 'M%'"));
            return;
        }
        String[] firstnames = {"john", "david", "mathew", "john", "jerry", "Uffe", "Sekar", "Suresh", "Ramesh", "Raja"};
        String[] secondnames = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"};
        String[] salary = {"10000", "20000", "15000", "5323", "2000", "5346", "1000", "4889", "7854", "2438"};
        String[] location = {"India", "Iceland", "Mexico", "Slovenia", "Poland", "Australia", "1000", "USA", "England", "Canada"};

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("firstname", firstnames[i]);
            dataMap.put("secondname", secondnames[i]);
            dataMap.put("salary", salary[i]);
            dataMap.put("location", location[i]);
            list.add(dataMap);
        }
        String rulesInfix = "( ( firstname = 'john' AND secondname = 'Eleven' ) OR ( salary = 15000 AND location = 'Mexico' OR ( firstname = 'mathew' AND secondname = 'Thirteen' ) ) )";
        System.out.println(applyExpression(list, rulesInfix));
//        String postfixExpr = postfix("( ( firstname = 'john' AND secondname = 'Eleven' ) OR ( salary = 15000 AND location = 'Mexico' OR ( firstname = 'mathew' AND secondname = 'Thirteen' ) ) )");
//        System.out.println(filter(list, postfixExpr));
    }

    public static List<Map<String, Object>> applyExpression(List<Map<String, Object>> list, String rulesInfix) {
        System.out.println(rulesInfix);

        if (rulesInfix == null || rulesInfix.isEmpty()) {
            return list;
        }
        rulesInfix = rulesInfix.replaceAll("\\s+", " ");
        rulesInfix = rulesInfix.replaceAll("NOT LIKE", "NOTLIKE");
        rulesInfix = rulesInfix.replaceAll("NOT NULL", "NOTNULL");
        String postfixExpr = postfix(rulesInfix.replaceAll("([\\(\\)])", " $1 "));
        System.out.println(postfixExpr);
        return filter(list, postfixExpr);
    }

    public static List<Map<String, Object>> filter(List<Map<String, Object>> list, String postFixRules) {
        if(list == null){
            return null;
        }
        List<Map<String, Object>> filtered = list.stream()
                .filter(p -> checkFilter(p, postFixRules)).collect(Collectors.toList());
        return filtered;
    }

    public static Boolean checkFilter(Map<String, Object> mapData, String postFixRules) {
        // Apply condition here and return true or false
        // return (mapData.get("firstname") + "").equalsIgnoreCase("john");
        String[] postfixRulesList = postFixRules.trim().split("\\s+");
        Deque<String> stack = new LinkedList<>();
        for (int i = 0; i < postfixRulesList.length; i++) {
            String postFixToken = postfixRulesList[i];
            if (ops.containsKey(postFixToken)) {
                String operator = postFixToken;
                String operand1 = getOperand(stack.pop(), mapData);
                String operand2 = getOperand(stack.pop(), mapData);
                stack.push(calculate(operand1, operand2, operator) + "");
            } else {
                stack.push(postFixToken);
            }
        }
        System.out.println("OUTPUT ===> " + stack);
        return stack.pop().equalsIgnoreCase("true");
    }

    private static Boolean calculate(String operand1, String operand2, String operator) {
        System.out.println("Operand 1 " + operand1 + " Operand 2 " + operand2 + " Operator " + operator);
        if (operator.trim().equalsIgnoreCase("=")) {
            return operand1.equalsIgnoreCase(operand2);
        }
        if (operator.trim().equalsIgnoreCase("and")) {
            return operand1.equalsIgnoreCase("true") && operand2.equalsIgnoreCase("true");
        }
        if (operator.trim().equalsIgnoreCase("OR")) {
            return (operand1.equalsIgnoreCase("true") ? true : operand2.equalsIgnoreCase("true"));
        }
        if (operator.trim().equalsIgnoreCase("!=")) {
            System.out.println("NOT EQUAL TO");
            return (!operand1.equalsIgnoreCase(operand2));
        }
        if (operator.trim().equalsIgnoreCase("<")) {
            System.out.println("Less");
            return (Float.parseFloat(operand2) < Float.parseFloat(operand1));

        }
        if (operator.trim().equalsIgnoreCase(">")) {
            System.out.println("Greater");
            return (Float.parseFloat(operand2) > Float.parseFloat(operand1));

        }
        if (operator.trim().equalsIgnoreCase("<=")) {
            System.out.println("LessThanOrEqual");
            return (Float.parseFloat(operand2) <= Float.parseFloat(operand1));

        }
        if (operator.trim().equalsIgnoreCase(">=")) {
            System.out.println("GreaterThanOrEqual");
            return (Float.parseFloat(operand2) >= Float.parseFloat(operand1));

        }
        if (operator.trim().equalsIgnoreCase("=''")) {
            System.out.println("is empty");
            System.out.println("operand1" + operand1 + "operand2" + operand2 + "operator" + operator);
            return (operand2.equals(""));

        }
        if (operator.trim().equalsIgnoreCase("!=''")) {
            System.out.println("not empty");
            return (!operand2.isEmpty());

        }

        if (operator.trim().equalsIgnoreCase("LIKE")) {
            return like(operand2, operand1);
        }

        if (operator.trim().equalsIgnoreCase("NOTLIKE")) {
            return notLike(operand2, operand1);
        }
        if (operator.trim().equalsIgnoreCase("NULL")) {
            System.out.println("null");
            return (operand2 == null);

        }
        if (operator.trim().equalsIgnoreCase("NOTNULL")) {
            System.out.println("not null");
            return (operand2 != null);

        }
        return false;
    }

    public static boolean like(String toBeCompare, String by) {
        System.out.println("toBeCompare "+toBeCompare+"  by"+by);
        if (toBeCompare != null && by != null) {
            if (by.startsWith("%") && by.endsWith("%")) {
                System.out.println("Contains");
                by = by.replaceAll("%", "");
                return (toBeCompare.contains(by));
            } else if (by.endsWith("%")) {
                System.out.println("beginWith");
                by = by.replaceAll("%", "");
                int strlen = by.length();
                toBeCompare = toBeCompare.substring(0, strlen);
                return (by.equalsIgnoreCase(toBeCompare));
            } else if (by.startsWith("%")) {
                System.out.println("endsWith");
                by = by.replaceAll("%", "");
                int length = toBeCompare.length();
                int strlen = by.length();
                toBeCompare = toBeCompare.substring(length - strlen);
                return (by.equalsIgnoreCase(toBeCompare));
            }
        }
        return false;
    }

    public static boolean notLike(String toBeCompare, String by) {
        if (toBeCompare != null && by != null) {
            if (by.startsWith("%") && by.endsWith("%")) {
                System.out.println("doesn't Contains");
                by = by.replaceAll("%", "");
                return (!toBeCompare.contains(by));
            } else if (by.endsWith("%")) {
                System.out.println("doesn't beginWith");
                by = by.replaceAll("%", "");
                int strlen = by.length();
                toBeCompare = toBeCompare.substring(0, strlen);
                return (!by.equalsIgnoreCase(toBeCompare));
            } else if (by.startsWith("%")) {
                System.out.println("doesn't endsWith");
                by = by.replaceAll("%", "");
                int length = toBeCompare.length();
                int strlen = by.length();
                toBeCompare = toBeCompare.substring(length - strlen);
                return (!by.equalsIgnoreCase(toBeCompare));
            }

        }
        return false;
    }

    private static String getOperand(String operand, Map<String, Object> mapData) {
        if (operand.equalsIgnoreCase("true") || operand.equalsIgnoreCase("false")) {
            return operand;
        }
        try {
            Double value = Double.parseDouble(operand);
            return operand;
        } catch (Exception e) {

        }

        if ((operand.startsWith("'") && operand.endsWith("'"))) {
            return operand.substring(1, operand.length() - 1);
//            String fieldName = operand.split("\\.")[1];
//            return mapData.get(fieldName) + "";
        }

        return mapData.get(operand) + "";
    }

}
