package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;

public class EvalExpressions implements Transform {

    private CopyOnWriteArrayList<ASTNode> nodes = new CopyOnWriteArrayList<>();

    @Override
    public void apply(AST ast) {
        setNodes(ast.root);
        nodes.forEach(this::replaceOperation);
        ast.root.body.removeAll(nodes.stream().filter(x -> x instanceof VariableAssignment).collect(Collectors.toList()));
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
            nodes.remove(node);
            nodes.remove((((Declaration) node).expression));
            ((Declaration) node).expression = literal;
        }
    }

    private int evalExpressions(Operation operation) {
        int lhsValue;
        int rhsValue;

        if (operation.rhs instanceof Operation) {
            rhsValue = this.evalExpressions((Operation) operation.rhs);
        } else if (operation.rhs instanceof VariableReference) {
            rhsValue = Integer.parseInt(getValueOfVariableReference(operation.rhs));
        } else {
            rhsValue = Integer.parseInt(operation.rhs.getValue());
        }

        if (operation.lhs instanceof Operation) {
            lhsValue = this.evalExpressions((Operation) operation.lhs);
        } else if (operation.lhs instanceof VariableReference) {
            lhsValue = Integer.parseInt(getValueOfVariableReference(operation.lhs));
        } else {
            lhsValue = Integer.parseInt(operation.lhs.getValue());
        }

        if (operation instanceof AddOperation) return lhsValue + rhsValue;
        if (operation instanceof SubtractOperation) return lhsValue - rhsValue;
        if (operation instanceof MultiplyOperation) return lhsValue * rhsValue;

        return 0;
    }

    private String getValueOfVariableReference(Expression expression) {
        if (expression instanceof VariableReference && expression.getExpressionType() == UNDEFINED) {

            Optional<ASTNode> optional = nodes.stream().filter(x -> x instanceof VariableReference)
                    .filter(y -> (((VariableReference) y).name.equals(((VariableReference) expression).name) &&
                            ((VariableReference) y).getExpressionType() != UNDEFINED)).findFirst();

            if (optional.isPresent() && optional.get() instanceof VariableReference) {
                return ((VariableReference) optional.get()).getValue();
            }
        }
        return null;
    }


}
