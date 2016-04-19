package de.draexlmaier.bpm.repro;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

public class TestCase
{
    private static final String THE_USER = "theUser";

    @Rule
    public ProcessEngineRule engineRule = new ProcessEngineRule();

    @Test
    @Deployment(resources = "process.bpmn")
    public void test() throws InterruptedException
    {
        final ProcessEngine processEngine = this.engineRule.getProcessEngine();
        final TaskService taskService = processEngine.getTaskService();
        final RuntimeService runtimeService = processEngine.getRuntimeService();

        // Start a process
        final String processInstanceId = runtimeService.startProcessInstanceByKey("process").getId();

        // TaskService.setAssignee() => This works
        Task task = taskService.createTaskQuery().singleResult();
        setAssigneeDirectly(taskService, task);
        validateAssignee(processEngine, processInstanceId);

        taskService.setAssignee(task.getId(), null);

        // TaskService.saveTask() => This works NOT
        task = taskService.createTaskQuery().singleResult();
        setAssigneeViaSaveTask(taskService, task);
        validateAssignee(processEngine, processInstanceId);
    }

    private static void validateAssignee(final ProcessEngine processEngine, final String processInstanceId)
    {
        // Validate Task entities
        final List<Task> tasks =
                processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
        for(final Task task : tasks)
        {
            assertEquals(THE_USER, task.getAssignee());
        }

        // Validate HistoricActivityInstance entities
        final List<HistoricActivityInstance> activityInstances =
                processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId).unfinished().list();
        for(final HistoricActivityInstance activityInstance : activityInstances)
        {
            if(!"userTask".equals(activityInstance.getActivityType()))
            {
                continue;
            }

            assertEquals(THE_USER, activityInstance.getAssignee());
        }
    }

    private static void setAssigneeDirectly(final TaskService taskService, final Task task)
    {
        taskService.setAssignee(task.getId(), THE_USER);
    }

    private static void setAssigneeViaSaveTask(final TaskService taskService, final Task task)
    {
        task.setAssignee(THE_USER);
        taskService.saveTask(task);
    }
}