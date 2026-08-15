package queryBuilder;

import queryBuilder.compiler.ICompiler;
import queryBuilder.validator.IValidator;

public interface IQueryBuilder {
    public ICompiler getCompiler();
    public IValidator getValidator();
}
