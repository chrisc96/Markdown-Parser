package markdown_tree;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeadingNode extends BlockNode {

    public static final int H1 = 1;
    public static final int H2 = 2;
    public static final int H3 = 3;
    public static final int H4 = 4;
    public static final int H5 = 5;
    public static final int H6 = 6;

    @Getter
    int headingNum;

    public HeadingNode(int headLevel) {
        this.headingNum = headLevel;
    }

    public static int getLevelFrom(String str) {
        return (int) str.chars().filter(ch -> ch =='#').count();
    }
}
