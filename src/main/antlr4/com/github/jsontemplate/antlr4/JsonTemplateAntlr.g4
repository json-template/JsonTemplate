grammar JsonTemplateAntlr;

root : templatePart (',' templatePart)*;
templatePart : typeDefinition | jsonObject | jsonArray;
jsonObject : objectTypeInfo? '{' properties '}';
objectTypeInfo : jsonValue;
properties : property (',' property)*;
property : singleProperty | pairProperty;
singleProperty : propertyName;
pairProperty : propertyName ':' propertyValueSpec;
propertyName : IDENTIFIER;

typeDefinition : typeDef ':' propertyValueSpec;
typeDef : '@'typeName;
typeName : IDENTIFIER;

propertyValueSpec : jsonValue | jsonArray | jsonObject | propertyVariableWrapper;
propertyVariableWrapper : variableWrapper;
jsonValue : typeInfo typeParamSpec?;
typeInfo : '@'typeName;

typeParamSpec : '(' singleParam ')' | '(' listParams ')' | '(' mapParams ')';
variableWrapper : variable | IDENTIFIER | VALUE | RAW;
singleParam : variableWrapper;
listParams : variableWrapper (',' variableWrapper )+;
mapParams : mapParam (',' mapParam)*;
mapParam : IDENTIFIER '=' variableWrapper;

jsonArray : arrayTypeInfo? itemsArray ;
arrayTypeInfo : jsonValue;
itemsArray : '[' items? ']' arrayParamSpec?;
arrayParamSpec : typeParamSpec;
items : item  (',' item)*;
item : propertyValueSpec;

variable : '$'variableName;
variableName : IDENTIFIER;
RAW : '`' .*? '`';

IDENTIFIER : [a-zA-Z0-9_'.']+;
VALUE : ~([,:=@${}() \t\n\r]|'['|']')+;

WS : [ \t\n\r]+ -> skip ;
