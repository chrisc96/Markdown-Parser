package markdown_tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class I_BlockNode {

    @Getter
    List<BlockNode> children = new ArrayList<>();

    private void addChildVia(BlockNode blockNode) {
        getChildren().add(blockNode);
    }

    /**
     * Created purely for semantics of code description.
     * Doesn't do anything differently to addChildVia
     * @param blockNode node to add to children of this node
     */
    public void addChild(BlockNode blockNode) {
        addChildVia(blockNode);
    }
}
