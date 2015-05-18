package com.dotTracePlugin.agent.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Alexey.Totin on 5/15/2015.
 */

@XmlRootElement(name = "Report")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProfiledMethods {

    @XmlElement(name = "Function")
    private List<ProfiledMethod> methods = new ArrayList<ProfiledMethod>();

    public List<ProfiledMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ProfiledMethod> methods) {
        this.methods = methods;
    }
}
