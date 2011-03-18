package edu.nku.cs.csc440.team2.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Shane Crandall
 * @version 0
 */
public class Message {

    private static LinkedList<String> downloadPriority = null;
    private static Region[] regions = null;
    private final static String TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<smil id=\"\">\n" + "<head>\n" + "<layout>\n" + "</layout>\n"
            + "</head>\n" + "<body>\n" + "</body>\n" + "</smil>";
    private final String ID;
    private final RootLayout ROOT_LAYOUT;
    private LinkedList<Body> bodyImpSeq = new LinkedList<Body>();
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;

    public Message(String id) {
        ID = new CdataValidator().validate(id, "");
        ROOT_LAYOUT = new RootLayout();
    }

    public Message(String id, SmilDimension dimensions, String backgroundColor) {
        ID = new CdataValidator().validate(id, "");
        ROOT_LAYOUT = new RootLayout(dimensions, backgroundColor);
    }

    public Message(File file) throws SAXException, ParserConfigurationException, IOException {

        downloadPriority = new LinkedList<String>();
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(file);

        // <smil id="">
        Node smil = doc.getElementsByTagName("smil").item(0);
        ID = smil.getAttributes().item(0).getNodeValue();

        // <root-layout background-color="" height="" width=""/>
        Node rootLayout = doc.getElementsByTagName("root-layout").item(0);
        String backgroundColor = rootLayout.getAttributes().item(0).getNodeValue();
        int height = Integer.valueOf(rootLayout.getAttributes().item(1).getNodeValue());
        int width = Integer.valueOf(rootLayout.getAttributes().item(2).getNodeValue());
        ROOT_LAYOUT = new RootLayout(new SmilDimension(width, height), backgroundColor);

        // <region background-color="" fit="" height="" id="" left="" top="" width="" z-index=""/>
        NodeList regionList = doc.getElementsByTagName("region");
        NamedNodeMap nnm = regionList.item(regionList.getLength() - 1).getAttributes();
        int j = Integer.valueOf(nnm.item(3).getNodeValue().substring(1)) - 1;
        regions = new Region[j];

        for (int i = 0; i < regionList.getLength(); i++) {
            NamedNodeMap regionAttrs = regionList.item(i).getAttributes();
            backgroundColor = regionAttrs.item(0).getNodeValue();
            String fit = regionAttrs.item(1).getNodeValue();
            height = Integer.valueOf(regionAttrs.item(2).getNodeValue());
            String id = regionAttrs.item(3).getNodeValue();
            int left = Integer.valueOf(regionAttrs.item(4).getNodeValue());
            int top = Integer.valueOf(regionAttrs.item(5).getNodeValue());
            width = Integer.valueOf(regionAttrs.item(6).getNodeValue());
            int zIndex = Integer.valueOf(regionAttrs.item(7).getNodeValue());
            int index = Integer.valueOf(id.substring(1)) - 1;
            if (index >= regions.length) {
                regions = Arrays.copyOf(regions, index + 1);
            }
            regions[index] = new Region(new SmilDimension(width, height), backgroundColor, new SmilDimension(left, top), fit, id, zIndex);
        }

        parse(doc.getElementsByTagName("body").item(0), bodyImpSeq);
    }

