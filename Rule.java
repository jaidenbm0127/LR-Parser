public class Rule
{
    private final String input;
    private final String output;

    public Rule(String input, String output)
    {
        this.input = input;
        this.output = output;
    }

    public String getInput()
    {
        return input;
    }

    public String getOutput()
    {
        return output;
    }
}
