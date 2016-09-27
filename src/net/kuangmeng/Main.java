package net.kuangmeng;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args){
		//提示用户开始输入多项式
       System.out.print("请输入多项式：");
       //读入多项式
    @SuppressWarnings("resource")
	Scanner in = new Scanner(System.in);
       String poly = in.nextLine();
       //新建字符串数组保存每一项
       String[] List = new String[100];
       //整形数组保存每一项的符号
       int[] sign=new int[10];
       //进行化简并返回项数
       int num=expresstion(poly,List,sign);
       //开始读入指令
       System.out.print("请输入命令：");
       String cmd = in.next();
       String assign= new String();
       if(cmd.equals("!simplify")){
    	   //读入化简的var序列
           assign=in.nextLine();
           if(assign.length()<4 || !Character.isDigit(assign.charAt(assign.length()-1))){
        	   System.out.println("指令有误！");
    		   System.exit(1);
           }else{
        	   for(int i=0;i<assign.length()-1;i++){
        		   if((Character.isDigit(assign.charAt(i)) && Character.isLetter(assign.charAt(i+1))) || (Character.isLetter(assign.charAt(i)) && Character.isDigit(assign.charAt(i+1)))){
        		   System.out.println("指令有误！");
        		   System.exit(1);
        		   }
        	   }
           }
           System.out.print("化简结果为：");
           simplify(List,num,assign,sign);
       }else if(cmd.equals("!d/d")){
    	   //读入求导变量
    	   assign=in.next();
    	   if(assign.length()>1 || !Character.isLetter(assign.charAt(0))){
    		   System.out.println("指令有误！");
    		   System.exit(1);
    	   }
    	   System.out.print("求导结果为：");
    	   if(!poly.contains(assign)){
    		   System.out.print(0);
    		   System.exit(0);
    	   }
    	   derivative(List,num,assign,sign);
       }else{
    	   //错误命令判断
    	   System.out.println("命令有误！");
    	   System.exit(0);
       }
	}
	//存储表达式，并输出表达式
    public static int expresstion(String poly,String[] List,int[] sign){
    	//处理空格
    	char[] chars=poly.toCharArray();
    	String result=new String();
    	for(char ch:chars){
    		if(ch!=' ' && ch!='\t') result+=ch;
    	}
    	 int size =result.length();
    	 //判断多项式是否有误
    	 for(int i=0;i<size;i++){
    		 if(!(Character.isLetter(result.charAt(i)) || Character.isDigit(result.charAt(i))) && !(Character.isLetter(result.charAt(i+1)) || Character.isDigit(result.charAt(i+1)))){
      		   System.out.println("多项式有误！");
      		   System.exit(1);
      		   }
    	 }
    	 //多项式分割
    	 int first = 0;
    	 int t=0;
    	 char[] element= new char[10];
    	 int index=0;
    	 if(result.charAt(0)=='-')  sign[0]=0;
		 else sign[0]=1;
    	 for(int i=0;i<size;i++){
    		 if(Character.isLetter(result.charAt(i))){
    		    element[index++]=result.charAt(i);	
    		 }
    		 if(i>0 && !Character.isLetter(result.charAt(i)) && !Character.isDigit(result.charAt(i))){
    			 if(result.charAt(i) == '+' || result.charAt(i)=='-'){
    				 if(result.charAt(i)=='+')  sign[t+1]=1;
    				 else sign[t+1]=0;
    				 List[t++]=result.substring(first, i);
    				 first = i+1;
    			 }
    		 }
    	 }
    	 List[t++]=result.substring(first);
    	 if(List[0].charAt(0)=='-') List[0]=List[0].substring(1);
    	 for(int i=0;i<index-1;i++){
    		 for(int j=i+1;j<index;j++){
    			 if(element[i]==element[j] && element[i] != ' '){
    				 element[j]=' ';
    			 }
    		 }
    	 }
    	 int arity=0;
    	 for(int i=0;i<index;i++){
    		 if(element[i] != ' ') arity++;
    	 }
    	 //计算次数
    	 int[] time=new int[t];
    	 for(int i=0;i<t;i++){
    		 for(int j=0;j<List[i].length();j++){
    			 if(Character.isLetter(List[i].charAt(j))){
    				 time[i]++; 
    			 }else if(List[i].charAt(j)=='^'){
    				 time[i]+=Integer.parseInt(String.valueOf(List[i].charAt(j+1)))-1;
    			 }
    		 }
    	 }
    	 for(int i=1;i<t;i++){
    		 if(time[i]>time[0]) time[0]=time[i];
    	 }
    	 System.out.println("该多项式为"+arity+"元"+time[0]+"次"+t+"项式："+result);
    	 //化简List，处理省略*的情况
    	 for(int i=0;i<t;i++){
    		 int len=List[i].length();
    		 char[] chs=new char[100];
    		 int suffix=0;
    		 for(int j=0;j<len-1;j++){
    			 chs[suffix++]=List[i].charAt(j);
    			 if((Character.isDigit(List[i].charAt(j)) && Character.isLetter(List[i].charAt(j+1))) ||(Character.isLetter(List[i].charAt(j)) && Character.isLetter(List[i].charAt(j+1))) )  chs[suffix++]='*';
    		 }
    		 chs[suffix++]=List[i].charAt(len-1);
    		 List[i]=String.valueOf(chs);
    	 }
    	 //化简List ，处理^
    	 for(int i=0;i<t;i++){
    		 List[i]=List[i].trim();
    		 int len=List[i].length();
    		 char[] chs=new char[100];
    		 int suffix=0;
    		 if(len<=2) continue;
    		 else{
    			 chs[suffix++]=List[i].charAt(0);
    			 for(int j=1;j<len;j++){
        			 if(List[i].charAt(j)=='^'){
        				 int num=Integer.parseInt(String.valueOf(List[i].charAt(j+1)));
        				 for(int k=0;k<num-1;k++){
        					 chs[suffix++]='*';
        					 chs[suffix++]=List[i].charAt(j-1);
        				 }
        			 } else if(List[i].charAt(j-1) !='^'){
        				 chs[suffix++]=List[i].charAt(j);
        			 }
        		 }
    		 }
    		 List[i]=String.valueOf(chs);
    	 }
    	 return t;
    }
    //化简表达式
    public static void simplify(String[] List,int num,String assign,int[] sign){
    	String[] sim=List;
    	int size=num;
    	String[] strArr = new String[10];
        int index =0;
        int first = 1;
        for(int i=1;i<assign.length();i++){
     	   if(' ' == assign.charAt(i)){
     		   strArr[index++]=assign.substring(first, i);
     		   first=i+1;
     	   }
        }
        strArr[index++]=assign.substring(first, assign.length());
    	char[] indexChar = new char[index];
    	String[] indexInt = new String[index];
    	int[] sym=new int[index];
    	for(int i=0;i<index;i++){
    		indexChar[i]=strArr[i].charAt(0);
    		if(strArr[i].charAt(2)=='-'){
    			sym[i]=0;
    			indexInt[i] =strArr[i].substring(3,strArr[i].length());
    		}
    		else {
    			sym[i]=1;
    			indexInt[i] =strArr[i].substring(2,strArr[i].length());
    		}
    	}
    	for(int i=0;i<size;i++){
    		for(int j=0;j<List[i].length();j++){
    		    for(int k=0;k<index;k++){
    		    	if(List[i].charAt(j)==indexChar[k]){
    		    		sim[i]=List[i].substring(0, j)+indexInt[k]+List[i].substring(j+1,List[i].length());
    		    		if(sym[k]==0){
    		    			if(sign[i]==1) sign[i]=0;
    		    			else sign[i]=1;
    		    		}
    		    	}
    		    }
    		}
    	}
    	String com=new String();
    	for(int i=0;i<size;i++){
    		if(sign[i]==1 && i>0) com+="+";
    		else if(sign[i]==0) com+="-";
    		com+=compute(sim[i]);
    	}
    	output(com);
    }
    //求导
    public static void derivative(String[] List,int num,String assign,int[] sign){
    	String[] sim = List;
    	int size = num;
    	int[] items=new int[size];
    	for(int i=0;i<size;i++){
    		items[i]=0;
    		//求出每个项的未知数个数
    		for(int j=0;j<sim[i].length();j++){
    			if(sim[i].charAt(j) == assign.charAt(0)){
    				items[i]++;
    			}
    		}
    		if(items[i]==0){
    			sim[i]="";
    		}else{
    			for(int j=0;j<sim[i].length();j++){
    				if(sim[i].charAt(j) == assign.charAt(0)){
    					sim[i]=sim[i].substring(0, j)+String.valueOf(items[i])+sim[i].substring(j+1, sim[i].length());
    					break;
    				}
    			}
    		}
    	}
       	//求导结果
     String com=new String();
    	for(int i=0;i<size;i++){
    		if(sim[i] !=""){
    				if(sign[i]==1) com+="+";
    				else com+="-";
    				com+=compute(sim[i]);		
    		  }
    	}
    	if(com.charAt(0)=='+') com=com.substring(1);
    	output(com);
    	}
