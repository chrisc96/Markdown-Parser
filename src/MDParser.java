import markdown_tree.BlockNode;
import markdown_tree.DocumentNode;
import markdown_tree.HeadingNode;
import outputs.OutputStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDParser {

    String markdown;
    OutputStrategy type;

    // Useful Patterns

    // Core

    // 100% working
    static Pattern HEADING = Pattern.compile("^ {0,3}(#{1,6} ) *([^\\n]+?) *#* *(?:\\n+|$)|^ {0,3}(#{1,6} )");

    // UNTESTED
    static Pattern ITALIC = Pattern.compile("");
    static Pattern EMPHASIS = Pattern.compile("");
    static Pattern BOLD = Pattern.compile("");

    static Pattern HARDBREAK = Pattern.compile("( {2})(\\n)");
    static Pattern SOFTBREAK = Pattern.compile("\n");
    static Pattern PARAGRAPHBLOCK = Pattern.compile("\n\n");

    static Pattern BLOCKQUOTE = Pattern.compile("(^> | {4})");

    static Pattern TEXT = Pattern.compile("^[^\\n]+");


    public MDParser(Path inputFile, OutputStrategy output) {
        String s = convertPathToString(inputFile);
        this.markdown = preProcessMarkdown(s);
        this.type = output;
    }

    // Used by JUnit tests instead of reading test cases/acceptance tests
    public MDParser(String markdown, OutputStrategy output) {
        this.markdown = preProcessMarkdown(markdown);
        this.type = output;
    }

    private String convertPathToString(Path inputFile) {
        // May not be the best idea on large files as it will put whole String into memory. OK for now.
        try {
            return new String(Files.readAllBytes(inputFile));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void parse() throws MDParseException {
        Map<String, BlockNode> pBlocks = getParagraphBlocks(markdown);

        // Create tree structure, begin to parse each paragraph block
        DocumentNode markdownTree = new DocumentNode();

        for (String string : pBlocks.keySet()) {
            BlockNode pBlock = pBlocks.get(string);
            pBlock.setBlockType(BlockNode.ParagraphBlock);
            markdownTree.getBlockParagraphs().add(pBlock);

            Scanner sc = new Scanner(string);
            sc.useDelimiter("\t");
            parseParagraph(sc, pBlock);
        }
    }

    private void parseParagraph(Scanner sc, BlockNode pBlock) {
        if (sc.hasNext(HEADING)) {
            // Go through groupings, should be #### SOME INLINES
            for (int i = 1; i <= sc.match().groupCount(); i++) {
                if (sc.match().group(i).matches(HEADING.pattern())) {
                    parseHeading(pBlock, sc.match().group(i));
                }
                else {
                    // Should be an inline??
                }
                System.out.println("Group " + i + ": " + sc.match().group(i));
            }

            String str = sc.next();
        }
        // Default fall back case, parse it as text
        else if (sc.hasNext(TEXT)) {

        }
    }

    private void parseHeading(BlockNode block, String headingStr) {
        // Count how many #### there are, then pass that into the heading level
        BlockNode heading = new HeadingNode(HeadingNode.getLevelFrom(headingStr));
        block.getChildren().add(heading);
    }



    private Matcher firstPatternOf(String para, Pattern... pattern) {
        Integer lowestIndex = Integer.MAX_VALUE;
        Matcher firstPattern = null;
        for (Pattern p: pattern) {
            Matcher m = p.matcher(para);
            if (m.find() && lowestIndex > m.start()) {
                lowestIndex = m.start();
                firstPattern = m;
            }
        }
        return firstPattern;
    }

    /**
     * Goes through markdown input and locates \n\n which define paragraph blocks.
     * This defines where our tokeniser doesn't need to keep looking any further.
     * E.G
     * 1st\n
     * Paragraph\n
     * \n
     * 2nd\n
     * Paragraph
     *
     * Returns two Strings, 1. 1st\nParagraph\n\n
     *                      2. 2nd\nParagraph
     *
     * @param markdownInput some markdown input
     * @return Map of each paragraphs content to their equal BlockNode
     */
    public Map<String, BlockNode> getParagraphBlocks(String markdownInput) {
        Map<String, BlockNode> pBlocks = new LinkedHashMap<>();

        Matcher m = PARAGRAPHBLOCK.matcher(markdownInput);
        int lastIdxFound = 0;
        while (m.find()) {
            int idx = m.end();

            BlockNode block = new BlockNode();
            // idx - 2 removes \n\n at end of paragraph
            pBlocks.put(markdownInput.substring(lastIdxFound, idx - 2), block);
            lastIdxFound = idx;
        }
        // Last paragraph
        BlockNode block = new BlockNode();
        pBlocks.put(markdownInput.substring(lastIdxFound, markdownInput.length()), block);

        return pBlocks;
    }

    private String preProcessMarkdown(String md) {
        // Standardize line endings:
        md = md.replaceAll("\\r\\n", "\n "); 	// Windows to Unix
        md = md.replaceAll("\\r", "\n ");    	// Mac to Unix

        // Remove un-necessary line breaks (No need for 5, when 2 signifies a markdown line break)
        md = md.replaceAll("\\n\\n(?=\\n+)", "\n\n");

//        md = md.trim(); // Strip blank lines at the beginning and end of the document.

        return md;
    }
}