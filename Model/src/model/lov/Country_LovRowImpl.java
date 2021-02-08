package model.lov;

import oracle.jbo.RowSet;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Nov 24 09:24:17 BDT 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class Country_LovRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        Country {
            public Object get(Country_LovRowImpl obj) {
                return obj.getCountry();
            }

            public void put(Country_LovRowImpl obj, Object value) {
                obj.setCountry((String)value);
            }
        }
        ,
        BuyerId {
            public Object get(Country_LovRowImpl obj) {
                return obj.getBuyerId();
            }

            public void put(Country_LovRowImpl obj, Object value) {
                obj.setBuyerId((Number)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(Country_LovRowImpl object);

        public abstract void put(Country_LovRowImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int COUNTRY = AttributesEnum.Country.index();
    public static final int BUYERID = AttributesEnum.BuyerId.index();

    /**
     * This is the default constructor (do not remove).
     */
    public Country_LovRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute Country.
     * @return the Country
     */
    public String getCountry() {
        return (String) getAttributeInternal(COUNTRY);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Country.
     * @param value value to set the  Country
     */
    public void setCountry(String value) {
        setAttributeInternal(COUNTRY, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BuyerId.
     * @return the BuyerId
     */
    public Number getBuyerId() {
        return (Number) getAttributeInternal(BUYERID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BuyerId.
     * @param value value to set the  BuyerId
     */
    public void setBuyerId(Number value) {
        setAttributeInternal(BUYERID, value);
    }


    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }
}
