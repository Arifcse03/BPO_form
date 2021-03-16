package xx_om_bpo;


import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

import oracle.adf.model.DataControlFrame;

import com.asn1c.core.Null;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import model.services.AppModuleImpl;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.view.rich.component.rich.RichPoll;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.ApplicationModule;

import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControlFrame;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.faces.bi.event.graph.SelectionEvent;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichForm;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;


import oracle.binding.BindingContainer;


import oracle.jbo.JboException;
import oracle.jbo.NavigatableRowIterator;
import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.event.PollEvent;

public class main {
    private RichCommandButton g;
    private RichPopup mypopup;

    //private RichColumn parent;
    private RichInputText parent1;
    private RichTable bpoTable;
    private RichPoll refresh;
    private RichCommandButton showSumBtn;
    private RichSelectOneChoice status;
    private RichOutputText poCIDfrompoc;
    private RichInputText pocID_inputField;

    public main() {
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


    //code to get the poc id

    public String getFobId() {

        ViewObject vo = appM.getXxOmBpoInfoTVO1();

        String FobId = null;
        try {

            FobId = vo.getCurrentRow().getAttribute("FobId").toString();
            // Row cu=vo.getCurrentRow();
            // cu.setAttribute("Parent", FobId);


        } catch (Exception e) {
            // TODO: Add catch code
            ;
        }
        System.out.println("Fob ID:" + FobId);
        return FobId;
    }
 /*** method to check if the qty grater than the TBA qty if so relavent Quentity will be change*/
    public void CheckMethod() {
        // Add event code here...
        ViewObject vo = appM.getXxOmBpoInfoTVO1();
        String mat = "TBA";
        String bpo = "BPO";
       // String parent = "";
        Double prqty = null;
        String s = null;
        String vqty = null;
        Double dqty = null;
        Double fid = null;
        s = vo.getCurrentRow().getAttribute("FobId").toString();
        fid = new Double(s);
        System.out.println(s);
        // clsfic = vo.getCurrentRow().getAttribute("Classification").toString();
        vqty = vo.getCurrentRow().getAttribute("Qty").toString();
        // int pqty=Integer.parseInt(vqty);
        dqty = new Double(vqty);
        ////code to get the qty value//
        String query =
            " select Qty  " + " FROM  XX_OM_BPO_INFO_T t" + " WHERE   t.Fob_id=t.parent " +
            " and t.fob_id= '" + getFobId() + "' ";
        ResultSet rs;
        System.out.println(dqty);
        try {
            System.out.println("i am in try ");

            rs =
 appM.getDBTransaction().createStatement(0).executeQuery(query);
            System.out.println("statement done");
            if (rs.next()) {
                System.out.println("i am in if ");
                prqty = rs.getDouble("Qty"); //getDouble("Qty");
                System.out.println(prqty + "   " + dqty);
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new JboException(e);
        }
        ///for checking if bpo qty is grater or not
        if (dqty > prqty) {
        
        /**to set the tba value 0 if tba value less than bpo**/
        
        ViewObject to = appM.getXxOmBpoInfoTVO1();
                    String pparent =
                        to.getCurrentRow().getAttribute("FobId").toString();
                    Row cu = vo.getCurrentRow();
                    // cu.setAttribute("Parent", FobId);
                    cu.setAttribute("Parent", pparent);
                    appM.getDBTransaction().commit();
                    refresh();

                    AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
                    int resut = 0;

                    String pl = "BEGIN\n" +
                        "UPDATE XX_OM_BPO_INFO_T T\n" +
                        " SET T.QTY=0 \n" +
                        " WHERE\n" +
                        " T.FOB_ID=T.PARENT\n" +
                    "AND T.CLASSIFICATION='TBA'\n" +
                        "AND T.FOB_ID=?;\n" +
                        "END;\n";
                    CallableStatement stat = null;
                    try {
                        stat =
        appM.getDBTransaction().createCallableStatement(pl, appM.getDBTransaction().DEFAULT);
                       
                        stat.setDouble(1, fid);
                        resut = stat.executeUpdate();
                        vo.clearCache();
                        appM.getDBTransaction().commit();
                        refresh();
                        appM.getDBTransaction().clearEntityCache("Qty");
                        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        
        
        
        
        
        
        
        
          /*  System.out.println("insite sub if");
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p><b>BPO Quantity cant be </b></p>");
            message.append("<p><b>Greater then its parent TBA Quantity.</b</p>");
            message.append("<p><b>please Increase the </b></p>");
            message.append("</body>TBA quantity then try again</html>");
            FacesMessage fm = new FacesMessage(message.toString());
            fm.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext fctx = FacesContext.getCurrentInstance();
            fctx.addMessage(null, fm);
            vo.getCurrentRow().setAttribute("Qty", 0);
            //vo.removeCurrentRow();
            vo.executeQuery();
           */
            
          
            
          
            
            
            
            
        } else {
            ViewObject to = appM.getXxOmBpoInfoTVO1();
            String pparent =
                to.getCurrentRow().getAttribute("FobId").toString();
            Row cu = vo.getCurrentRow();
            // cu.setAttribute("Parent", FobId);
            cu.setAttribute("Parent", pparent);
            appM.getDBTransaction().commit();
            refresh();

            AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
            int resut = 0;

            String pl = "BEGIN\n" +
                "UPDATE XX_OM_BPO_INFO_T T\n" +
                " SET T.QTY=T.QTY-? \n" +
                " WHERE\n" +
                " T.FOB_ID=T.PARENT\n" +
            "AND T.CLASSIFICATION='TBA'\n" +
                "AND T.FOB_ID=?;\n" +
                "END;\n";
            CallableStatement stat = null;
            try {
                stat =
appM.getDBTransaction().createCallableStatement(pl, appM.getDBTransaction().DEFAULT);
                stat.setDouble(1, dqty);
                stat.setDouble(2, fid);
                resut = stat.executeUpdate();
                vo.clearCache();
                appM.getDBTransaction().commit();
                refresh();
                appM.getDBTransaction().clearEntityCache("Qty");
                AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("save the data");
        } //end of else
    }

    public void value(ValueChangeEvent valueChangeEvent) {

        // Add event code here...
        String bpo = "BPO";
        String tba = "TBA";
        valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
        Map map = ((UIComponent)valueChangeEvent.getSource()).getAttributes();
        String reportTypeValue = (String)map.get("rowIndexVal");
        if (reportTypeValue.equals(bpo)) {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            this.getMypopup().show(hints);
        } else if (reportTypeValue.equals(tba)) {
            System.out.println(reportTypeValue);
            ViewObject vo = appM.getXxOmBpoInfoTVO1();
            String FobId = vo.getCurrentRow().getAttribute("FobId").toString();

            Row cu = vo.getCurrentRow();
            // cu.setAttribute("Parent", FobId);
            cu.setAttribute("Parent", FobId);

            AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);

        } else {
            System.out.println("nothing");
        }
    }

    public void setMypopup(RichPopup mypopup) {
        this.mypopup = mypopup;
    }

    public RichPopup getMypopup() {
        return mypopup;
    }


    public void setParent1(RichInputText parent1) {
        this.parent1 = parent1;
    }

    public RichInputText getParent1() {
        return parent1;
    }

    public void setBpoTable(RichTable bpoTable) {
        this.bpoTable = bpoTable;
    }

    public RichTable getBpoTable() {
        return bpoTable;
    }

    public void dialoghandle(DialogEvent dialogEvent) {
        // Add event code here...
        ViewObject vo = appM.getXxOmBpoInfoTVO1();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
        try{
                if (dialogEvent.getOutcome().equals(dialogEvent.getOutcome().yes))

                        {
                            System.out.println("indise yes");
                            callSave();
                            appM.getDBTransaction().commit();
                            /*appM.getDBTransaction().commit();
                            ViewObject vo=appM.getXxOmBpoInfoTVO1();
                            Row cu=vo.getCurrentRow();
                            String q=cu.getAttribute("Qty").toString();
                            System.out.println(q);*/
                            //  vo.executeQuery();
                            // refresh();
                            // AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
                            CheckMethod();

                        } else {
                            System.out.println("i m inside dialog no");


                            String parent = "0";
                            Row cu = vo.getCurrentRow();
                            // cu.setAttribute("Parent", FobId);
                            cu.setAttribute("Parent", parent);
                            appM.getDBTransaction().commit();
                            vo.executeQuery();
                            refresh();
                            AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
                        }
            
            }
        catch(Exception e){e.printStackTrace();}
    }

    public void callSave() {
        appM.getDBTransaction().commitAndSaveChangeSet();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
    }

    public void refresh() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String refreshpage = facesContext.getViewRoot().getViewId();
        ViewHandler viewHandler =
            facesContext.getApplication().getViewHandler();
        UIViewRoot viewroot =
            viewHandler.createView(facesContext, refreshpage);
        viewroot.setViewId(refreshpage);
        facesContext.setViewRoot(viewroot);
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
    }


    public void printFob(ActionEvent actionEvent) {
        ViewObject vo = appM.getXxOmBpoInfoTVO1();

        Row[] rArray = vo.getAllRowsInRange();
        Double q = 0.0;
        String n = null;
        String p = null;
        int i = 0;
        System.out.println(vo.getRangeSize());
        ////new medhod
        for (i = 0; i < rArray.length - 1; i++) {
            System.out.println(q + "value of i:" + i);
            p = rArray[i].getAttribute("FobId").toString();
            Double pp = new Double(p);
            n = rArray[i + 1].getAttribute("FobId").toString();
            Double nn = new Double(n);

            if (!pp.equals(nn)) {
                System.out.println(rArray[i].getAttribute("FobId").toString() +
                                   " not the same with  " +
                                   rArray[i + 1].getAttribute("FobId").toString() +
                                   "\n");


                // Create a row and fill in the columns.
                Row newRow =
                    appM.getXxOmBpoInfoTVO1().createRow(); //getEmpViewObjEOBased1().createRow();
                newRow.setNewRowState(Row.STATUS_INITIALIZED);
                //get instance of the above created view object

                // to insert row at the end of the table
                vo.insertRowAtRangeIndex(vo.getRangeSize() + i + 2, newRow);
                System.out.println(appM.getXxOmBpoInfoTVO1().getCurrentRowIndex());
                newRow.setAttribute("Qty", q);
                q = 0.0;
            } else {
                String qq = rArray[i].getAttribute("Qty").toString();
                q = new Double(qq) + q;
                System.out.println(q + "value of i:" + i);

            }


        } //for
        /* Row newRow = vo.createRow();
                  newRow.setNewRowState(Row.STATUS_INITIALIZED);
                  vo.insertRowAtRangeIndex(vo.getRangeSize()+i+2,newRow);
                  System.out.println( appM.getXxOmBpoInfoTVO1().getCurrentRowIndex());
                  newRow.setAttribute("Qty", q);*/


    } ///prinfob


    public void ShowSum(PopupFetchEvent popupFetchEvent) {
        // Add event code here...
        ViewObject sum = appM.getXxOmBpoInfoTVO1();

        String PocId = sum.getCurrentRow().getAttribute("PocId").toString();
        ViewObject populatevo = appM.getSumOfView_bpo_info_t1();
        populatevo.clearCache();
        populatevo.setWhereClause(null);
        populatevo.setWhereClauseParam(0, PocId);
        populatevo.executeQuery();
        populatevo.first();
    }

    public void setShowSumBtn(RichCommandButton showSumBtn) {
        this.showSumBtn = showSumBtn;
    }

    public RichCommandButton getShowSumBtn() {
        return showSumBtn;
    }

    public void setStatus(RichSelectOneChoice status) {
        this.status = status;
        status.setValue(0);
    }

    public RichSelectOneChoice getStatus() {
        return status;
    }


    //Bpo FobId  PoType Incoterms  Qty

    public void copy() {

        ViewObject cloneVo = appM.getXxOmBpoInfoTVO1();

        Row currRow = cloneVo.getCurrentRow();
        Row cloneRow = getClonLine(cloneVo);


       /* try {
            cloneRow.setAttribute("Bpo", currRow.getAttribute("Bpo"));
        } catch (Exception e) {
            ;
        }*/
        try {
            cloneRow.setAttribute("FobId", currRow.getAttribute("FobId"));
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
        /*try {
            cloneRow.setAttribute("Qty", currRow.getAttribute("Qty"));
        } catch (Exception e) {
            ;
        }*/
        try {
            cloneRow.setAttribute("Status", currRow.getAttribute("Status"));
        } catch (Exception e) {
            ;
        }
        appM.getDBTransaction().commitAndSaveChangeSet();
      //  refresh();
        cloneVo.executeQuery();
    } //end of copy function


    public Row getClonLine(ViewObject vo) {

        Row row = vo.createRow();
        vo.insertRow(row);
        row.setNewRowState(Row.STATUS_INITIALIZED);
        return row;
    } //copy function creating new row


    public String customsave() {
        // Add event code here...
      ViewObject re=appM.getXxOmBpoInfoTVO1();

        appM.getDBTransaction().commit();
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
        refresh();
        re.clearCache();
        re.executeQuery();;
        showMessage("Successfull","info");


        return null;
    }//end of customsave()

    public String action_call() {
        // Add event code here...
      
        ViewObject searchVO = appM.getpoc_searchVO1();
        String pocidfrom_pocform="0";
       //// System.out.println(searchVO.getCurrentRow().getAttribute("Style").toString());
        //System.out.println(searchVO.getCurrentRow().getAttribute("PocId").toString());
               
        try{
             pocidfrom_pocform=poCIDfrompoc.getValue().toString();
            }        catch (Exception e) {
                            pocidfrom_pocform = "0";
                        }
        
        
        //String pocidfrom_pocform=poCIDfrompoc.getValue().toString();
        System.out.println(pocidfrom_pocform);
                int PocId  = 0;
                int PocId1=0;
                String STYLE=null;
                try {
                   PocId =Integer.parseInt(searchVO.getCurrentRow().getAttribute("PocId").toString());
                } catch (Exception e) {
                    PocId = 0;
                }
      
        try {
           PocId1 =Integer.parseInt(pocidfrom_pocform);
        } catch (Exception e) {
            PocId1 = 0;
        }
        try {
           STYLE =searchVO.getCurrentRow().getAttribute("Style").toString();
        } catch (Exception e) {
             e.printStackTrace();
        }
      /****if(!pocidfrom_pocform.isEmpty()&& pocidfrom_pocform=="0"){
            
                pocID_inputField.setValue(PocId );
            
            }
                System.out.println("PocId ............." + PocId );*/
                

                int pram = 1;
                appM.getDBTransaction().commit();
                ViewObject oder = appM.getXxOmBpoInfoTVO1();
                oder.setNamedWhereClauseParam("param", pram);
                oder.setWhereClause("POC_ID = '" + PocId + "'"+"or POC_ID='"+PocId1+"'");  
                oder.executeQuery();

                AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
        
        
        
        return null;
    }

    public String CreareCustom() {
        // Add event code here...
        ViewObject processvo = appM.getXxOmBpoInfoTVO1();
        System.out.println("count of row is"+processvo.getRowCountInRange()); 
        ViewObject searchVO = appM.getpoc_searchVO1();

        int rowcount=processvo.getRowCountInRange();
        int poc = 0;
        // String Buyer = null;
        int OrgId = 0;
        
        String message=null;
        String message1=null;
        String message2=null;
        String message3=null;
        /**code for disabling the create button*/
        
        if(rowcount!=0){
        showMessage("Sorry you can't use this button","info");
        }
        
        else{        try {
        poc =Integer.parseInt(searchVO.getCurrentRow().getAttribute("PocId").toString());
        } catch (Exception e) {
        poc = 0;
        }

        String stmt = "BEGIN XX_OM_BPO_CREATE1(:1,:2,:3,:4,:5); end;";

        java.sql.CallableStatement cs =
        appM.getDBTransaction().createCallableStatement(stmt, 1);
        try {


        cs.setInt(1, poc);
        cs.registerOutParameter(2, oracle.jdbc.OracleTypes.VARCHAR);
        cs.registerOutParameter(3, oracle.jdbc.OracleTypes.VARCHAR);
        cs.registerOutParameter(4, oracle.jdbc.OracleTypes.VARCHAR);
        cs.registerOutParameter(5, oracle.jdbc.OracleTypes.VARCHAR);
            

        cs.execute();
        message = cs.getString(2);
        message1 = cs.getString(3);  
        message2= cs.getString(4);
        message3= cs.getString(5);    
        cs.close();
            

        } catch (SQLException e) {
        e.printStackTrace();

        }
            
        processvo.executeQuery();
       
        AdfFacesContext.getCurrentInstance().addPartialTarget(bpoTable);
            if (message!=null){showMessage(message,"info");}
            if (message1 !=null){showMessage(message1,"info");}
            if (message2 !=null){showMessage(message2,"info");}
            if (message3 !=null){showMessage(message3,"info");}  
        }
        
        
        return null;
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
    public void setPoCIDfrompoc(RichOutputText poCIDfrompoc) {
        this.poCIDfrompoc = poCIDfrompoc;
        
    }

    public RichOutputText getPoCIDfrompoc() {
        return poCIDfrompoc;
       
    }


    public void setPocID_inputField(RichInputText pocID_inputField) {
        this.pocID_inputField = pocID_inputField;
    }

    public RichInputText getPocID_inputField() {
        return pocID_inputField;
    }
} //main class
