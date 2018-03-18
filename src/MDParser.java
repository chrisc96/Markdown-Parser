import lombok.NonNull;
import outputs.OutputStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class MDParser {

    Path src;
    OutputStrategy type;

    // Useful Patterns
    static Pattern TEXT = Pattern.compile("^[a-zA-Z ]*$");

    // Core
    static Pattern HEADER = Pattern.compile("(#{1,6} )");
    static Pattern ITALIC = Pattern.compile("");
    static Pattern EMPHASIS = Pattern.compile("");

    static Pattern BLOCKQUOTE = Pattern.compile("(^> | {4})");


    public MDParser(Path inputFile, OutputStrategy output) {
        this.src = inputFile;
        this.type = output;
    }

    public void parse() throws MDParseException, IOException {
        // May not be the best idea on large files as it will put whole String into memory. OK for now.
        String markdown = preProcessMarkdown(new String(Files.readAllBytes(src)));

    }

    private String preProcessMarkdown(String md) {
        // Standardize line endings:
        md = md.replaceAll("\\r\\n", "\n"); 	// Windows to Unix
        md = md.replaceAll("\\r", "\n");    	// Mac to Unix

        md = md.replaceAll("\t", "    "); // De-tabify (4 spaces)

        // Remove un-necessary line breaks (No need for 5, when 2 signifies a markdown line break)
        md = md.replaceAll("\\n\\n(?=\\n+)", "\\n\\n");

        return md;
    }
}