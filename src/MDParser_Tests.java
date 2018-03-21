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
     * Should this return a match? I say no as it's on a new line
     * and headers only contain inline content. Therefore
     * we should not be looking for a match on a new line.
     */
    @Test
    public void Test_Heading_Regex_Bad_02() {
        String input = "#5 bolt\n\n# hashtag";
        Matcher m = MDParser.HEADING.matcher(input);
        Assert.assertFalse(m.find());
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