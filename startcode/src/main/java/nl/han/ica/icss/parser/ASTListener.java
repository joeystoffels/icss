package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.PropertyName;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.Stack;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast = new AST();

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private Stack<ASTNode> currentContainer = new Stack<>();

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        this.currentContainer.push(this.ast.root);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        super.enterStylerule(ctx);
        Stylerule stylerule = new Stylerule();
        this.currentContainer.push(stylerule);
        this.ast.root.addChild(stylerule);
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        super.enterClassSelector(ctx);
        addChildToParent(new ClassSelector(ctx.getText()));
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        super.enterIdSelector(ctx);
        addChildToParent(new IdSelector(ctx.getText()));
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        super.enterTagSelector(ctx);
        addChildToParent(new TagSelector(ctx.getText()));
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        super.enterDeclaration(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (parent instanceof Declaration || parent instanceof VariableAssignment) {
            parent = popAndPeek();
        }
        addChildToParentAndPush(new Declaration());
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        super.enterPropertyName(ctx);
        addChildToParent(new PropertyName(ctx.getText()));
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        super.enterPixelLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (isNodeOfTypeOperationAndComplete(parent)) {
            parent = popAndPeek();
        }
        parent.addChild(new PixelLiteral(ctx.getText()));
    }

    private boolean isNodeOfTypeOperationAndComplete(ASTNode node) {
        return node instanceof Operation && (((Operation) node).lhs != null && ((Operation) node).rhs != null);
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        super.enterPercentageLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (isNodeOfTypeOperationAndComplete(parent)) {
            parent = popAndPeek();
        }
        parent.addChild(new PercentageLiteral(ctx.getText()));
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        super.enterColorLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (isNodeOfTypeOperationAndComplete(parent)) {
            parent = popAndPeek();
        }
        parent.addChild(new ColorLiteral(ctx.getText()));
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        super.enterScalarLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (isNodeOfTypeOperationAndComplete(parent)) {
            parent = popAndPeek();
        }
        parent.addChild(new ScalarLiteral(ctx.getText()));
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        super.enterBoolLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (isNodeOfTypeOperationAndComplete(parent)) {
            parent = popAndPeek();
        }
        parent.addChild(new BoolLiteral(ctx.getText()));
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.enterVariableAssignment(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (parent instanceof VariableAssignment) {
            parent = popAndPeek();
        }
        addChildToParentAndPush(new VariableAssignment());
    }

    @Override
    public void enterExpression(ICSSParser.ExpressionContext ctx) {
        super.enterExpression(ctx);

        ctx.children.forEach(x -> {
            if (x instanceof ICSSParser.AddOperationContext) {
                addChildToParentAndPush(new AddOperation());
            }
            if (x instanceof ICSSParser.SubtractOperationContext) {
                addChildToParentAndPush(new SubtractOperation());
            }
            if (x instanceof ICSSParser.MultiplyOperationContext) {
                addChildToParentAndPush(new MultiplyOperation());
            }
        });
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        super.enterIfClause(ctx);
        ASTNode parent = this.currentContainer.peek();
        while (!(parent instanceof Stylerule) && !(parent instanceof IfClause)) {
            parent = popAndPeek();
        }
        addChildToParentAndPush(new IfClause());
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        super.enterVariableReference(ctx);
        addChildToParent(new VariableReference(ctx.getText()));
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.exitVariableAssignment(ctx);
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof VariableAssignment) {
            VariableAssignment variableAssignment = (VariableAssignment) parent;
            variableAssignment.name.setExpressionType(variableAssignment.expression.getExpressionType());
            variableAssignment.name.value = variableAssignment.expression.getValue();
        }

        if (parent instanceof Operation) {
            ExpressionType type = ExpressionType.UNDEFINED;
            if (parent instanceof AddOperation || parent instanceof SubtractOperation) {
                type = ((Operation) parent).lhs.getExpressionType();
            }
            if (parent instanceof MultiplyOperation) {
                MultiplyOperation operation = (MultiplyOperation) parent;
                if (operation.lhs.getExpressionType() == ExpressionType.SCALAR) {
                    type = operation.rhs.getExpressionType();
                } else {
                    type = operation.lhs.getExpressionType();
                }
            }

            while (!(parent instanceof VariableAssignment)) {
                parent = popAndPeek();
            }
            ((VariableAssignment) parent).name.setExpressionType(type);
        }
    }

    private ASTNode popAndPeek() {
        this.currentContainer.pop();
        return this.currentContainer.peek();
    }

    private void addChildToParent(ASTNode node) {
        this.currentContainer.peek().addChild(node);
    }

    private void addChildToParentAndPush(ASTNode node) {
        addChildToParent(node);
        this.currentContainer.push(node);
    }

}
