package querybuilder.validator.rule;

final class FunctionArguments {

    private FunctionArguments() {}

    static String[] of(String content, String function) {
        int start = content.indexOf(function);
        String functionWithArgs = content.substring(start, content.indexOf(')', start) + 1);
        return functionWithArgs
                .substring(functionWithArgs.indexOf('(') + 1, functionWithArgs.indexOf(')'))
                .replaceAll("\\s", "")
                .split(",");
    }
}
