package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.SmilDimension;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Video;

public class MessageConverter {
	Region getRegion(ParcelableRegion region) {
		Region result = new Region(new SmilDimension(region.getBounds().width(), region.getBounds().height()));
		result.setOrigin(new SmilDimension(region.getBounds().left, region.getBounds().height()));
		result.setZindex(region.getZindex());
		return result;
	}
	
	Audio getAudio(AudioBox box) {
		return new Audio(box.getBegin(), box.getEnd(), box.getSource(), box.getClipBegin(), box.getClipEnd());
	}
	
	Image getImage(ImageBox box) {
		return new Image(box.getBegin(), box.getEnd(), box.getSource(), getRegion(box.getRegion()));
	}
	
	Text getText(TextBox box) {
		return new Text(box.getBegin(), box.getEnd(), box.getSource(), getRegion(box.getRegion()));
	}
	
	Video getVideo(VideoBox box) {
		return new Video(box.getBegin(), box.getEnd(), box.getSource(), getRegion(box.getRegion()), box.getClipBegin(), box.getClipEnd());
	}
	
	Media getMedia(Box box) {
		Media result = null;
		if (box instanceof AudioBox) {
			result = getAudio((AudioBox) box);
		} else if (box instanceof ImageBox) {
			result = getImage((ImageBox) box);
		} else if (box instanceof TextBox) {
			result = getText((TextBox) box);
		} else if (box instanceof VideoBox) {
			result = getVideo((VideoBox) box);
		}
		return result;
	}
	
	Message getMessage(TrackManager tm, String id) {
		Message result = new Message(id);
		Parallel par = new Parallel();
		LinkedList<Box> elts = tm.getBoxes();
		for (Box b : elts) {
			par.addElement(getMedia(b));
		}
		result.addElement(par);
		return result;
	}
}
