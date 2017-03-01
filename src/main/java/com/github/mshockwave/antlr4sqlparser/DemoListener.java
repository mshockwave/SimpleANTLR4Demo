package com.github.mshockwave.antlr4sqlparser;

import com.github.mshockwave.antlr4sqlparser.antlr.SQLiteBaseListener;
import com.github.mshockwave.antlr4sqlparser.antlr.SQLiteParser;

import java.util.HashSet;
import java.util.Set;

/**
 * This class shows the role of listener in ANTLR4 framework.
 * Keep in mind that the input program has been parsed before entering
 * listener, so actually both enterFoo and exitFoo callback methods can
 * retrieve the fully parsed content(i.e. the parse tree) of the "foo" rule.
 * Then what's the different between enterFoo and exitFoo?
 * The walker would walk into the nested rules within "foo" after calling enterFoo
 * and before exitFoo. So the key point is that exitFoo can observe the actions
 * or more generally speaking, side effects, performed by nested rules,
 * but not for enterFoo.
 * **/
public class DemoListener extends SQLiteBaseListener{

    private Set<String> mExprStrings = new HashSet<>();
    private enum Action {
        SELECT,
        VALUES
    }
    private Action mCurrentAction = Action.SELECT;

    @Override
    public void enterSelect_core(SQLiteParser.Select_coreContext ctx) {

        if(ctx.K_VALUES() != null){
            System.out.println("Entering VALUES statement");
            System.out.println("Got " + ctx.expr().size() + " expression(s)");
            mCurrentAction = Action.VALUES;
        }else{
            System.out.println("Entering SELECT statement");
            System.out.println("Exist FROM: " + (ctx.K_FROM() != null));
            System.out.println("Exist WHERE: " + (ctx.K_WHERE() != null));
            mCurrentAction = Action.SELECT;
        }
    }

    @Override
    public void enterExpr(SQLiteParser.ExprContext ctx) {
        // expr rule is nested within select_core rule
        mExprStrings.add(ctx.getText());
    }

    @Override
    public void exitSelect_core(SQLiteParser.Select_coreContext ctx) {

        switch (mCurrentAction){
            case SELECT:
                System.out.println("Expression(s) in this SELECT:");
                break;
            case VALUES:
                System.out.println("Expression(s) in this VALUES:");
                break;
        }

        // Can observe the side effects produced by the nested rules,
        // that is, enterExpr in this case
        for(String exprString : mExprStrings){
            System.out.println(exprString);
        }
        mExprStrings.clear();
    }
}
