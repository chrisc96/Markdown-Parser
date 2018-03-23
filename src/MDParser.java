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

    static Pattern SINGLE_LINE_TEXT = Pattern.compile("^[^\\n\\n]+");

    static Pattern TEXT = Pattern.compile("^([^\\n]+(?:\\n?(?!" + HEADING + ")[^\\n]+)+)", Pattern.MULTILINE);


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
     * @param markdownInput some markdown input
     * @return Map of each paragraphs content to their equal ParagraphBlockNode
     */
    public Map<String, BlockNode> getParagraphBlocks(String markdownInput) {
        Map<String, BlockNode> pBlocks = new LinkedHashMap<>();

        Matcher firstPattern = firstPattern(markdownInput, HEADING, PARAGRAPHBLOCK, TEXT);
        int endIdxOfMatch = 0;
        int endIdxOfLastMatch = 0;

        while (firstPattern != null) {
            endIdxOfMatch += firstPattern.end();

            // Checking if the first pattern we found was a heading
            if (firstPattern.pattern() == HEADING) {
                String headingToEdit = markdownInput.substring(endIdxOfLastMatch, endIdxOfMatch);
                String headingWithNoNewLines = headingToEdit.replaceAll("\n", ""); // We don't want new lines inside

                pBlocks.put(headingWithNoNewLines, new BlockNode());
            }
            else if (firstPattern.pattern() == TEXT) {
                String textNoNewLines = markdownInput.substring(endIdxOfLastMatch, endIdxOfMatch);
                pBlocks.put(textNoNewLines, new BlockNode());
            }
            endIdxOfLastMatch = endIdxOfMatch;
            firstPattern = firstPattern(markdownInput.substring(endIdxOfLastMatch, markdownInput.length()), HEADING, PARAGRAPHBLOCK, TEXT);
        }
        // Last paragraph. Doesn't matter what type it is as there's only one line left.
        if (endIdxOfLastMatch != markdownInput.length()) {
            pBlocks.put(markdownInput.substring(endIdxOfLastMatch, markdownInput.length()),
                        new BlockNode());
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