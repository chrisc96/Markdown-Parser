package markdown_tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends DocumentNode {

    public static final int ParagraphBlock = 0;
    // HEADING 1-6 NEED TO HAVE VALUE 1-6 UNLESS YOU WANT TO BOILERPLATE CLASSES...
    public static final int H1 = 1;
    public static final int H2 = 2;
    public static final int H3 = 3;
    public static final int H4 = 4;
    public static final int H5 = 5;
    public static final int H6 = 6;

    public static final int Text = 7;
    public static final int Bold = 8;
    public static final int Italic = 9;

    @Getter @Setter
    int blockType;

    public BlockNode(int type) {
        this.blockType = type;
    }
    public BlockNode() {}

    public boolean isHeading() {
        return  blockType == H1 ||
                blockType == H2 ||
                blockType == H3 ||
                blockType == H4 ||
                blockType == H5 ||
                blockType == H6;
    }
}