package com.seva60plus.hum.mediacentre;

/**
 * @author raisahab.ritwik
 **/
import java.util.ArrayList;

import com.seva60plus.hum.model.Video;

/** Interface that provides call-back on loading of you-tube play-list */
public interface YoutubePlayListListener {

	/** Returns call back of the Video play-list on this method */
	void videoPlaylistAsyncCallback(ArrayList<Video> result);

}
