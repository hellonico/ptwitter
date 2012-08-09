// OLD example, no integration

import winterwell.jtwitter.guts.*;
import com.winterwell.jgeoplanet.*;
import winterwell.json.*;
import winterwell.jtwitter.android.*;
import winterwell.jtwitter.ecosystem.*;
import winterwell.jtwitter.*;

// http://www.winterwell.com/software/jtwitter/javadoc/
// http://www.winterwell.com/software/jtwitter/javadoc/winterwell/jtwitter/Twitter.html



Twitter twitter;
TwitterStream a;
XML xml;
String OAUTH_KEY;
String OAUTH_SECRET;

class MyCallback implements Twitter.ICallback {
    boolean process(List<Status> statuses) {
      background(0);
      for (int i = 0, n = statuses.size(); i < n; i++) {
       text(statuses.get(i).toString(), 10, 50*(i+1)); 
      }
      return false;
    } 
}  

class MyListener implements AStream.IListen {
    int counter = 0;
    boolean  processEvent(TwitterEvent event)  {
       return false;
    }
    boolean  processSystemEvent(java.lang.Object[] obj) {
       return false;
    } 
    boolean  processTweet(Twitter.ITweet tweet) {
      if(counter<20) {
         text(tweet.getText(), 10, 30*(counter+1));
         counter++; 
      } else {
          counter=0;
          background(0);
      }
      return false;
    }
}

OAuthSignpostClient askForAuthentication() {
  OAuthSignpostClient oauthClient = new OAuthSignpostClient(OAUTH_KEY, OAUTH_SECRET, "oob");
  oauthClient.authorizeDesktop();
  String v = oauthClient.askUser("Please enter the verification PIN from Twitter");
  oauthClient.setAuthorizationCode(v);
  String[] accessToken = oauthClient.getAccessToken();
  
  // to keep for later
  println(accessToken[0]);
  println(accessToken[1]);
  
  return oauthClient;
}

OAuthSignpostClient reuseAuthentication() {  
  String ACCESS_TOKEN_0 = xml.getChild("token_0").getContent();
  String ACCESS_TOKEN_1 = xml.getChild("token_1").getContent();
  return new OAuthSignpostClient(OAUTH_KEY, OAUTH_SECRET, ACCESS_TOKEN_0, ACCESS_TOKEN_1);
}

void setup() {
  xml = loadXML("twitter.xml");
  OAUTH_KEY = xml.getChild("oauth_key").getContent();
  OAUTH_SECRET = xml.getChild("oauth_secret").getContent(); 
  
  //twitter = new Twitter("my-name", askForAuthentication() );
  twitter = new Twitter("my-name", reuseAuthentication() );
  
  size(800,400);
  background(0);
  color(0);
  List l = twitter.getHomeTimeline();
   
  text(twitter.getStatus("hellonico").toString(), 50, 50);
  
  a = new TwitterStream(twitter);
  a.connect();
  a.setTrackKeywords(Arrays.asList(["ありがとう"]));
  a.addListener(new MyListener());
  
  //  twitter.setStatus("Morning Coffee");
  
}



void draw() {
    
}

void keyPressed() {
  println(keyCode);
  if(keyCode==32) {
   if(a.isConnected()) {
      a.close();
   } else {
      a.connect();
      background(0);
   }  
  } 
  if(keyCode==83) {
    twitter.search("ありがとう" , new MyCallback(), 3);  
  }
  
}
