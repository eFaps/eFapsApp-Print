package org.efaps.esjp.print.printnode;

import java.util.List;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Context;
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

    public void printRaw(final String _printerKey, final String _content)
        throws EFapsException
    {
        final RestClient client = new RestClient();
        final List<PrinterDto> printers = client.printers();
        for (final PrinterDto printer : printers) {
            if (printer.getName().equals(_printerKey)) {
                client.printJobs(printer.getId(), _content);
                break;
            }
        }
    }
}
