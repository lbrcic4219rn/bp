package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class AvgFunction extends Function {

    public AvgFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "Avg");

        String res = "AVG(%s) AS %s ".formatted(args.getFirst(), args.get(1));

        setAlias(args.get(1));
        setRes(res);
        return res;
    }
}
