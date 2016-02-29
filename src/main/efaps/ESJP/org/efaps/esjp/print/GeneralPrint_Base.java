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
import java.util.Properties;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.event.Return.ReturnValues;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.MultiPrintQuery;
import org.efaps.esjp.ci.CIFormPrint;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.esjp.common.AbstractCommon;
import org.efaps.esjp.common.uiform.Field;
import org.efaps.esjp.common.uiform.Field_Base.DropDownPosition;
import org.efaps.esjp.print.util.Print;
import org.efaps.util.EFapsException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@EFapsUUID("3ba9d01c-f0fe-4238-bccc-8fc3e2c49fae")
@EFapsApplication("eFapsApp-Print")
public abstract class GeneralPrint_Base
    extends AbstractCommon
{

    /**
     * Print for instance.
     *
     * @param _parameter the _parameter @return the return @throws
     * EFapsException the e faps exception
     */
    public Return print4Instance(final Parameter _parameter)
        throws EFapsException
    {
        final Instance objInst = _parameter.getInstance();
        if (objInst != null && objInst.isValid()) {
            final Properties props = Print.GENERALPRINT4INSTANCE.get();
            final String type = objInst.getType().getName();
            final String tmpl = props.getProperty(type + ".Template");

            final Instance templateInst = Template.getTemplateInst4Name(_parameter, tmpl);

            final Instance printerInst = Instance.get(
                            _parameter.getParameterValue(CIFormPrint.Print_GeneralPrint4InstanceForm.printer.name));
            if (printerInst != null && printerInst.isValid()) {
                new Template().print(_parameter, templateInst, objInst, printerInst);
            }
        }
        return new Return();
    }

    /**
     * Gets the printer ui field value for instance.
     *
     * @param _parameter the _parameter
     * @return the printer ui field value4 instance
     * @throws EFapsException the e faps exception
     */
    public Return getPrinterUIFieldValue4Instance(final Parameter _parameter)
        throws EFapsException
    {
        final List<DropDownPosition> values = new ArrayList<DropDownPosition>();
        final Instance objInst = _parameter.getInstance();
        if (objInst != null && objInst.isValid()) {
            final Properties props = Print.GENERALPRINT4INSTANCE.get();
            final String type = objInst.getType().getName();
            final String tmpl = props.getProperty(type + ".Template");
            final Instance templateInst = Template.getTemplateInst4Name(_parameter, tmpl);
            final List<Instance> printerInst = Printer.getPrinterInst4Template(_parameter, templateInst);
            final MultiPrintQuery multi = new MultiPrintQuery(printerInst);
            multi.addAttribute(CIPrint.PrinterAbstract.Name);
            multi.execute();
            while (multi.next()) {
                values.add(new Field().getDropDownPosition(_parameter, multi.getCurrentInstance().getOid(),
                                multi.getAttribute(CIPrint.PrinterAbstract.Name)));
            }
        }
        final Return ret = new Return();
        ret.put(ReturnValues.VALUES, values);
        return ret;
    }
}
