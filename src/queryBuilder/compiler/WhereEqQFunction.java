package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class WhereEqQFunction extends Function{

    private String res = "";//krajnji rez je formata npr: where job_id =; znaci bez zagrada i uopste celog naziva subquery-ja;to se setuje u compiler-u
    private String pattern_part1 = "WHERE ";
    private String pattern_part2 = "= ";
    private String sub_query;

    public WhereEqQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        ArrayList<String> args;
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereEqQ");
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern_part1;
        res += args.get(0);
        res += " ";
        res += this.pattern_part2;

        this.sub_query = args.get(1);
        setRes(res);
        return res;
    }

    public String getSub_query() {
        return sub_query;
    }
}
