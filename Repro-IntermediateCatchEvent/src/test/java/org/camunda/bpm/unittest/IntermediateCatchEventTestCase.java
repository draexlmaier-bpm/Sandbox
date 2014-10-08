package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IntermediateCatchEventTestCase
{
    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    private RuntimeService runtimeService;
    private TaskService taskService;

    @Before
    public void setup()
    {
        this.runtimeService = this.rule.getRuntimeService();
        this.taskService = this.rule.getTaskService();
    }

    @After
    public void shutdown()
    {
        for(final ProcessInstance pi : this.runtimeService.createProcessInstanceQuery().list())
        {
            this.runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "Shutdown", true);
        }
    }

    @Test
    @Deployment(resources = { "testIntermediateCatchEventProcess.bpmn" })
    public void testIntermediateCatchEventProcess()
    {
        this.runtimeService.startProcessInstanceByKey("testIntermediateCatchEventProcess");

        this.runtimeService.messageEventReceived("AbortEvent", this.taskService.createTaskQuery().singleResult()
                .getExecutionId());
    }

}
