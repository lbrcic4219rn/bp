package querybuilder;

import querybuilder.compiler.ICompiler;
import querybuilder.validator.IValidator;

public interface IQueryBuilder {
    ICompiler getCompiler();

    IValidator getValidator();
}
