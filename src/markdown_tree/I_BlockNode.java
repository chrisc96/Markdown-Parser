package markdown_tree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class I_BlockNode {

    @Getter
    List<BlockNode> children = new ArrayList<>();

    public void addChildVia(BlockNode blockNode) {
        getChildren().add(blockNode);
    }

    /**
     * Created purely for semantics
     * @param blockNode
     */
    public void addChild(BlockNode blockNode) {
        addChildVia(blockNode);
    }

}
