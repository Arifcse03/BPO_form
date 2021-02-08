package xx_om_bpo;

import java.io.InputStreamReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import model.services.AppModuleImpl;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.ApplicationModule;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import oracle.jbo.server.DBTransaction;

import org.apache.myfaces.trinidad.model.UploadedFile;

public class BPO extends main {
    private RichTable sizewisel;
   
    private RichTable sizewisemultipleup;

    public BPO() {
    }
    //code to get access to the appmodule

    public ApplicationModule getAppM() {
        DCBindingContainer bindingContainer =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        //BindingContext bindingContext = BindingContext.getCurrent();
        DCDataControl dc =
            bindingContainer.findDataControl("AppModuleDataControl"); // Name of application module in datacontrolBinding.cpx
        AppModuleImpl appM = (AppModuleImpl)dc.getDataProvider();
        return appM;
    }
    AppModuleImpl appM = (AppModuleImpl)this.getAppM();


    public void bpoUpload(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
        try {
            // clearSizeBreakDownTable();
            parseFile2(file.getInputStream());
            // AdfFacesContext.getCurrentInstance().addPartialTarget(bpOHTable);
            //ViewObject re=appM.getXxOmBpoInfoTVO1();

            //  appM.getDBTransaction().commit();
            // re.clearCache();
            // re.executeQuery();;

        } catch (IOException e) {
            // TODO
        }
    } //end of bpoUpload

    public void parseFile2(java.io.InputStream file) {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(file));
        String strLine = "";
        StringTokenizer st = null;
        int lineNumber = 0, tokenNumber = 0;
        Row rw = null;

        ViewObject vo = appM.getXxOmBpoInfoTVO1();

