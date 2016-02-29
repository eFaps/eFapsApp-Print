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

import java.util.ArrayList;
import java.util.List;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("170e0ffb-42d1-4051-88a7-36703ca4e650")
@EFapsApplication("eFapsApp-Print")
public abstract class Printer_Base
{

    /**
     * Gets the printer inst4 template.
     *
     * @param _parameter the _parameter
     * @param _templateInst the _template inst
     * @return the printer inst4 template
     * @throws EFapsException the e faps exception
     */
    public static List<Instance> getPrinterInst4Template(final Parameter _parameter,
                                                         final Instance _templateInst)
        throws EFapsException
    {
        final List<Instance> ret = new ArrayList<>();
        final QueryBuilder queryBldr = new QueryBuilder(CIPrint.Template2Printer);
        queryBldr.addWhereAttrEqValue(CIPrint.Template2Printer.FromLink, _templateInst);
        final SelectBuilder selPrintInst = SelectBuilder.get().linkto(CIPrint.Template2Printer.ToLink).instance();
        final MultiPrintQuery multi = queryBldr.getPrint();
        multi.addSelect(selPrintInst);
        multi.executeWithoutAccessCheck();
        while (multi.next()) {
            ret.add(multi.<Instance>getSelect(selPrintInst));
        }
        return ret;
    }
}
