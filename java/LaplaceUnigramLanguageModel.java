import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LaplaceUnigramLanguageModel implements LanguageModel 
{
    private HashMap<String,Integer> wordCount;
    private long totalWords;
    Set<String> words;

  /** Initialize your data structures in the constructor. */
  public LaplaceUnigramLanguageModel(HolbrookCorpus corpus) 
  {
        wordCount = new HashMap<String, Integer>();
        totalWords = 0;
        words = new HashSet<String>();
        train(corpus);
  }

  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
    @Override
  public void train(HolbrookCorpus corpus) 
  {
    // TODO: your code here
      for (Sentence sentence:corpus.getData())
      {
          for(Datum datum:sentence)
          {
              String word = datum.getWord();
	      words.add(word);
	      totalWords++;
	      if (wordCount.containsKey(word))
	      {
	          wordCount.put(word, (wordCount.get(word)+1));
	      }
	      else
	      {
	          wordCount.put(word, 1);
	      }
          }
      }
  }

  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
    @Override
  public double score(List<String> sentence) 
  {
    // TODO: your code here
      double score = 0.0;
      
      for (String w:sentence)
      {
          int count = 1;
          if (wordCount.containsKey(w))
              count = wordCount.get(w) + 1;
          
          double probability = Math.log(count / (totalWords + words.size()));
          //double probability = count/totalWords;
          score += probability;
      }
    return score;
  }
}

