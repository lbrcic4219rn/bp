package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class DateFunction extends Function {

    String pattern = "CONVERT(DATETIME,'";
    String res = "";

    public DateFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "Date");
        List<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern;
        res += args.getFirst() + "',";
        res += "103" + ")";

        setAlias(args.get(1));
        setRes(res);
        return res;
    }
}
