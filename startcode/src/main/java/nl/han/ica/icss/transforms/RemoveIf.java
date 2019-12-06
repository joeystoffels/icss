package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static nl.han.ica.icss.ast.types.ExpressionType.BOOL;

public class RemoveIf implements Transform {

    private CopyOnWriteArrayList<ASTNode> nodes = new CopyOnWriteArrayList<>();

    @Override
    public void apply(AST ast) {
        setNodes(ast.root);
        nodes.forEach(this::evalIfClause);
    }

    private void setNodes(ASTNode node) {
        if (!node.getChildren().isEmpty()) {
            nodes.addAll(node.getChildren());
            node.getChildren().forEach(this::setNodes);
        }
    }

    private void evalIfClause(ASTNode node) {
        if (node instanceof IfClause) {
            IfClause ifClause = (IfClause) node;

            if (ifClause.conditionalExpression instanceof BoolLiteral) {
                BoolLiteral literal = (BoolLiteral) ((IfClause) node).conditionalExpression;
                if (literal.getValue().equals("false")) {
                    nodes.remove(node);
                    ifClause.body = new ArrayList<>();
                }
                ((VariableReference) ifClause.conditionalExpression).value = "";
            }

            if (ifClause.conditionalExpression instanceof VariableReference) {
                Optional<ASTNode> optional = getVariable((VariableReference) ((IfClause) node).conditionalExpression);
                if (optional.isPresent() && optional.get() instanceof VariableReference) {
                    VariableReference reference = (VariableReference) optional.get();
                    if (reference.value.equals("false")) {
                        nodes.remove(node);
                        ifClause.body = new ArrayList<>();
                    }
                }
                ((VariableReference) ifClause.conditionalExpression).value = "";
            }
        }
    }

    private Optional<ASTNode> getVariable(VariableReference variableReference) {
        return nodes.stream().filter(x -> x instanceof VariableReference)
                .filter(y -> (((VariableReference) y).name.equals(variableReference.name) &&
                        ((VariableReference) y).getExpressionType() == BOOL)).findFirst();
    }

}
