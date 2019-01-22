package de.draexlmaier.bpm.process.multiinstancetaskreprocase;

import javax.inject.Inject;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.task.Task;

public class Listener4 implements JavaDelegate
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

        // the code above does not work for MultiInstanceTasks when the timer is fired for than one Task at the same time
        // How can i evaluate the timerJob (and the taskTimedOut) for the current DelegateExecution "execution" ?
        // How can i forward the taskId to the Service Task "SendMail2" in the Event Sub Process "SubProcessEscalation" ?
        // What would the best practice here?
    }
}
