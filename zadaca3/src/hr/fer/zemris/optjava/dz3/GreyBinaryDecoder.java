package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;

public class GreyBinaryDecoder extends BitVectorDecoder{
	
	private NaturalBinaryDecoder binDec;

	public GreyBinaryDecoder(double[] mins, double[] maxs, int[] nBits, int n) {
		super(mins, maxs, nBits, n);
		this.binDec = new NaturalBinaryDecoder(mins, maxs, nBits, n);
	}
	
	public GreyBinaryDecoder(double mins, double maxs, int nBits, int n) {
		super(mins, maxs, nBits, n);
		this.binDec = new NaturalBinaryDecoder(mins, maxs, nBits, n);
	}
	
	@Override
	public double[] decode(SingleObjectiveSolution obj) {
		
		BitVectorSolution solution = (BitVectorSolution)obj;
		// broj varijabli
		int n = getN();
		// bitvektor cijelog rjesenja
		byte[] bits = solution.getBits();
		// bitovi nakon pretvorbe iz greyevog koda u binarni
		byte[] binaryBits = solution.newLikeThis().getBits();
		// dekodiran vektor
		double[] decoded = new double[n];
		
		for(int i = 0; i < n; i++) {
			int numOfBits = this.getnBits()[i];
			// kopija rjesenja u greyevom kodu
			byte[] tempGrey = Arrays.copyOfRange(bits, i * numOfBits, i * numOfBits + getnBits()[i]);
			// pretvorba u binarni kod
			byte[] tempBinary = greytoBinary(tempGrey);
			// rekonstrukcija rjesenja 
			for(int j = 0; j < tempBinary.length; j++) {
				binaryBits[i * numOfBits + j] = tempBinary[j];
			}
		}
		// poziv vec gotovog binarnog dekodera
		decoded = binDec.decode(new BitVectorSolution(binaryBits));
		return decoded;
	}

	@Override
	public void decode(double[] v, SingleObjectiveSolution obj) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Funkcija za pretvorbu iz greyegov koda u binarni
	 * 
	 * @param gray byte polje koje treba konvertirati
	 * @return kod u binarnoj reprezrentaciji predanog vektora
	 */
	public static byte[] greytoBinary(byte[] gray) 
	{ 
	    byte[] binary = new byte[gray.length]; 
	  
	    binary[0] = gray[0]; 
	  
	    for (int i = 1; i < gray.length; i++) { 
	        
	    	binary[i] = (byte)(binary[i - 1] ^ gray[i]);
	    } 
	    return binary; 
	} 

	
	public static void main(String[] args) {
//		byte[] bin = new byte[] {1,0,0,0};
//		byte[] gray = graytoBinary(bin);
//		System.out.println(Arrays.toString(bin));
//		System.out.println(Arrays.toString(gray));
		
		BitVectorSolution sos;
		sos = new BitVectorSolution(2*3);
		sos.setBits(new byte[] {1,1,0,0,1,1});
		System.out.println(sos);
		GreyBinaryDecoder dec = new GreyBinaryDecoder(2, 2, 3, 2);
		double[] decoded = dec.decode(sos);
		for(int i = 0; i < decoded.length; i++) {
			System.out.println(decoded[i]);
		}
		
	}
	
}
