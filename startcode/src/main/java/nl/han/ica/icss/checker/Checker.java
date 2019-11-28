package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    private ArrayList<ASTNode> nodes = new ArrayList<>();

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        findAllNodes(ast.root);
        nodes.stream().filter(x -> x instanceof IChecker).forEach(x -> ((IChecker) x).check());
    }

    private void findAllNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::findAllNodes);
        }
    }


}
