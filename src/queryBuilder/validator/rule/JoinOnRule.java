package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.HashMap;

public class JoinOnRule implements IRule{
    @Override
    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator) {
        boolean hasJoin = query.getValidFucntions().contains("Join");
        boolean hasOn = query.getValidFucntions().contains("On");
        if(!hasJoin && hasOn){
            validator.pushFeedback("On can only be used if Join fucntion is used and or is valid in query: " + query.getName());
            return;
        }
        if(hasJoin && !hasOn){
            validator.pushFeedback("When using Join function On must be used or be valid in query: " + query.getName());
            return;
        }
    }
}
