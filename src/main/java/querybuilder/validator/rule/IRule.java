package querybuilder.validator.rule;

import querybuilder.validator.Query;
import querybuilder.validator.Validator;

import java.util.Map;

public interface IRule {

    void checkRule(Query query, Map<String, Integer> supportedFunctions, Validator validator);
}
