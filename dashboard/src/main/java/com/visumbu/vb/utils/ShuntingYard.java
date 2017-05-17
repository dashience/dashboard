package com.visumbu.vb.utils;

import java.util.*;
import java.util.stream.Collectors;

public class ShuntingYard {

    private enum Operator {

        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), NOTEQUALS(5), EQUALS(8), LESSTHAN(9), GREATERTHAN(10), LESSTHANOREQUAL(11), GREATERTHANOREQUAL(12), EMPTY(13), NOTEMPTY(14), LIKE(15), NOTLIKE(16), NULL(17), NOTNULL(18), AND(6), OR(7);
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

        for (String token : infix.split("\\s")) {
            // operator
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
        if (toBeCompare != null && by != null) {
            if (by.startsWith("%") && by.endsWith("%")) {
                System.out.println("Contains");
                by = by.replaceAll("%", "");
                return (by.equalsIgnoreCase(toBeCompare));
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
                return (!by.equalsIgnoreCase(toBeCompare));
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
