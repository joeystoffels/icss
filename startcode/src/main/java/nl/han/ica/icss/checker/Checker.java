package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.Optional;

import static nl.han.ica.icss.ast.types.ExpressionType.COLOR;
import static nl.han.ica.icss.ast.types.ExpressionType.SCALAR;
import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;

public class Checker {

    private ArrayList<ASTNode> nodes = new ArrayList<>();

    public void check(AST ast) {
        findAllNodes(ast.root);
        this.checkVars();
        this.checkOperations();
    }

    private void findAllNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::findAllNodes);
        }
    }

    private void checkVars() {
        for (ASTNode node : nodes) {

            // CH01
            if (node instanceof VariableReference) {
                boolean notFound = nodes.stream().filter(x -> x instanceof VariableAssignment).
                        noneMatch(x -> ((VariableAssignment) x).name.name.equals(((VariableReference) node).name));
                if (notFound) node.setError("Variable not used: " + node.getNodeLabel());
            }
        }
    }

    private void checkOperations() {
        for (ASTNode node : nodes) {

            // CH03
            if (node instanceof Operation) {
                Operation operation = (Operation) node;

                if (getExpressionTypeOfExpression(operation.lhs) == COLOR || getExpressionTypeOfExpression(operation.rhs) == COLOR) {
                    node.setError("Operation consists of one or more color values!");
                }
            }


            // CH02
            if (node instanceof MultiplyOperation) {
                Operation operation = (Operation) node;
                if (operation.lhs.getExpressionType() != SCALAR && operation.rhs.getExpressionType() != SCALAR) {
                    node.setError("Multiply operand consists of one or two non-scalar values!");
                }
            }

            // CH02
            if (node instanceof AddOperation || node instanceof SubtractOperation) {
                Operation operation = (Operation) node;
                if (operation.lhs.getExpressionType() != operation.rhs.getExpressionType() &&
                        (getExpressionTypeOfExpression(operation.lhs) != getExpressionTypeOfExpression(operation.rhs))) {
                    node.setError("Operands are not matching for operation!");
                }
            }
        }
    }

    private ExpressionType getExpressionTypeOfExpression(Expression expression) {
        if (expression.getExpressionType() != UNDEFINED && expression instanceof VariableReference) {

            Optional<ASTNode> optional = nodes.stream().filter(x -> x instanceof VariableReference)
                    .filter(y -> (((VariableReference) y).name.equals(((VariableReference) expression).name) &&
                            ((VariableReference) y).getExpressionType() != UNDEFINED)).findFirst();

            if (optional.isPresent() && optional.get() instanceof VariableReference) {
                return ((VariableReference) optional.get()).getExpressionType();
            }
        }

        return expression.getExpressionType();
    }
}
