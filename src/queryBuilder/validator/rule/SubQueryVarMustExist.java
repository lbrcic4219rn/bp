package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SubQueryVarMustExist implements IRule {
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        String content = query.getContent();
        boolean hasWhereInQ = query.getValidFucntions().contains("WhereInQ");
        boolean hasWhereEqQ = query.getValidFucntions().contains("WhereEqQ");
        ArrayList<String> queryNeeded = new ArrayList();
        if(hasWhereEqQ || hasWhereInQ){
            if(hasWhereEqQ){
                String functionWithArgs = content.substring(content.indexOf("WhereEqQ"), content.indexOf(')', content.indexOf("WhereEqQ")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                queryNeeded.addAll(Arrays.asList(singleArgArray));
            }
            if(hasWhereInQ){
                String functionWithArgs = content.substring(content.indexOf("WhereInQ"), content.indexOf(')', content.indexOf("WhereInQ")) + 1);
                String args = functionWithArgs.substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')')).replaceAll("\\s", "");
                String singleArgArray = args.split(",")[1];
                queryNeeded.addAll(Arrays.asList(singleArgArray));
            }
        }
        if(!hasWhereEqQ && !hasWhereInQ){
            return;
        }
        ArrayList<Query> queries = validator.getQueries();
        int flag = 0;
        for(int i = 0; i < queryNeeded.size(); i++){
            flag = 0;
            for(int j = 0; j < queries.size(); j++){
                if(queryNeeded.get(i).replaceAll("\\s", "").equals(queries.get(j).getName())){
                   flag = 1;
                }
            }
            if(flag == 0){
                validator.pushFeedback("Query: " + queryNeeded.get(i) + " not found for query: " + query.getName());
            }
        }
    }
}
