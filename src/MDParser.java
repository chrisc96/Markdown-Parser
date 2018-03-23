import cli_parse.FilePathConverter;
import markdown_tree.*;
import outputs.OutputStrategy;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class MDParser extends MDCoreLexer {


    MDParser(Path inputFile, OutputStrategy output) {
        String s = new FilePathConverter().convert(inputFile);
        this.markdown = preProcessMarkdown(s);
        this.type = output;
    }

    // Used by JUnit tests instead of reading test cases/acceptance tests
    MDParser(String markdown, OutputStrategy output) {
        this.markdown = preProcessMarkdown(markdown);
        this.type = output;
    }

    public void parse() {
        // Create tree structure, begin to parse each paragraph block
        I_BlockNode markdownTree = new DocumentNode();

        Map<String, BlockNode> pBlocks = getParagraphBlocks(markdown);
        for (String string : pBlocks.keySet()) {
            Scanner sc = new Scanner(string);
            sc.useDelimiter("\t"); // May need to change
            parseParagraph(sc, markdownTree);
        }
    }

    private void parseParagraph(Scanner sc, I_BlockNode parent) {
        BlockNode paragraphBlock = new BlockNode(BlockNode.ParagraphBlock);
        parent.addChild(paragraphBlock);

        // Parse inner content
        if (sc.hasNext(HEADING)) {
            parseHeading(paragraphBlock, sc);
        }
        // Default fall back case, parse it as text
        else {
            parseText(paragraphBlock, sc);
        }
    }

    private void parseHeading(BlockNode parent, Scanner sc) {
        // Go through groupings matched by regex
        MatchResult headingMatchResult = sc.match();
        int headingLevel = HeadingNode.getLevelFrom(headingMatchResult.group(1));
        BlockNode heading = new HeadingNode(headingLevel);
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
    private void parseInline(BlockNode parent, Scanner sc, String group) {
        if (sc.hasNext(BOLD) || sc.hasNext(ITALIC)) {
            Matcher m = findFirstPattern(group, BOLD, ITALIC);
            if (m.pattern() == BOLD) {
                BlockNode bold = new BlockNode(BlockNode.Bold);
                parent.addChild(bold);

                parseInline(parent, sc, m.group());
            }
            else {
                BlockNode italic = new BlockNode(BlockNode.Italic);
                parent.addChild(italic);

                parseInline(parent, sc, m.group());
            }
        }
        // Must be text
        else {
            parseText(parent, sc);
        }
    }

    private void parseText(BlockNode parent, Scanner sc) {
        Matcher m = TEXT.matcher(sc.match().group());
        if (m.find()) {
            BlockNode text = new TextNode(m.group());
            parent.addChild(text);
        }
    }
}