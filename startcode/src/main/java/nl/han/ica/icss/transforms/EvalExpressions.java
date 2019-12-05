package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

public class EvalExpressions implements Transform {

    @Override
    public void apply(AST ast) {

        for (ASTNode node : ast.root.getChildren()) {
            if (node instanceof Operation) {
                if (node instanceof AddOperation) {
                    int result = Integer.parseInt(((Operation) node).lhs.getValue()) + Integer.parseInt(((Operation) node).rhs.getValue());
                    node = new ScalarLiteral(result);
                }

                if (node instanceof SubtractOperation) {
                    int result = Integer.parseInt(((Operation) node).lhs.getValue()) - Integer.parseInt(((Operation) node).rhs.getValue());
                    node = new ScalarLiteral(result);
                }

                if (node instanceof MultiplyOperation) {
                    int result = Integer.parseInt(((Operation) node).lhs.getValue()) * Integer.parseInt(((Operation) node).rhs.getValue());
                    node = new ScalarLiteral(result);
                }
            }
        }
    }
}
