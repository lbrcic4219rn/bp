package queryBuilder.compiler;

import queryBuilder.validator.Query;

import java.util.ArrayList;

public class SelectFunction extends Function{

    String pattern = "SELECT ";
    String res = "";// krajnji rezultat ce biti npr: SELECT MANAGER_ID, LOCATION_ID

    public SelectFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {

        int idx = getIndexInStringWhereStarts(q.getContent(), "Select");

        ArrayList<String> columns;
        columns = getArgsThatAreNotAllString(idx, q.getContent());

        this.res += pattern;

        for(int i = 0; i < columns.size(); i++){
            if(i != columns.size()-1){
                this.res += columns.get(i);
                this.res += ", ";
            }else{
                this.res += columns.get(i);
                this.res += " ";
            }
        }
        setRes(res);
        return this.res;
    }

    public String getRes() {
        return res;
    }
}
