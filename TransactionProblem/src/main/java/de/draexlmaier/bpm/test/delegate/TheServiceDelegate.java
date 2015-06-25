package de.draexlmaier.bpm.test.delegate;

import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.draexlmaier.bpm.test.service.TheService;

@Named
public class TheServiceDelegate implements JavaDelegate
{
    @Inject
    private TheService theService;

    @Override
    public void execute(final DelegateExecution execution) throws Exception
    {
        execution.setVariable("data", this.theService.readData(1L));
    }
}
