package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public interface ICompiler {

    CompilationResult compile(List<Query> queries);
}
