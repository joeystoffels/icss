package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Expression;

import java.util.ArrayList;

public class Generator {

	private ArrayList<ASTNode> nodes = new ArrayList<>();

    public String generate(AST ast) {
        findAllNodes(ast.root);
        nodes.stream().filter(x -> x instanceof Expression).forEach(x -> printCss((Expression) x));
        return "SUCCESS";
    }

    private void findAllNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::findAllNodes);
        }
    }

    private void printCss(Expression node) {
        System.out.println(node.getCssString());
//        node.getChildren().stream().filter(x -> x instanceof Expression).forEach(x -> printCss((Expression) x));
    }
}
