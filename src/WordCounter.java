import java.io.*;
import java.util.*;

public class WordCounter
{
	private int numChapters;
	private ArrayList<Integer> wordsByChapter = new ArrayList<>();
	private int numWords;
	private int numCharacters;
	private Map<String, Integer> wordMap = new HashMap<String, Integer>();
	private String dir;
	final String LOG_FILE = "/wordHistory.txt";

	public WordCounter(String dir)
	{
		this.dir = dir;
		analyzeTxtFiles();
		printResults();
	}

	private void analyzeTxtFiles()
	{
		int chapter = 1;
		while(true)
		{
			StringBuilder chapterTxt = new StringBuilder();
			String path = dir + "/Chapter_" + String.valueOf(chapter) + ".txt";
			try
			{
				File file = new File(path);
				Scanner myReader = new Scanner(file);
				while (myReader.hasNextLine())
				{
					String data = myReader.nextLine();
					chapterTxt.append(data);
				}
				analyzeTxt(chapterTxt.toString());
				myReader.close();
			}
			catch (FileNotFoundException e)
			{
				if (chapter == 1)
					System.out.println("No chapters found- did you give me a bad directory?");
				else
					numChapters = chapter - 1;
				return;
			}
			chapter++;
		}
	}

	private void analyzeTxt(String novel)
	{
		wordsByChapter.add(novel.length());
		numCharacters = numCharacters + novel.length();
		String[] allWords = novel.split("\\s+");
		for (String s : allWords)
		{
			if (wordMap.containsKey(s))
				wordMap.put(s, wordMap.get(s) + 1);
			else
				wordMap.put(s, 1);
		}
		numWords = numWords + allWords.length;
	}

	private String getMostUsedString()
	{
		String mostUsedWord = null;
		int numTimesUsed = 0;
		String secondMostUsed = null;
		int numTimesSecondUsed = 0;
		String thirdMostUsed = null;
		int numTimesThirdUsed = 0;
		for (Map.Entry<String,Integer> entry : wordMap.entrySet())
		{
			if (entry.getValue() > numTimesUsed)
			{
				thirdMostUsed = secondMostUsed;
				numTimesThirdUsed = numTimesSecondUsed;
				secondMostUsed = mostUsedWord;
				numTimesSecondUsed = numTimesUsed;
				numTimesUsed = entry.getValue();
				mostUsedWord = entry.getKey();
			}
		}
		String s1 = mostUsedWord + " (used " + String.valueOf(numTimesUsed) + " times), ";
		String s2 = secondMostUsed + " (used " + String.valueOf(numTimesSecondUsed) + " times), ";
		String s3 = thirdMostUsed + " (used " + String.valueOf(numTimesThirdUsed) + " times)";
		return s1 + s2 + s3;
	}

	public void printResults()
	{
		StringBuilder results = new StringBuilder();

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);

		results.append("\n=======================================\n");
		results.append("Results " + today.getTime());
		results.append("\n");
		results.append("Chapters:\t" + String.valueOf(numChapters));
		results.append("\n");
		results.append("Words:\t" + String.valueOf(numWords));
		results.append("\n");
		results.append("Characters:\t" + String.valueOf(numCharacters));
		for(int i = 0; i < wordsByChapter.size(); i++)
			results.append("\nWords in Chapter #" + String.valueOf(i + 1) + ":\t" + String.valueOf(wordsByChapter.get(i)));
		results.append("\n");
		results.append("Most Used Words:\t" + getMostUsedString());
		results.append("\n=======================================");
		try
		{
			File log = new File(dir + LOG_FILE);
			if (!log.exists()) log.createNewFile();
			Writer outputWriter = new BufferedWriter(new FileWriter(dir + LOG_FILE, true));
			outputWriter.write(results.toString());
			outputWriter.close();
		}
		catch (Exception e) {e.printStackTrace();}
		System.out.println(results.toString());
	}

	public static void main (String[] args)
	{
		if (args.length > 0)
		{
			WordCounter counter = new WordCounter(args[0]);
		}
	}
}
