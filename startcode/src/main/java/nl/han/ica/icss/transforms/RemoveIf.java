package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static nl.han.ica.icss.ast.types.ExpressionType.BOOL;
import static nl.han.ica.icss.ast.types.ExpressionType.PERCENTAGE;
import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;

public class RemoveIf implements Transform {

//    private CopyOnWriteArrayList<ASTNode> nodes = new CopyOnWriteArrayList<>();
//    private AST ast;

    @Override
    public void apply(AST ast) {
    }


//    @Override
//    public void apply(AST ast) {
//        this.ast = ast;
//        setNodes(ast.root);
////        nodes.forEach(this::replaceOperation);
//        List<ASTNode> clauses = nodes.stream()..filter(x -> x instanceof IfClause).collect(Collectors.toList());
//        clauses.forEach(this::checkIfClause);
////        ast.root.body.removeAll(clauses);
//        ast.root.body.removeAll(nodes.stream().filter(x -> x instanceof VariableAssignment).collect(Collectors.toList()));
//    }
//
//    private void setNodes(ASTNode node) {
//        if (!node.getChildren().isEmpty()) {
//            nodes.addAll(node.getChildren());
//            node.getChildren().forEach(this::setNodes);
//        }
//    }
//
//    private void checkIfClause(ASTNode node) {
//        if (node instanceof IfClause) {
//            if (((IfClause) node).conditionalExpression instanceof BoolLiteral) {
//                if (((BoolLiteral) node).getValue().equals("FALSE")) {
//                    ((IfClause) node).body.forEach(x -> ast.root.body.remove(x));
//                    nodes.remove(node);
//                    ast.root.body.remove(node);
//                } else {
////                    ASTNode parent = ast.root.body.get(ast.root.body.indexOf(node) -1);
////                    if (parent instanceof Stylerule) {
////                        ((Stylerule) parent).body.addAll(((IfClause) node).body);
////                    }
////                    nodes.remove(node);
////                    ast.root.body.remove(node);
//                }
//            }
//            if (((IfClause) node).conditionalExpression instanceof VariableReference) {
//                boolean bool = getValueOfVariableReference(((IfClause) node).conditionalExpression).equals("false");
//                if (bool) {
////                    nodes.remove(node);
//                    ((IfClause) node).body.forEach(x -> ast.root.body.remove(x));
//                } else {
//
//                }
//            }
//        }
//    }
//
//    private String getValueOfVariableReference(Expression expression) {
//        if (expression instanceof VariableReference && expression.getExpressionType() == UNDEFINED) {
//
//            Optional<ASTNode> optional = nodes.stream().filter(x -> x instanceof VariableReference)
//                    .filter(y -> (((VariableReference) y).name.equals(((VariableReference) expression).name) &&
//                            ((VariableReference) y).getExpressionType() != UNDEFINED)).findFirst();
//
//            if (optional.isPresent() && optional.get() instanceof VariableReference) {
//                return ((VariableReference) optional.get()).getValue();
//            }
//        }
//        return null;
//    }
}
