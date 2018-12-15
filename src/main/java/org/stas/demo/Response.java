package org.stas.demo;

import org.stas.demo.persistence.model.Contact;

/**
 * @author stas ambartsumov
 * contains a response for rest api
 */
public class Response {
    private String status;
    private Contact data;

    public Response(){

    }

    public Response(String status, Contact data){
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Contact getData() {
        return data;
    }

    public void setData(Contact data) {
        this.data = data;
    }
}
