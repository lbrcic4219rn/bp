package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class QueryFunction extends Function{

   private String pattern = "FROM ";
   private String res = ""; // uvek je na kraju formata npr:  FROM Departments ;
   private String tableName;

    public QueryFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(), "Query");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        this.res += this.pattern;
        this.res += args.get(0);
        this.res += " ";

        setRes(res);
        this.tableName = args.get(0);
        return res;
    }

    public String getRes() {
        return res;
    }

    public String getTableName() {
        return tableName;
    }
}
