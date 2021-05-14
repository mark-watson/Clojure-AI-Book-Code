(ns deeplearning-dl4j-clj.wisconsin-data
  (:import [org.datavec.api.split FileSplit]
           [org.deeplearning4j.datasets.datavec
            RecordReaderDataSetIterator]
           [org.datavec.api.records.reader.impl.csv
            CSVRecordReader]
           [org.deeplearning4j.nn.conf
            NeuralNetConfiguration$Builder]
           [org.deeplearning4j.nn.conf.layers
            OutputLayer$Builder DenseLayer$Builder]
           [org.deeplearning4j.nn.weights WeightInit]
           [org.nd4j.linalg.activations Activation]
           [org.nd4j.linalg.lossfunctions
            LossFunctions$LossFunction]
           [org.deeplearning4j.optimize.listeners
            ScoreIterationListener]
           [org.deeplearning4j.nn.multilayer
            MultiLayerNetwork]
           [java.io File]
           [org.nd4j.linalg.learning.config Adam Sgd
            AdaDelta AdaGrad AdaMax Nadam NoOp]))

(def numHidden 3)
(def numOutputs 1)
(def batchSize 64)

(def initial-seed (long 33117))

(def numInputs 9)
(def labelIndex 9)
(def numClasses 2)


(defn -main
  "Using DL4J with Wisconsin data"
  [& args]
  (let [recordReader (new CSVRecordReader)
        _ (. recordReader
             initialize
             (new FileSplit (new File "data/", "training.csv")))
        trainIter (new RecordReaderDataSetIterator recordReader
                       batchSize labelIndex numClasses)
        recordReaderTest (new CSVRecordReader)
        _ (. recordReaderTest initialize
             (new FileSplit (new File "data/", "testing.csv")))
        testIter (new RecordReaderDataSetIterator
                      recordReaderTest batchSize labelIndex numClasses)
        conf (->
               (new NeuralNetConfiguration$Builder)
               (.seed initial-seed)
               (.activation Activation/TANH)
               (.weightInit (WeightInit/XAVIER))
               (.updater (new Sgd 0.1))
               (.l2 1e-4)
               (.list)
               (.layer
                 0,
                 (-> (new DenseLayer$Builder)
                     (.nIn numInputs)
                     (.nOut numHidden)
                     (.build)))
               (.layer
                 1,
                 (-> (new OutputLayer$Builder
                          LossFunctions$LossFunction/MCXENT)
                     (.nIn numHidden)
                     (.nOut numClasses)
                     (.activation Activation/SOFTMAX)
                     (.build)))
               (.build))
        model (new MultiLayerNetwork conf)
        score-listener (ScoreIterationListener. 100)]
    (. model init)
    (. model setListeners (list score-listener))
    (. model fit trainIter 10)
    (while (. testIter hasNext)
      (let [ds (. testIter next)
            features (. ds getFeatures)
            labels (. ds getLabels)
            predicted (. model output features false)]
        ;; 23 test samples in data/testing.csv:
        (doseq [i (range 0 46 2)]
          (println
            "target: [" (. labels getDouble i)
            (. labels getDouble (+ i 1)) "]"
            "predicted : ["
            (format "%1.2f"
                    (. predicted getDouble i))
            (format "%1.2f"
                    (. predicted getDouble
                       (+ i 1))) "]"))))))

