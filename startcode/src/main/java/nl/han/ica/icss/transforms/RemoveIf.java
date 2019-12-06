package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.ElseClause;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
                    handleFalseIfClauseBody(node, ifClause);
                }
                handleTrueIfClauseBody(ifClause);
            }

            if (ifClause.conditionalExpression instanceof VariableReference) {
                Optional<ASTNode> optional = getVariable((VariableReference) ((IfClause) node).conditionalExpression);
                if (optional.isPresent() && optional.get() instanceof VariableReference) {
                    VariableReference reference = (VariableReference) optional.get();
                    if (reference.value.equals("false")) {
                        handleFalseIfClauseBody(node, ifClause);
                    }
                    handleTrueIfClauseBody(ifClause);
                }
            }
        }
    }

    private void handleFalseIfClauseBody(ASTNode node, IfClause ifClause) {
        nodes.remove(node);
        Optional<ASTNode> elseClause = ifClause.getChildren().stream().filter(x -> x instanceof ElseClause).findFirst();
        if (elseClause.isPresent() && elseClause.get() instanceof ElseClause) {
            ifClause.body = ((ElseClause) elseClause.get()).body;
        } else {
            ifClause.body = new ArrayList<>();
        }
    }

    private void handleTrueIfClauseBody(IfClause ifClause) {
        List<ASTNode> elseClauses = ifClause.body.stream().filter(x -> x instanceof ElseClause).collect(Collectors.toList());
        ifClause.body.removeAll(elseClauses);
        ((VariableReference) ifClause.conditionalExpression).value = "";
    }

    private Optional<ASTNode> getVariable(VariableReference variableReference) {
        return nodes.stream().filter(x -> x instanceof VariableReference)
                .filter(y -> (((VariableReference) y).name.equals(variableReference.name) &&
                        ((VariableReference) y).getExpressionType() == BOOL)).findFirst();
    }

}
