package markdown_tree;

import lombok.Getter;

public class TextNode extends BlockNode {

    @Getter
    String contents;

    public TextNode(I_BlockNode parent, String contents) {
        super(parent);
        this.contents = contents;
    }

    @Override
    public StringBuilder outputToHtml() {
        StringBuilder block = new StringBuilder("");
        if (parent instanceof HeadingNode) {
            block.append(contents);
        }
        else {
            block.append("<p>").append(contents).append("</p>");
        }

        for (BlockNode node: children) {
            block.append(node.outputToHtml());
        }
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
