package net.kuangmeng;

public class sayHello {
   private String name;
   public String getName(){
	   return name;
   }
   public void setName(String name){
	   this.name=name;
   }
   public sayHello(){
	   System.out.println("Hello");
   }
   public sayHello(String name){
	   System.out.println("Hello:"+getName());
	   System.out.println("匡盟盟");
   }
}
