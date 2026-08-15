package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class OnlyAggregationElementsInHavingRule implements IRule{
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        boolean hasAvg = query.getValidFucntions().contains("Avg");
        boolean hasCount = query.getValidFucntions().contains("Count");
        boolean hasMin = query.getValidFucntions().contains("Min");
        boolean hasMax = query.getValidFucntions().contains("Max");
        ArrayList<String> aggregationArguments = new ArrayList<>();
        boolean hasHaving = query.getValidFucntions().contains("Having");
        boolean hasAndHaving = query.getValidFucntions().contains("AndHaving");
        boolean hasOrHaving = query.getValidFucntions().contains("OrHaving");
        //uzimaju se argumenti iz funkcija agregacije kako bi se uporedilo sa onim u selektu
        ArrayList<String> havingArguments = new ArrayList<String>();
        int count = 0;
        if (hasHaving || hasOrHaving || hasAndHaving) {
            if (hasAvg){
                String functionWithArgs = content.substring(content.indexOf("Having"), content.indexOf(')', content.indexOf("Having")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String [] singleArgArray = args.split(",");
                havingArguments.addAll(Arrays.asList(singleArgArray));
                count++;
            }
            if (hasOrHaving) {
                String functionWithArgs = content.substring(content.indexOf("OrHaving"), content.indexOf(')', content.indexOf("OrHaving")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String [] singleArgArray = args.split(",");
                havingArguments.addAll(Arrays.asList(singleArgArray));
                count++;
            }
            if (hasAndHaving){
                String functionWithArgs = content.substring(content.indexOf("AndHaving"), content.indexOf(')', content.indexOf("AndHaving")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String [] singleArgArray = args.split(",");
                havingArguments.addAll(Arrays.asList(singleArgArray));
                count++;
            }
        }else
            return;

        if (hasAvg || hasCount || hasMin || hasMax) {
            if (hasAvg){
                String functionWithArgs = content.substring(content.indexOf("Avg"), content.indexOf(')', content.indexOf("Avg")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
            if (hasCount) {
                String functionWithArgs = content.substring(content.indexOf("Count"), content.indexOf(')', content.indexOf("Count")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
            if (hasMin) {
                String functionWithArgs = content.substring(content.indexOf("Min"), content.indexOf(')', content.indexOf("Min")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
            if (hasMax){
                String functionWithArgs = content.substring(content.indexOf("Max"), content.indexOf(')', content.indexOf("Max")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
        }
        if(!hasHaving && (hasAndHaving || hasOrHaving)){
            validator.pushFeedback("Cant use AndHaving and or OrHaving if having is not declared query: " + query.getName());
            return;
        }
        if(!(hasAvg || hasCount || hasMin || hasMax) && hasHaving) {
            validator.pushFeedback("No valid aggregation arguments/functions found but has having (having can only use aggregation function alias query: " + query.getName());
            return;
        }

        int flag = 0;
        for(int i = 0; i < aggregationArguments.size(); i++){
            if(havingArguments.contains(aggregationArguments.get(i)))
                flag++;
        }
        if(flag < count){
            validator.pushFeedback("When using having andhaving orhaving only aggregation values can be used in query: " + query.getName());
        }
    }
}
