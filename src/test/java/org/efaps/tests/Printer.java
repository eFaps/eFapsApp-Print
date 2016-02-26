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

package org.efaps.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.lang3.ArrayUtils;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
public class Printer
{

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final AttributeSet aset = new HashAttributeSet();
        aset.add(new PrinterName("HP-LaserJet-100-colorMFP-M175nw", null));
        final PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, aset);
        for (final PrintService serv : pservices) {
            System.out.println(serv);
        }

        try {
            final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // pras.add(new Copies(2));

            final PrintService pservice = getPrintService4Name("HP-LaserJet-100-colorMFP-M175nw");
            pservice.getSupportedDocFlavors();
            final DocPrintJob job = pservice.createPrintJob();

            final DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            final String filename = "/home/janmoxter/Downloads/Notas_de_Pedido_null.DOCX";
            final FileInputStream fis = new FileInputStream(filename);
            final DocAttributeSet das = new HashDocAttributeSet();
            final Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final PrintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static PrintService getPrintService4Name(final String _name)
    {
        PrintService ret = null;
        final PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);
        if (!ArrayUtils.isEmpty(pservices)) {
            for (final PrintService serv : pservices) {
                if (serv.getName().equals(_name)) {
                    ret = serv;
                    break;
                }
            }
        }
        return ret;
    }

}
