package deprecated.org.crf.postagging.postaggers.crf;

import deprecated.org.crf.crf.CrfModel;
import deprecated.org.crf.crf.filters.CrfFilteredFeature;
import deprecated.org.crf.crf.run.CrfTrainer;
import deprecated.org.crf.postagging.postaggers.PosTaggerTrainer;
import deprecated.org.crf.utilities.AbsoluteValueComparator;
import deprecated.org.crf.utilities.CrfException;
import deprecated.org.crf.utilities.PosTaggerUtilities;
import deprecated.org.crf.utilities.TaggedToken;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * A {@link PosTaggerTrainer} which creates a {@link CrfPosTagger}. Training and inference are performed by the CRF algorithm. 
 * 
 * @author Asher Stern
 * Date: Nov 10, 2014
 *
 */
public class CrfPosTaggerTrainer implements PosTaggerTrainer<List<? extends List<? extends TaggedToken<String, String>>>>
{
	public static final String SAVE_LOAD_FILE_NAME = "crfptmdl.ser";
	public static final String HUMAN_READABLE_FILE_NAME = "rdbl_mdl.txt";
	private static final Logger logger = Logger.getLogger(CrfPosTaggerTrainer.class);
	private final CrfTrainer<String, String> crfTrainer;
	private CrfPosTagger crfPosTagger = null;

	public CrfPosTaggerTrainer(CrfTrainer<String, String> crfTrainer)
	{
		super();
		this.crfTrainer = crfTrainer;
	}

	@Override
	public void train(List<? extends List<? extends TaggedToken<String, String>>> corpus)
	{
		crfTrainer.train(corpus);
		crfPosTagger = new CrfPosTagger(crfTrainer.getInferencePerformer());
	}

	@Override
	public CrfPosTagger getTrainedPosTagger()
	{
		if (null==crfPosTagger) throw new CrfException("Not yet trained.");
		return this.crfPosTagger;
	}

	@Override
	public void save(File modelDirectory)
	{
		if (null==crfPosTagger) {throw new CrfException("Not trained.");}

		if (!modelDirectory.exists()) {throw new CrfException("Given directory: "+modelDirectory.getAbsolutePath()+" does not exist.");}
		if (!modelDirectory.isDirectory()) {throw new CrfException("The loader requires a directory, but was provided with a file: "+modelDirectory.getAbsolutePath()+".");}

		CrfModel<String, String> crfModel = crfTrainer.getLearnedModel();

		try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(modelDirectory,SAVE_LOAD_FILE_NAME))))
		{
			outputStream.writeObject(crfModel);
		}
		catch (IOException e)
		{
			throw new CrfException("Saving the pos tagger failed.",e);
		}

		try
		{
			createHumanReadableModelFile(new File(modelDirectory,HUMAN_READABLE_FILE_NAME),crfModel);
		}
		catch(RuntimeException e)
		{
			logger.error("Could not write a human readable file. However, this does NOT cause the program to stop.",e);
		}
	}
	
	private void createHumanReadableModelFile(File file, CrfModel<String, String> crfModel)
	{
		try(PrintWriter writer = new PrintWriter(file))
		{
			writer.println("This is a human readable model file, provided for convenience only. It is NOT used by the system at all. Changing, and even deleting this file has no effect on the system, and the loaded pos-tagger.");

			CrfFilteredFeature<String, String>[] features = crfModel.getFeatures().getFilteredFeatures();
			ArrayList<Double> parameters = crfModel.getParameters();
			if (features.length!=parameters.size())
			{
				throw new CrfException("mostlikelytreefromlist.length!=parameters.size()");
			}

			Map<Integer,Double> parametersMap = PosTaggerUtilities.listToMap(parameters);
			List<Integer> sortedIndexes = PosTaggerUtilities.sortByValue(parametersMap, Collections.reverseOrder(new AbsoluteValueComparator()));

			for (int index : sortedIndexes)
			{
				double parameter = parametersMap.get(index);
				writer.printf("%-10.5f\t\t%s\n",parameter,features[index].getFeature().toString());
			}
		}
		catch (FileNotFoundException e)
		{
			throw new CrfException("Could not write human-readable model file.",e);
		}
	}
}
