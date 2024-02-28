import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * This works by checking if any possible word in the english matches the patern LXLXX, which is the last word in the puzzle
 */
public class Solver{
    public static void main(String[] args) {
        //All English words with only letters (no numbers/punctuation)
        ArrayList<String> allWords = load("words_alpha.txt");

        ArrayList<String> sizeWords = new ArrayList<String>();
        for(String word : allWords) {
            //Assuming all symbols correspond to at least one character, LXLXX can't be less than 4 chars long or exactly 6 chars long
            if(word.length() <= 4 || word.length() == 6) continue;

            sizeWords.add(word);
        }
        save("sizeWords.txt", sizeWords);

        ArrayList<String> sizeWords2 = new ArrayList<String>();
        for(String word : sizeWords) {
            //The word has to contain the first character at least twice
            String shorterWord = word.substring(1, word.length());

            if(!shorterWord.contains("" + word.charAt(0))) continue;
            sizeWords2.add(word);
            //System.out.println(word);
        }
        save("sizeWords2.txt", sizeWords2);

        ArrayList<String> sizeWords3 = new ArrayList<String>();
        for(String word : sizeWords2) {
            // Similary, the word has to include the second character at least twice
            // This does not apply for characters past the second
            String shorterWord = word.substring(2, word.length());

            if(!shorterWord.contains("" + word.charAt(1))) continue;
            sizeWords3.add(word);
            System.out.println(word);
        }
        save("sizeWords3.txt", sizeWords3);

        ArrayList<String> sizeWords4 = new ArrayList<String>();
        for(String word : sizeWords3) {
            // The solved word has to contain at least 3 of the same character
            if(maxCharTimes(word) < 3) continue;

            sizeWords4.add(word);
            System.out.println(word);
        }
        save("sizeWords4.txt", sizeWords4);

        // Checks if the character has a possible repeating group of characters
        // Because LXLXX contains "LX" twice, there has to be some repeating group of characters
        ArrayList<String> almostAnswer = new ArrayList<String>();
        for(String word : sizeWords4) {
            if(!hasRepeat(word)) continue;

            almostAnswer.add(word);
        }

        save("almostAnswer.txt", almostAnswer);
    }

    //Checks if the word contains any repeating set of characters. This essentially checks if the word has a possible LX metacharacter
    private static boolean hasRepeat(String str) {
        for(int count = (str.length() / 2); count >= 2; count--) {
            if(repeatFirst(str, count)) return true;
        }

        return false;
    }

    //Checks if a string starts with a given substring length
    private static boolean repeatFirst(String str, int subStrLength) {
        String shorterWord = str.substring(subStrLength, str.length());
        String first = str.substring(0, subStrLength);

        //If it doesn't start with the substring, it doesn't repeat it
        if(!shorterWord.startsWith(first)) return false;

        //If the word is double the length of the substring, the word isn't valid
        if(str.length() == 2 * subStrLength) return false;

        //If the word is more than 3 times the length of the substring, the word isn't valid
        if(str.length() >= 3 * subStrLength) return false;
        return true;
    }

    //Counts the most amount of times a character appears in a word
    // i.e., "test" returns 2, because the character 't', appears twice, more than any other character
    private static int maxCharTimes(String str) {
        int maxTimes = -1;

        for(char c = 'a'; c <= 'z'; c++) {
            int charCount = charTimes(str, c);

            if(charCount > maxTimes) maxTimes = charCount;
        }

        return maxTimes;
    }

    //Counts the amount of times a character appears in a word
    private static int charTimes(String str, char testChr) {
        int count = 0;

        for(char chr : str.toCharArray()) {
            if(chr == testChr) count++;
        }

        return count;
    }

    // Saves the list of words into a file, useful for reading the output of the filters
    private static void save(String filePath, ArrayList<String> list) {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for(String word : list) {
                bw.write(word + "\n");
            }

            bw.close();
        } catch (IOException e) {
            System.err.println("Could not save to " + filePath);
            e.printStackTrace();
            System.exit(0);
        }
    }

    // Loads a list of words into an ArrayList, useful for testing and loading the dictionary
    private static ArrayList<String> load(String filePath) {
        ArrayList<String> words = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(new File(filePath));

            while(sc.hasNext()) {
                String word = sc.nextLine();
                //System.out.println(word);
                words.add(word);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not open " + filePath);
            e.printStackTrace();
            System.exit(0);
        } 

        return words;
    }
}