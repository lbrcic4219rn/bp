package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class HavingFunction extends Function {

    public HavingFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "Having");

        String res = "HAVING %s %s %s ".formatted(args.getFirst(), args.get(1), args.get(2));

        setAlias(args.getFirst());
        setAliasPosition(0);
        setRes(res);
        return res;
    }
}
