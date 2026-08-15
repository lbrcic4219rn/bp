package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class WhereInFunction extends Function{
    private String pattern_part1 = "WHERE ";
    private String patter_part2 = "IN ";
    private String res = "";

    public WhereInFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        ArrayList<String> args_where_in;

        int idx = getIndexInStringWhereStarts(q.getContent(), "WhereIn");
        args_where_in = getArgsThatAreNotAllString(idx, q.getContent());

        idx = getIndexInStringWhereStarts(q.getContent(), "ParametarList");
        ArrayList<String> args;
        args = getArgsThatAreNotAllString(idx, q.getContent());

        res += this.pattern_part1;
        res += args_where_in.get(0);
        res += " ";
        res += this.patter_part2;
        res += "(";

        for(int i = 0; i < args.size(); i++){
            if(i != args.size()-1){
                this.res += args.get(i);
                this.res += ", ";
            }else{
                this.res += args.get(i);
            }
        }
        res += ") ";
        setRes(res);
        return res;
    }
}
