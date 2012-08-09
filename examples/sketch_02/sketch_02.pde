
TwitterLibrary twitter;
Twitter jtwitter;

void setup() {
  size(800,400);
  background(0);
  color(0);
  twitter = new TwitterLibrary(this);
  jtwitter = twitter.get();
  
  twitter.startStream("ありがとう");  
}

void draw() {
	
}