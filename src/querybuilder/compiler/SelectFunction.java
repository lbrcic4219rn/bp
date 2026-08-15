package querybuilder.compiler;

import querybuilder.validator.Query;

import java.util.List;

public class SelectFunction extends Function {

    String pattern = "SELECT ";
    String res = ""; // krajnji rezultat ce biti npr: SELECT MANAGER_ID, LOCATION_ID

    public SelectFunction(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected String parseQuery(Query q) {
        int idx = getIndexInStringWhereStarts(q.getContent(), "Select");
        List<String> columns = getArgsThatAreNotAllString(idx, q.getContent());

        StringBuilder sb = new StringBuilder(pattern);
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i)).append(i != columns.size() - 1 ? ", " : " ");
        }

        res = sb.toString();
        setRes(res);
        return res;
    }

    public String getRes() {
        return res;
    }
}
