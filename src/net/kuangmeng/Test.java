package net.kuangmeng;

public class Test {
	private String name;
	   public String getName(){
		   return name;
	   }
	   public void setName(String name){
		   this.name=name;
	   }
	   public void sayHello(){
		   System.out.println("Hello");
	   }
	   public void sayHello(String name){
		   System.out.println("Hello:"+getName());
		   System.out.println("匡盟盟");
	   }
}
