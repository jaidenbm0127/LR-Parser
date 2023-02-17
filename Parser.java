import java.util.HashMap;
import java.util.Objects;

public class Parser
{
    StringBuilder stack;
    HashMap<String, Rule> rules;
    HashMap<String, HashMap<String, String>> actionOrGoTo;
    String stringToParse;

    public Parser(String testString)
    {
        this.actionOrGoTo = new HashMap<>();
        this.stringToParse = testString;

        makeTable();
        makeRules();
    }

    private void makeRules()
    {
        rules = new HashMap<>()
        {{
            put("6", new Rule("id", "F"));
            put("5", new Rule("(E)", "F"));
            put("4", new Rule("F", "T"));
            put("3", new Rule("T*F", "T"));
            put("2", new Rule("T", "E"));
            put("1", new Rule("E+T", "E"));
        }};
    }

    private void makeTable()
    {
        HashMap<String, String> idStates = new HashMap<>()
        {{
            put("0", "s5"); put("4", "s5"); put("6", "s5"); put("7", "s5");
        }};
        actionOrGoTo.put("id", idStates);

        HashMap<String, String> plusStates = new HashMap<>()
        {{
            put("1", "s6"); put("2", "r2"); put("3", "r4"); put("5", "r6"); put("8", "s6"); put("9", "r1"); put("10", "r3"); put("11", "r5");
        }};
        actionOrGoTo.put("+", plusStates);

        HashMap<String, String> timesStates = new HashMap<>()
        {{
            put("2", "s7"); put("3", "r4"); put("5", "r6"); put("9", "s7"); put("10", "r3"); put("11", "r5");
        }};
        actionOrGoTo.put("*", timesStates);

        HashMap<String, String> leftParenthesis = new HashMap<>()
        {{
            put("0", "s4"); put("4", "s4"); put("6", "s4"); put("7", "s4");
        }};
        actionOrGoTo.put("(", leftParenthesis);

        HashMap<String, String> rightParenthesis = new HashMap<>()
        {{
            put("2", "r2"); put("3", "r4"); put("5", "r6"); put("8", "s11"); put("9", "r1"); put("10", "r3"); put("11", "r5");
        }};
        actionOrGoTo.put(")", rightParenthesis);

        HashMap<String, String> dollaSign = new HashMap<>()
        {{
            put("1", "acc"); put("2", "r2"); put("3", "r4"); put("5", "r6"); put("9", "r1"); put("10", "r3"); put("11", "r5");
        }};
        actionOrGoTo.put("$", dollaSign);

        HashMap<String, String> E = new HashMap<>()
        {{
            put("0", "1"); put("4", "8");
        }};
        actionOrGoTo.put("E", E);

        HashMap<String, String> T = new HashMap<>()
        {{
            put("0", "2"); put("4", "2"); put("6", "9");
        }};
        actionOrGoTo.put("T", T);

        HashMap<String, String> F = new HashMap<>()
        {{
            put("0", "3"); put("4", "3"); put("6", "3"); put("7", "10");
        }};
        actionOrGoTo.put("F", F);
    }

    public void parseString()
    {
        stack = new StringBuilder("0");
        String lastOperation = "";
        while(!Objects.equals(lastOperation, "acc"))
        {
            if(lastOperation.isBlank())
            {
                if(checkIfID())
                {
                    lastOperation = actionOrGoTo.get("id").get("0");
                }
                else
                {
                    lastOperation = actionOrGoTo.get("(").get("0");
                }
            }
            else
            {
                if(lastOperation.charAt(0) == 's')
                {
                    if(checkIfID())
                    {
                        stack.append("id");
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("id", 2)[1];
                    }
                    else if(stringToParse.charAt(0) == '*')
                    {
                        String currentChar = String.valueOf(stringToParse.charAt(0));
                        stack.append(currentChar);
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("\\*", 2)[1];
                    }
                    else if(stringToParse.charAt(0) == '(')
                    {
                        String currentChar = String.valueOf(stringToParse.charAt(0));
                        stack.append(currentChar);
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("\\(", 2)[1];
                    }
                    else if(stringToParse.charAt(0) == ')')
                    {
                        String currentChar = String.valueOf(stringToParse.charAt(0));
                        stack.append(currentChar);
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("\\)", 2)[1];
                    }
                    else if(stringToParse.charAt(0) == '+')
                    {
                        String currentChar = String.valueOf(stringToParse.charAt(0));
                        stack.append(currentChar);
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("\\+", 2)[1];
                    }
                    else
                    {
                        String currentChar = String.valueOf(stringToParse.charAt(0));
                        stack.append(currentChar);
                        stack.append(lastOperation.split("s")[1]);
                        stringToParse = stringToParse.split("\\$", 2)[1];
                    }

                    if(checkIfID())
                    {
                        lastOperation = actionOrGoTo.get("id").get(lastOperation.split("s")[1]);
                    }
                    else
                    {
                        lastOperation = actionOrGoTo.get(String.valueOf(stringToParse.charAt(0))).get(lastOperation.split("s")[1]);
                    }
                }
                else
                {
                    String ruleNumber = lastOperation.split("r")[1];

                    switch(ruleNumber)
                    {
                        case "6", "4", "2" ->
                        {
                            int index = stack.lastIndexOf(rules.get(ruleNumber).getInput());
                            if(Objects.equals(rules.get(ruleNumber).getInput(), "id"))
                            {
                                stack.replace(index, index+2, rules.get(ruleNumber).getOutput());
                            }
                            else
                            {
                                stack.replace(index, index+1, rules.get(ruleNumber).getOutput());
                            }

                        }
                        case "5" ->
                        {
                            int start = stack.lastIndexOf("(");
                            int end = stack.lastIndexOf(")");
                            stack.replace(start, end+1, rules.get(ruleNumber).getOutput());
                        }
                        case "3" ->
                        {
                            int start = stack.lastIndexOf("T");
                            int end = stack.lastIndexOf("F");
                            stack.replace(start, end+1, rules.get(ruleNumber).getOutput());
                        }
                        case "1" ->
                        {
                            int start = stack.lastIndexOf("E");
                            int end = stack.lastIndexOf("T");
                            stack.replace(start, end+1, rules.get(ruleNumber).getOutput());
                        }
                    }
                    while(Character.isDigit(stack.charAt(stack.length()-1)))
                    {
                        stack.deleteCharAt((stack.length()-1));
                    }

                    String state = actionOrGoTo.get(rules.get(ruleNumber).getOutput()).get(String.valueOf(stack.charAt(stack.lastIndexOf(rules.get(ruleNumber).getOutput()) - 1)));
                    stack.append(state);

                    if(checkIfID())
                    {
                        lastOperation = actionOrGoTo.get("id").get(state);
                    }
                    else
                    {
                        lastOperation = actionOrGoTo.get(String.valueOf(stringToParse.charAt(0))).get(state);
                    }
                }
            }
            System.out.println("Stack: " + stack.toString() + " Input: " + stringToParse + " Operation: " + lastOperation);
        }
    }

    private String getNumberBeforeLetter(int letterIndex)
    {
        StringBuilder buildNum = new StringBuilder();
        if(stack.length() > 3)
        {
            for(int i = letterIndex - 2; i < letterIndex; i++)
            {
                buildNum.append(stack.charAt(i));
            }
        }
        else
        {
            stack.charAt(letterIndex - 1);
        }

        return buildNum.toString();
    }
    private boolean checkIfID()
    {
        return stringToParse.charAt(0) == 'i';
    }

    public static void main(String[] args)
    {
        Parser myParser = new Parser("id*(id+id)$");
        myParser.parseString();
    }
}
