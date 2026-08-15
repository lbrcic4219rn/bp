package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;

public class JoinOnRule implements IRule {
    @Override
    public void checkRule(
            Query query, Map<String, Integer> supportedFunctions, Validator validator) {
        boolean hasJoin = query.getValidFunctions().contains("Join");
        boolean hasOn = query.getValidFunctions().contains("On");
        if (!hasJoin && hasOn) {
            validator.pushFeedback(
                    "On can only be used if Join function is used and or is valid in query: "
                            + query.getName());
            return;
        }
        if (hasJoin && !hasOn) {
            validator.pushFeedback(
                    "When using Join function On must be used or be valid in query: "
                            + query.getName());
        }
    }
}
