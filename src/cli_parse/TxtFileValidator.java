package cli_parse;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TxtFileValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        String extension = getFileExtension(value);
        if (!extension.equalsIgnoreCase("txt")) {
            String message = String.format("[%s] is not a .txt file", value);
            throw new ParameterException(message);
        }

        Path pathToValidate = Paths.get(value);
        if (!exists(pathToValidate)) {
            String message = String.format("[%s] does not exist: ", value);
            throw new ParameterException(message);
        }
    }

    private boolean exists(Path p) {
        return (Files.exists(p, LinkOption.NOFOLLOW_LINKS));
    }

    private String getFileExtension(String value) {
        int i = value.lastIndexOf('.');
        if (i >= 0) {
            return value.substring(i+1);
        }
        return "";
    }
}