    private static void parse(Node node, LinkedList<Body> parent) {
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = children.item(i).getNodeName();
                    double begin, end;
                    String fill, id, regionId, src;
                    Region region;
                    int repeat, index = 0;
                    NamedNodeMap childAttrs = children.item(i).getAttributes();

                    if (nodeName.equals("text") || nodeName.equals("img")) {
                        // <tag begin="" end="" fill="" id="" region="" repeat="" src=""/>
                        begin = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        end = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        fill = childAttrs.item(index++).getNodeValue();
                        id = childAttrs.item(index++).getNodeValue();
                        regionId = childAttrs.item(index++).getNodeValue();
                        region = regions[Integer.valueOf(regionId.substring(1)) - 1];
                        repeat = Integer.valueOf(childAttrs.item(index++).getNodeValue());
                        src = childAttrs.item(index++).getNodeValue();

                        TextImage textImage;
                        if (nodeName.equals("text")) {
                            textImage = new Text(begin, end, src, region, id);
                        } else { // nodeName.equals("img")
                            textImage = new Image(begin, end, src, region, id);
                        }
                        textImage.setFill(fill);
                        textImage.setRepeat(repeat);
                        parent.add(textImage);
                    } else if (nodeName.equals("audio") || nodeName.equals("video")) {
                        // <audio begin="" clip-begin="" clip-end="" end=""         id=""           repeat="" src=""/>
                        // <video begin="" clip-begin="" clip-end="" end="" fill="" id="" region="" repeat="" src=""/>
                        double clipBegin, clipEnd;
                        fill = null;
                        region = null;
                        begin = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        clipBegin = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        clipEnd = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        end = Double.valueOf(childAttrs.item(index++).getNodeValue());

                        if (nodeName.equals("video")) {
                            fill = childAttrs.item(index++).getNodeValue();
                        }
                        id = childAttrs.item(index++).getNodeValue();
                        if (nodeName.equals("video")) {
                            regionId = childAttrs.item(index++).getNodeValue();
                            region = regions[Integer.valueOf(regionId.substring(1)) - 1];
                        }
                        repeat = Integer.valueOf(childAttrs.item(index++).getNodeValue());
                        src = childAttrs.item(index++).getNodeValue();

                        AudioVideo audioVideo;
                        if (nodeName.equals("audio")) {
                            audioVideo = new Audio(begin, end, src, clipBegin, clipEnd, id);
                        } else { // nodeName.equals("video")
                            audioVideo = new Video(begin, end, src, region, clipBegin, clipEnd, id);
                            audioVideo.setFill(fill);
                        }
                        audioVideo.setRepeat(repeat);
                        parent.add(audioVideo);
                        downloadPriority.add(id);
                    } else if (nodeName.equals("seq") || nodeName.equals("par")) {
                        // <tag begin="" end="" id="" repeat=""> OR <tag begin="" id="" repeat="">
                        begin = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        if (childAttrs.getLength() == 4) {
                            end = Double.valueOf(childAttrs.item(index++).getNodeValue());
                        } else {
                            end = -1.0;
                        }
                        id = childAttrs.item(index++).getNodeValue();
                        repeat = Integer.valueOf(childAttrs.item(index++).getNodeValue());

                        Timing timing;
                        if (nodeName.equals("seq")) {
                            timing = new Sequence(begin, end, id);
                        } else { // nodeName.equals("par")
                            timing = new Parallel(begin, end, id);
                        }
                        timing.setRepeat(repeat);
                        parent.add(timing);
                        parse(children.item(i), timing.getBody());
                    }
                }
            }
        }
    }

    public File toFile(String pathname) throws FileNotFoundException {
        File file = new File(pathname);
        PrintWriter output = new PrintWriter(file);
        output.print(this.toXml());
        output.close();
        return file;
    }

    String toXml() {
        StringBuilder xml = new StringBuilder(TEMPLATE);
        StringBuilder layout = new StringBuilder();
        Sequence s = new Sequence(bodyImpSeq, true);
        Iterator<Region> i = s.getRegions().iterator();

        layout.append(ROOT_LAYOUT.toXml());
        while (i.hasNext()) {
            layout.append(i.next().toXml());
        }

        xml.insert(xml.indexOf("id=\"\"") + 4, ID);
        xml.insert(xml.indexOf("</layout>"), layout);
        xml.insert(xml.indexOf("</body>"), s.toXml());
        return xml.toString();
    }

    public String getId() {
        return ID;
    }

    public RootLayout getRootLayout() {
        return ROOT_LAYOUT;
    }

    public LinkedList<String> getDownloadPriority() {
        return downloadPriority;
    }

    public TreeSet<String> getAllElementIds() {
        Sequence s = new Sequence(bodyImpSeq, true);
        return s.getAllElementIds();
    }

    public LinkedList<Body> getBody() {
        return bodyImpSeq;
    }

    public String addElement(Body element) {
        return new Sequence(bodyImpSeq, true).addElement(element);
    }

    public String addElement(Body element, int index) {
        return new Sequence(bodyImpSeq, true).addElement(element, index);
    }

    public Body getElement(String id) {
        Sequence s = new Sequence(bodyImpSeq, true);
        return s.getElement(id, false);
    }

    public void removeElement(String id) {
        new Sequence(bodyImpSeq, true).removeElement(id);
    }
}
