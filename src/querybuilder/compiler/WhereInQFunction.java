package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereInQFunction extends Function {

    private String res =
            ""; // krajnji rez je formata npr: where job_id in; znaci bez zagrada i uopste celog
    // naziva subquery-ja;to se setuje u compiler-u
    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "IN ";
    private String subQuery;

    public WhereInQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args;
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereInQ");
        args = getArgsThatAreNotAllString(idx, q.getContent()); // podaci koji nisu pod navodnicima

        res += PATTERN_PART_1;
        res += args.getFirst();
        res += " ";
        res += PATTERN_PART_2;

        this.subQuery = args.get(1);
        setRes(res);
        return res;
    }

    public String getSubQuery() {
        return subQuery;
    }
}
