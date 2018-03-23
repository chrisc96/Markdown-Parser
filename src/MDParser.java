import markdown_tree.*;
import outputs.OutputStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDParser {

    String markdown;
    OutputStrategy type;

    // Useful Patterns

    // 100% working
    static Pattern HEADING = Pattern.compile("^ {0,3}(#{1,6} ) *([^\\n]+?) *#* *(?:\\n+|$)|^ {0,3}(#{1,6} )", Pattern.MULTILINE);

    // UNTESTED
    static Pattern ITALIC = Pattern.compile("^_([^\\s_](?:[^_]|__)+?[^\\s_])_\\b|^\\*((?:\\*\\*|[^*])+?)\\*(?!\\*)");
    static Pattern BOLD = Pattern.compile("^__([\\s\\S]+?)__(?!_)|^\\*\\*([\\s\\S]+?)\\*\\*(?!\\*)");

    static Pattern HARDBREAK = Pattern.compile("( {2})(\\n)", Pattern.MULTILINE);
    static Pattern SOFTBREAK = Pattern.compile("\n");
    static Pattern PARAGRAPHBLOCK = Pattern.compile("\\n[\\n]+", Pattern.MULTILINE);

    static Pattern BLOCKQUOTE = Pattern.compile("(^> | {4})");

    static Pattern TEXT = Pattern.compile("^[^\\n]+", Pattern.MULTILINE);


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

    public void parse() {
        Map<String, BlockNode> pBlocks = getParagraphBlocks(markdown);

        // Create tree structure, begin to parse each paragraph block
        I_BlockNode markdownTree = new DocumentNode();

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
            Matcher m = firstPattern(group, BOLD, ITALIC);
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

    private Matcher firstPattern(String para, Pattern... pattern) {
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
     * Goes through markdown input and returns paragraphBlocks.
     * Essentially acts as a lexer.
     * E.G
     * 1st\n
     * Paragraph\n
     * \n
     * 2nd\n
     * Paragraph
     *
     * Returns two Strings, 1. 1st\nParagraph
     *                      2. 2nd\nParagraph
     *
     * E.G 2
     * # HELLO\n
     * Hi
     *
     * Returns two Strings  1. # Hello
     *                      2. Hi
     *
     * @param markdownInput some markdown input
     * @return Map of each paragraphs content to their equal BlockNode
     */
    public Map<String, BlockNode> getParagraphBlocks(String markdownInput) {
        Map<String, BlockNode> pBlocks = new LinkedHashMap<>();

        // When headings finish, they are their own paragraph block
        // so we need to combine this into our match

//        Pattern paragraphBlockComplete = Pattern.compile(PARAGRAPHBLOCK + "|" + HEADING, Pattern.MULTILINE);
//        Matcher m = paragraphBlockComplete.matcher(markdownInput);


        Matcher firstPattern = firstPattern(markdownInput, HEADING, PARAGRAPHBLOCK, TEXT);
        int idx = 0;
        int lastIdxFound = 0;

        while (firstPattern != null) {
            idx += firstPattern.end();
            // Checking the first pattern we found was a heading
            if (firstPattern.pattern() == HEADING) {
                BlockNode block = new BlockNode();
                String headingToEdit = markdownInput.substring(lastIdxFound, idx);
                String headingWithNoNewLines = headingToEdit.replaceAll("\n", "");

                pBlocks.put(headingWithNoNewLines, block);
            }
            else if (firstPattern.pattern() == PARAGRAPHBLOCK) {

            }
            else if (firstPattern.pattern() == TEXT) {
                BlockNode block = new BlockNode();
                String textNoNewLines = markdownInput.substring(lastIdxFound, idx);
                pBlocks.put(textNoNewLines, block);
            }
            lastIdxFound = idx;
            firstPattern = firstPattern(markdownInput.substring(lastIdxFound, markdownInput.length()), HEADING, PARAGRAPHBLOCK, TEXT);

//
//            Matcher heading = HEADING.matcher(m.group());
//
//            if (heading.find()) {
//                BlockNode block = new BlockNode();
//                String headingToEdit = markdownInput.substring(lastIdxFound, idx);
//                String headingWithNoNewLines = headingToEdit.replaceAll("\n", "");
//
//                pBlocks.put(headingWithNoNewLines, block);
//                lastIdxFound = idx;
//            }
//            // Must be text
//            else {
//                BlockNode block = new BlockNode();
//                Matcher pblock = PARAGRAPHBLOCK.matcher(m.group());
//                if (pblock.find()) {
//                    System.out.println("HERE");
//                }
//                String textNoNewLines = markdownInput.substring(lastIdxFound, idx);
//                pBlocks.put(textNoNewLines, block);
//                lastIdxFound = idx;
//            }
            // heading.reset();
        }
        // Last paragraph
        if (lastIdxFound != markdownInput.length()) {
            BlockNode block = new BlockNode();
            pBlocks.put(markdownInput.substring(lastIdxFound, markdownInput.length()), block);
        }
        return pBlocks;
    }

    private String preProcessMarkdown(String md) {
        // Standardize line endings:
        md = md.replaceAll("\\r\\n", "\n"); 	// Windows to Unix
        md = md.replaceAll("\\r", "\n");    	// Mac to Unix

        // Remove un-necessary line breaks (No need for 5, when 2 signifies a markdown line break)
        md = md.replaceAll("\\n\\n(?=\\n+)", "\n\n");

//        md = md.trim(); // Strip blank lines at the beginning and end of the document.

        return md;
    }
}