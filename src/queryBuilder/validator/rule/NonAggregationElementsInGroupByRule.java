package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class NonAggregationElementsInGroupByRule implements IRule{
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        boolean hasAvg = query.getValidFucntions().contains("Avg");
        boolean hasCount = query.getValidFucntions().contains("Count");
        boolean hasMin = query.getValidFucntions().contains("Min");
        boolean hasMax = query.getValidFucntions().contains("Max");
        ArrayList <String> aggregationArguments = new ArrayList<>();
        //uzimaju se argumenti iz funkcija agregacije kako bi se uporedilo sa onim u selektu
        if (hasAvg || hasCount || hasMin || hasMax) {
            if (hasAvg){
                String functionWithArgs = content.substring(content.indexOf("Avg"), content.indexOf(')', content.indexOf("Avg")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray[] = args.split(",");
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
            if (hasCount) {
                String functionWithArgs = content.substring(content.indexOf("Count"), content.indexOf(')', content.indexOf("Count")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray[] = args.split(",");
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
            if (hasMin) {
                String functionWithArgs = content.substring(content.indexOf("Min"), content.indexOf(')', content.indexOf("Min")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray[] = args.split(",");
                Collections.addAll(aggregationArguments, singleArgArray);
            }
            if (hasMax){
                String functionWithArgs = content.substring(content.indexOf("Max"), content.indexOf(')', content.indexOf("Max")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray[] = args.split(",");
                aggregationArguments.addAll(Arrays.asList(singleArgArray));
            }
        }else
            return;
        if(aggregationArguments.isEmpty()){
            return;
        }
        if(!query.getValidFucntions().contains("Select")){
            validator.pushFeedback("Select Function is not declared and or is wrong -- Non Aggregation elements must me in GroupBY query: " + query.getName());
            return;
        } else if(!query.getValidFucntions().contains("GroupBy")){
            validator.pushFeedback("GroupBy function is not declared and or is wrong -- Non Aggregation elements must me in GroupBY query: " + query.getName());
            return;
        }
        String functionWithArgs = content.substring(content.indexOf("Select"), content.indexOf(')', content.indexOf("Select")) + 1);
        String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
        String [] selectArguments = args.split(",");
        ArrayList<String> selectArgumentsArr = new ArrayList<>();
        selectArgumentsArr.addAll(Arrays.asList(selectArguments));
        for (int i = 0; i < aggregationArguments.size(); i++){
            selectArgumentsArr.remove(aggregationArguments.get(i));
        }
        String groupByWithArgs = content.substring(content.indexOf("GroupBy"), content.indexOf(')', content.indexOf("GroupBy")) + 1);
        String groupByatgs = groupByWithArgs.substring(groupByWithArgs.indexOf('(') + 1, groupByWithArgs.indexOf(')')).replaceAll("\\s", "");
        String groupByArgs[] = groupByatgs.split(",");
        ArrayList groupByArgsArr = new ArrayList();
        groupByArgsArr.addAll(Arrays.asList(groupByArgs));
        for(int i = 0; i < selectArgumentsArr.size(); i++){
            if(!groupByArgsArr.contains(selectArgumentsArr.get(i))){
                validator.pushFeedback(selectArgumentsArr.get(i) + " from select must be in groupBy function");
            }
        }

    }
}
