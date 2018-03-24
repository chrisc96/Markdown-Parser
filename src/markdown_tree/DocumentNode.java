package markdown_tree;

public class DocumentNode extends I_BlockNode {

    public DocumentNode(I_BlockNode parent) {
        super(parent);
    }

    @Override
    public String outputToHtml() {
        return null;
    }

    @Override
    public String outputToLaTeX() {
        return null;
    }

    @Override
    public String outputToASCII() {
        return null;
    }
}