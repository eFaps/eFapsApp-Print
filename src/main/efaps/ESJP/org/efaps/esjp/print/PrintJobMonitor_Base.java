/*
 * Copyright 2003 - 2016 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.efaps.esjp.print;

import javax.print.event.PrintJobEvent;

import org.efaps.admin.datamodel.Status;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.ci.CIStatus;
import org.efaps.db.Context;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.db.Update;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.esjp.erp.Naming;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("f8f81987-9b7c-423e-9e35-5b7cc1441ba2")
@EFapsApplication("eFapsApp-Print")
public abstract class PrintJobMonitor_Base
    implements javax.print.event.PrintJobListener
{
    /**
     * Logger used in this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(PrintJobMonitor.class);

    /** The completed. */
    private boolean completed = false;

    /** The job instance. */
    private final Instance jobInstance;

    /**
     * Instantiates a new prints the job monitor_ base.
     *
     * @param _parameter the _parameter
     * @param _printerInst the _printer inst
     * @param _objInst the _obj inst
     * @throws EFapsException the e faps exception
     */
    public PrintJobMonitor_Base(final Parameter _parameter,
                                final Instance _printerInst,
                                final Instance _objInst)
        throws EFapsException
    {
        final Insert insert = new Insert(CIPrint.Job);
        insert.add(CIPrint.Job.PrinterLink, _printerInst);
        if (_objInst != null && _objInst.isValid()) {
            insert.add(CIPrint.Job.GenInstId, _objInst.getGeneralId());
        }
        insert.add(CIPrint.Job.Name, new Naming().fromNumberGenerator(_parameter, CIPrint.Job.getType().getName()));
        insert.add(CIPrint.Job.Status, Status.find(CIPrint.JobStatus.Open));
        insert.executeWithoutAccessCheck();
        this.jobInstance = insert.getInstance();
        Context.save();
    }

    @Override
    public void printDataTransferCompleted(final PrintJobEvent _arg0)
    {
    }

    @Override
    public void printJobCanceled(final PrintJobEvent _arg0)
    {
        signalCompletion(CIPrint.JobStatus.Canceled);
    }

    @Override
    public void printJobCompleted(final PrintJobEvent _arg0)
    {
        signalCompletion(CIPrint.JobStatus.Completed);
    }

    @Override
    public void printJobFailed(final PrintJobEvent _arg0)
    {
        signalCompletion(CIPrint.JobStatus.Failed);
    }

    @Override
    public void printJobNoMoreEvents(final PrintJobEvent _event)
    {
        signalCompletion(CIPrint.JobStatus.Completed);
    }

    @Override
    public void printJobRequiresAttention(final PrintJobEvent _arg0)
    {

    }

    /**
     * Signal completion.
     *
     * @param _status the _status
     */
    protected void signalCompletion(final CIStatus _status)
    {
        synchronized (PrintJobMonitor_Base.this) {
            try {
                final Update update = new Update(this.jobInstance);
                update.add(CIPrint.Job.Status, Status.find(_status));
                update.executeWithoutAccessCheck();
                Context.save();
            } catch (final EFapsException e) {
                LOG.error("Catched exception", e);
            }
            this.completed = true;
            PrintJobMonitor_Base.this.notify();
        }
    }

    /**
     * Wait for job completion.
     */
    public synchronized void waitForJobCompletion()
    {
        try {
            while (!this.completed) {
                wait();
            }
        } catch (final InterruptedException e) {
            LOG.error("Catched exception", e);
        }
    }
}