        //read comma separated file line by line
        try {

            while ((strLine = reader.readLine()) != null) {
                lineNumber++;
                // create a new row skip the header (header has linenumber 1)
                if (lineNumber > 1) {
                    rw = MakeLinesClone();
                }

                //break comma separated line using ","
                //st = new StringTokenizer(strLine, ",");
                //while (st.hasMoreTokens()){

                String[] csvCols = strLine.split(",");

                for (; tokenNumber < csvCols.length; tokenNumber++) {
                    //display csv values
                    //tokenNumber++;
                    //String theToken = st.nextToken();

                    String theToken = csvCols[tokenNumber];

                    System.out.println("Line # " + lineNumber + ", Token # " +
                                       tokenNumber + ", Token : " + theToken);

                    if (lineNumber > 1) {
                        // set Attribute Value
                        System.out.println("token number:" + tokenNumber +
                                           "  theToken :" + theToken);
                        switch (tokenNumber) {

                        case 0:
                            rw.setAttribute("FobId", theToken);
                            break;
                        case 1:
                            rw.setAttribute("Bpo", theToken);
                            break;
                        case 2:
                            rw.setAttribute("Qty", theToken);
                            break;
                        case 3:
                            rw.setAttribute("Classification", theToken);
                            break;
                        case 4:
                            rw.setAttribute("PoType", theToken);
                            break;
                        case 5:
                            rw.setAttribute("Incoterms", theToken);
                            break;
                        case 6:
                            rw.setAttribute("Shipmode", theToken);
                            break;
                        case 7:
                            rw.setAttribute("Country", theToken);
                            break;

                        case 8:
                            rw.setAttribute("Shipdate",
                                            castToJBODate(theToken));
                            break;
                        case 9:
                            rw.setAttribute("Enddate",
                                            castToJBODate(theToken));
                            break;

                        case 10:
                            rw.setAttribute("Status", theToken);
                            break;

                        }
                    }
                }

                tokenNumber = 0;
            }
            // }
        } catch (IOException e) {
            // TODO add more

        } catch (Exception e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Error in Uploaded file",
                                             e.getMessage()));
        }
    }


    public Row MakeLinesClone() {

        ViewObject cloneVo = appM.getXxOmBpoInfoTVO1();

        Row currRow = cloneVo.getCurrentRow();
        Row cloneRow = getClonLine(cloneVo);


        try {
            cloneRow.setAttribute("FobId", currRow.getAttribute("FobId"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Bpo", currRow.getAttribute("Bpo"));
        } catch (Exception e) {
            ;
        }

        try {
            cloneRow.setAttribute("Qty", currRow.getAttribute("Qty"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Classification",
                                  currRow.getAttribute("Classification"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("PoType", currRow.getAttribute("PoType"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Incoterms",
                                  currRow.getAttribute("Incoterms"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Shipmode",
                                  currRow.getAttribute("Shipmode"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Country", currRow.getAttribute("Country"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Shipdate",
                                  currRow.getAttribute("Shipdate"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Enddate", currRow.getAttribute("Enddate"));
        } catch (Exception e) {
            ;
        }
        try {
            cloneRow.setAttribute("Status", currRow.getAttribute("Status"));
        } catch (Exception e) {
            ;
        }


        return cloneRow;
    }


    public Row getClonLine(ViewObject vo) {

        Row row = vo.createRow();
        vo.insertRow(row);
        row.setNewRowState(Row.STATUS_INITIALIZED);
        return row;
    }


    public oracle.jbo.domain.Date castToJBODate(String aDate) {
        //DateFormat formatter;
        SimpleDateFormat formatter;

        java.util.Date date;

        if (aDate != null) {

            try {

                formatter = new SimpleDateFormat("MM/dd/yyyy");

                date = formatter.parse(aDate); //parse(aDate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                oracle.jbo.domain.Date jboDate =
                    new oracle.jbo.domain.Date(sqlDate);

                System.out.println("#### Date: ####" + jboDate);

                return jboDate;
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        return null;
    } // end of castToJBODate

    public void bpoUploadFormat(FacesContext facesContext,
                                OutputStream outputStream) throws IOException {
        // Add event code here...

        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

        writer.write("*Fob-Id");
        writer.write(",");
        writer.write("*Bpo");
        writer.write(",");
        writer.write("*Qty");
        writer.write(",");
        writer.write("*Classification");
        writer.write(",");
        writer.write("*Po Type");
        writer.write(",");
        writer.write("*Incoterms");
        writer.write(",");
        writer.write("Shipment Mode");
        writer.write(",");
        writer.write("Country");
        writer.write(",");
        writer.write("Ship Date(mm/dd/yy)");
        writer.write(",");
        writer.write("End Date(mm/dd/yy)");
        writer.write(",");
        writer.write("Status");
        writer.write(",");
        writer.newLine();
        writer.flush();
        writer.close();


    } // end of bpoUploadFormat


    public void sizeUpload(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
        try {
            clearSizeBreakDownTable();
            parseFile3(file.getInputStream());
            AdfFacesContext.getCurrentInstance().addPartialTarget(sizewisel);
            appM.getDBTransaction().commit();
        } catch (IOException e) {
            // TODO
        }


    } //end of sizeupload


    public void clearSizeBreakDownTable() {

        appM.getDBTransaction().commit();

        ViewObject vo = appM.getXxOmSizewiseLTVO2();
        RowSetIterator it = vo.createRowSetIterator("aa");
        while (it.hasNext()) {
            it.next().remove();
        }
        vo.executeEmptyRowSet();
        it.closeRowSetIterator();

    } // end of clearSizeBreakDownTable

    public void parseFile3(java.io.InputStream file) {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(file));
        String strLine = "";
        StringTokenizer st = null;
        int lineNumber = 0, tokenNumber = 0;
        Row rw = null;

        ViewObject vo = appM.getXxOmSizewiseLTVO2();

        //read comma separated file line by line
        try {
            while ((strLine = reader.readLine()) != null) {
                lineNumber++;
                // create a new row skip the header (header has linenumber 1)
                if (lineNumber > 1) {
                    rw = vo.createRow();
                    rw.setNewRowState(Row.STATUS_INITIALIZED);
                    vo.insertRow(rw);
                }

                //break comma separated line using ","
                st = new StringTokenizer(strLine, ",");

                double sizeProjQty = 0, sizeActualQty = 0, addDeductQty;

                while (st.hasMoreTokens()) {
                    //display csv values
                    tokenNumber++;

                    String theToken = st.nextToken();
                    System.out.println("Line # " + lineNumber + ", Token # " +
                                       tokenNumber + ", Token : " + theToken);

                    if (lineNumber > 1) {
                        // set Attribute Values
                        switch (tokenNumber) {
                        case 1:
                            rw.setAttribute("SizeName", theToken);
                            break;
                        case 2:
                            rw.setAttribute("Qty", theToken);
                            break;

                        }
                    }
                }
                //reset token number
                tokenNumber = 0;
            }
        } catch (IOException e) {
            // TODO add more
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(sizewisel.getClientId(fctx),
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                             "Content Error in Uploaded file",
                                             e.getMessage()));
        } catch (Exception e) {
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Error in Uploaded file",
                                             e.getMessage()));
        }
    }
    public void setSizewisel(RichTable sizewisel) {
        this.sizewisel = sizewisel;
    }

    public RichTable getSizewisel() {
        return sizewisel;
    }


    public void save(ActionEvent actionEvent) {
        // Add event code here...
        ViewObject vo = appM.getXxOmSizewiseLTVO2();
        appM.getDBTransaction().commit();
        vo.clearCache();
        vo.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(sizewisel);
        
        
        
        
    }

    public void sizeUploadMultiple(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
                  try {
                     // clearSizeBreakDownTable();
                      parseFile4(file.getInputStream());
                     AdfFacesContext.getCurrentInstance().addPartialTarget(sizewisemultipleup);
                      
                      appM.getDBTransaction().commit();
                     
                      AdfFacesContext.getCurrentInstance().addPartialTarget(sizewisemultipleup);
                      
                  } catch (IOException e) {
                  // TODO
                  }
    }// end of sizeUploadMultiple
    
    
    public void parseFile4(java.io.InputStream file) {
             BufferedReader reader = new BufferedReader(new InputStreamReader(file));
             String strLine = "";
             StringTokenizer st = null;
             int lineNumber = 0, tokenNumber = 0;
             Row rw = null;
              
             ViewObject vo =  appM.getXxOmSizewiseLTVO1();
             
             //read comma separated file line by line
             try
             {
                 while ((strLine = reader.readLine()) != null)
                 {
                     lineNumber++;
                     // create a new row skip the header (header has linenumber 1)
                     if (lineNumber>1) {
                         rw = vo.createRow();
                         rw.setNewRowState(Row.STATUS_INITIALIZED);
                         vo.insertRow(rw);
                        
                     }
                      
                     //break comma separated line using ","
                     st = new StringTokenizer(strLine, ",");
                         
                     double sizeProjQty=0, sizeActualQty=0,addDeductQty;
                         
                     while (st.hasMoreTokens())
                     {
                         //display csv values
                         tokenNumber++;
                          
                         String theToken = st.nextToken();
                         System.out.println("Line # " + lineNumber + ", Token # " +
                         tokenNumber +
                         ", Token : " + theToken);
                         
                         if (lineNumber>1){
                             // set Attribute Values
                             switch (tokenNumber) {
                                case 1:rw.setAttribute("BpoId",theToken);
                                          break;
                               
                                 case 3: rw.setAttribute("SizeName", theToken);
                                         break;
                                 case 4: rw.setAttribute("Qty", theToken);
                                         break;
                                        
                             }
                         }
                     }
                     //reset token number
                     tokenNumber = 0;
                 }
                 showMessage("data successfully Added in the database", "info");  
             }
             catch (IOException e) {
             // TODO add more
                 FacesContext fctx = FacesContext.getCurrentInstance();
                 fctx.addMessage(sizewisemultipleup.getClientId(fctx), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Content Error in Uploaded file", e.getMessage()));
             }
             catch (Exception e) {
             FacesContext fctx = FacesContext.getCurrentInstance();
             fctx.addMessage( null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
             "Data Error in Uploaded file", e.getMessage()));
             }
         }


    public void SIzewiseUploadFormat(FacesContext facesContext,
                                OutputStream outputStream) throws IOException {
        // Add event code here...
        String BpoId=null;
        String Bpo=null;
        

        BufferedWriter writer =
        new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

        writer.write("*Bp0-Id");
        writer.write(",");
        writer.write("*Bpo");
        writer.write(",");
        writer.write("*Size-Name");
        writer.write(",");
        writer.write("*Qty");
        writer.write(",");
        writer.newLine();
        ViewObject vo = appM.getXxOmBpoInfoTVO1();
              RowSetIterator iter =  vo.createRowSetIterator(null);
              while(iter.hasNext()){
                  Row r = iter.next();
                 BpoId= r.getAttribute("BpoId").toString();
                  writer.write(BpoId);
                  writer.write(",");
                  
                  Bpo= r.getAttribute("Bpo").toString();
                   writer.write(Bpo);
                   writer.write(",");
                  writer.newLine();
                  
                  
                  
              }
              iter.closeRowSetIterator();
        
        
        writer.flush();
        writer.close();


    } // end of bpoUploadFormat


    public void setSizewisemultipleup(RichTable sizewisemultipleup) {
        this.sizewisemultipleup = sizewisemultipleup;
    }

    public RichTable getSizewisemultipleup() {
        return sizewisemultipleup;
    }
    
    
    
    public void showMessage(String message, String severity) {

           FacesMessage fm = new FacesMessage(message);

           if (severity.equals("info")) {
               fm.setSeverity(FacesMessage.SEVERITY_INFO);
               System.out.println("inside message");
           } else if (severity.equals("warn")) {
               fm.setSeverity(FacesMessage.SEVERITY_WARN);
           } else if (severity.equals("error")) {
               fm.setSeverity(FacesMessage.SEVERITY_ERROR);
           }

           FacesContext context = FacesContext.getCurrentInstance();
           context.addMessage(null, fm);
          
       }
    
    
    
    
    
}// end of main