//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.22 at 10:31:37 AM CDT 
//


package org.cbioportal.g2smutation.util.blast;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "statistics"
})
@XmlRootElement(name = "Iteration_stat")
public class IterationStat {

    @XmlElement(name = "Statistics", required = true)
    protected Statistics statistics;

    /**
     * Gets the value of the statistics property.
     * 
     * @return
     *     possible object is
     *     {@link Statistics }
     *     
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param value
     *     allowed object is
     *     {@link Statistics }
     *     
     */
    public void setStatistics(Statistics value) {
        this.statistics = value;
    }

}
