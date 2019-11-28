package nl.han.ica.icss.parser;

import java.util.Stack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
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
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		System.out.println("ENTER_STYLESHEET: "  + ctx.getText());
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		super.enterTagSelector(ctx);
		System.out.println("TAG_SELECTOR: "  + ctx.getText());
		TagSelector tagSelector = new TagSelector(ctx.getText());
		currentContainer.push(tagSelector);
		ast.root.addChild(tagSelector);
//		ctx.children.forEach(x -> this.ast.root.addChild(new TagSelector(x.getText())));
//		currentContainer.add(ctx.children.get(0).getParent())
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		super.enterDeclaration(ctx);
		System.out.println("DECLARATION: "  + ctx.getText());
		Declaration declaration = new Declaration(ctx.getText());
		currentContainer.push(declaration);
		ast.root.addChild(declaration);
	}



//	@Override
//	public void enterLiteral(ICSSParser.LiteralContext ctx) {
//		super.enterLiteral(ctx);
//		System.out.println("LITERAL: " + ctx.getText());
//		Literal literal = new BoolLiteral(ctx.getText());
//		currentContainer.push(literal);
//		ast.root.addChild(literal);
////		ctx.children.forEach(x -> this.ast.root.addChild(new BoolLiteral(ctx.getText())));
//	}

//	@Override
//	public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
//		super.enterPropertyName(ctx);
//		System.out.println("PROPERTY_NAME: " + ctx.getText());
//		PropertyName propertyName = new PropertyName(ctx.getText());
//		currentContainer.push(propertyName);
//		ast.root.addChild(propertyName);
////		ctx.children.forEach(x -> this.ast.root.addChild(new PropertyName(x.getText())));
//	}
}
