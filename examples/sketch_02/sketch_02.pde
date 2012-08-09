import net.hellonico.jtwitter.*;
import net.hellonico.potato.*;
import winterwell.jtwitter.Twitter.*;

JTwitterLibrary twitter;
Twitter jtwitter;

void setup() {
  size(800,400);
  background(0);
  color(0);
  twitter = new JTwitterLibrary(this);
  jtwitter = twitter.get();
  
  twitter.startStream("ありがとう");  
}

boolean processTweer(winterwell.jtwitter.Twitter.ITweet tweet) {
   println(tweet);
   return false; 
}

void draw() {
	
}