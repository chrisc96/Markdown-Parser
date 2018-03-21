package cli_parse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Getter;

import java.nio.file.Path;

@Parameters(separators = "=")
public class MainCLIParameters {

    // Constants
    public static final int HTML = 0;
    public static final int LATEX = 1;
    public static final int ASCII = 2;

    @Parameter( names = {"--help", "-h"},
                help = true,
                description = "Displays help information")
    @Getter boolean help;

    @Parameter( names = {"--inputFile", "-i"},
                required = true,
                validateWith = TxtFileValidator.class,
                converter = FilePathConverter.class,
                description = "Markdown file in a .txt to be converted.")
    @Getter Path inputFile;

    @Parameter( names = {"--format", "-f"},
                validateWith = OutputStrategyValidator.class,
                description = "Format for markdown to be exported to (HTML = 0, LaTeX = 1, ASCII = 2")
    @Getter int format = HTML;
}
