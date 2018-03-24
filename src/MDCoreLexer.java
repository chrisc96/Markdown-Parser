import lombok.Getter;
import markdown_tree.BlockNode;
import markdown_tree.I_BlockNode;
import outputs.OutputHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * Contains all the helper functions for the parser.
 * Holds all the regex for pattern matching and
 * Handles all the preprocessing for the markdown input
 * before it's parsed
 *
 */
public class MDCoreLexer {

    Path inputFile;
    String markdown;

    @Getter
    I_BlockNode markdownTree;
    OutputHandler outputHandler;

    // Patterns
    static Pattern PARAGRAPHBLOCK = Pattern.compile("\\n[\\n]+", Pattern.MULTILINE);
    static Pattern HEADING_PREFIX = Pattern.compile("^ {0,3}(#{1,6} )");
    static Pattern HEADING = Pattern.compile(HEADING_PREFIX + " *([^\\n]+?) *#* *(?:\\n+|$)|" + HEADING_PREFIX,  Pattern.MULTILINE);

    // Matches multi line text of anything until it finds either a heading (which it doesn't capture) or double line break.
    // Includes \n single occurences if found.
    static Pattern TEXT = Pattern.compile("^([^\\n]+(?:\\n?(?!" + HEADING + ")[^\\n]+)+)", Pattern.MULTILINE);

    // E.G Hello\nHello gives two matches. One match per line.
    static Pattern SINGLE_LINE_TEXT = Pattern.compile("^[^\\n{2}]+", Pattern.MULTILINE);

    // UNTESTED
    static Pattern ITALIC = Pattern.compile("^_([^\\s_](?:[^_]|__)+?[^\\s_])_\\b|^\\*((?:\\*\\*|[^*])+?)\\*(?!\\*)");
    static Pattern BOLD = Pattern.compile("^__([\\s\\S]+?)__(?!_)|^\\*\\*([\\s\\S]+?)\\*\\*(?!\\*)");
    static Pattern HARDBREAK = Pattern.compile("( {2})(\\n)", Pattern.MULTILINE);
    static Pattern SOFTBREAK = Pattern.compile("\n");
    static Pattern BLOCKQUOTE = Pattern.compile("(^> | {4})");

    /*----------------------*/
    /*                      */
    /*                      */
    /*     PREPROCESSING    */
    /*                      */
    /*                      */
    /*----------------------*/

    String preProcessMarkdown(String md) {
        // Standardize line endings:
        md = md.replaceAll("\\r\\n", "\n"); 	// Windows to Unix
        md = md.replaceAll("\\r", "\n");    	// Mac to Unix

        // Remove un-necessary line breaks (No need for 5, when 2 signifies a markdown line break)
        md = md.replaceAll("\\n\\n(?=\\n+)", "\n\n");

        return md;
    }


    /**
     * Slices the markdown text into paragraph blocks.
     * See MDParser_Tests for examples of use cases and how it should work.
     *
     * Essentially acts as a tokeniser so the parser only needs to parse what is necessary.
     * Makes the parsing/stringify process more efficient for initial overhead
     *
     *
     * @param markdownTree root node
     * @param markdownInput some markdown string
     * @return Map of each paragraph block's content to their equal BlockNode with outputHandler ParagraphBlock
     */
    public Map<String, BlockNode> getParagraphBlocks(I_BlockNode markdownTree, String markdownInput) {
        // String = contents of block node, BlockNode represents that structure
        Map<String, BlockNode> pBlocks = new LinkedHashMap<>();

        Matcher firstPattern = findFirstPattern(markdownInput, HEADING, PARAGRAPHBLOCK, TEXT);
        int endIdxOfMatch = 0;
        int endIdxOfLastMatch = 0;

        while (firstPattern != null) {
            endIdxOfMatch += firstPattern.end();

            // Checking if the first pattern we found was a heading
            if (firstPattern.pattern() == HEADING) {
                String headingToEdit = markdownInput.substring(endIdxOfLastMatch, endIdxOfMatch);
                // We don't want new lines inside Headings
                // We will remove leading/trailing whitespace from text inside heading when parsing
                String headingWithNoNewLines = headingToEdit.replaceAll("\n", "");

                pBlocks.put(headingWithNoNewLines, new BlockNode(markdownTree));
            }
            else if (firstPattern.pattern() == TEXT) {
                String textNoNewLines = markdownInput.substring(endIdxOfLastMatch, endIdxOfMatch);
                pBlocks.put(textNoNewLines, new BlockNode(markdownTree));
            }
            endIdxOfLastMatch = endIdxOfMatch;
            firstPattern = findFirstPattern(markdownInput.substring(endIdxOfLastMatch, markdownInput.length()), HEADING, PARAGRAPHBLOCK, TEXT);
        }
        // Last paragraph. Doesn't matter what outputHandler it is as there's only one line left.
        if (endIdxOfLastMatch != markdownInput.length()) {
            pBlocks.put(markdownInput.substring(endIdxOfLastMatch, markdownInput.length()),
                        new BlockNode(markdownTree));
        }
        return pBlocks;
    }

    /*
     *
     *  HELPER FUNCTIONS
     *
    */

    Matcher findFirstPattern(String para, Pattern... pattern) {
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
}
