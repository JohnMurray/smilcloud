package edu.nku.cs.csc440.team2.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
        ID = smil.getAttributes().getNamedItem("id").getNodeValue();

        // <root-layout background-color="" height="" width=""/>
        Node rootLayout = doc.getElementsByTagName("root-layout").item(0);
        String backgroundColor = rootLayout.getAttributes().getNamedItem("background-color").getNodeValue();
        int height = Integer.valueOf(rootLayout.getAttributes().getNamedItem("height").getNodeValue());
        int width = Integer.valueOf(rootLayout.getAttributes().getNamedItem("width").getNodeValue());
        ROOT_LAYOUT = new RootLayout(new SmilDimension(width, height), backgroundColor);

        // <region background-color="" fit="" height="" id="" left="" top="" width="" z-index=""/>
        NodeList regionList = doc.getElementsByTagName("region");
        NamedNodeMap nnm = regionList.item(regionList.getLength() - 1).getAttributes();
        int j = Integer.valueOf(nnm.getNamedItem("id").getNodeValue().substring(1)) - 1;
        regions = new Region[j];

        for (int i = 0; i < regionList.getLength(); i++) {
            NamedNodeMap regionAttrs = regionList.item(i).getAttributes();
            backgroundColor = regionAttrs.getNamedItem("background-color").getNodeValue();
            String fit = regionAttrs.getNamedItem("fit").getNodeValue();
            height = Integer.valueOf(regionAttrs.getNamedItem("height").getNodeValue());
            String id = regionAttrs.getNamedItem("id").getNodeValue();
            int left = Integer.valueOf(regionAttrs.getNamedItem("left").getNodeValue());
            int top = Integer.valueOf(regionAttrs.getNamedItem("top").getNodeValue());
            width = Integer.valueOf(regionAttrs.getNamedItem("width").getNodeValue());
            int zIndex = Integer.valueOf(regionAttrs.getNamedItem("z-index").getNodeValue());
            int index = Integer.valueOf(id.substring(1)) - 1;
            if (index >= regions.length) {
            	// regions = Arrays.copyOf(regions, index + 1);
            	Region[] copyOf = new Region[index + 1];
                for(int k = 0; k < regions.length ; k++){
                	copyOf[k] = regions[k];
                }
                regions = copyOf;
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
                    int repeat;
                    NamedNodeMap childAttrs = children.item(i).getAttributes();

                    if (nodeName.equals("text") || nodeName.equals("img")) {
                        // <tag begin="" end="" fill="" id="" region="" repeat="" src=""/>
                        begin = Double.valueOf(childAttrs.getNamedItem("begin").getNodeValue());
                        end = Double.valueOf(childAttrs.getNamedItem("end").getNodeValue());
                        fill = childAttrs.getNamedItem("fill").getNodeValue();
                        id = childAttrs.getNamedItem("id").getNodeValue();
                        regionId = childAttrs.getNamedItem("region").getNodeValue();
                        region = regions[Integer.valueOf(regionId.substring(1)) - 1];
                        repeat = Integer.valueOf(childAttrs.getNamedItem("repeat").getNodeValue());
                        src = childAttrs.getNamedItem("src").getNodeValue();

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
                        begin = Double.valueOf(childAttrs.getNamedItem("begin").getNodeValue());
                        clipBegin = Double.valueOf(childAttrs.getNamedItem("clip-begin").getNodeValue());
                        clipEnd = Double.valueOf(childAttrs.getNamedItem("clip-end").getNodeValue());
                        end = Double.valueOf(childAttrs.getNamedItem("end").getNodeValue());

                        if (nodeName.equals("video")) {
                            fill = childAttrs.getNamedItem("fill").getNodeValue();
                        }
                        id = childAttrs.getNamedItem("id").getNodeValue();
                        if (nodeName.equals("video")) {
                            regionId = childAttrs.getNamedItem("region").getNodeValue();
                            region = regions[Integer.valueOf(regionId.substring(1)) - 1];
                        }
                        repeat = Integer.valueOf(childAttrs.getNamedItem("repeat").getNodeValue());
                        src = childAttrs.getNamedItem("src").getNodeValue();

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
                        begin = Double.valueOf(childAttrs.getNamedItem("begin").getNodeValue());
                        if (childAttrs.getLength() == 4) {
                            end = Double.valueOf(childAttrs.getNamedItem("end").getNodeValue());
                        } else {
                            end = -1.0;
                        }
                        id = childAttrs.getNamedItem("id").getNodeValue();
                        repeat = Integer.valueOf(childAttrs.getNamedItem("repeat").getNodeValue());

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
