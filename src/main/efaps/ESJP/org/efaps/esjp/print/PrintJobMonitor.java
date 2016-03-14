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
@EFapsUUID("f8f81987-9b7c-423e-9e35-5b7cc1441ba2")
@EFapsApplication("eFapsApp-Print")
public class PrintJobMonitor
    extends PrintJobMonitor_Base
{

    /**
     * Instantiates a new prints the job monitor.
     *
     * @param _parameter the _parameter
     * @param _printerInst the _printer inst
     * @param _objInst the _obj inst
     * @throws EFapsException the e faps exception
     */
    public PrintJobMonitor(final Parameter _parameter,
                           final Instance _printerInst,
                           final Instance _objInst)
        throws EFapsException
    {
        super(_parameter, _printerInst, _objInst);
    }
}
