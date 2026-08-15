package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class SelectFunction extends Function {

    public SelectFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "Select");

        String res = "SELECT %s ".formatted(String.join(", ", args));

        setRes(res);
        return res;
    }
}
