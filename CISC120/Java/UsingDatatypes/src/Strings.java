public class Strings {
    public static void main(String[] args)
    {
         String myString ="Hello!";
         char[] chars = {'H', 'e', 'l', 'l', 'o', '!'};
         String myOtherString = new String(chars);

         for (int i = 0; i < myString.length(); i++) {
             char c = myString.charAt(i);
         }

         System.out.println(myString.substring(2));
         System.out.println(myString.contains("lo"));

         String url = "http://harrisburgu.edu";
         if (url.startsWith("https")){
             System.out.println("Secure transfer");
         }
         int j = url.indexOf("%");
         System.out.println(j);

         myOtherString = myOtherString.replace("l", "r");
         System.out.println(myOtherString);

         String dirty = "     Some stuff       ";
         System.out.println(">" + dirty + "<");
        System.out.println(">" + dirty.trim() + "<");

        System.out.println(url.matches("^h[t]{2}p[s]?://.*$"));

        String mail = "mailto:pgrim@harrisburgu.edu";

        System.out.println(mail.hashCode());
    }
}
