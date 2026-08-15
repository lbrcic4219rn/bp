package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class DateFunction extends Function {
    // String s = "SELECT last_name, hire_date FROM employees WHERE hire_date >
    // CONVERT(DATETIME,'17/06/2003', 103)";//primer generisanog sql-a za datum
    String pattern = "CONVERT(DATETIME,'"; // CONVERT(DATETIME,'17/06/2003', 103)
    String res = "";

    public DateFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) { // Date("dd-mm-yyyy", "alias")
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
