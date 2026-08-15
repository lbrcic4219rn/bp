package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereBetweenFunction extends Function {

    public WhereBetweenFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "WhereBetween");

        String res =
                "WHERE %s BETWEEN  %s AND  %s "
                        .formatted(args.getFirst(), args.get(1), args.get(2));

        setRes(res);
        return res;
    }
}
