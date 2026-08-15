package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.HashMap;

public class ArgNumberRule implements IRule{

    private int charOccurrences(String str, char c){
        int res = 0;
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == c)
                res++;
        return res;
    }
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        //System.out.println(query.getValidFucntions());
        for(int i = 0; i < query.getValidFucntions().size(); i++){
            String content = query.getContent();
            String currentFunction = query.getValidFucntions().get(i);

            int requiredArgNumber = supportedFunctions.get(currentFunction);
            String functionWithArgs = content.substring(content.indexOf(currentFunction), content.indexOf(')', content.indexOf(currentFunction)) + 1);
            //System.out.println("Args check function with args: " + functionWithArgs + " number of args rquired: " + requiredArgNumber);
            String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')'));
            args = args.replaceAll("\\s", "");
            //System.out.println("argumenti: " + args);
            int numberOfQuotes = charOccurrences(args, '"');
            //System.out.println("navodnik se pojavljuje puta u fji: " + numberOfQuotes + "   " + functionWithArgs);
            if(args.indexOf(',') == -1 && args.length() == 0){
                validator.pushFeedback("Function can not have zero arguments for function: " + currentFunction);
                query.getValidFucntions().remove(i);
                i--;
                continue;
            }
            if(args.indexOf(',') == -1 && args.length() != 0){
                if(numberOfQuotes != 0 && numberOfQuotes != 2){
                    validator.pushFeedback("Problem with quotes and or , operator for function: " + currentFunction);
                    query.getValidFucntions().remove(i);
                    i--;
                    continue;
                }
                if(numberOfQuotes > 0 && (args.charAt(0) != '"' || args.charAt(args.length() - 1) != '"')){
                    validator.pushFeedback("Quotes need to be at the start and end of a string for attribute: " + args + " for function: " + currentFunction);
                    query.getValidFucntions().remove(i);
                    i--;
                    continue;
                }
                if(requiredArgNumber != 1 && requiredArgNumber != -1){
                    if(currentFunction.equals("Avg") || currentFunction.equals("Count") || currentFunction.equals("Min") || currentFunction.equals("Max")
                            || currentFunction.equals("Having") || currentFunction.equals("AndHaving") || currentFunction.equals("OrHaving")){
                        if(supportedFunctions.get(currentFunction) - 1 == 1)
                            continue;
                    }
                    validator.pushFeedback("Expected " + requiredArgNumber + " arguments but only got 1 for function: " + currentFunction);
                    query.getValidFucntions().remove(i);
                    i--;
                    continue;
                }
            }
            String[] arguments = args.split(",");
            //System.out.println(currentFunction);
            //System.out.println("Nadjeno argumenata: " + arguments.length);
            boolean flag = false;
            for (int j = 0; j < arguments.length; j++){
                int argumentQuoteNo = charOccurrences(arguments[j], '"');
                if(argumentQuoteNo != 0 && argumentQuoteNo != 2){
                    validator.pushFeedback("Problem with quotes need to be ether 0 for int and 2 for string values attribute: " + arguments[j] + " for function: " + currentFunction);
                    flag = true;
                    continue;
                }
                if(argumentQuoteNo > 0 && (arguments[j].charAt(0) != '"' || arguments[j].charAt(arguments[j].length() - 1) != '"')){
                    validator.pushFeedback("Quotes need to be at the start and end of a string for attribute: " + arguments[j] + " for function: " + currentFunction);
                    flag = true;
                    continue;
                }
                if(supportedFunctions.get(currentFunction) != arguments.length && supportedFunctions.get(currentFunction) != -1){
                    if(currentFunction.equals("Avg") || currentFunction.equals("Count") || currentFunction.equals("Min") || currentFunction.equals("Max")
                            || currentFunction.equals("Having") || currentFunction.equals("AndHaving") || currentFunction.equals("OrHaving")){
                        if(supportedFunctions.get(currentFunction) - 1 == arguments.length)
                            continue;
                    }
                    validator.pushFeedback("Expected" + requiredArgNumber + " arguments but got: " + arguments.length + " for function: " + currentFunction);
                    flag = true;
                    continue;
                }
            }
            if(flag){
                query.getValidFucntions().remove(i);
                i--;
            }
        }
    }
}
