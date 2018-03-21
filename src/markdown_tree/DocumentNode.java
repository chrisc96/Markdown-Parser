package markdown_tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DocumentNode implements I_BlockNode {

    @Getter
    List<BlockNode> blockParagraphs = new ArrayList<>();
}