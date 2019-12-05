package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.concurrent.CopyOnWriteArrayList;

public class EvalExpressions implements Transform {

    private CopyOnWriteArrayList<ASTNode> nodes = new CopyOnWriteArrayList<>();
    private AST ast;

    @Override
    public void apply(AST ast) {
        this.ast = ast;
        setNodes(ast.root);
        nodes.forEach(this::replaceOperation);
    }

    private void setNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::setNodes);
        }
    }

    private void replaceOperation(ASTNode node) {
        if (node instanceof Declaration && ((Declaration) node).expression instanceof Operation) {
            Literal literal = new PixelLiteral(evalExpressions((Operation) ((Declaration) node).expression));
            System.out.println(nodes.remove(node));
            System.out.println(nodes.remove((((Declaration) node).expression)));
            System.out.println(literal);

            ((Declaration) node).expression = literal;
        } else {
            return;
        }
        System.out.println(ast.root.body);
        System.out.println(nodes);
    }

    private int evalExpressions(Operation operation) {

        int lhsValue;
        int rhsValue;

        if (operation.rhs instanceof Operation) {
            rhsValue = this.evalExpressions((Operation) operation.rhs);
        } else {
            rhsValue = Integer.parseInt(operation.rhs.getValue());
        }

        if (operation.lhs instanceof Operation) {
            lhsValue = this.evalExpressions((Operation) operation.lhs);
        } else {
            lhsValue = Integer.parseInt(operation.lhs.getValue());
        }

        if (operation instanceof AddOperation) {
            return lhsValue + rhsValue;
        }

        if (operation instanceof SubtractOperation) {
            return lhsValue - rhsValue;
        }

        if (operation instanceof MultiplyOperation) {
            return lhsValue * rhsValue;
        }

        return 0;
    }
}
