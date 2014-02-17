import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class HuffmanMain {

	public Map<Character, Integer> ftable; 
	public BufferedBitReader bitInput;
	
	public Map<Character, Integer> FrequencyTable(String compressedPathName) throws IOException {
		bitInput = new BufferedBitReader(compressedPathName);
		while(bitInput.readBit() != -1) {
			if(ftable.containsKey(key)) {
				int temp = ftable.get(key) + 1;
				ftable.put(key, temp);
			}
			else {
				
			}
		}
		
		bitInput.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
}
