package queryBuilder;

import queryBuilder.compiler.Compiler;
import queryBuilder.compiler.ICompiler;
import queryBuilder.validator.IValidator;
import queryBuilder.validator.Validator;

public class QueryBuilder implements IQueryBuilder{

    private IValidator validator;
    private ICompiler compiler;

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
