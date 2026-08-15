package queryBuilder.validator.rule;

import queryBuilder.validator.Query;
import queryBuilder.validator.Validator;

import java.util.HashMap;

public interface IRule {

    public void checkRule(Query query, HashMap<String, Integer> supportedFunctions, Validator validator);
}
