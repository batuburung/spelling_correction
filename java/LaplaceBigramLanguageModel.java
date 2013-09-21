import java.util.*;


public class LaplaceBigramLanguageModel implements LanguageModel {

  Set<String> words;
  HashMap<String, Integer> unigramCount;
  HashMap<String, Integer> bigramCount;
  
  /** Initialize your data structures in the constructor. */
  public LaplaceBigramLanguageModel(HolbrookCorpus corpus) 
  {
  	words = new HashSet<String>();
  	unigramCount = new HashMap<String, Integer>();
  	bigramCount = new HashMap<String, Integer>();
	train(corpus); 
  }
  
  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
  public void train(HolbrookCorpus corpus) 
  { 
    // TODO: your code here
    String prevWord = "";
    for (Sentence sentence:corpus.getData())
    {
    	for (Datum datum:sentence)
    	{
    		String word = datum.getWord();
    		words.add(word); //calculating vocabulary

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
 		prevWord = word;
    	}
    }
    
  }


  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
  public double score(List<String> sentence) 
  {
    // TODO: your code here
    double score = 0.0;
    double probability = 0.0;
    String prevWord = "";

    for (String word:sentence)
    {
    	String bigram = prevWord + word;
    	double countB = 1.0;
    	
    	if (bigramCount.containsKey(bigram))
    		countB = bigramCount.get(bigram) + 1.0;
    		
    	double countA = 0.0;
    	if (unigramCount.containsKey(prevWord))
    		countA = unigramCount.get(prevWord);

    	probability = Math.log((countB) / (double)(countA + words.size()));
    	score += probability;

    	prevWord = word;
    }
    return score;
  }
}
