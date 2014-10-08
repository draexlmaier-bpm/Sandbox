package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.RuntimeService;
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

    @Before
    public void setup()
    {
        this.runtimeService = this.rule.getRuntimeService();
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
    }

}
