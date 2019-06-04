package hufs.ces.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.math3.linear.RealMatrix;

public class GutenbergTFIDFMain {

	public static void main(String[] args) throws URISyntaxException {
		System.out.println("wfList 생성 시작");
		long start = System.currentTimeMillis();
		Stream<String> String_stream = null;
		List<String> documents = new ArrayList<>();
		Map<String, Integer> termFreqMap = new HashMap<String, Integer>();

		/* create a dictionary of all terms */
		TermDictionary termDictionary = new TermDictionary();

		/* need a basic tokenizer to parse text */
		//Tokenizer tokenizer = new StreamTokenizer();
		Tokenizer tokenizer = new SimpleTokenizer();

		URL url = GutenbergTFIDFMain.class.getResource("/resources/gutenberg");

		Stream.of(new File(url.toURI()).listFiles()).map(file -> {
			String str = null;
			try {
				str = Files.lines(Paths.get(file.toURI()),Charset.defaultCharset())
						.collect(Collectors.joining());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return str;
		}).parallel()
				.peek(doc -> documents.add(doc))
				.map(doc -> tokenizer.getTokens(doc))
				.forEach(temp-> {for (String tok : temp){
					Integer freq = termFreqMap.get(tok);
					if (freq==null || freq.intValue()==0){
						termFreqMap.put(tok, 1);
					}
					else {
						freq++;
						termFreqMap.put(tok, freq);
					}
				}
				});
		termFreqMap.keySet().stream().map(k -> new TermFreqPair(k, termFreqMap.get(k)))//;
				.filter(wf->wf.getFreq()>1)
				.sorted()
				.limit(1000)
				.forEach(wf->{
					termDictionary.addTerm(wf.getTerm());
					System.out.println(wf.getFreq()+"--"+wf.getTerm());
				});

		System.out.println("size(termDictionary)="+termDictionary.getNumTerms());
		System.out.println("wfList 생성 시간 : "+(System.currentTimeMillis() - start)/1000.0+"S");

		/* create of matrix of word counts for each sentence
		Vectorizer vectorizer = new Vectorizer(termDictionary, tokenizer, false);
		RealMatrix counts = vectorizer.getCountMatrix(documents);
		System.out.println(counts.getRowDimension()+","+counts.getColumnDimension());
		 */
		/* ... or create a binary counter
		Vectorizer binaryVectorizer = new Vectorizer(termDictionary, tokenizer, true);
		RealMatrix binCounts = binaryVectorizer.getCountMatrix(documents);
		System.out.println(binCounts.getRowDimension()+","+binCounts.getColumnDimension());
		*/
		///* ... or create a matrix TFIDF
		TFIDFVectorizer tfidfVectorizer = new TFIDFVectorizer(termDictionary, tokenizer);
		RealMatrix tfidf = tfidfVectorizer.getTFIDF(documents);
		System.out.println(tfidf.getRowDimension()+","+tfidf.getColumnDimension());

		IntStream.range(1,101).forEach((i)->System.out.format(" %s-%02d     %s   |","Term",i,"TFID"));
		System.out.println();
		for (int i=0; i<tfidf.getRowDimension(); ++i) {
			double[] rowVector = tfidf.getRow(i);
			IntStream.range(0,rowVector.length)
					.mapToObj(iterm->new TermTFIDFPair(iterm, rowVector[iterm]))
					.sorted()
					.limit(100)
					.forEach(t->System.out.format("%8s,%10.2f |",termDictionary.getTerm(t.getTerm()),t.getTFID()));
			System.out.println();
		}
		//*/
	}

	public static String readFilesToString(File file) {
		String text = null;
		try {
			text = Files.lines(Paths.get(file.toURI()),Charset.defaultCharset())
					.collect(Collectors.joining());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
