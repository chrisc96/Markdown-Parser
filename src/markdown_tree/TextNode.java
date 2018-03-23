package markdown_tree;

import lombok.Getter;

public class TextNode extends BlockNode {

    @Getter
    String contents;

    public TextNode(String contents) {
        this.blockType = BlockNode.Text;
        this.contents = contents;
    }
}
