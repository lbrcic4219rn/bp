package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class WhereEqQFunction extends Function {

    private String res = "";

    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "= ";
    @Getter private String subQuery;

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
}
