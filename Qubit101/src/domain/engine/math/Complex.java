package domain.engine.math;

import java.math.BigDecimal;

public class Complex {

	private Double r;
	private Double i;
	
	/**
	 * Construct a new complex number.
	 * If the third argument is set to false,
	 * the complex number will be: a + b*i.
	 * If it is true, it will be: a*exp(i*b)
	 * 
	 * @param a: real part if polar=false, else the magnitude of the complex vector.
	 * @param b: imaginary part if polar=false, else the argument of the complex vector.
	 * @param polar: true indicates a complex number in polar form.
	 */
	public Complex(Double a, Double b, Boolean polar){
		if(!polar){
			setReal(a);
			setImaginary(b);
		}else{
			setReal(a*Math.cos(b));
			setImaginary(i=a*Math.sin(b));
		}
	}
	
	/**
	 * 
	 * Construct a new complex number.
	 * The complex number will be: a + b*i.
	 * 
	 * @param a: real part.
	 * @param b: imaginary part.
	 */
	public Complex(Double a, Double b){
		this(a,b,false);
	}
	
	/**
	 * Empty constructor, it creates a zero complex number.
	 */
	public Complex(){
		r=0.0;
		i=0.0;
	}
	
	/**
	 * @return the real part of the complex number.
	 */
	public Double getReal(){
		return r;
	}
	
	/**
	 * @return the imaginary part of the complex number.
	 */
	public Double getImaginary(){
		return i;
	}
	
	/**
	 * @return the argument of the complex number in polar form.
	 */
	public Double getArgument(){
		Double res = 0.0;
		if(r.equals(0.0) && i.equals(0.0)){
			res=0.0;
		}else if(r>=0){
			res=Math.asin(i/getMagnitude());
		}else if(r<0){
			res=-Math.asin(i/getMagnitude())+Math.PI;
		}
		return res;
	}
	
	/**
	 * @return the magnitude of the complex number.
	 */
	public Double getMagnitude(){
		return Math.sqrt(r*r+i*i);
	}
	
	/**
	 * Set the real part of the complex number to r.
	 * @param r: the real part of the complex number.
	 */
	public void setReal(Double r){
		this.r=r;
	}
	
	/**
	 * Set the imaginary part of the complex number to i.
	 * @param i: the imaginary part of the complex number.
	 */
	public void setImaginary(Double i){
		this.i=i;
	}
	
	/**
	 * Set the argument part of the complex number to arg.
	 * @param arg: the argument of the complex number.
	 */
	public void setArgument(Double arg){
		setReal(getMagnitude()*Math.cos(arg));
		setImaginary(getMagnitude()*Math.sin(arg));
	}
	
	/**
	 * Set the magnitude of the complex number to mag.
	 * @param mag: the magnitude of the complex number.
	 */
	public void setMagnitude(Double mag){
		setReal(mag*Math.cos(getArgument()));
		setImaginary(mag*Math.sin(getArgument()));
	}
	
	/**
	 * Sums the complex number with the argument.
	 * @param z: The complex number to be added.
	 * @return The resulting complex number.
	 */
	public Complex add(Complex z){
		return new Complex(r+z.getReal(),i+z.getImaginary(),false);
	}
	
	/**
	 * Subtracts the complex number with the argument.
	 * @param z: The complex number to be subtracted.
	 * @return The resulting complex number.
	 */
	public Complex subtract(Complex z){
		return new Complex(r-z.getReal(),i-z.getImaginary(),false);
	}
	
	/**
	 * Multiplies the complex number with the argument.
	 * @param z: The complex number to be multiplied.
	 * @return The resulting complex number.
	 */
	public Complex multiply(Complex z){
		return new Complex(r*z.getReal()-i*z.getImaginary(),i*z.getReal()+r*z.getImaginary(),false);
	}
	
	/**
	 * Divides the complex number with the argument.
	 * @param z: The complex divisor.
	 * @return The resulting complex number.
	 */
	public Complex divide(Complex z){
		if(z.equals(new Complex())) throw new ArithmeticException("Division by zero");
		return new Complex((r*z.getReal()+i*z.getImaginary())/(z.getReal()*z.getReal()+z.getImaginary()*z.getImaginary()),(i*z.getReal()-r*z.getImaginary())/(z.getReal()*z.getReal()+z.getImaginary()*z.getImaginary()),false);
	}
	
	/**
	 * Returns the result of raising this complex number to the power
	 * of the real argument.
	 * @param exp: The exponent of the operation.
	 * @return: The resulting complex number.
	 */
	public Complex pow(Double exp){
		return new Complex(Math.pow(getMagnitude(), exp),getArgument()*exp,true);
	}
	
	/**
	 * @return the complex conjugate of this complex number.
	 */
	public Complex conjugate(){
		return new Complex(getReal(),-getImaginary(),false);
	}
	
	
	
	
	public boolean equals(Object o){
		if(!(o instanceof Complex)) throw new IllegalArgumentException("The object must be a complex number");
		Complex z = (Complex)o;
		return(z.getReal().equals(r) && z.getImaginary().equals(i));
	}
	
	public String toString(){
		String res="";
		double dr = new BigDecimal(r).setScale(15, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		double di = new BigDecimal(i).setScale(15, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(dr==0.0 && di==0.0){
			res="0.0";
		}else if(dr==0.0){
			res=di+"i";
		}else if(di==0.0){
			res=String.valueOf(dr);
		}else{
			res = dr + "+" + di + "i";
		}
		return res;
	}
	
	/**
	 * @return the polar form representation of this complex number.
	 */
	public String polarForm(){
		String res = "";
		Double m = getMagnitude();
		Double a = getArgument();
		if(a == 0.0){
			res = String.valueOf(m);
		}else if(m==0.0){
			res = "0.0";
		}else if(m==1.0){
			res = "exp(" + a +"*i)";
		}else{
			res = m + "*exp(" + a +"*i)";
		}
		return res;
	}
}
