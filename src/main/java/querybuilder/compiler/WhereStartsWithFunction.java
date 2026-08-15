package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereStartsWithFunction extends Function {

    public WhereStartsWithFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "WhereStartsWith");

        String pattern = args.get(1) + "%";
        String res = "WHERE %s LIKE '%s'".formatted(args.getFirst(), pattern);

        setRes(res);
        return res;
    }
}
