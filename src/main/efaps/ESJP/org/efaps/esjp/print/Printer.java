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

import java.util.List;

import org.efaps.admin.event.Parameter;
import org.efaps.admin.program.esjp.EFapsApplication;
import org.efaps.admin.program.esjp.EFapsUUID;
import org.efaps.db.Instance;
import org.efaps.util.EFapsException;

/**
 * This class must be replaced for customization, therefore it is left empty.
 * Functional description can be found in the related "<code>_Base</code>"
 * class.
 *
 * @author The eFaps Team
 */
@EFapsUUID("66b2bf0a-463c-4caf-9f55-5efdd8594594")
@EFapsApplication("eFapsApp-Print")
public class Printer
    extends Printer_Base
{

    /**
     * Gets the printer inst4 template.
     *
     * @param _parameter @param _templateInst
     * @param _templateInst the _template inst
     * @return the printer inst  for template
     * @throws EFapsException the eFaps exception
     */
    public static List<Instance>  getPrinterInst4Template(final Parameter _parameter,
                                                          final Instance _templateInst)
        throws EFapsException
    {
        return Printer_Base.getPrinterInst4Template(_parameter, _templateInst);
    }
}
