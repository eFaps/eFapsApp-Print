/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.esjp.print.printnode;

import java.util.List;

import org.efaps.admin.datamodel.Status;
import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Context;
import org.efaps.db.Insert;
import org.efaps.db.Instance;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.esjp.print.printnode.dto.PrinterDto;
import org.efaps.esjp.print.printnode.dto.WhoamiDto;
import org.efaps.esjp.print.printnode.rest.RestClient;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EFapsUUID("0d0d7a8c-720d-4408-b0c9-851414ecbce0")
@EFapsApplication("eFapsApp-Print")
public class PrintNodeService_Base
{

    private static final Logger LOG = LoggerFactory.getLogger(PrintNodeService.class);

    public Return whoami(final Parameter _parameter)
        throws EFapsException
    {
        LOG.info("Whoami is called by {}", Context.getThreadContext().getPerson().getName());
        final RestClient client = new RestClient();
        final WhoamiDto whoami = client.whoami();
        final Return ret = new Return();
        ret.put(ReturnValues.VALUES, whoami.toString());
        return ret;
    }

    public void printRaw(final Instance _printerInst, final Instance _objInst, final String _printerKey, final String _content)
        throws EFapsException
    {
        final RestClient client = new RestClient();
        final List<PrinterDto> printers = client.printers();
        for (final PrinterDto printer : printers) {
            if (printer.getName().equals(_printerKey)) {
                final Long jobId = client.printJobs(printer.getId(), _content);

                final Insert insert = new Insert(CIPrint.Job);
                insert.add(CIPrint.Job.PrinterLink, _printerInst);
                if (_objInst != null && _objInst.isValid()) {
                    insert.add(CIPrint.Job.GenInstId, _objInst.getGeneralId());
                }
                insert.add(CIPrint.Job.Name, "PrintNode jobId: " + jobId);
                insert.add(CIPrint.Job.Status, Status.find(CIPrint.JobStatus.Completed));
                insert.executeWithoutAccessCheck();
                break;
            }
        }

    }
}
