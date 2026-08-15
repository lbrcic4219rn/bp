package queryBuilder.validator;

import java.util.ArrayList;
import java.util.Objects;

public class Query {

    private String name;
    private String content;
    private ArrayList<String> functions = new ArrayList<>();
    private ArrayList<String> validFucntions = new ArrayList<>();

    public Query(String str){
        this.content = str;
    }

    @Override
    public boolean equals(Object o) {
        if(((Query) o).getName().equals(this.getName())){
            return true;
        }else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFunctions() {
        return functions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunctions(ArrayList<String> functions) {
        this.functions = functions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setValidFucntions(ArrayList<String> validFucntions) {
        this.validFucntions = validFucntions;
    }

    public ArrayList<String> getValidFucntions() {
        return validFucntions;
    }
}
