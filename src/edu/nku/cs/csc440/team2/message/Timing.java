package edu.nku.cs.csc440.team2.message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Timing extends Body {

    LinkedList<Body> content = new LinkedList<Body>();
    boolean isImplicit = false;
    private static Body b = null;
    private static TreeSet<String> ids = new TreeSet<String>();

    Timing() {
    }

    Timing(double begin) {
        setBegin(begin);
    }

    Timing(double begin, double end) {
        setBegin(begin);
        setEnd(end);
    }

    TreeSet<String> getAllElementIds() {
        ids = new TreeSet<String>();
        getElement("", false);
        return ids;
    }

    void removeElement(String id) {
        getElement(id, true);
    }

    Body getElement(String id, boolean isRemoving) {
        b = null;
        getElement(id, content, isRemoving);
        return b;
    }

    private static void getElement(String id, LinkedList<Body> content, boolean isRemoving) {
        for (Body body : content) {
            if (body != null && body.getId() != null) {
                boolean isParallel = body.getId().startsWith("p");
                boolean isSequence = body.getId().startsWith("s");
                if (body.getId().equals(id)) {
                    if (isRemoving) {
                        b = null;
                        content.remove(body);
                        break;
                    } else {
                        b = body;
                    }
                } else if (isParallel || isSequence) {
                    Timing t = (Timing) body;
                    Timing.getElement(id, t.getBody(), isRemoving);
                }
                ids.add(body.getId());
            }
        }
    }

    public String addElement(Body element) {
        if (element != null) {
            content.add(element);
            return element.getId();
        } else {
            return null;
        }
    }

    public String addElement(Body element, int index) {
        if (element != null && index >= 0 && index <= content.size()) {
            content.add(index, element);
            return element.getId();
        } else {
            return null;
        }
    }

    public LinkedList<Body> getBody() {
        return content;
    }

    TreeSet<Region> getRegions() {
        TreeSet<Region> regions = new TreeSet<Region>();
        Iterator<Body> i = content.iterator();
        while (i.hasNext()) {
            Body body = i.next();
            if (body.getClass().getSuperclass().getName().contains("Timing")) {
                Timing t = (Timing) body;
                regions.addAll(t.getRegions());
            } else if (body.getClass().getName().contains("Audio")); else {
                Media m = (Media) body;
                regions.add(m.getRegion());
            }
        }
        return regions;
    }

    @Override
    String toXml() {
        // <par id="p1" repeat="1" begin="0.0" end="10.0">
        // </par>
        // <seq id="s1" repeat="1" begin="1.0" end="11.0">
        // </seq>
        StringBuilder xml = new StringBuilder();
        String closingTag = null;

        if (!isImplicit) {
            if (id.startsWith("p")) {
                xml.append("<par");
                closingTag = "</par>";
            }
            if (id.startsWith("s")) {
                xml.append("<seq");
                closingTag = "</seq>";
            }
            xml.append(super.toXml());
            xml.append(">\n");
        }
        Iterator<Body> i = content.iterator();
        while (i.hasNext()) {
            xml.append(i.next().toXml());
        }

        if (!isImplicit) {
            xml.append(closingTag);
            xml.append("\n");
        }
        return xml.toString();
    }
}
