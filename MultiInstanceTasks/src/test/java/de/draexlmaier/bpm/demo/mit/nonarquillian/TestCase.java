package de.draexlmaier.bpm.demo.mit.nonarquillian;

import static java.lang.String.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.collections.RingCursor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import de.draexlmaier.bpm.demo.mit.ProcessConstants;

public class TestCase implements ProcessConstants
{
    private static final String THE_USER = "theUser";

    private static final int TASK_COUNT = 3;

    @Rule
    public ProcessEngineRule engineRule = new ProcessEngineRule();

    private String theUser;

    @Test
    @Deployment(resources = "process.bpmn")
    public void test() throws InterruptedException
    {
        final ProcessEngine processEngine = this.engineRule.getProcessEngine();

        // Start a process
        final String processInstanceId =
                processEngine
                        .getRuntimeService()
                        .startProcessInstanceByKey(PROCESS_DEFINITION_KEY,
                                Collections.<String, Object> singletonMap(VAR_ITEMS, getMultiInstanceItems())).getId();

        // Query tasks
        final List<Task> tasks = processEngine.getTaskService().createTaskQuery().list();

        assertNotNull(tasks);
        assertEquals(TASK_COUNT, tasks.size());

        for(final Task task : tasks)
        {
            processEngine.getTaskService().setAssignee(task.getId(), THE_USER);
        }

        final List<HistoricActivityInstance> activityInstances =
                processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId).unfinished().list();

        assertEquals(TASK_COUNT + 1, activityInstances.size());

        for(final HistoricActivityInstance activityInstance : activityInstances)
        {
            if(!"userTask".equals(activityInstance.getActivityType()))
            {
                continue;
            }

            assertEquals(THE_USER, activityInstance.getAssignee());
        }
    }

    private static List<String> getMultiInstanceItems()
    {
        final List<String> result = new ArrayList<String>(TASK_COUNT);

        for(int i = 1; i <= TASK_COUNT; ++i)
        {
            result.add(valueOf(i));
        }

        return result;
    }

    private static <E> List<E> take(final RingCursor<E> ringCursor, final int count)
    {
        final List<E> result = new ArrayList<E>(count);

        for(int i = 0; i < count; ++i)
        {
            result.add(ringCursor.next());
        }

        return result;
    }

}
