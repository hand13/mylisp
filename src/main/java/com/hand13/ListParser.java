package com.hand13;

import java.io.InputStream;

import static com.hand13.Token.LAMBDA;

public class ListParser {
   private TokenStream ts;

   public ListParser(InputStream in) {
       ts = new TokenStream(in);
   }

   public List getNextList() {
       String token = ts.getNextToken();
       if(token ==null || !token.equals("(")) {
           throw new RuntimeException("error parse");
       }
       token =ts.getNextToken();
       if(token == null) {
           throw new RuntimeException("error parse");
       }
       if(token.equals(")")) {
           return new List(null,null);
       }
       ts.back();
       List header= new List(getNextObject(),null);
       List tmp = header;
       Object o;
       while((o = getNextObject()) != null) {
           tmp.snd  = new List(o,null);
           tmp = (List)tmp.snd;
       }
       String t = ts.getCurrentToken();
       if(t == null) {
           throw new RuntimeException("parse error");
       }
       return header;
   }
   public boolean isInner(String token) {
       return token.equals("lambda");
   }
   public Object getNextObject() {
       String token = ts.getNextToken();
       if(token == null || token.equals(")")) {
           return null;
       }
       if(token.equals("(")) {
           ts.back();
           return getNextList();
       }
       if(token.equals("'")) {
           return new QuotedList(getNextObject());
       }
       //字符串
       if(token.startsWith("\"")) {
           return token.substring(1,token.length()-1);
       }
       if(Character.isDigit(token.charAt(0))) {
           return Double.valueOf(token);
       }
       if(isInner(token)) {
           return LAMBDA;
       }
       return new Symbol(token);
   }
}
