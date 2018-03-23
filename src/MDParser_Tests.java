import org.junit.Assert;
import org.junit.Test;
import outputs.html.HTMLStrategy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public class MDParser_Tests {

    @Test
    public void Paragraph_Slicing_01() {
        String input = "Hello\n\nTest That\nShould\n\nReturn 3 paragraph blocks";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("Hello");
        expectedOutput.add("Test That\nShould");
        expectedOutput.add("Return 3 paragraph blocks");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_02() {
        String input = "1st\n\nParagraph";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("1st");
        expectedOutput.add("Paragraph");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_03() {
        String input = "1st\nParagraph\n\n2nd\nParagraph";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("1st\nParagraph");
        expectedOutput.add("2nd\nParagraph");

        Assert.assertEquals(expectedOutput, list);
    }

    @Test
    public void Paragraph_Slicing_04() {
        String input = "#### Hello\nHi";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("#### Hello");
        expectedOutput.add("Hi");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_05() {
        String input = "# Sushi\n## Maguro";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("# Sushi");
        expectedOutput.add("## Maguro");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_06() {
        String input = "# Sushi\n## Maguro\n## Hello";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("# Sushi");
        expectedOutput.add("## Maguro");
        expectedOutput.add("## Hello");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_07() {
        String input = "# Sushi\nHello\nHi\n## Maguro\n## Hello";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("# Sushi");
        expectedOutput.add("Hello\nHi");
        expectedOutput.add("## Maguro");
        expectedOutput.add("## Hello");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Paragraph_Slicing_08() {
        String input = "#Sushi\nSameLine\n## NotSame";

        MDParser parser = new MDParser(input, new HTMLStrategy());
        Set<String> list = parser.getParagraphBlocks(input).keySet();

        Set<String> expectedOutput = new LinkedHashSet<>();
        expectedOutput.add("#Sushi\nSameLine");
        expectedOutput.add("## NotSame");

        Assert.assertEquals(list, expectedOutput);
    }

    @Test
    public void Test_Heading_Regex_00() {
        List<String> inputs = new ArrayList<>();
        inputs.add("# ");
        inputs.add("## ");
        inputs.add("### ");
        inputs.add("#### ");
        inputs.add("##### ");
        inputs.add("###### ");

        for (String str: inputs) {
            Matcher m = MDParser.HEADING.matcher(str);
            Assert.assertTrue(m.find());
        }
    }

    @Test
    public void Test_Heading_Regex_01() {
        List<String> inputs = new ArrayList<>();
        inputs.add("# foo");
        inputs.add("## foo");
        inputs.add("### foo");
        inputs.add("#### foo");
        inputs.add("##### foo");
        inputs.add("###### foo");

        for (String str: inputs) {
            Matcher m = MDParser.HEADING.matcher(str);
            Assert.assertTrue(m.find());
        }
    }

    /**
     * Tests that any amount of leading and trailing blanks are ignored in the inline
     * content of the heading and that only 1-3 spaces are allowed before a '#'
     */
    @Test
    public void Test_Heading_Regex_02() {
        List<String> inputs = new ArrayList<>();
        inputs.add("#    foo   ");
        inputs.add(" ##     foo   ");
        inputs.add("  ### foo   ");
        inputs.add("   ####    foo  ");
        inputs.add("  #####  foo    ");
        inputs.add(" ######           foo        ");
        inputs.add("######       foo        ");

        for (String str: inputs) {
            Matcher m = MDParser.HEADING.matcher(str);
            Assert.assertTrue(m.find());
        }
    }

    @Test
    public void Test_Heading_Regex_03() {
        String input = "# foo *bar* \\*baz\\*";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertTrue(m.find());
    }

    @Test
    public void Test_Heading_Regex_04() {
        String input = "#                  foo                     ";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertTrue(m.find());
    }

    /**
     * Shouldn't match as there's 7 #
     */
    @Test
    public void Test_Heading_Regex_Bad_01() {
        String input = "#######";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertFalse(m.find());
    }

    /**
     * Should this return a match? Yes.
     * The second line gets matched against as the regex
     * looks for matches on a multi line basis.
     */
    @Test
    public void Test_Heading_Regex_Bad_02() {
        String input = "#5 bolt\n\n# hashtag";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertTrue(m.find());
    }

    /**
     * Test that more than 3 leading spaces before a heading doesn't work
     */
    @Test
    public void Test_Heading_Regex_Bad_03() {
        String input = "    # hello";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertFalse(m.find());
    }

    @Test
    public void Test_Heading_Regex_Bad_04() {
        String input = "foo\\n    # bar";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertFalse(m.find());
    }
}