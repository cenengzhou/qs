package com.gammon.qs.webservice;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPMessage;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;


public class WSSEHeaderWebServiceMessageCallback implements WebServiceMessageCallback {

    public static final String WSS_10_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public final static String WSSE_Security_Elem = "Security";
    public final static String WSSE_Security_prefix = "wsse";
    public final static String WSSE_UsernameToken_Elem = "UsernameToken";
    public final static String WSSE_Username_Elem = "Username";
    public final static String WSSE_Password_Elem = "Password";

    private String user;
    private String pass;

    public WSSEHeaderWebServiceMessageCallback(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
 
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

        try {
            // you have to use the default SAAJWebMessageFactory 
        	AxiomSoapMessage saajSoapMessage = (AxiomSoapMessage) message;
        	SOAPMessage soapMessage = saajSoapMessage.getAxiomMessage();

            // then grab for the SOAP header, and...
        	SOAPEnvelope soapEnvelope = soapMessage.getSOAPEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();

            // ... add the WS-Security Header Element
            OMNamespace wsseNamespace = OMAbstractFactory.getOMFactory().createOMNamespace(WSS_10_NAMESPACE, WSSE_Security_prefix);
            SOAPHeaderBlock soapHeaderBlock = soapHeader.addHeaderBlock(WSSE_Security_Elem, wsseNamespace);

            // otherwise a RST without appliesTo fails
            soapHeaderBlock.setMustUnderstand(true);
            
            //soapHeaderBlock
            OMElement usernameToken = OMAbstractFactory.getOMFactory().createOMElement(WSSE_UsernameToken_Elem, wsseNamespace);
            OMElement username =  OMAbstractFactory.getOMFactory().createOMElement(WSSE_Username_Elem, wsseNamespace);
            username.setText(this.user);
            
            OMElement password =  OMAbstractFactory.getOMFactory().createOMElement(WSSE_Password_Elem, wsseNamespace);
            password.setText(this.pass);
            
            usernameToken.addChild(username);
            usernameToken.addChild(password);
            soapHeaderBlock.addChild(usernameToken);
            
        } catch (Exception soapException) {
            throw new RuntimeException("WSSEHeaderWebServiceMessageCallback", soapException);
        }
    }
}