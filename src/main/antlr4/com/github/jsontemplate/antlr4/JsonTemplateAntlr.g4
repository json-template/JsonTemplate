grammar JsonTemplateAntlr;

root : jsonObject | jsonArray;
jsonObject : objectTypeInfo? '{' properties '}';
objectTypeInfo : jsonValue;
properties : property (',' property)*;
property : singleProperty | pairProperty;
singleProperty : propertyNameSpec;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | typeDef ;
propertyName : IDENTIFIER;
typeDef : '@'typeName;
typeName : IDENTIFIER;

propertyValueSpec : jsonValue | jsonArray | jsonObject | propertyVariableWrapper;
propertyVariableWrapper : variableWrapper;
jsonValue : typeInfo typeParamSpec?;
typeInfo : '@'typeName;

typeParamSpec : '(' singleParam ')' | '(' listParams ')' | '(' mapParams ')';
variableWrapper : IDENTIFIER | variable | RAW;
singleParam : variableWrapper;
listParams : variableWrapper (',' variableWrapper )+;
mapParams : mapParam (',' mapParam)*;
mapParam : IDENTIFIER '=' variableWrapper;

jsonArray : arrayTypeInfo? itemsArray ;
arrayTypeInfo : jsonValue;
itemsArray : '[' items? ']' arrayParamSpec?;
arrayParamSpec : typeParamSpec;
items : item (',' item)*;
item : jsonValue | itemVariableWrapper;
itemVariableWrapper : variableWrapper;

variable : '$'variableName;
variableName : IDENTIFIER;
RAW : '`' .*? '`';

IDENTIFIER : ([a-zA-Z0-9+-]|'.')+;

WS : [ \t\n\r]+ -> skip ;