package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class AndWhereFunction extends Function {

    public AndWhereFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "AndWhere");

        String value = quoteIfLike(args.get(1), args.get(2));
        String res = "AND %s %s %s ".formatted(args.getFirst(), args.get(1), value);

        setAlias(args.get(2));
        setAliasPosition(0);
        setRes(res);
        return res;
    }
}
