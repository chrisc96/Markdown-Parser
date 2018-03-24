package cli_parse;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static cli_parse.TxtFileValidator.getFileExtension;

public class OutputFileValidator implements IParameterValidator {

    private static final String HTML_EXT = "html";
    private static final String LATEX_EXT = "tex";
    private static final String ASCII_EXT = "ascii";

    @Override
    public void validate(String s, String value) throws ParameterException {
        // Validate if extension is valid
        String extension = getFileExtension(value);
        if (!extension.equalsIgnoreCase(HTML_EXT) &&
            !extension.equalsIgnoreCase(LATEX_EXT) &&
            !extension.equalsIgnoreCase(ASCII_EXT)) {

            String message = String.format("[%s] is not a correct type of output file", value);
            throw new ParameterException(message);
        }

        // Validate if directory is valid
        int val = value.lastIndexOf("/");
        if (val == -1) {
            // Unix Systems
            val = value.lastIndexOf("\\");
        }
        String dirPath = value.substring(0, val);
        if (!Files.exists(Paths.get(dirPath))) {
            throw new ParameterException("Directory doesn't exist");
        }
    }

    public static int getOutputFileFormat(Path file) {
        if (file == null) return 0;

        String extension = getFileExtension(file.toString());
        if (extension.equalsIgnoreCase(HTML_EXT))       return 0;
        else if (extension.equalsIgnoreCase(LATEX_EXT)) return 1;
        else if (extension.equalsIgnoreCase(ASCII_EXT)) return 2;

        return 0;
    }

}
