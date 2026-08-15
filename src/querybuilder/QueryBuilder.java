package querybuilder;

import querybuilder.compiler.Compiler;
import querybuilder.compiler.ICompiler;
import querybuilder.validator.IValidator;
import querybuilder.validator.Validator;

public class QueryBuilder implements IQueryBuilder {

    private final IValidator validator;
    private final ICompiler compiler;

    public QueryBuilder(Validator validator, Compiler compiler) {
        this.validator = validator;
        this.compiler = compiler;
    }

    @Override
    public ICompiler getCompiler() {
        return this.compiler;
    }

    @Override
    public IValidator getValidator() {
        return this.validator;
    }
}
