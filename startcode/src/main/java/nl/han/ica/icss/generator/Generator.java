package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylerule;

public class Generator {

    private StringBuilder cssString = new StringBuilder();

    public String generate(AST ast) {
        cssString.append("/* Generated from ICSS, do not edit */\n\n");
        ast.root.getChildren().forEach(this::printNodes);
        return cssString.toString();
    }

    private void printNodes(ASTNode node) {
        cssString.append(node.getCssString());

        if (node instanceof Stylerule) {
            ((Stylerule) node).selectors.forEach(x -> cssString.append(x.getSelector()));
            cssString.append(" {");
        }

        node.getChildren().forEach(this::printNodes);

        if (node instanceof Stylerule) {
            cssString.append("\n}\n");
        }
    }

}
