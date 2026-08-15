package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class WhereEqQFunction extends Function {

    private String res =
            ""; // krajnji rez je formata npr: where job_id =; znaci bez zagrada i uopste celog
    // naziva subquery-ja;to se setuje u compiler-u
    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "= ";
    private String subQuery;

    public WhereEqQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        List<String> args;
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereEqQ");
        args = getArgsThatAreNotAllString(idx, q.getContent());

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
