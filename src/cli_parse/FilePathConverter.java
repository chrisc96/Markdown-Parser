package cli_parse;

import com.beust.jcommander.IStringConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathConverter implements IStringConverter<Path>{
    @Override
    public Path convert(String s) {
        return Paths.get(s);
    }

    public String convert(Path s) {
        try {
            return new String(Files.readAllBytes(s));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}