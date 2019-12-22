
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class SelectTest {

	private static final int MAX_VALUE = 1000000;
	private static final int MAX_LENGTH = 100;
	private static final int NUM_OF_TESTS = 20;


	public static void main(String[] args) {

		selectProblems s = new selectProblems();

		for (int n = 1; n <= MAX_LENGTH; n++) {

			for (int i = 0; i < NUM_OF_TESTS; i++) {
				int[] array = randomArray(n);
				int[] sortedArray = array.clone();
				Arrays.sort(sortedArray);

				for (int k = 1; k <= n; k++) {

					int actual = s.selectRandQuickSort(array.clone(), k).getKey();
					if (actual != sortedArray[k-1]) {
						error("selectRandQuickSort", array, actual, sortedArray[k-1], k);
						return;
					}
					
					actual = s.selectInsertionSort(array.clone(), k).getKey();
					if (actual != sortedArray[k-1]) {
						error("selectInsertionSort", array, actual, sortedArray[k-1], k);
						return;
					}
					
					
				
					
					actual = s.randQuickSelect(array.clone(), k).getKey();
					if (actual != sortedArray[k-1]) {
						error("randQuickSelect", array, actual, sortedArray[k-1], k);
						return;
					}
					
					actual = s.medOfMedQuickSelect(array.clone(), k).getKey();
					if (actual != sortedArray[k-1]) {
						error("medOfMedQuickSelect", array, actual, sortedArray[k-1], k);
						return;
					}
					
				}

			}

		}
		
		System.out.println("Done");

	}

	private static void error(String algo, int[] array, int actual, int expected, int k) {
		
		System.out.println(algo + " failed!");
		System.out.println("Input array: " + Arrays.toString(array));
		System.out.print("k: " + k + "  |  ");
		System.out.print("Expected: " + expected + "  |  ");
		System.out.print("Actual:" + actual);
		
	}

	private static int[] randomArray(int n) {

		int[] res = new int[n];

		for (int i = 0; i < n; i++) {
			res[i] = randInt(0, MAX_VALUE);
		}

		return res;
	}

	private static int randInt(int min, int max) {

		return ThreadLocalRandom.current().nextInt(min, max + 1);

	}

}
