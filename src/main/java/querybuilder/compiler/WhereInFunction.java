package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereInFunction extends Function {

    public WhereInFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "WhereIn");

        List<String> parameters = extractArguments(q.getContent(), "ParameterList");

        String res = "WHERE %s IN (%s) ".formatted(args.getFirst(), String.join(", ", parameters));

        setRes(res);
        return res;
    }
}
