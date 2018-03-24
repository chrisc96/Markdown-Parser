package markdown_tree;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeadingNode extends BlockNode {

    @Getter
    int headingLevel;

    public HeadingNode(I_BlockNode parent, int headLevel) {
        super(parent);
        this.headingLevel = headLevel;
    }

    public static int getLevelFrom(String str) {
        return (int) str.chars().filter(ch -> ch =='#').count();
    }

    @Override
    public StringBuilder outputToHtml() {
        StringBuilder block = new StringBuilder("");

        block.append("<H").append(headingLevel).append(">");
        for (BlockNode node: children) {
            block.append(node.outputToHtml());
        }
        block.append("</H").append(headingLevel).append(">");

        return block;
    }

    @Override
    public StringBuilder outputToLaTeX() {
        return null;
    }

    @Override
    public StringBuilder outputToASCII() {
        return null;
    }

}
