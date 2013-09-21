import java.util.*;

public class StupidBackoffModel3gram implements LanguageModel 
{
  Set<String> words;
  HashMap<String, Integer> unigramCount;
  HashMap<String, Integer> bigramCount;
  HashMap<String, Integer> trigramCount;
  long totalWords;
  
  /** Initialize your data structures in the constructor. */
  public StupidBackoffModel3gram(HolbrookCorpus corpus) 
  {
        words = new HashSet<String>();
  	unigramCount = new HashMap<String, Integer>();
  	bigramCount = new HashMap<String, Integer>();
  	trigramCount = new HashMap<String, Integer>();
  	
  	totalWords = 0;
        train(corpus);
  }

  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
  public void train(HolbrookCorpus corpus) 
  {
    String prevWord = "";
    String prevPrevWord = "";
    for (Sentence sentence:corpus.getData())
    {
    	for (Datum datum:sentence)
    	{
    		String word = datum.getWord();
    		words.add(word); //calculating vocabulary
		totalWords++;
		
		//calculating unigram counts
    		if (unigramCount.containsKey(word))
    			unigramCount.put(word, (unigramCount.get(word)+1));
    		else
    			unigramCount.put(word,1);

    		//calculating bigram counts
    		if (!prevWord.equals(""))
    		{
    			String bigram = prevWord + word;
    			if (bigramCount.containsKey(bigram))
    				bigramCount.put(bigram, (bigramCount.get(bigram)+1));
    			else
    				bigramCount.put(bigram,1);
    		}
    		
    		//calculating trigram counts
    		if (!prevPrevWord.equals(""))
    		{
    			String trigram = prevPrevWord + prevWord + word;
    			if (trigramCount.containsKey(trigram))
    				trigramCount.put(trigram,(trigramCount.get(trigram)+1));
    			else
    				trigramCount.put(trigram,1);
    		}
    		prevPrevWord = prevWord;
 		prevWord = word;
    	}
    }

  }

  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
  public double score(List<String> sentence) 
  {
  	double score = 0.0;
	double probability = 0.0;
        String prevWord = "";
        String prevPrevWord = "";

	for (String word:sentence)
    	{
    		String trigram = prevPrevWord + prevWord + word;
	    	String bigram = prevWord + word;
	    	double countB = 0.0;
	    	double countC = 0.0;
	    	double countA = 0.0;
		    		    	
	    	if (trigramCount.containsKey(trigram))
	    	{
	    		countC = trigramCount.get(trigram);
	    		
	    		if (bigramCount.containsKey(bigram))
		    		countB = bigramCount.get(bigram);
	    		
	    		probability = Math.log((countC) / (double)(countB));
	    		score += probability;
	    	}
	    	else if (bigramCount.containsKey(bigram))
	    	{
	    		countB = bigramCount.get(bigram);// + 1.0;	    		

		    	if (unigramCount.containsKey(prevWord))
		    		countA = unigramCount.get(prevWord);

		    	probability = Math.log((countB) / (double)(countA)) + Math.log(0.4);
		    	score += probability;
	    	}
	    	else
	    	{
	    		if (unigramCount.containsKey(word))
		    		countA = unigramCount.get(word) + 1;

		    	probability = Math.log((countA) / (double)(words.size()+totalWords)) + Math.log(0.4);
		    	score += probability;
	    	}
	    	
	    	prevPrevWord = prevWord;
	    	prevWord = word;
    	}
	return score;
  }
}
