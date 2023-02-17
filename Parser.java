import java.util.HashMap;

public class Parser
{
    StringBuilder stack;
    HashMap<String, String> rules;
    HashMap<String, HashMap<Integer, String>> actionOrGoTo;
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
        this.rules = new HashMap<>()
        {{
            put("id", "F"); put("(E)", "F"); put("F", "T"); put("T*F", "T"); put("T", "E"); put("E+T", "E");
        }};
    }

    private void makeTable()
    {
        HashMap<Integer, String> idStates = new HashMap<>()
        {{
            put(0, "s5"); put(4, "s5"); put(6, "s5"); put(7, "s5");
        }};
        actionOrGoTo.put("id", idStates);

        HashMap<Integer, String> plusStates = new HashMap<>()
        {{
            put(1, "s6"); put(2, "r2"); put(3, "r4"); put(5, "r6"); put(9, "s6"); put(10, "s6"); put(11, "s6");
        }};
        actionOrGoTo.put("+", plusStates);

        HashMap<Integer, String> timesStates = new HashMap<>()
        {{
            put(2, "s7"); put(3, "r4"); put(5, "r6"); put(9, "s7"); put(10, "r3"); put(11, "r5");
        }};
        actionOrGoTo.put("*", timesStates);

        HashMap<Integer, String> leftParenthesis = new HashMap<>()
        {{
            put(0, "s4"); put(4, "s4"); put(6, "s4"); put(7, "s4");
        }};
        actionOrGoTo.put("(", leftParenthesis);

        HashMap<Integer, String> rightParenthesis = new HashMap<>()
        {{
            put(2, "r2"); put(3, "r4"); put(5, "r6"); put(8, "s11"); put(9, "r1"); put(10, "r3"); put(11, "r5");
        }};
        actionOrGoTo.put(")", rightParenthesis);

        HashMap<Integer, String> dollaSign = new HashMap<>()
        {{
            put(1, "acc"); put(2, "r2"); put(3, "r4"); put(5, "r6"); put(9, "r1"); put(10, "r3"); put(11, "r5");
        }};
        actionOrGoTo.put("$", dollaSign);

        HashMap<Integer, String> E = new HashMap<>()
        {{
            put(0, "1"); put(4, "8");
        }};
        actionOrGoTo.put("E", E);

        HashMap<Integer, String> T = new HashMap<>()
        {{
            put(0, "2"); put(4, "2"); put(6, "2");
        }};
        actionOrGoTo.put("T", T);

        HashMap<Integer, String> F = new HashMap<>()
        {{
            put(0, "3"); put(4, "3"); put(6, "3"); put(7, "10");
        }};
        actionOrGoTo.put("F", F);
    }

    public void parseString()
    {
        stack = new StringBuilder("0");
        String lastOperation = "";
        String currentOperation = "";
        while(!stringToParse.isBlank())
        {
            if(lastOperation.isBlank())
            {
                if(checkIfID())
                {
                    currentOperation = actionOrGoTo.get("id").get(0);
                }
                else
                {
                    currentOperation = actionOrGoTo.get("(").get(0);
                }
            }
            else
            {

            }

        }
    }

    private boolean checkIfID()
    {
        return stringToParse.charAt(0) == 'i';
    }

    public static void main(String[] args)
    {
        Parser myParser = new Parser("id(id+id)$");
        myParser.parseString();
    }
}
