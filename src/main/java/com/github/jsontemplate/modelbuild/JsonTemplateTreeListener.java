package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.antlr4.JsonTemplateAntlrBaseListener;
import com.github.jsontemplate.antlr4.JsonTemplateAntlrParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Stack;
import java.util.stream.IntStream;

public class JsonTemplateTreeListener extends JsonTemplateAntlrBaseListener {


    private Stack<SimplePropertyDeclaration> stack = new Stack<>();
    private boolean debug = false;
    private boolean inArrayParamSpec;

    public SimplePropertyDeclaration getRoot() {
        //System.out.println("stack size " + stack.size());
        return stack.peek();
    }

    @Override
    public void enterPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
        debug("enterPairProperty ", ctx);
        stack.push(new SimplePropertyDeclaration());
    }

    @Override
    public void exitPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
        debug("exitPairProperty ", ctx);
        SimplePropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterArrayParamSpec(JsonTemplateAntlrParser.ArrayParamSpecContext ctx) {
        inArrayParamSpec = true;
    }

    @Override
    public void exitArrayParamSpec(JsonTemplateAntlrParser.ArrayParamSpecContext ctx) {
        inArrayParamSpec = false;
    }

    @Override
    public void enterMapParam(JsonTemplateAntlrParser.MapParamContext ctx) {
        debug("enterMapParam", ctx);
        String key = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        SimplePropertyDeclaration peek = stack.peek();
        if (inArrayParamSpec) {
            peek.getArrayTypeSpec().getMapParam().put(key, value);
        } else {
            peek.getTypeSpec().getMapParam().put(key, value);
        }
    }

    @Override
    public void enterListParams(JsonTemplateAntlrParser.ListParamsContext ctx) {
        debug("enterListParams", ctx);
        IntStream.range(0, ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .forEach(param -> {
                    SimplePropertyDeclaration peek = stack.peek();
                    if (inArrayParamSpec) {
                        peek.getArrayTypeSpec().getListParam().add(param);
                    } else {
                        peek.getTypeSpec().getListParam().add(param);
                    }
                });
    }

    @Override
    public void enterSingleParam(JsonTemplateAntlrParser.SingleParamContext ctx) {
        debug("enterSingleParam", ctx);
        SimplePropertyDeclaration peek = stack.peek();
        if (inArrayParamSpec) {
            peek.getArrayTypeSpec().setSingleParam(ctx.getText());
        } else {
            peek.getTypeSpec().setSingleParam(ctx.getText());
        }
    }

    @Override
    public void enterPropertyNameSpec(JsonTemplateAntlrParser.PropertyNameSpecContext ctx) {
        stack.peek().setPropertyName(ctx.getText());
    }

    private boolean inPropertyVariableWrapper;

    @Override
    public void enterPropertyVariableWrapper(JsonTemplateAntlrParser.PropertyVariableWrapperContext ctx) {
        inPropertyVariableWrapper = true;
    }

    @Override
    public void exitPropertyVariableWrapper(JsonTemplateAntlrParser.PropertyVariableWrapperContext ctx) {
        inPropertyVariableWrapper = false;
    }

    @Override
    public void enterVariableWrapper(JsonTemplateAntlrParser.VariableWrapperContext ctx) {
        if (inPropertyVariableWrapper) {
            if (ctx.getChild(0) instanceof JsonTemplateAntlrParser.VariableContext) {
                stack.peek().getTypeSpec().setTypeName(ctx.getText());
            } else {
                stack.peek().getTypeSpec().setSingleParam(ctx.getText());
            }
        }
    }

    @Override
    public void enterTypeName(JsonTemplateAntlrParser.TypeNameContext ctx) {
        debug("enterTypeName", ctx);
        if (!(ctx.getParent() instanceof JsonTemplateAntlrParser.TypeDefContext)) {
            stack.peek().getTypeSpec().setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        debug("enterSingleProperty", ctx);
        stack.push(new SimplePropertyDeclaration());
    }

    @Override
    public void exitSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        debug("exitSingleProperty ", ctx);
        SimplePropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterJsonObject(JsonTemplateAntlrParser.JsonObjectContext ctx) {
        debug("enterJsonObject", ctx);
        if (stack.isEmpty()) {
            stack.push(new SimplePropertyDeclaration());
        }
        SimplePropertyDeclaration pop = stack.pop();
        stack.push(pop.asObjectProperty());
    }

    @Override
    public void enterJsonArray(JsonTemplateAntlrParser.JsonArrayContext ctx) {
        if (stack.isEmpty()) {
            stack.push(new SimplePropertyDeclaration());
        }
        SimplePropertyDeclaration pop = stack.pop();
        stack.push(pop.asArrayProperty());
    }

    @Override
    public void enterItem(JsonTemplateAntlrParser.ItemContext ctx) {
        stack.push(new SimplePropertyDeclaration());
    }

    @Override
    public void exitItem(JsonTemplateAntlrParser.ItemContext ctx) {
        SimplePropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterItemVariableWrapper(JsonTemplateAntlrParser.ItemVariableWrapperContext ctx) {
        stack.peek().getTypeSpec().setSingleParam(ctx.getText());
    }

    private void debug(String message, ParserRuleContext ctx) {
        if (debug) {
            System.out.println(message + " " + ctx.getText());
        }
    }
}
