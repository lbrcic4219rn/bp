package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class DateFunction extends Function {

    public DateFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args = extractArguments(q.getContent(), "Date");

        String res = "CONVERT(DATETIME,'%s',103)".formatted(args.getFirst());

        setAlias(args.get(1));
        setRes(res);
        return res;
    }
}
