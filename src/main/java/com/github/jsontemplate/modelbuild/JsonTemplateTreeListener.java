package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.antlr4.JsonTemplateAntlrBaseListener;
import com.github.jsontemplate.antlr4.JsonTemplateAntlrParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Stack;
import java.util.stream.IntStream;

public class JsonTemplateTreeListener extends JsonTemplateAntlrBaseListener {


    private Stack<PropertyDeclaration> stack = new Stack<>();

    private boolean debug = false;
    private boolean inArrayParamSpec;

    public PropertyDeclaration getRoot() {
        //System.out.println("stack size " + stack.size());
        return stack.peek();
    }

    @Override
    public void enterPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
        debug("enterPairProperty ", ctx);
        stack.push(new PropertyDeclaration());
    }

    @Override
    public void exitPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
        debug("exitPairProperty ", ctx);
        PropertyDeclaration pop = stack.pop();
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
        PropertyDeclaration peek = stack.peek();
        if (inArrayParamSpec) {
            peek.getArrayMapParam().put(key, value);
        } else {
            peek.getMapParam().put(key, value);
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
                    PropertyDeclaration peek = stack.peek();
                    if (inArrayParamSpec) {
                        peek.getArrayListParam().add(param);
                    } else {
                        peek.getListParam().add(param);
                    }
                });
    }

    @Override
    public void enterSingleParam(JsonTemplateAntlrParser.SingleParamContext ctx) {
        debug("enterSingleParam", ctx);
        PropertyDeclaration peek = stack.peek();
        if (inArrayParamSpec) {
            peek.setArraySingleParam(ctx.getText());
        } else {
            peek.setSingleParam(ctx.getText());
        }
    }

    @Override
    public void enterPropertyNameSpec(JsonTemplateAntlrParser.PropertyNameSpecContext ctx) {
        stack.peek().setValueName(ctx.getText());
    }

    @Override
    public void enterPropertyVariableWrapper(JsonTemplateAntlrParser.PropertyVariableWrapperContext ctx) {
        stack.peek().setTypeName(ctx.getText());
    }

    @Override
    public void enterTypeName(JsonTemplateAntlrParser.TypeNameContext ctx) {
        debug("enterTypeName", ctx);
        if (!(ctx.getParent() instanceof JsonTemplateAntlrParser.TypeDefContext)) {
            stack.peek().setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        debug("enterSingleProperty", ctx);
        stack.push(new PropertyDeclaration());
    }

    @Override
    public void exitSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        debug("exitSingleProperty ", ctx);
        PropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterJsonObject(JsonTemplateAntlrParser.JsonObjectContext ctx) {
        debug("enterJsonObject", ctx);
        if (stack.isEmpty()) {
            stack.push(new PropertyDeclaration());
        }
        stack.peek().setAsObject(true);
    }

    @Override
    public void enterJsonArray(JsonTemplateAntlrParser.JsonArrayContext ctx) {
        if (stack.isEmpty()) {
            stack.push(new PropertyDeclaration());
        }
        stack.peek().setAsArray(true);
    }

    @Override
    public void enterItem(JsonTemplateAntlrParser.ItemContext ctx) {
        stack.push(new PropertyDeclaration());
    }

    @Override
    public void exitItem(JsonTemplateAntlrParser.ItemContext ctx) {
        PropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterItemVariableWrapper(JsonTemplateAntlrParser.ItemVariableWrapperContext ctx) {
        stack.peek().setSingleParam(ctx.getText());
    }

    private void debug(String message, ParserRuleContext ctx) {
        if (debug) {
            System.out.println(message + " " + ctx.getText());
        }
    }
}
