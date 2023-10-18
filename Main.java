import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	//return char[] of alphabet a-z
	static char[] getAlphabet() {
		char[] alphabet = new char[26];
		alphabet[0] = 'a';
		//fill alphabet with letters a-z
		for (int i = 1; i < 26; i++)
			alphabet[i] = (char)((int)alphabet[i-1] + 1);
		return alphabet;
	}
	//fill letterCt with ct of each letter in given string
	static int[] getLetterCt(String word) {
		char[] alphabet = getAlphabet();
		int[] letterCt = new int[26];
		for (int i = 0; i < 26; i++)
			letterCt[i] = 0;
		for (int i = 0; i < word.length(); i++)
			for (int j = 0; j < 26; j++)
				if (word.charAt(i) == alphabet[j]) letterCt[j]++;
		return letterCt;
	}
	//check if word fits in letters (input letterCt arrays)
	static boolean doesFit(int[] word, int[] letters) {
		for (int i = 0; i < 26; i++) {
			//System.out.println(letters[i] + ", " + word[i]);
			if (letters[i] < word[i]) return false;
		}
		return true;
	}
	//check if word can be drawn in gameboard
	static boolean canDraw(String word, char[][] gameBoard) {
		if (word.length() < 2) return false;
		ArrayList<Integer> xList = new ArrayList<Integer>();
		ArrayList<Integer> yList = new ArrayList<Integer>();
		for (int r = 0; r < gameBoard.length; r++)
			for (int c = 0; c < gameBoard[0].length; c++)
				if (word.charAt(0) == gameBoard[r][c]) {
					xList.add(r);
					yList.add(c);
					int xDif, yDif, x, y;
					int iStart = 0;
					boolean canAdd;
					do {
						while (iStart == 9 && xList.size() > 0) {
							if (xList.size() > 1) iStart = 3*(yList.get(0) - yList.get(1)) + xList.get(0) - xList.get(1) + 5;
							xList.remove(0);
							yList.remove(0);
						}
						for (int i = iStart; i < 9; i++) {
							canAdd = true;
							xDif = i%3-1;
							yDif = i/3-1;
							x = xList.get(0) + xDif;
							y = yList.get(0) + yDif;
							if (x < 0 || x >= gameBoard.length || y < 0 || y >= gameBoard[0].length) canAdd = false;
							if (canAdd && gameBoard[x][y] != word.charAt(xList.size())) canAdd = false;
							if (canAdd)
								for (int j = 0; j < xList.size(); j++)
									if (x == xList.get(j) && y == yList.get(j)) canAdd = false;
							if (canAdd) {
								xList.add(0,x);
								yList.add(0,y);
								iStart = 0;
								if (xList.size() == word.length()) return true;
								break;
							} else if (i == 8) {
									if (xList.size() > 1) iStart = 3*(yList.get(0) - yList.get(1)) + xList.get(0) - xList.get(1) + 5;
									xList.remove(0);
									yList.remove(0);
							}
							
						}
					} while(xList.size() > 0);
				}
		return false;
	}
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
		
		System.out.print("Welcome to AnaNets!\n\nName of word list (ex: Dictionary.txt): ");
		Scanner dictReader = new Scanner(new File(in.nextLine()));
		System.out.println("\nSeparate lines by /, otherwise regular anagrams");
		System.out.print("Letters: ");
		
		//grab letters used for anagram
		String letters = in.nextLine();
		int[] letterCt = getLetterCt(letters);
		
		//only for use as multi-line game
		boolean multiLine = letters.indexOf("/") > 0;
		char[][] gameBoard = new char[0][0];
		if (multiLine) {
			//number of columns is number of chars before it gets to a /
			int colCt = letters.indexOf("/");
			int rowCt = (letters.length()+1)/(colCt+1);
			gameBoard = new char[rowCt][colCt];
			for (int r = 0; r < rowCt; r++)
				for (int c = 0; c < colCt; c++)
					gameBoard[r][c] = letters.charAt(r*(colCt+1)+c);
		}
		
		//create solutions list
		ArrayList<String> solutions = new ArrayList<String>();
		String word;
		while (dictReader.hasNextLine()) {
			word = dictReader.nextLine();
			if (word.length() > 2 && doesFit(getLetterCt(word),letterCt) && (!multiLine || canDraw(word,gameBoard))) solutions.add(word);
		}
		int maxLen = 0, solLen;
		for (int i = 0; i < solutions.size(); i++) {
			solLen = solutions.get(i).length();
			if (solLen > maxLen) maxLen = solLen;
		}
		String output = "";
		//create list of all solutions from longest to shortest
		for (int i = maxLen; i > 0; i--)
			for (int j = 0; j < solutions.size(); j++) {
				word = solutions.get(j);
				if (word.length() == i) output += word + ", ";
			}
		output = output.length()>2?output.substring(0,output.length()-2):"No solution found";
		System.out.println(output);
				
		
	}		
	
}