package querybuilder.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Query {

    private String name;
    private String content;
    private List<String> functions = new ArrayList<>();
    private List<String> validFunctions = new ArrayList<>();

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
}
