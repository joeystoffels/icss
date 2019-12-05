package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.Optional;

import static nl.han.ica.icss.ast.types.ExpressionType.BOOL;
import static nl.han.ica.icss.ast.types.ExpressionType.COLOR;
import static nl.han.ica.icss.ast.types.ExpressionType.PIXEL;
import static nl.han.ica.icss.ast.types.ExpressionType.SCALAR;
import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;

public class Checker {

    private ArrayList<ASTNode> nodes = new ArrayList<>();

    public void check(AST ast) {
        setNodes(ast.root);
        nodes.forEach(this::run);
    }

    private void setNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::setNodes);
        }
    }

    private void run(ASTNode node) {
        if (node instanceof VariableReference) this.checkVariable((VariableReference) node);
        if (node instanceof Operation) this.checkOperation((Operation) node);
//        if (node instanceof IfClause) this.checkIfClause((IfClause) node);
        if (node instanceof Declaration) this.checkDeclaration((Declaration) node);
    }

    // CH01
    private void checkVariable(VariableReference reference) {
        boolean notFound = nodes.stream().filter(x -> x instanceof VariableAssignment).
                noneMatch(x -> ((VariableAssignment) x).name.name.equals(reference.name));
        if (notFound) reference.setError("Variable not used: " + reference.getNodeLabel());
    }

    // CH02 && CH03
    private void checkOperation(Operation operation) {
        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            ExpressionType lhsExpressionType = getExpressionTypeOfExpression(operation.lhs);
            ExpressionType rhsExpressionType = getExpressionTypeOfExpression(operation.rhs);

            if (lhsExpressionType != rhsExpressionType) {
                operation.setError("Operands are not matching for operation! : " + operation + " -> " +
                        lhsExpressionType + " " + rhsExpressionType);
            }
        }

        if (operation instanceof MultiplyOperation &&
                operation.lhs.getExpressionType() != SCALAR && operation.rhs.getExpressionType() != SCALAR) {
            operation.setError("Multiply operand consists of one or two non-scalar values!");
        }

        if (getExpressionTypeOfExpression(operation.lhs) == COLOR ||
                getExpressionTypeOfExpression(operation.rhs) == COLOR) {
            operation.setError("Operation consists of one or more color values!");
        }
    }

    // CH04
    private void checkDeclaration(Declaration declaration) {
        if (declaration.expression instanceof Operation) {
            return; // check in checkOperations?
        }

        String propertyName = declaration.property.name;
        ExpressionType expressionType = getExpressionTypeOfExpression(declaration.expression);

        if (expressionType == PIXEL && (propertyName.equals("width") || propertyName.equals("height"))) return;
        if (expressionType == BOOL && (propertyName.equals("false") || propertyName.equals("true"))) return;
        if (expressionType == COLOR && (propertyName.equals("color") || propertyName.equals("background-color")))
            return;

        declaration.setError("Type of value does not match with property!");
    }

    // CH05
    private void checkIfClause(IfClause ifClause) {
        if (ifClause.getConditionalExpression().getExpressionType() != BOOL) {
            ifClause.setError("IfClause is not of type boolean!");
        }
    }

    // Also checks if undefined variable types are defined with a defined and returns that ExpressionType
    private ExpressionType getExpressionTypeOfExpression(Expression expression) {

        if (expression instanceof VariableReference && expression.getExpressionType() == UNDEFINED) {

            Optional<ASTNode> optional = nodes.stream().filter(x -> x instanceof VariableReference)
                    .filter(y -> (((VariableReference) y).name.equals(((VariableReference) expression).name) &&
                            ((VariableReference) y).getExpressionType() != UNDEFINED)).findFirst();

            if (optional.isPresent() && optional.get() instanceof VariableReference) {
                return ((VariableReference) optional.get()).getExpressionType();
            }
        }

        if (expression instanceof Operation) {
            return getExpressionTypeOfOperation((Operation) expression);
        }

        return expression.getExpressionType();
    }

    private ExpressionType getExpressionTypeOfOperation(Operation operation) {
        if (operation.lhs instanceof Operation) {
            return this.getExpressionTypeOfOperation((Operation) operation.lhs);
        }

        if (operation.rhs instanceof Operation) {
            return this.getExpressionTypeOfOperation((Operation) operation.rhs);
        }

        if (operation instanceof MultiplyOperation) {
            if (getExpressionTypeOfExpression(operation.lhs) == SCALAR) {
                return getExpressionTypeOfExpression(operation.rhs);
            } else {
                return getExpressionTypeOfExpression(operation.lhs);
            }
        }

        if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            return operation.lhs.getExpressionType() ==
                    operation.rhs.getExpressionType() ?
                    operation.lhs.getExpressionType() : UNDEFINED;
        }

        return UNDEFINED;
    }
}
