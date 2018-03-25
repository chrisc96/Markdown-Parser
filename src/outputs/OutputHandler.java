package outputs;

import lombok.Setter;
import markdown_tree.I_BlockNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public abstract class OutputHandler {

    @Setter
    public I_BlockNode root;

    @Setter
    Path outputFile;

    public abstract StringBuilder convert();
    public abstract void outputToFile();

    // Inner classes for conciseness
    public static class HTMLOutput extends OutputHandler{

        @Override
        public StringBuilder convert() {
            return root.outputToHtml();
        }

        @Override
        public void outputToFile() {
            StringBuilder html = convert();

            try {
                Files.write(outputFile, Collections.singletonList(html), StandardCharsets.UTF_8);
                System.out.println("HTML file output at: " + outputFile.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class LaTeXOutput extends OutputHandler {

        @Override
        public StringBuilder convert() {
            return root.outputToLaTeX();
        }

        @Override
        public void outputToFile() {
            StringBuilder latex = convert();
        }
    }

    public static class ASCII extends OutputHandler {

        @Override
        public StringBuilder convert() {
            return root.outputToASCII();
        }

        @Override
        public void outputToFile() {
            StringBuilder ascii = convert();
        }
    }
}