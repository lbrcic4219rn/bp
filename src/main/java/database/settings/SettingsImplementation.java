package database.settings;

import java.util.HashMap;
import java.util.Map;

public class SettingsImplementation implements Settings {

    private final Map<String, String> parameters = new HashMap<>();

    @Override
    public String getParameter(String parameter) {
        return this.parameters.get(parameter);
    }

    @Override
    public void addParameter(String parameter, String value) {
        this.parameters.put(parameter, value);
    }
}
