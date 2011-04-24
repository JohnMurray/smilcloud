package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import android.content.Context;
import android.widget.RelativeLayout;
import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Body;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Sequence;
import edu.nku.cs.csc440.team2.message.SmilDimension;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Video;

/**
 * Negotiate between the classes of the messenger package and the classes of the
 * player package. (of the edu.nku.cs.csc440.team2 namespace)
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class PriceLine {
	private Context context;
	private Message message;
	private Hashtable<String, RegionData> regions = new Hashtable<String, RegionData>(
			20, .75f);
	private SeqPlayer root = new SeqPlayer();
	private ArrayList<Pair<String, Pair<Integer, RelativeLayout>>> layouts;
	private RelativeLayout rootViewGroup;
	private Arbiter subject = new Arbiter();

	/**
	 * instantiate the translator with the messenger classes
	 * 
	 * @param m
	 * @param context
	 * @param rvg
	 */
	public PriceLine(Message m, Context context, RelativeLayout rvg) {
		this.context = context;
		this.message = m;
		this.rootViewGroup = rvg;

		this.root.bindArbiter(this.subject);
	}

	/**
	 * Internally translate the message into the SMIL representation that is
	 * going to be used by the Player classes
	 */
	public void negotiateBigDeal() {
		this.makePrivateDeal(this.message.getBody(), this.root);

		this.generateViewGroupsAndDemandACheaperRate();
		this.renderViewGroupsAndDemandAGoodDeal();
		this.attachViewGroupsAndStareFullPriceInTheEye(this.root);

		this.sortSequenceAndEatRawDealForBreakfast(this.root);
	}

	/**
	 * Helper method for negotiateBigDeal method. Builds a hash of Region
	 * objects that will later be translated into RelativeView (Android-
	 * specific) objects and the player package's internal reperesentation of
	 * the media objects.
	 * 
	 * @param body
	 * @param parent
	 */
	private void makePrivateDeal(LinkedList<Body> body, ContainerPlayer parent) {
		/*
		 * Assuming that the Body contains every element and we need to
		 * explicitly find everything that is not a par or sequence object
		 */
		for (Body b : body) {
			/*
			 * If we are dealing with a Parallel or Sequence object, then we
			 * need to recursively call this function.
			 */
			if (b instanceof Parallel) {
				ParPlayer par = new ParPlayer(b.getBegin(), b.getEnd());
				par.bindArbiter(this.subject);
				parent.addComponent(par);
				makePrivateDeal(((Parallel) b).getBody(), par);
			} else if (b instanceof Sequence) {
				SeqPlayer seq = new SeqPlayer();
				seq.bindArbiter(this.subject);
				parent.addComponent(seq);
				makePrivateDeal(((Sequence) b).getBody(), seq);
			}
			/*
			 * Otherwise, we just need to build the object and add it to the
			 * parent container and stop the recusrive calling.
			 */
			else if (b instanceof Media) {
				SingleInstancePlayer media;
				if (b instanceof Text) {
					/*
					 * Get everything we need from the Text object and add it to
					 * the container instance
					 */
					media = new TextPlayer(((Text) b).getSrc(), b.getBegin(),
							b.getEnd() - b.getBegin());
				} else if (b instanceof Image) {
					/*
					 * Get everything we need from the Image object and add it
					 * to the container instance
					 */
					media = new ImagePlayer(((Image) b).getSrc(), b.getBegin(),
							b.getEnd() - b.getBegin());

				} else if (b instanceof Audio) {
					/*
					 * Get everything we need from the Audio object and add it
					 * to the container instance
					 */
					media = new AudioPlayer(((Audio) b).getSrc(), b.getBegin(),
							b.getEnd() - b.getBegin(),
							((Audio) b).getClipBegin());
				} else // b instanceof Video
				{
					/*
					 * Get everything we need from and Video object and add it
					 * to the container instance
					 */
					SmilDimension dims = ((Media) b).getRegion()
							.getDimensions();
					media = new VideoPlayer(((Video) b).getSrc(), b.getBegin(),
							b.getEnd() - b.getBegin(),
							((Video) b).getClipBegin(), dims.getWidth(),
							dims.getHeight());
				}

				/*
				 * Add region information from the SMIL Document. This will be
				 * used later to generate ViewGroup (Relative Layout)
				 * informaiton
				 */
				if (((Media) b).getRegion() != null) {
					this.regions.put(((Media) b).getRegion().getId(),
							(new RegionData(
									((Media) b).getRegion().getZindex(),
									((Media) b).getRegion().getDimensions(),
									((Media) b).getRegion().getOrigin())));
					media.layoutId = ((Media) b).getRegion().getId();
				}

				/*
				 * Bind an Arbiter to the media object and add it to the parent
				 * container class.
				 */
				media.bindArbiter(this.subject);
				parent.addComponent(media);
			}
		}
	}

	/**
	 * Method to generate ViewGroups (RelativeLayouts) based on the data
	 * collected in MakePrivateDeal. Works with the Hashtable<String,
	 * RegionData> object.
	 */
	private void generateViewGroupsAndDemandACheaperRate() {
		this.layouts = new ArrayList<Pair<String, Pair<Integer, RelativeLayout>>>();
		Enumeration<String> e = this.regions.keys();
		while (e.hasMoreElements()) {
			String id = e.nextElement();
			/*
			 * Obtain the layout information from the regionData and use it to
			 * build a RelativeLayout ViewGroup
			 */
			RegionData regionData = this.regions.get(id);
			SmilDimension smilDimension = regionData.dimension;
			SmilDimension origin = regionData.origin;

			RelativeLayout temp = new RelativeLayout(this.context);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					smilDimension.getWidth(), smilDimension.getHeight());
			lp.setMargins(origin.getWidth(), origin.getHeight(), 0, 0);

			temp.setLayoutParams(lp);

			/*
			 * Add the newly built ViewGroup to the Hashtable with the zIndex as
			 * the ID
			 */
			this.layouts.add(new Pair<String, Pair<Integer, RelativeLayout>>(
					id, new Pair<Integer, RelativeLayout>(regionData.zIndex,
							temp)));
		}
	}

	/**
	 * Render the ViewGroups to the container ViewGroup as passed into the
	 * PriceLine constructor (this.rootViewGroup).
	 */
	private void renderViewGroupsAndDemandAGoodDeal() {
		/*
		 * Define an array of tuples to hold a String and an Integer value to
		 * hold id's and zIndexes.
		 */
		ArrayList<Pair<Integer, RelativeLayout>> pairList = new ArrayList<Pair<Integer, RelativeLayout>>(
				this.layouts.size());

		/*
		 * Get an array of zIndexes and the associated ID's for each
		 * RelativeLayout and sort by zIndex
		 */
		for (Pair<String, Pair<Integer, RelativeLayout>> p : this.layouts) {
			pairList.add(p.two);
		}
		Collections.sort(pairList,
				new Comparator<Pair<Integer, RelativeLayout>>() {
					public int compare(Pair<Integer, RelativeLayout> a,
							Pair<Integer, RelativeLayout> b) {
						return a.one.compareTo(b.one);
					}
				});

		/*
		 * Starting with the lowest ID, render each RelativeView to the screen.
		 */
		for (Pair<Integer, RelativeLayout> p : pairList) {
			this.rootViewGroup.addView(p.two);
		}
	}

	/**
	 * Recursive function to attach a ViewGroup to any media element that is
	 * assigned one. This should be all elements besides container classes (Par
	 * and Seq) and audio elements.
	 */
	private void attachViewGroupsAndStareFullPriceInTheEye(
			ContainerPlayer parent) {
		for (Player p : parent.getComponents()) {
			if (p instanceof ContainerPlayer) {
				this.attachViewGroupsAndStareFullPriceInTheEye((ContainerPlayer) p);
			} else {
				((SingleInstancePlayer) p).bindView(this
						.findViewById(((SingleInstancePlayer) p).layoutId));
			}
		}
	}

	/**
	 * Return the RelativeLayout ViewGroup from the internal data-structure
	 * given the ID to the layout (generated from the SMIL message)
	 * 
	 * @param id
	 *            ID of the viewgroup
	 */
	private RelativeLayout findViewById(String id) {
		for (Pair<String, Pair<Integer, RelativeLayout>> p : this.layouts) {
			if (p.one == id) {
				return p.two.two;
			}
		}
		return null;
	}

	/**
	 * The current structure for the root (implicit Sequence object) is sorted
	 * by the ID (String value) and I need to sort them by the playback start-
	 * time... so we need to do some sorting here. (and eat raw deals of course)
	 * 
	 * @param cp
	 *            The container player to sort
	 * 
	 */
	private void sortSequenceAndEatRawDealForBreakfast(SeqPlayer cp) {
		Collections.sort(cp.components, new Comparator<Player>() {
			public int compare(Player a, Player b) {
				return ((Double) a.start).compareTo((Double) b.start);
			}
		});
		for (Player p : cp.components) {
			if (p instanceof SeqPlayer) {
				sortSequenceAndEatRawDealForBreakfast((SeqPlayer) p);
			}
		}
	}

	/**
	 * Return the implicit SeqPlayer root object as generated by the PriceLine
	 * class.
	 */
	public SeqPlayer getDocumentAndNameYourOwnPrice() {
		return this.root;
	}

	/**
	 * Define a struct-like class just to hold some data for a brief period of
	 * time.
	 * 
	 * @author John Murray
	 * @version 1.0 4/24/11
	 */
	private class RegionData {
		public SmilDimension origin;
		public SmilDimension dimension;
		public int zIndex;

		public RegionData(int zIndex, SmilDimension dim, SmilDimension origin) {
			this.origin = origin;
			this.dimension = dim;
			this.zIndex = zIndex;
		}
	}

	/**
	 * Define a Pair (or 2-tuple) since Java refuses to do so... :-(
	 * 
	 * @author John Murray
	 * @version 1.0 4/24/11
	 * 
	 * @param <T>
	 *            Generic Parameter
	 * @param <V>
	 *            Generic Parameter
	 */
	private class Pair<T, V> {
		public T one;
		public V two;

		public Pair(T one, V two) {
			this.one = one;
			this.two = two;
		}
	}
}
