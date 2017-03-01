package com.github.mshockwave.antlr4sqlparser;

import com.github.mshockwave.antlr4sqlparser.antlr.SQLiteBaseListener;
import com.github.mshockwave.antlr4sqlparser.antlr.SQLiteParser;

import java.util.HashSet;
import java.util.Set;

public class DemoListener extends SQLiteBaseListener{

    private Set<String> mExprStrings = new HashSet<>();
    private enum Action {
        SELECT,
        VALUES
    }
    private Action mCurrentAction = Action.SELECT;

    @Override
    public void enterSelect_core(SQLiteParser.Select_coreContext ctx) {
        //System.out.println("Enter select_core");
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
        mExprStrings.add(ctx.getText());
    }

    @Override
    public void exitSelect_core(SQLiteParser.Select_coreContext ctx) {
        //System.out.println("Exit select_core");
        switch (mCurrentAction){
            case SELECT:
                System.out.println("Expression(s) in this SELECT:");
                break;
            case VALUES:
                System.out.println("Expression(s) in this VALUES:");
                break;
        }

        for(String exprString : mExprStrings){
            System.out.println(exprString);
        }
        mExprStrings.clear();
    }
}
