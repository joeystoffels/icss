package nl.han.ica.icss.ast.operations;

import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;

public class AddOperation extends Operation {

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getNodeLabel() {
        return "Add";
    }

    @Override
    public ExpressionType getExpressionType() {
        return null;
    }
}
