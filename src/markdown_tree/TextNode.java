package markdown_tree;

import lombok.Getter;

public class TextNode extends BlockNode {

    @Getter
    String contents;

    public TextNode(I_BlockNode parent, String contents) {
        super(parent);
        this.blockType = BlockNode.Text;
        this.contents = contents;
    }
}
