package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public interface ICompiler {

    void parseText(List<Query> queries);
}
