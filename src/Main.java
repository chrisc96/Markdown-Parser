import cli_parse.MainCLIParameters;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import outputs.ascii.AsciiStategy;
import outputs.html.HTMLStrategy;
import outputs.latex.LatexStategy;
import outputs.OutputStrategy;

public class Main {

    private final MainCLIParameters mainArgs = new MainCLIParameters();
    private OutputStrategy output;

    public static void main(String ... argv) {
        Main main = new Main();
        main.handleInputArgs(argv);
    }

    private void handleInputArgs(String[] argv) {
        JCommander jcommander = new JCommander(mainArgs);
        jcommander.setProgramName("MD2MARKUP");

        try {
            jcommander.parse(argv);
        }
        catch (ParameterException e) {
            System.out.println(e.getMessage());
            showUsage(jcommander);
        }

        if (mainArgs.isHelp()) {
            showUsage(jcommander);
        }

        // Gets value passed in via command line and sets output strategy (HTML, LaTeX, Ascii)
        setStrategy(mainArgs.getFormat());
    }

    private void showUsage(JCommander jcommander) {
        jcommander.usage();
        System.exit(0);
    }

    private void setStrategy(Integer format) {
        output =    format == MainCLIParameters.HTML ? new HTMLStrategy() :
                    format == MainCLIParameters.LATEX ? new LatexStategy() : new AsciiStategy();
    }
}