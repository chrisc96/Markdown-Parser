package markdown_tree;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeadingNode extends BlockNode {

    public HeadingNode(I_BlockNode parent, int headLevel) {
        super(parent);
        this.blockType = headLevel;
    }

    public static int getLevelFrom(String str) {
        return (int) str.chars().filter(ch -> ch =='#').count();
    }
}
