package estadistica.pkg2014;

public class Estadistica2014 {

    public static void main(String[] args) {
        
	double A,B,C;
        int i;
	A=2014;
	for(i=10;i<=30;i++)
	{	
		B = Math.pow(10, i); 
		C=A+B-B;
		System.out.println (i+"   "+A+"   "+B+"   "+C);
	}
    }
}
