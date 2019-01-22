package de.draexlmaier.bpm.process.multiinstancetaskreprocase;

import javax.inject.Inject;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.task.Task;

public class Listener3 implements JavaDelegate
{
    @Inject
    private ProcessEngine processEngine;

    @Override
    public void execute(final DelegateExecution execution) throws Exception
    {
        final Job timerJob =
                this.processEngine.getManagementService().createJobQuery().processInstanceId(execution.getProcessInstanceId())
                        .activityId(execution.getCurrentActivityId()).singleResult();
        final Task taskTimedOut =
                this.processEngine.getTaskService().createTaskQuery().executionId(timerJob.getExecutionId()).singleResult();

        final String taskId = taskTimedOut.getId();

        // some code to forward the TaskId to the Service Task "SendMail2" in the Event Sub Process "SubProcessEscalation"
        // maybe as Processvariable or IO Mapping ???????
        // What would the best practice here?
    }
}
