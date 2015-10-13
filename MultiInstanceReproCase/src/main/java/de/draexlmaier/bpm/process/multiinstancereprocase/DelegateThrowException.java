package de.draexlmaier.bpm.process.multiinstancereprocase;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DelegateThrowException implements JavaDelegate
{
    @Override
    public void execute(final DelegateExecution execution) throws Exception
    {
        final String counter = (String) execution.getVariable("counter");
        if("3".equals(counter))
        {
            throw new RuntimeException("Counter 3 not allowed!");
        }
    }
}
