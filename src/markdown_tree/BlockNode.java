package markdown_tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BlockNode implements I_BlockNode {

    public static final int ParagraphBlock = 0;
    public static final int Text = 1;
    public static final int Bold = 2;
    public static final int Italic = 3;

    @Getter @Setter
    int blockType;

    @Getter
    List<I_BlockNode> children = new ArrayList<>();

}