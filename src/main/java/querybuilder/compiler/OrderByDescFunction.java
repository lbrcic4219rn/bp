package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrderByDescFunction extends Function {

    public OrderByDescFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "OrderByDesc");

        String res = "ORDER BY %s  DESC ".formatted(String.join(", ", args));

        setRes(res);
        return res;
    }
}
