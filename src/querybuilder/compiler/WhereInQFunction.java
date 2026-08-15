package querybuilder.compiler;

import lombok.Getter;

import querybuilder.validator.Query;

import java.util.List;

public class WhereInQFunction extends Function {

    private String res = "";

    private static final String PATTERN_PART_1 = "WHERE ";
    private static final String PATTERN_PART_2 = "IN ";
    @Getter private String subQuery;

    public WhereInQFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        List<String> args;
        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereInQ");
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
