package Classes;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.ClassificationViaClustering;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

/**
 * Created by mahmut on 05.04.2017.
 */
public class WekaClass {
    IBk lr;
    MultilayerPerceptron mlp;
    public static void main(String [] args){
        WekaClass weka = new WekaClass();

        weka.multiLayerPerception();
        weka.getScore("deneme:d=4,o=6,b=480:1c.5,2c#5,4d5,1c#5,1c#5,1c#.5,1d.5,2e5,2d5,1e5,4f5,4f#5,1f.5,2f5,1e5,4f5,1f5,2f5,2f#5,2f#5,4f5,4e5,4f5,4e5,4e5,1d.5,1c#.5,2d5,2e5,4e5,1e.5,4e5,4f5,1e.5,1e.5,4f5,1e.5,1d5,4d5,2c#5,2c5,1c#.5,2d5,1d.5,1e.5,2e5,4e5,1d5,1c#.5,1d.5");
        //weka.tryNetwork(1);


    }
    public void clusterer(){
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader("file2.arff"));
            Instances train = new Instances(reader);
            //train.setClassIndex(train.numAttributes()-1);

            String[] options = new String[4];
            options[0] = "-I";                 // max. iterations
            options[1] = "100";
            options[2] = "-l";
            options[3] = "cluster.model";
            SimpleKMeans clusterer = new SimpleKMeans();   // new instance of clusterer
            clusterer.setOptions(options);     // set the options
            clusterer.buildClusterer(train);    // build the clusterer


            ClusterEvaluation eval = new ClusterEvaluation();
            eval.setClusterer(clusterer);// the cluster to evaluate
            eval.evaluateClusterer(train);                                // data to evaluate the clusterer on
            System.out.println("# of clusters: " + eval.getNumClusters());  // output # of clusters
            System.out.println(eval.clusterResultsToString());



            ClassificationViaClustering cvc = new ClassificationViaClustering();
            cvc.setClusterer(clusterer);train.setClassIndex(train.numAttributes()-1);
            cvc.buildClassifier(train);

            //Instance instanceToClassify = new Instance(train.firstInstance());
            //instanceToClassify.setDataset(train); // the instance to be classified has to have access to the dataset
            //double cls = cvc.classifyInstance(instanceToClassify); // classify instance based by the cluster it belongs to
            //System.out.println(cls);


