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

    private void add(ASTNode node) {
        this.currentContainer.push(node);
        this.ast.root.addChild(node);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        super.enterStylerule(ctx);
        Stylerule stylerule = new Stylerule();
        this.add(stylerule);
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        super.enterClassSelector(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new ClassSelector(ctx.getText()));
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        super.enterIdSelector(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new IdSelector(ctx.getText()));
    }

    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        super.enterTagSelector(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new TagSelector(ctx.getText()));
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        super.enterDeclaration(ctx);
        Declaration declaration = new Declaration();
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof Declaration) {
            this.currentContainer.pop();
            parent = this.currentContainer.peek();
        }
        parent.addChild(declaration);
        this.currentContainer.push(declaration);
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        super.enterPropertyName(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new PropertyName(ctx.getText()));
    }

    @Override
    public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        super.enterPixelLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new PixelLiteral(ctx.getText()));
    }

    @Override
    public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        super.enterPercentageLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new PercentageLiteral(ctx.getText()));
    }

    @Override
    public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        super.enterColorLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new ColorLiteral(ctx.getText()));
    }

    @Override
    public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        super.enterScalarLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new ScalarLiteral(ctx.getText()));
    }

    @Override
    public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        super.enterBoolLiteral(ctx);
        ASTNode parent = this.currentContainer.peek();
        parent.addChild(new BoolLiteral(ctx.getText()));
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.enterVariableAssignment(ctx);
        VariableAssignment assignment = new VariableAssignment();
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof VariableAssignment) {
            this.currentContainer.pop();
            parent = this.currentContainer.peek();
        }
        parent.addChild(assignment);
        this.currentContainer.push(assignment);
    }

    @Override
    public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
        super.enterAddOperation(ctx);
        ASTNode parent = this.currentContainer.peek();
        AddOperation addOperation = new AddOperation();
        parent.addChild(addOperation);
        this.currentContainer.push(addOperation);
    }

    @Override
    public void enterSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
        super.enterSubtractOperation(ctx);
        ASTNode parent = this.currentContainer.peek();
        SubtractOperation subtractOperation = new SubtractOperation();
        parent.addChild(subtractOperation);
        this.currentContainer.push(subtractOperation);
    }

    @Override
    public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
        super.enterMultiplyOperation(ctx);
        ASTNode parent = this.currentContainer.peek();
        MultiplyOperation multiplyOperation = new MultiplyOperation();
        parent.addChild(multiplyOperation);
        this.currentContainer.push(multiplyOperation);
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        super.enterIfClause(ctx);
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof Declaration) {
            this.currentContainer.pop();
            parent = this.currentContainer.peek();
        }
        IfClause ifClause = new IfClause();
        parent.addChild(ifClause);
        this.currentContainer.push(ifClause);
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        super.enterVariableReference(ctx);
        ASTNode parent = this.currentContainer.peek();
        VariableReference variableReference = new VariableReference(ctx.getText());
        if (parent instanceof Declaration) {
            variableReference.setExpressionType(parent.);
        }
        parent.addChild(variableReference);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        super.exitVariableAssignment(ctx);
        ASTNode parent = this.currentContainer.peek();
        if (parent instanceof VariableAssignment) {
            ((VariableAssignment) parent).name.setExpressionType(((VariableAssignment) parent).expression.getExpressionType());
        }
    }
}
