package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class OrderByFunction extends Function {

    public OrderByFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "OrderBy");

        String res = "ORDER BY %s ".formatted(String.join(", ", args));

        setRes(res);
        return res;
    }
}
