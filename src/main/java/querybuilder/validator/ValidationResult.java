package querybuilder.validator;

import java.util.List;

public record ValidationResult(List<Query> queries, List<String> errors) {

    public ValidationResult {
        queries = List.copyOf(queries);
        errors = List.copyOf(errors);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }
}
