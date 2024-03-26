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
package org.efaps.esjp.print.eql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.SelectBuilder;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.esjp.common.eql.AbstractSelect;
import org.efaps.util.EFapsException;

/**
 * The Class PrintInfoSelect_Base.
 *
 * @author The eFaps Team
 */
@EFapsUUID("c1febf3d-39b7-47a2-b521-df8adce57f5c")
@EFapsApplication("eFapsApp-Print")
public abstract class PrintInfoSelect_Base
    extends AbstractSelect
{

    @Override
    public void initialize(final List<Instance> _instances,
                           final String... _parameters)
        throws EFapsException
    {
        final List<Long> generalIds = new ArrayList<>();
        for (final Instance instance : _instances) {
            generalIds.add(instance.getGeneralId());
        }
        final Map<Long, String> generalId2Val = new HashMap<>();
        final QueryBuilder queryBldr = new QueryBuilder(CIPrint.JobAbstract);
        queryBldr.addWhereAttrEqValue(CIPrint.JobAbstract.GenInstId, generalIds.toArray());
        final MultiPrintQuery multi = queryBldr.getPrint();
        final SelectBuilder selPrinterName = SelectBuilder.get().linkto(CIPrint.JobAbstract.PrinterLink).attribute(
                        CIPrint.PrinterAbstract.Name);
        multi.addSelect(selPrinterName);
        multi.addAttribute(CIPrint.JobAbstract.Name, CIPrint.JobAbstract.GenInstId);
        multi.execute();
        while (multi.next()) {
            final Long genInstId = multi.getAttribute(CIPrint.JobAbstract.GenInstId);
            final String jobName = multi.getAttribute(CIPrint.JobAbstract.Name);
            final String printerName = multi.getSelect(selPrinterName);
            final StringBuilder val = new StringBuilder();
            if (generalId2Val.containsKey(genInstId)) {
                val.append(generalId2Val.get(genInstId)).append(", ");
            }
            val.append(jobName).append(" ").append(printerName);
            generalId2Val.put(genInstId, val.toString());
        }
        for (final Instance instance : _instances) {
            if (generalId2Val.containsKey(instance.getGeneralId())) {
                getValues().put(instance, generalId2Val.get(instance.getGeneralId()));
            }
        }
    }
}
