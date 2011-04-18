package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.SmilDimension;

public class MessageConverter {
	Region getRegion(ParcelableRegion region) {
		Region result = new Region(new SmilDimension(region.getBounds().width(), region.getBounds().height()));
		result.setOrigin(new SmilDimension(region.getBounds().left, region.getBounds().height()));
		result.setZindex(region.getZindex());
		return result;
	}
	
	Message getMessage(TrackManager tm, String id) {
		Message result = new Message(id);
		Parallel par = new Parallel();
		LinkedList<Box> elts = tm.getBoxes();
		for (Box b : elts) {
			par.addElement(b.toMedia());
		}
		result.addElement(par);
		return result;
	}
}
