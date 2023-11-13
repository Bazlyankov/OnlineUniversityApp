package com.studentsystemapp.util;

import java.util.List;

public class TaskDescriptions {

    private final static String TASK_ONE_DESCRIPTION = "Draw the graph of the function x^2-3x+2 for -1<x<1";
    private final static String TASK_TWO_DESCRIPTION = "Find the derivative of the function x^2-3x+2";
    private final static String TASK_THREE_DESCRIPTION = "Integrate the function x^2-3x+2 in bounds -1<x<1";

    public final static List<String> taskDescriptions = List.of(TASK_ONE_DESCRIPTION, TASK_TWO_DESCRIPTION, TASK_THREE_DESCRIPTION);
}
