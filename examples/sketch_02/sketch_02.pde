import net.hellonico.ptwitter.*;
import net.hellonico.potato.*;
import winterwell.jtwitter.*;

JTwitterLibrary twitter;

void setup() {
  size(1024, 800);
  background(0);
  color(0);
  
  // bare minimum
  twitter = new JTwitterLibrary(this);
  twitter.startStream("Tokyo");  
}

boolean processTweet(winterwell.jtwitter.Twitter.ITweet tweet) {
   fill(random(255),random(128),random(255),127+ random(127));
   text(tweet.getText(), random(width), random(height));
   // keep looking
   return false; 
}

void mousePressed() {
  // clean up the mess
  background(0);  
}

void draw() {
  
}