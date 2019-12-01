package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;

public class Checker {

    private ArrayList<ASTNode> nodes = new ArrayList<>();

    public void check(AST ast) {
        findAllNodes(ast.root);
        nodes.stream().filter(x -> x instanceof IChecker).forEach(x -> ((IChecker) x).check());
        this.checkVars();
        this.checkOperands();
    }

    private void findAllNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::findAllNodes);
        }
    }

    private void checkVars() {
        for (ASTNode node : nodes) {
            if (node instanceof VariableReference) {

                boolean notFound = nodes.stream().filter(x -> x instanceof VariableAssignment).
                        noneMatch(x -> ((VariableAssignment) x).name.name.equals(((VariableReference) node).name));

                if (notFound) {
                    node.setError("Variable not used: " + node.getNodeLabel());
                }
            }
        }
    }

    private void checkOperands() {
        for (ASTNode node : nodes) {
            if (node instanceof AddOperation || node instanceof SubtractOperation) {
                Operation operation = (Operation) node;
                if (!(operation.lhs.getExpressionType().equals((operation.rhs.getExpressionType())))) {
                    if (operation.lhs instanceof VariableReference && operation.lhs.getExpressionType().equals(ExpressionType.UNDEFINED)) {
                        if (nodes.stream().filter(x -> x instanceof VariableReference)
                                .noneMatch(y -> ((VariableReference) y).name.equals(((VariableReference) operation.lhs).name) &&
                                        !((VariableReference) y).getExpressionType().equals(ExpressionType.UNDEFINED))) {
                            node.setError("Operands are not matching for operation: " +
                                    node.getNodeLabel() + " lhs: " +
                                    ((Operation) node).lhs.getExpressionType() +
                                    " rhs: " + ((Operation) node).rhs.getExpressionType());
                        }
                    }
                }
            }
        }
    }

}
