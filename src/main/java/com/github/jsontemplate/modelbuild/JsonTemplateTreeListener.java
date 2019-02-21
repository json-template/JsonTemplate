/*
 * Copyright 2019 Haihan Yin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.antlr4.JsonTemplateAntlrBaseListener;
import com.github.jsontemplate.antlr4.JsonTemplateAntlrParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Stack;
import java.util.stream.IntStream;

public final class JsonTemplateTreeListener extends JsonTemplateAntlrBaseListener {

    private Stack<SimplePropertyDeclaration> stack = new Stack<>();
    private boolean inArrayParamSpec;
    private boolean inPropertyVariableWrapper;

    public SimplePropertyDeclaration getRoot() {
        return stack.peek();
    }

    @Override
    public void enterPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
        stack.push(new SimplePropertyDeclaration());
    }

    @Override
    public void exitPairProperty(JsonTemplateAntlrParser.PairPropertyContext ctx) {
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
        String key = ctx.getChild(0).getText();
        String value = stripRawText(ctx.getChild(2).getText());
        SimplePropertyDeclaration peek = stack.peek();
        if (inArrayParamSpec) {
            peek.getArrayTypeSpec().getMapParam().put(key, value);
        } else {
            peek.getTypeSpec().getMapParam().put(key, value);
        }
    }

    @Override
    public void enterListParams(JsonTemplateAntlrParser.ListParamsContext ctx) {
        IntStream.range(0, ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .map(this::stripRawText)
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
        SimplePropertyDeclaration peek = stack.peek();
        String text = stripRawText(ctx.getText());
        if (inArrayParamSpec) {
            peek.getArrayTypeSpec().setSingleParam(text);
        } else {
            peek.getTypeSpec().setSingleParam(text);
        }
    }

    @Override
    public void enterPropertyNameSpec(JsonTemplateAntlrParser.PropertyNameSpecContext ctx) {
        stack.peek().setPropertyName(ctx.getText());
    }

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
        if (!(ctx.getParent() instanceof JsonTemplateAntlrParser.TypeDefContext)) {
            stack.peek().getTypeSpec().setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        stack.push(new SimplePropertyDeclaration());
    }

    @Override
    public void exitSingleProperty(JsonTemplateAntlrParser.SinglePropertyContext ctx) {
        SimplePropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterJsonObject(JsonTemplateAntlrParser.JsonObjectContext ctx) {
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

    private String stripRawText(String text) {
        if (text.startsWith(Token.RAW.getTag()) && text.endsWith(Token.RAW.getTag())) {
            return text.substring(1, text.length() - 1);
        } else {
            return text;
        }
    }
}
