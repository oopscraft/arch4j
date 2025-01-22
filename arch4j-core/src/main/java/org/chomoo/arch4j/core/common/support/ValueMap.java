package org.chomoo.arch4j.core.common.support;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ValueMap extends LinkedHashMap<String,Object> {

    public ValueMap(){
        super();
    }

    @Override
    public Object put(String name, Object value) {
        // CLOB case
        if(value instanceof Clob) {
            StringBuffer buffer = new StringBuffer();
            Clob clob = (Clob)value;
            Reader reader = null;
            try {
                reader = clob.getCharacterStream();
                char[] buff = new char[1024];
                int nchars = 0;
                while ((nchars = reader.read(buff)) > 0) {
                    buffer.append(buff, 0, nchars);
                }
            }catch(SQLException |IOException e) {
                buffer.append(e.getMessage());
            }finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        buffer.append(e.getMessage());
                    }
                }
            }
            return super.put(name, buffer.toString());
        }
        // default
        return super.put(name, value);
    }

    /**
     * Setter method
     * @param name
     * @param value
     */
    public void set(String name, Object value) {
        this.put(name, value);
    }

    /**
     * Setter string
     * @param name
     * @param value
     */
    public void setString(String name, Object value) {
        if(value == null) {
            this.set(name, null);
        }else if(value instanceof String) {
            this.set(name, value);
        }else{
            this.set(name, value.toString());
        }
    }

    /**
     * Getter string
     * @param name
     * @return
     */
    public String getString(String name) {
        Object value = this.get(name);
        if(value == null) {
            return null;
        }else if(value instanceof String) {
            return (String)value;
        }else{
            return value.toString();
        }
    }

    /**
     * Setter number
     * @param name
     * @param value
     */
    public void setNumber(String name, Object value) {
        if(value == null) {
            this.set(name, BigDecimal.ZERO);
        }else if(value instanceof BigDecimal) {
            this.set(name, value);
        }else{
            this.set(name, new BigDecimal(value.toString()));
        }
    }

    /**
     * getNumber
     * @param name
     * @return
     */
    public BigDecimal getNumber(String name) {
        Object value = this.get(name);
        if(value == null) {
            return BigDecimal.ZERO;
        }else if(value instanceof BigDecimal) {
            return (BigDecimal)value;
        }else{
            return new BigDecimal(value.toString());
        }
    }

}