            Instances unlabeled = new Instances(new BufferedReader(new FileReader("file.arff")));
            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
            // label instances
            System.out.println(unlabeled.numInstances());
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                double clsLabel = cvc.classifyInstance(unlabeled.instance(i));
                System.out.println("score:"+clsLabel);
            }

        }catch (Exception e){
            System.out.println("error :"+e);
        }
    }
    public void classifier(){
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader("file2.arff"));
            Instances train = new Instances(reader);
            train.setClassIndex(train.numAttributes()-1);

            String[] options = new String[1];
            options[0] = "-U";            // unpruned tree
            J48 tree = new J48();         // new instance of tree
            tree.setOptions(options);     // set the options
            tree.buildClassifier(train);   // build classifier

            Evaluation eTest = new Evaluation(train);
            eTest.evaluateModel(tree, train);
            // Print the result à la Weka explorer:
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);


            Instances unlabeled = new Instances(new BufferedReader(new FileReader("file.arff")));
            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
            // label instances
            System.out.println(unlabeled.numInstances());
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                double clsLabel = tree.classifyInstance(unlabeled.instance(i));
                System.out.println("score:"+clsLabel);
                train.instance(i).setClassValue(clsLabel);
            }
            // save labeled data
            BufferedWriter writer = new BufferedWriter(new FileWriter("file3.arff"));
        }catch (Exception e){
            System.out.println("error :"+e);
        }
    }
    public void trees(){
        try {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource("file2.arff");
            Instances dataset = source.getDataSet();
            //set class index to the last attribute
            dataset.setClassIndex(dataset.numAttributes()-1);
            lr = new IBk();
            lr.setKNN(5);
            lr.buildClassifier(dataset);


            int lenght = dataset.numInstances();
            double totalErrorAbs=0,standartDev=0;
            double totalError=0;
            for(int i=0;i<dataset.numInstances();i++){
                double actualVal = dataset.instance(i).classValue();
                double predicVal =  lr.classifyInstance(dataset.instance(i));
                double diff = Math.pow(actualVal-predicVal,2) ;
                totalErrorAbs += Math.abs(actualVal-predicVal);
                standartDev += diff;
                totalError += actualVal-predicVal;
            }
            standartDev = Math.sqrt(standartDev/lenght);
            totalErrorAbs = totalErrorAbs /lenght;
            System.out.println("mean error abs:"+totalErrorAbs+" std:"+standartDev);

        }catch (Exception e){
            System.out.println("error :"+e);
        }
    }
    public void multiLayerPerception(){
        try{
            //Reading training arff file
            FileReader trainreader = new FileReader("file2.arff");
            FileReader testreader = new FileReader("file.arff");
            Instances data = new Instances(trainreader);
            Instances dataTest = new Instances(testreader);
            data.setClassIndex(data.numAttributes()-1);
            dataTest.setClassIndex(dataTest.numAttributes()-1);

            String[] options = new String[2];
            options[0] = "-R";                                    // "range"
            options[1] = "1";                                     // first attribute
            Remove remove = new Remove();                         // new instance of filter
            remove.setOptions(options);                           // set options
            remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
            Instances inst = Filter.useFilter(data, remove);   // apply filter
            Instances instTest = Filter.useFilter(dataTest, remove);   // apply filter


            //Serialization modelleri kaydedip okumaya yarıyor
            //mlp = (MultilayerPerceptron) SerializationHelper.read("data.model");

            mlp = new MultilayerPerceptron();
            mlp.setLearningRate(0.155);
            mlp.setMomentum(0.181);
            mlp.setTrainingTime(500);
            //int hiddenLayerLenght = Math.round(Variables.inputLenght/10)*8;
            //String hiddenArg = ""+hiddenLayerLenght;
            String hiddenArg = "40,20";
            //kaç hidden layer var(, sayısı +1), her layerda kaç node var
            mlp.setHiddenLayers(hiddenArg);
            mlp.buildClassifier(inst);
            //alttaki iki satır sonuçları softmax'e alıyor [0-1] range i
            mlp.setNormalizeAttributes(true);
            mlp.setNormalizeNumericClass(true);
            SerializationHelper.write("data.model",mlp);



            Evaluation eval = new Evaluation(instTest);
            eval.evaluateModel(mlp, instTest);
            System.out.println(eval.toSummaryString()); //Summary of Training




            /*for (i = 0; i < train.numInstances(); i++) {
                double clsLabel = mlp.classifyInstance(train.instance(i));

                if(Math.abs(train.instance(i).classValue()-clsLabel)>=0.6  ){
                    System.out.println(data.instance(i).value(0)+" cls:"+new DecimalFormat("##.##").format(clsLabel)+" +:"
                            +data.instance(i).value(data.numAttributes()-1));
                }
            }*/
        }catch (Exception e){
            System.out.println("error e:"+e);
        }
    }
    public void SVM(){
        try{
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader("file2.arff"));
            Instances train = new Instances(reader);
            train.setClassIndex(train.numAttributes()-1);
            Id3 tree = new Id3(); // support vector machine
            //J48 tree = new J48(); //desicion tree
            tree.buildClassifier(train);
            Evaluation eTest = new Evaluation(train);
            eTest.evaluateModel(tree, train);
            // Print the result à la Weka explorer:
            String strSummary = eTest.toSummaryString();
            System.out.println("s"+strSummary);
        }catch (Exception e){
            System.out.println("error :"+e);
        }
    }

    //sisteme göre parse edilmiş inputun kaç puan aldığını döndürür
    public double getScore(String data){
        try {
        String[] arr = data.split(",");
        Attribute[] attPitch = new Attribute[Variables.inputLenght];
        Attribute[] attDuration = new Attribute[Variables.inputLenght];
        for (int i=0;i<Variables.inputLenght;i++){
            attPitch[i] = new Attribute("pitch"+i);
            attDuration[i] = new Attribute("duration"+i);
        }
        Attribute ClassAttribute = new Attribute("score");
        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector((Variables.inputLenght*2)+1);
        for (int i=0;i<Variables.inputLenght;i++){
            fvWekaAttributes.addElement(attPitch[i]);
            fvWekaAttributes.addElement(attDuration[i]);
        }
        fvWekaAttributes.addElement(ClassAttribute);

        Instance pitchExample = new Instance((Variables.inputLenght*2)+1);
        Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 1);
        for(int j=0;j<Variables.inputLenght-1;j++){
                int picth = Integer.parseInt(arr[j * 2]);
            /*if(picth < 2){}
            else if(picth%12<2){
                picth = (picth%12)+12;
            }else{
                picth %= 12;
            }*/

                pitchExample.setValue((Attribute) fvWekaAttributes.elementAt(j * 2), picth);
                pitchExample.setValue((Attribute) fvWekaAttributes.elementAt((j * 2) + 1), Integer.parseInt(arr[(j * 2) + 1]));
                isTrainingSet.add(pitchExample);

        }
        isTrainingSet.setClassIndex(isTrainingSet.numAttributes()-1);

            double clsLabel= mlp.classifyInstance(isTrainingSet.instance(0));

            return clsLabel;


        }catch(Exception e){
            System.out.println("error :"+e+" arg:"+data);
        }
            return 0;

    }

    public void tryNetwork(int opt){
        int testLenght = Variables.testLenght;
        String[] results = new String[testLenght];

        Random r = new Random();
        // Declare two numeric attributes
        Attribute[] attPitch = new Attribute[Variables.inputLenght];
        Attribute[] attDuration = new Attribute[Variables.inputLenght];
        for (int i=0;i<Variables.inputLenght;i++){
            attPitch[i] = new Attribute("pitch"+i);
            attDuration[i] = new Attribute("duration"+i);
        }
        Attribute ClassAttribute = new Attribute("score");

        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector((Variables.inputLenght*2)+1);
        for (int i=0;i<Variables.inputLenght;i++){
            fvWekaAttributes.addElement(attPitch[i]);
            fvWekaAttributes.addElement(attDuration[i]);
        }
        fvWekaAttributes.addElement(ClassAttribute);

        Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
        for(int i=0;i<testLenght;i++){
            Instance pitchExample = new Instance((Variables.inputLenght*2)+1);
            int b = r.nextInt(351)+100;
            String temp = "deneme:d=50,o=4,b="+b+":";
            String duration = "";
            for(int j=0;j<Variables.inputLenght;j++){
                int d = r.nextInt(6);
                int bb=0;
                switch (d){
                    case 0: bb=(60000/b)*4; duration="1";break;
                    case 1: bb=(60000/b)*2; duration="2";break;
                    case 2: bb=(60000/b); duration="4";break;
                    case 3: bb=(60000/b)/2; duration="8";break;
                    case 4: bb=(60000/b)/4; duration="16";break;
                    case 5: bb=(60000/b)/8; duration="32";break;
                }
                temp+=duration;
                int resultA = r.nextInt(50);
                if(resultA<2)temp=temp+"p";
                else
                switch ((resultA-2)%12){
                    case 0: temp+="a";break;case 1: temp+="a#";break;case 2:temp+="b"; break;case 3: temp+="c";break;
                    case 4: temp+="c#";break;case 5: temp+="d";break;case 6:temp+="d#"; break;case 7: temp+="e";break;
                    case 8: temp+="f";break;case 9: temp+="f#";break;case 10:temp+="g"; break;case 11:temp+="g#"; break;
                }
                if(r.nextInt(2)==0) {
                    bb = bb * 3 / 2;
                    temp+=".";
                }
                int octave=4;
                if(resultA>1){
                    octave =((resultA-2)/12)+4;
                    temp+=octave+",";
                }
                results[i]=temp;
                pitchExample.setValue((Attribute)fvWekaAttributes.elementAt(j*2), resultA);
                pitchExample.setValue((Attribute)fvWekaAttributes.elementAt((j*2)+1), bb);
            }

            //pitchExample.setValue((Attribute)fvWekaAttributes.elementAt(Variables.inputLenght*2), 1.0);
            isTrainingSet.add(pitchExample);
        }
        isTrainingSet.setClassIndex(isTrainingSet.numAttributes()-1);
        try {
            for (int i = 0; i < testLenght; i++) {
                double clsLabel = 0;
                switch (opt){
                    case 0:clsLabel= lr.classifyInstance(isTrainingSet.instance(i));break;
                    case 1:clsLabel= mlp.classifyInstance(isTrainingSet.instance(i));break;
                }
                //System.out.println(clsLabel);
                if(clsLabel<1 && clsLabel>=0.8) {
                    System.out.println(clsLabel+":"+results[i]);
                    //break;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
