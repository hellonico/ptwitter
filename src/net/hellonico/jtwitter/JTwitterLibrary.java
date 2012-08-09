/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package net.hellonico.jtwitter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import processing.core.PApplet;
import winterwell.jtwitter.AStream;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterEvent;
import winterwell.jtwitter.TwitterStream;

/**
 * This is a template class and can be used to start a new processing library or
 * tool. Make sure you rename this class as well as the name of the example
 * package 'template' to your own library or tool naming convention.
 * 
 * @example Hello
 * 
 *          (the tag @example followed by the name of an example included in
 *          folder 'examples' will automatically include the example in the
 *          javadoc.)
 * 
 */

public class JTwitterLibrary {

	PApplet myParent;
	Twitter twitter;
	TwitterStream stream;

	public final static String VERSION = "##library.prettyVersion##";

	public JTwitterLibrary(PApplet theParent) {
		myParent = theParent;

		try {
			Class klass = Class.forName("net.hellonico.potato.Potato");
			Constructor c = klass.getConstructor(PApplet.class);
			Object potato = c.newInstance(theParent);
			Method m = klass.getMethod("getSettings", String.class);
			HashMap settings = (HashMap) m.invoke(potato, "twitter");

			twitter = new Twitter("my-name", authenticate(settings));
		} catch (Exception e) {
			throw new RuntimeException("This is carrot day." + e.getMessage());
		}
	}

	public JTwitterLibrary(PApplet theParent, HashMap settings) {
		myParent = theParent;
		twitter = new Twitter("my-name", authenticate(settings));
	}

	private OAuthSignpostClient authenticate(HashMap settings) {
		if (settings.containsKey("token0")) {
			try {
				return this.reuseAuthentication(settings);
			} catch (Exception e) {
				// maybe outdated token ?
				// retry authentication
			}
		}
		return this.askForAuthentication(settings);
	}

	public Twitter get() {
		return this.twitter;
	}

	public static String version() {
		return VERSION;
	}

	public OAuthSignpostClient askForAuthentication(HashMap settings) {
		OAuthSignpostClient oauthClient = new OAuthSignpostClient(
				(String) settings.get("key"), (String) settings.get("secret"),
				"oob");
		oauthClient.authorizeDesktop();
		String v = oauthClient
				.askUser("Please enter the verification PIN from Twitter");
		oauthClient.setAuthorizationCode(v);
		String[] accessToken = oauthClient.getAccessToken();

		// to keep for later
		System.out.println("token0:" + accessToken[0]);
		System.out.println("token1:" + accessToken[1]);

		return oauthClient;
	}

	public OAuthSignpostClient reuseAuthentication(HashMap settings) {
		return new OAuthSignpostClient((String) settings.get("appKey"),
				(String) settings.get("secret"),
				(String) settings.get("token0"),
				(String) settings.get("token1"));
	}
	
	public void startStream(String... keyword) {
		stream = new TwitterStream(twitter);
		stream.connect();
		stream.setTrackKeywords(Arrays.asList(keyword));
		stream.addListener(new SimpleListener(this.myParent));  
	}
	public void stopStream() {
		stream.close();
	}
	
	class SimpleListener implements AStream.IListen {
		Method tweetMethod;
		private PApplet parent;
		
		public SimpleListener(PApplet parent) {
			this.parent = parent;
			try {
				Method tweetMethod = myParent.getClass().getMethod("processTweer",
						new Class[] { winterwell.jtwitter.Twitter.ITweet.class });
			} catch (Exception e) {
				
			}
		}
		
	    public boolean  processEvent(TwitterEvent event)  {
	       return false;
	    }
	    public boolean  processSystemEvent(java.lang.Object[] obj) {
	       return false;
	    } 
	    public boolean  processTweet(Twitter.ITweet tweet) {
	    	try {
				return (Boolean) tweetMethod.invoke(parent, tweet);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	    }
	}

}
