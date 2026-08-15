package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public interface ICompiler {

    public void parseText(ArrayList<Query> queries);

}
