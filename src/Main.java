import cli_parse.MainCLIParameters;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import outputs.OutputHandler;

import java.nio.file.Path;

import static cli_parse.OutputFileValidator.getOutputFileFormat;

public class Main {

    private final MainCLIParameters mainArgs = new MainCLIParameters();

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

        // Gets values for input and output files passed in via command line
        Path inputFile = mainArgs.getInputFile();
        Path outputFile = mainArgs.getOutputFile();
        OutputHandler outputFormat = getStrategy(getOutputFileFormat(outputFile));
        outputFormat.setOutputFile(outputFile);

        MDParser parser = new MDParser(inputFile, outputFormat);
        parser.parse();
    }

    private void showUsage(JCommander jcommander) {
        jcommander.usage();
        System.exit(0);
    }

    private OutputHandler getStrategy(Integer format) {
        return  format == MainCLIParameters.HTML ? new OutputHandler.HTMLOutput() :
                format == MainCLIParameters.LATEX ? new OutputHandler.LaTeXOutput() : new OutputHandler.ASCII();
    }
}