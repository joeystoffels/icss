package nl.han.ica.icss.parser;

import java.util.Stack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private Stack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();
	}
	public AST getAST() {
		return ast;
	}

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		this.currentContainer.push(this.ast.root);
	}

	private void add(ASTNode node){
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
		if(parent instanceof Declaration){
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

}
