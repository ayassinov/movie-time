package com.ninjas.movietime.batch.cli;

import com.ninjas.movietime.core.domain.api.TaskEnum;

/**
 * @author ayassinov on 28/07/15.
 */
public class ArgumentParser {

    private static ArgumentParser CONTEXT = new ArgumentParser();

    private ArgumentParser() {
    }

    public static ArgumentParser instance() {
        return CONTEXT;
    }

    public TaskEnum parseTaskArgument(String[] args) {
        if (args.length == 0)
            throw new NotValidTaskNameException("Program argument is empty. -t argument is mandatory");

        for (String arg : args) {
            if (arg.startsWith("-t") || arg.startsWith("-T")) {
                final String[] split = arg.split("=");
                if (split.length == 2) {
                    return TaskEnum.valueOf(split[1].toUpperCase().trim());
                }
            }
        }

        throw new NotValidTaskNameException("No -t (Task name) argument found.");
    }

}
