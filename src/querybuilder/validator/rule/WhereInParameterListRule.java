package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;

public class WhereInParameterListRule implements IRule {
    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        boolean hasWhereIn = query.getValidFunctions().contains("WhereIn");
        boolean hasParameterList = query.getValidFunctions().contains("ParameterList");
        if (!hasWhereIn && hasParameterList) {
            validator.pushFeedback(
                    "ParameterList can only be used if WhereIn function is used and or is valid in"
                            + " query: "
                            + query.getName());
            return;
        }
        if (hasWhereIn && !hasParameterList) {
            validator.pushFeedback(
                    "When using WhereIn function ParameterList must be used or be valid in query: "
                            + query.getName());
        }
    }
}
