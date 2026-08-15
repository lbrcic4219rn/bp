package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;

public class WhereInParametarListRule implements IRule {
    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        boolean hasWhereIn = query.getValidFunctions().contains("WhereIn");
        boolean hasParametarList = query.getValidFunctions().contains("ParametarList");
        if (!hasWhereIn && hasParametarList) {
            validator.pushFeedback(
                    "ParametarList can only be used if WhereIn fucntion is used and or is valid in"
                            + " query: "
                            + query.getName());
            return;
        }
        if (hasWhereIn && !hasParametarList) {
            validator.pushFeedback(
                    "When using WhereIn function ParametarList must be used or be valid in query: "
                            + query.getName());
        }
    }
}
