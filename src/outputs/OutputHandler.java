package outputs;

import lombok.Setter;
import markdown_tree.I_BlockNode;

public abstract class OutputHandler {

    @Setter
    public I_BlockNode root;

    public abstract String convert();

    public abstract void outputToFile();

    // Inner classes for conciseness
    public static class HTMLOutput extends OutputHandler{

        @Override
        public String convert() {
            return root.outputToASCII();
        }

        @Override
        public void outputToFile() {

        }
    }

    public static class LaTeXOutput extends OutputHandler {

        @Override
        public String convert() {
            return root.outputToLaTeX();
        }

        @Override
        public void outputToFile() {

        }
    }

    public static class ASCII extends OutputHandler {

        @Override
        public String convert() {
            return root.outputToASCII();
        }

        @Override
        public void outputToFile() {

        }
    }
}