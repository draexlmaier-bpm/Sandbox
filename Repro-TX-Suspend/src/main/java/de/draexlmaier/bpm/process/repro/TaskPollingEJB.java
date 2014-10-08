package de.draexlmaier.bpm.process.repro;

import static javax.ejb.TransactionManagementType.BEAN;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.task.Task;
import org.jboss.logging.Logger;

@Singleton
@TransactionManagement(BEAN)
public class TaskPollingEJB implements TaskPollingEJBLocal
{
    @Inject
    private BusinessProcess businessProcess;

    @Inject
    private TaskService taskService;

    @Resource
    private UserTransaction tx;

    private static Logger logger = Logger.getLogger(TaskPollingEJB.class);

    @Override
    public void pollNow()
    {
        for(final Task task : this.taskService.createTaskQuery().list())
        {
            try
            {
                handleTaskInternal(task);
            }
            catch(final NotSupportedException | SystemException ex)
            {
                logger.error("Oops!", ex);
            }
        }
    }

    protected boolean handleTask(final Task task)
    {
        return true;
    }

    private void handleTaskInternal(final Task task) throws NotSupportedException, SystemException
    {
        this.tx.begin();

        // Associate task (needed in internal code later)
        this.businessProcess.setTask(task);

        try
        {
            // Do the real work
            final boolean result = handleTask(task);

            // Flush data
            this.businessProcess.flushVariableCache();
            this.businessProcess.saveTask();

            if(result)
            {
                this.taskService.complete(task.getId());
            }

            this.tx.commit();
        }
        catch(final Exception ex)
        {
            this.tx.rollback();

            logIncident(task, ex);
        }
        finally
        {
            // Disassociate task
            this.businessProcess.stopTask();
        }
    }

    private void logIncident(final Task task, final Exception ex)
    {
        IncidentLogger.logIncident(task.getExecutionId(), "polling.exception", ex.getMessage());
    }
}