//乘式化简    
public static String compute(String str){
    	String revert=new String();
    	if(!str.contains("*")){
    		return str;
    	}else{
    		String[] item=new String[10];
    		int index=0;
    		int first=0;
    		for(int i=0;i<str.length();i++){
    			if(str.charAt(i)=='*'){
    				item[index++]=str.substring(first, i);
    				first=i+1;
    			}
    		}
    		item[index++]=str.substring(first, str.length());
    		int size=index;
    		int[] num=new int[size];
    		for(int i=0;i<size;i++){
    			num[i]=1;
    			if(Character.isDigit(item[i].charAt(0))){
    				num[i]=Integer.parseInt(item[i].trim());
    			}
    		}
    		int numsize=1;
    		for(int i=0;i<size;i++){
    			numsize*=num[i];
    		}
    		revert+=String.valueOf(numsize);
    	    for(int i=0;i<size;i++){
    	    	if(Character.isLetter(item[i].charAt(0))){
    	    		revert+="*"+item[i];
    	    	}
    	    }
    	}
		return revert;
    }
//加式化简  
 public static void output(String com){
    	int intcom=0;
    	String backstr=new String();
    	//默认不超过10项
    	String[] poly=new String[10];
    	int[] sign=new int[10];
    	int first=0;
    	int index=0;
    	if(com.charAt(0)=='-') sign[0]=0;
    	else sign[0]=1;
     for(int i=0;i<com.length();i++){
	       if((com.charAt(i) == '+' || com.charAt(i)=='-') && i>0){
	    	   if(com.charAt(i)=='+') sign[index+1]=1;
	    	   else sign[index+1]=0;
	    	    poly[index++]=com.substring(first, i);
	    	    first=i+1;
	       }
   }
     poly[index++]=com.substring(first, com.length());
     if(poly[0].charAt(0)=='-') poly[0]=poly[0].substring(1);
     for(int i=0;i<index;i++){
    	    if(isNumeric(poly[i])){
    	    	if(sign[i]==1) intcom+=Integer.parseInt(poly[i].trim());
    	    	else intcom-=Integer.parseInt(poly[i].trim());
    	    }
     }
     if(intcom != 0) backstr+=String.valueOf(intcom);
     for(int i=0;i<index;i++){
    	      if((poly[i].contains("*") || Character.isLetter(poly[i].charAt(0))) && sign[i]==1) backstr+="+"+poly[i].trim();
    	      else if((poly[i].contains("*") || Character.isLetter(poly[i].charAt(0))) && sign[i]==0) backstr+="-"+poly[i].trim();
     }
     if(backstr.charAt(0)=='+') backstr=backstr.substring(1);
     System.out.println(backstr);
    }
//判断是否是数字   
public static boolean isNumeric(String str){
	   str=str.trim();
	   for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
			   	return false;
		   }
	   }
	   return true;
	  }

//测试
public void sayHello(){
	System.out.println("Hello");
	System.out.println("Hello");
}
}

