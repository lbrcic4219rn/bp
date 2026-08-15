package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class GroupByFunction extends Function {

    public GroupByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "GroupBy");

        String res = "GROUP BY %s ".formatted(args.getFirst());

        setRes(res);
        return res;
    }
}
