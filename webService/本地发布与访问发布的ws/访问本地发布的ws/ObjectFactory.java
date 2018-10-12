
package com.sobey.phonepublish;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sobey.phonepublish package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetPhoneService_QNAME = new QName("http://soap.sobey.com/", "getPhoneService");
    private final static QName _GetPhoneServiceResponse_QNAME = new QName("http://soap.sobey.com/", "getPhoneServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sobey.phonepublish
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Phone }
     * 
     */
    public Phone createPhone() {
        return new Phone();
    }

    /**
     * Create an instance of {@link GetPhoneServiceResponse }
     * 
     */
    public GetPhoneServiceResponse createGetPhoneServiceResponse() {
        return new GetPhoneServiceResponse();
    }

    /**
     * Create an instance of {@link GetPhoneService }
     * 
     */
    public GetPhoneService createGetPhoneService() {
        return new GetPhoneService();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPhoneService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.sobey.com/", name = "getPhoneService")
    public JAXBElement<GetPhoneService> createGetPhoneService(GetPhoneService value) {
        return new JAXBElement<GetPhoneService>(_GetPhoneService_QNAME, GetPhoneService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPhoneServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.sobey.com/", name = "getPhoneServiceResponse")
    public JAXBElement<GetPhoneServiceResponse> createGetPhoneServiceResponse(GetPhoneServiceResponse value) {
        return new JAXBElement<GetPhoneServiceResponse>(_GetPhoneServiceResponse_QNAME, GetPhoneServiceResponse.class, null, value);
    }

}
