package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.HashMap;

public class WhereInParametarListRule implements IRule{
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        boolean hasWhereIn = query.getValidFucntions().contains("WhereIn");
        boolean hasParametarList = query.getValidFucntions().contains("ParametarList");
        if(!hasWhereIn && hasParametarList){
            validator.pushFeedback("ParametarList can only be used if WhereIn fucntion is used and or is valid in query: " + query.getName());
            return;
        }
        if(hasWhereIn && !hasParametarList){
            validator.pushFeedback("When using WhereIn function ParametarList must be used or be valid in query: " + query.getName());
            return;
        }
    }
}
