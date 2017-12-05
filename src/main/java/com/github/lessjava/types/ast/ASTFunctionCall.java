package com.github.lessjava.types.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.lessjava.visitor.impl.LJGenerateJava;

/**
 * Decaf function call that is intended to return a value at runtime.
 */
public class ASTFunctionCall extends ASTExpression {
    public String name;
    public List<ASTExpression> arguments;

    public ASTFunctionCall(String name) {
        this.name = name;
        this.arguments = new ArrayList<ASTExpression>();
    }

    public String getNameArgString() {
        StringBuilder sb = new StringBuilder(name);
        for (ASTExpression e : arguments) {
            sb.append(e.type.toString() + ",");
        }

        return sb.toString();
    }

    @Override
    public void traverse(ASTVisitor visitor) {
        visitor.preVisit(this);
        for (ASTExpression e : arguments) {
            e.traverse(visitor);
        }
        visitor.postVisit(this);
    }

    @Override
    public String toString() {
        StringBuilder argString = new StringBuilder();
        argString.append("(");
        for (ASTExpression e : arguments) {
            String exprString = e.toString();
            if (argString.length() > 1) {
                argString.append(", ");
            }
            argString.append(exprString);
        }
        argString.append(")");

        if (name.equals("size")) {
            return String.format("%s.size()", this.arguments.get(0));
        }

        if (LJGenerateJava.libraryFunctions.containsKey(name)) {
            return LJGenerateJava.libraryFunctions.get(name) + argString;
        }

        return name + argString;
    }
}
