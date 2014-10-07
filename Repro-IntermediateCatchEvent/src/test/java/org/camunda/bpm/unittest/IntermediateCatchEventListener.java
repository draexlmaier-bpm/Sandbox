package org.camunda.bpm.unittest;

import static org.junit.Assert.fail;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class IntermediateCatchEventListener implements JavaDelegate
{

    public void execute(final DelegateExecution arg0) throws Exception
    {
        fail("Message Listener was executed but no message was ever sent!");
    }

}
