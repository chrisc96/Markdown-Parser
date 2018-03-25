package markdown_tree;

/**
 * Represents the root node of the markdown tree
 */
public class DocumentNode extends I_BlockNode {

    public DocumentNode(I_BlockNode parent) {
        super(parent);
    }

    @Override
    public StringBuilder outputToHtml() {
        StringBuilder string = new StringBuilder(
            "<!DOCTYPE html>\n<html>\n<head>\n<title>Markdown2HTML</title>\n</head>\n\n<body>\n");

        for (BlockNode node: children) {
            string.append(node.outputToHtml());
        }

        string.append("</body>\n\n</html>");
        return string;
    }

    @Override
    public StringBuilder outputToLaTeX() {
        return null;
    }

    @Override
    public StringBuilder outputToASCII() {
        return null;
    }
}