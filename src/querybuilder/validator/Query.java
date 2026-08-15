package querybuilder.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Query {

    private String name;
    private String content;
    private List<String> functions = new ArrayList<>();
    private List<String> validFucntions = new ArrayList<>();

    public Query(String str) {
        this.content = str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Query other)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public List<String> getFunctions() {
        return functions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunctions(List<String> functions) {
        this.functions = functions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setValidFucntions(List<String> validFucntions) {
        this.validFucntions = validFucntions;
    }

    public List<String> getValidFucntions() {
        return validFucntions;
    }
}
