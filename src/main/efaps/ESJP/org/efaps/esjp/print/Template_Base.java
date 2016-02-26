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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.event.Return;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.db.PrintQuery;
import org.efaps.esjp.ci.CIPrint;
import org.efaps.util.EFapsException;

import simple.escp.SimpleEscp;
import simple.escp.json.JsonTemplate;

/**
 * The Class Template_Base.
 *
 * @author The eFaps Team
 */
@EFapsUUID("9816de5a-d96c-45c2-9ebc-4d914664dd6f")
@EFapsApplication("eFapsApp-Print")
public abstract class Template_Base
{

    /**
     * Test print.
     *
     * @param _parameter Parameter as passed by the eFaps API
     * @return the return
     * @throws EFapsException on error
     */
    public Return testPrint(final Parameter _parameter)
        throws EFapsException
    {
        final Instance templInst = _parameter.getInstance();
        if (templInst.isValid() && templInst.getType().isCIType(CIPrint.TemplateESCP)) {
            final PrintQuery print = new PrintQuery(templInst);
            print.addAttribute(CIPrint.TemplateESCP.Template);
            print.execute();
            final String templStr = print.getAttribute(CIPrint.TemplateESCP.Template);
            final JsonTemplate template = new JsonTemplate(templStr);

            final Map<String, Object> value = new HashMap<>();
            value.put("invoiceNo", "INVC-00001");
            final List<Map<String, Object>> tables = new ArrayList<>();
            for (int i=0; i<5; i++) {
                final Map<String, Object> line = new HashMap<>();
                line.put("code", String.format("CODE-%d", i));
                line.put("name", String.format("Product Random %d", i));
                line.put("qty", String.format("%d", i*i));
                tables.add(line);
            }
            value.put("table_source", tables);

            final SimpleEscp simpleEscp = new SimpleEscp();
            simpleEscp.print(template, value);
        }
        return new Return();
    }
}
