import cli_parse.FilePathConverter;
import markdown_tree.*;
import outputs.OutputHandler;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 * Converts the markdown document structure into a Parse tree
 * and calls an OutputHandler to output to the desired format/location
 */
public class MDParser extends MDCoreLexer {

    MDParser(Path inputFile, OutputHandler output) {
        this.inputFile = inputFile;

        // Convert contents of file to String
        String markdown = new FilePathConverter().convert(inputFile);
        this.markdown = preProcessMarkdown(markdown);
        this.outputHandler = output;
    }

    // Used by JUnit tests instead of reading test cases/acceptance tests
    MDParser(String markdown, OutputHandler output) {
        this.markdown = preProcessMarkdown(markdown);
        this.outputHandler = output;
    }

    public void parse() {
        // Create tree structure, begin to parse each paragraph block
        markdownTree = new DocumentNode(null);
        outputHandler.setRoot(markdownTree);

        Map<String, BlockNode> pBlocks = getParagraphBlocks(markdownTree, markdown);
        for (String string : pBlocks.keySet()) {
            Scanner sc = new Scanner(string);
            sc.useDelimiter("\t"); // May need to change
            parseParagraph(markdownTree, sc);
        }

        outputHandler.outputToFile();
    }

    private void parseParagraph(I_BlockNode parent, Scanner sc) {
        BlockNode paragraphBlock = new ParagraphBlockNode(parent);
        parent.addChild(paragraphBlock);

        // Parse inner content
        if (sc.hasNext(HEADING)) {
            parseHeading(paragraphBlock, sc);
        }
        // Default fall back case, parse it as text
        else if (sc.hasNext(TEXT)) {
            parseText(paragraphBlock, sc, sc.match().group());
        }
    }

    private void parseHeading(I_BlockNode parent, Scanner sc) {
        // Go through groupings matched by regex
        MatchResult headingMatchResult = sc.match();
        int headingLevel = HeadingNode.getLevelFrom(headingMatchResult.group());
        BlockNode heading = new HeadingNode(parent, headingLevel);
        parent.addChild(heading);

        if (headingMatchResult.group(2) != null) {
            parseInline(heading, sc, sc.match().group(2).trim());
        }
    }

    /**
     * Sub parser for inline content
     * Recognises text, italics, bold, inline codespan
     * @param parent
     * @param sc
     * @param group
     */
    private void parseInline(I_BlockNode parent, Scanner sc, String group) {
        if (sc.hasNext(BOLD) || sc.hasNext(ITALIC)) {
            Matcher m = findFirstPattern(group, BOLD, ITALIC);
            if (m.pattern() == BOLD) {
                BlockNode bold = new BoldNode(parent);
                parent.addChild(bold);

                parseInline(parent, sc, m.group());
            }
            else {
                BlockNode italic = new ItalicNode(parent);
                parent.addChild(italic);

                parseInline(parent, sc, m.group());
            }
        }
        // Must be text
        else {
            parseText(parent, sc, group);
        }
    }

    private void parseText(I_BlockNode parent, Scanner sc, String group) {
        Matcher m = TEXT.matcher(group);
        if (m.find()) {
            BlockNode text = new TextNode(parent, group);
            parent.addChild(text);
        }
    }
}