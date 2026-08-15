package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class AggregationAliasMustExistIfHaving implements IRule{
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        boolean hasAvg = query.getValidFucntions().contains("Avg");
        boolean hasCount = query.getValidFucntions().contains("Count");
        boolean hasMin = query.getValidFucntions().contains("Min");
        boolean hasMax = query.getValidFucntions().contains("Max");
        boolean hasHaving = query.getValidFucntions().contains("Having");
        boolean hasAndHaving = query.getValidFucntions().contains("AndHaving");
        boolean hasOrHaving = query.getValidFucntions().contains("OrHaving");
        ArrayList<String> aggregationArguments = new ArrayList<>();
        //uzimaju se argumenti iz funkcija agregacije kako bi se uporedilo sa onim u selektu
        if(hasHaving || hasAndHaving || hasOrHaving) {
            if (hasAvg || hasCount || hasMin || hasMax) {
                if (hasAvg) {
                    String functionWithArgs = content.substring(content.indexOf("Avg"), content.indexOf(')', content.indexOf("Avg")) + 1);
                    String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                    String [] singleArgArray = args.split(",");
                    if(supportedFunctions.get("Avg") != singleArgArray.length){
                        validator.pushFeedback("When using having aggregation function(Avg) must contain alias-- found for query: " + query.getName());
                        query.getValidFucntions().remove("Avg");
                    }

                }
                if (hasCount) {
                    String functionWithArgs = content.substring(content.indexOf("Count"), content.indexOf(')', content.indexOf("Count")) + 1);
                    String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                    String [] singleArgArray = args.split(",");
                    if(supportedFunctions.get("Count") != singleArgArray.length){
                        validator.pushFeedback("When using having aggregation function(Count) must contain alias-- found for query: " + query.getName());
                        query.getValidFucntions().remove("Count");
                    }
                }
                if (hasMin) {
                    String functionWithArgs = content.substring(content.indexOf("Min"), content.indexOf(')', content.indexOf("Min")) + 1);
                    String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                    String [] singleArgArray = args.split(",");
                    if(supportedFunctions.get("Min") != singleArgArray.length){
                        validator.pushFeedback("When using having aggregation function(Min) must contain alias-- found for query: " + query.getName());
                        query.getValidFucntions().remove("Min");
                    }
                }
                if (hasMax) {
                    String functionWithArgs = content.substring(content.indexOf("Max"), content.indexOf(')', content.indexOf("Max")) + 1);
                    String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                    String [] singleArgArray = args.split(",");
                    if(supportedFunctions.get("Max") != singleArgArray.length){
                        validator.pushFeedback("When using having aggregation function(Max) must contain alias-- found for query: " + query.getName());
                        query.getValidFucntions().remove("Max");
                    }
                }
            }
        }
    }
}
