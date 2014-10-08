package de.draexlmaier.bpm.process.repro;

import javax.inject.Inject;

import org.camunda.bpm.engine.RuntimeService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.draexlmaier.bpm.util.DependencyResolver;
import de.draexlmaier.bpm.util.WebArchiveBuilder;

@RunWith(Arquillian.class)
public class ProcessTestTxExceptions
{
    @Deployment
    public static WebArchive createDeployment()
    {
        return new WebArchiveBuilder("Repro-Tx-Suspend.war")//
                .addPackages("de.draexlmaier.bpm")//
                .addLibraries(DependencyResolver.resolveMavenDependencies(true, false))//
                .addBeansXml("META-INF/beans.xml")//
                .addProcessesXml("META-INF/processes.xml")//
                .addBpmnProcessFiles()//
                .getWebArchive();
    }

    @Inject
    private TaskPollingEJBLocal taskPollingEJB;

    @Inject
    RuntimeService runtimeService;

    @Test
    public void testIt()
    {
        this.runtimeService.startProcessInstanceByKey("reproProcessTx");
        this.runtimeService.startProcessInstanceByKey("reproProcessTx");

        this.taskPollingEJB.pollNow();
    }
}
