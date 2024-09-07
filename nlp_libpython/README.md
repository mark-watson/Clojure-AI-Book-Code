# python_interop_deeplearning - THIS CHAPTER IS NO LONGER IN MY BOOK (chapter included here)

#### I appended the NLP_libpython chapter to the README file and removed it from my book. The reason I did this is because the required Linux and Python setup is difficult.


This project contains 3 demos that I hope will be easily hackable and thus
useful to your projects:

- Using the spaCY NLP library
- Using the Hugging Face Transformer library for question answering
- Combined spaCy and Hugging Face Transformer library question answering demo

Note: the last example (Combined spaCy and Hugging Face Transformer library question answering demo)
uses material from later in the book on Knowledge Graph and semantic web technologies. I use a bit of
code developed later in the book without explanation to get descriptive text for people,
locations, and organizations.

## Installation

You will probably want to use either a Docker container of a separate VPS server.

Please see INSTALL_MLW.txt for my notes on setting up a GCP VPS.

There is no reason why you couldn't just install everything on your laptop
(but expect difficulties if you use an M1 ARM MacBook), but I personally like
using VPS servers that I can turn on just when I need them: low cost and
convenient.

# Python/Clojure Interoperation Using the libpython-clj Library {#libpython}

In the last chapter we used the Java OpenNLP library for natural language processing (NLP). Here we take an alternative approach of using the **libpython-clj** library to access the [spaCy](https://spacy.io) NLP library implemented in Python (and the embedded compiled code written in FORTRAN and C/C++). The **libpython-clj** library can also be used to tap into the wealth of deep learning and numerical computation libraries written in Python. See the file **INSTALL_MLW.txt** for project dependencies.

This example also uses the [Hugging Face Transformer models](https://huggingface.co/transformers/) for NLP question answering.

To get started using **libpython-clj** I want to direct you toward two resources that you will want to familiarize yourself with:

- [libpython-clj GitHub repository](https://github.com/clj-python/libpython-clj)
- [Carin Meier's libpython-clj examples GitHub repository](https://github.com/gigasquid/libpython-clj-examples)

I suggest bookmarking the **libpython-clj** GitHub repository for reference and treat Carin Meier's **libpython-clj** examples as your main source for using a wide variety of Python libraries with **libpython-clj**.

## Using spaCy for Natural Language Processing

**spaCy** is a great library that is likely all you need for processing text and NLP. **spaCy** is written in Python and in the past for accessing **spaCy** I have used the Hy language (Clojure syntax Lisp that sits on top of Python), used the **py4cl** library with Common Lisp, or I just used Python. The **libpython-clj** library now gives me a great fourth option.

Let's start by looking at example code in a REPL session and output for this example (we will implement the code later). I reformatted the following output to fit the page width:

{lang=text,linenos=on}
~~~~~~~~
$ ~/Clojure-AI-Book-Code/nlp_libpython$ lein repl

nlp-libpython-spacy.core=> (def test-text "John Smith
  worked for IBM in Mexico last year and earned $1
  million in salary and bonuses.")
#'nlp-libpython-spacy.core/test-text

nlp-libpython-spacy.core=> (text->entities
                             test-text)
(["John Smith" "PERSON"] ["IBM" "ORG"] ["Mexico" "GPE"]
 ["last year" "DATE"] ["$1 million" "MONEY"])

nlp-libpython-spacy.core=> (text->tokens-and-pos
                             test-text)
(["John" "PROPN"] ["Smith" "PROPN"] ["worked" "VERB"]
 ["for" "ADP"] ["IBM" "PROPN"] ["in" "ADP"]
 ["Mexico" "PROPN"] ["last" "ADJ"] ["year" "NOUN"]
 ["and" "CCONJ"] ["earned" "VERB"] ["$" "SYM"]
 ["1" "NUM"] ["million" "NUM"] ["in" "ADP"]
 ["salary" "NOUN"] ["and" "CCONJ"] ["bonuses" "NOUN"]
 ["." "PUNCT"])

nlp-libpython-spacy.core=> (text->pos test-text)
("PROPN" "PROPN" "VERB" "ADP" "PROPN" "ADP" "PROPN"
 "ADJ" "NOUN" "CCONJ" "VERB" "SYM" "NUM" "NUM"
 "ADP" "NOUN" "CCONJ" "NOUN" "PUNCT")

nlp-libpython-spacy.core=> (text->tokens test-text)
("John" "Smith" "worked" "for" "IBM" "in" "Mexico"
 "last" "year" "and" "earned" "$" "1" "million"
 "in" "salary" "and" "bonuses" ".")
~~~~~~~~

The part of speech tokens are defined in the repository directory for the last chapter in the file **nlp_opennlp/README.md**.
  
## Using the Hugging Face Transformer Models for Question Answering

Deep learning NLP libraries like BERT and and other types of Transformer models have changed the landscape for applications like translation and question answering. Here we use a Hugging Face Transformer Model to answer questions when provided with a block of text that contains the answer to the questions. Before looking at the code for this example, let's look at how it is used:

{lang=clojure",linenos=on}
~~~~~~~~
lp-libpython-spacy.core=> (def context-text "Since last
   year, Bill lives in Seattle. He likes to skateboard.")
#'nlp-libpython-spacy.core/context-text

nlp-libpython-spacy.core=> (qa
                             "where does Bill call home?"
                             context-text)
{'score': 0.9626545906066895, 'start': 31, 'end': 38,
 'answer': 'Seattle'}
 
nlp-libpython-spacy.core=> (qa
                             "what does Bill enjoy?"
                             context-text)
{'score': 0.9084932804107666, 'start': 52, 'end': 62,
 'answer': 'skateboard'}
~~~~~~~~

Nice results that show the power of using publicly available pre-trained deep learning models. Notice that the model handles equating the words "likes" with "enjoy." Similarly, the phrase "call home" is known to be similar to the word "lives." In traditional NLP systems, these capabilities would be handled with a synonym dictionary and a lot of custom code. By training Transformer models of (potentially) hundreds of gigabytes of text, an accurate model of natural language, grammar, synonyms, different sentence structure, etc. are handled with no extra custom code. By using word, phrase, and sentence embeddings Transformer models also learn the relationships between words including multiple word meanings.

Usually the context text block that contains the information to answer queries will be a few paragraphs of text. This is a simple example containing only eleven words. When I use these Transformer models at work I typically provide a few paragraphs of text, which we will also do later in this chapter when we query the public DBPedia Knowledge Graph for context text.

## Combined spaCy and Transformer Question Answering

Let's look at two example queries: "What is the population of Paris?" and "Where does Bill Gates Work?." Here use both the spaCy library and the Hugging Face Transformer library. We also use some material covered in detail later in the book for accessing public Knowledge Graphs to get context text for entities found in the questions we are processing.

Let's use a REPL session to see some results (the printout of the context text is abbreviated for concision). I added a debug printout to the example code to print out the context text (this debug printout is not in the repository for the book example code):

{linenos=on}
~~~~~~~~
lp-libpython-spacy.core=> (spacy-qa-demo
   "what is the population of Paris?")
* * context text: Paris (French pronunciation: ​[paʁi] ()) is the capital and most populous city of France, with an estimated population of 2,150,271 residents as of 2020, in an area of 105 square kilometres (41 square miles). Since the 17th century, Paris has been one of Europe's major centres of finance, diplomacy, commerce, fashion, science and arts.
 The City of Paris is the centre and seat of government of the Île-de-France, or Paris Region ...
 {'score': 0.9000497460365295, 'start': 122, 'end': 131,
  'answer': '2,150,271'}
nlp-libpython-spacy.core=> (spacy-qa-demo
   "where does Bill Gates Work?")
* * context text: William Henry Gates III (born October 28, 1955) is an American business magnate, software developer,
 investor, and philanthropist. He is best known as the co-founder of Microsoft Corporation. During his career at Micro
soft, Gates held the positions of chairman, chief executive officer (CEO), president and chief software architect, while also being the largest individual shareholder until May 2014. He is one of the best-known entrepreneurs and pioneers of the microcomputer revolution of the 1970s and 1980s.
{'score': 0.3064478039741516, 'start': 213, 'end': 222,
 'answer': 'Microsoft'}
~~~~~~~~

This example may take longer to run because the example code is making SPARQL queries to the DBPedia public Knowledge Graph to get context text, a topic we will cover in depth later in the book.

## Using libpython-clj with the spaCy and Hugging Face Transformer Python NLP Libraries

I combined the three examples we just saw in one project for this chapter. Let's start with the project file which is largely copied from Carin Meier's **libpython-clj** examples GitHub repository.


{lang="clojure",linenos=on}
~~~~~~~~
(defproject python_interop_deeplearning "0.1.0-SNAPSHOT"
  :description
  "Example using libpython-clj with spaCy"
  :url
  "https://github.com/gigasquid/libpython-clj-examples"
  :license
  {:name
   "EPL-2.0 OR GPL-2+ WITH Classpath-exception-2.0"
   :url "https://www.eclipse.org/legal/epl-2.0/"}
  :jvm-opts ["-Djdk.attach.allowAttachSelf"
             "-XX:+UnlockDiagnosticVMOptions"
             "-XX:+DebugNonSafepoints"]
    :plugins [[lein-tools-deps "0.4.5"]]
    :middleware
    [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
    :lein-tools-deps/config {:config-files [:project]
                             :resolve-aliases []}
    :mvn/repos
    {"central" {:url "https://repo1.maven.org/maven2/"}
     "clojars" {:url "https://clojars.org/repo"}}
   :dependencies [[org.clojure/clojure "1.10.1"]
                  [clj-python/libpython-clj "1.37"]
                  [clj-http "3.10.3"]
                  [com.cemerick/url "0.1.1"]
                  [org.clojure/data.csv "1.0.0"]
                  [org.clojure/data.json "1.0.0"]]
  :main ^:skip-aot nlp-libpython-spacy.core
  :target-path "target/%s"
  :profiles
  {:uberjar
    {:aot :all
          :jvm-opts
          ["-Dclojure.compiler.direct-linking=true"]}})
~~~~~~~~

Before looking at the example code, let's go back to a REPL session to experiment with **libpython-clj** Python accessor functions. In the following example we call directly into the **spaCy** library and we use a separate Python file **QA.py** to wrap the Hugging Face Transformer mode. This provides you, dear reader, with examples of both techniques I use (direct calls and using separate Python wrappers). We will list the file **QA.py** later.

In lines 1-8 of the example program we set up the Clojure namespace and define  accessor functions for interacting with Python. Before we jump into the example code listing, I want to show you a few things in a REPL:

{linenos=on}
~~~~~~~~
$ lein repl
nlp-libpython-spacy.core=> (nlp "The cat ran")
The cat ran
nlp-libpython-spacy.core=> (type (nlp "The cat ran"))
:pyobject
~~~~~~~~

The output on line 3 prints as a string but is really a Python object (a **spaCy** **Document**) returned as a value from the wrapped **nlp** function. The Python **dir** function prints all methods and attributes of a Python object. Here, I show only four out of the  eighty-eight methods and attributes on a **spaCy** **Document** object:

{linenos=on}
~~~~~~~~
nlp-libpython-spacy.core=> (py/dir (nlp "The cat ran"))
["__iter__" "lang" "sentiment" "text" "to_json" ...]
~~~~~~~~

The method **__iter__** is a Python iterator and allows Clojure code using **libpython-clj** to iterate through a Python collection using the Clojure **map** function as we will see in the example program. The **text** method returns a string representation of a **spaCy** **Document** object and we will also use **text** to get the print representation of **spaCy** **Token** objects.

Here we call two of the wrapper functions in our example:

{linenos=on}
~~~~~~~~
nlp-libpython-spacy.core=> (text->tokens "the cat ran")
("the" "cat" "ran")
nlp-libpython-spacy.core=> (text->tokens-and-pos
                             "the cat ran")
(["the" "DET"] ["cat" "NOUN"] ["ran" "VERB"])
~~~~~~~~

Now let's look at the listing of the example project for this chapter. The Python file **QA.py** loaded in line 9 will be seen later. The **spaCy** library requires a model file to be loaded as seen in line 11.

The combined demo that uses **spaCY**, the transformer model, and queries the public DBPedia Knowledge Graph is implemented in function **spacy-qa-demo** (lines 38-61). In line 49 we call a utility function **dbpedia-get-entity-text-by-name** that is described in a later chapter; for now it is enough to know that it uses the SPARQL query template in the file **get_entity_text.sparql** to get context text for an entity from DBPedia. This code is wrapped in the local function **get-text-fn** that is called for each entity name from in the natural language query.

{lang="clojure",linenos=on}
~~~~~~~~
(ns nlp-libpython-spacy.core
    (:require [libpython-clj.require :refer
                                     [require-python]]
              [libpython-clj.python :as py
                                    :refer
                                    [py. py.-]]))

(require-python '[spacy :as sp])
(require-python '[QA :as qa]) ;; loads the file QA.py

(def nlp (sp/load "en_core_web_sm"))

(def test-text "John Smith worked for IBM in Mexico last year and earned $1 million in salary and bonuses.")

(defn text->tokens [text]
  (map (fn [token] (py.- token text))
       (nlp text)))

(defn text->pos [text]
  (map (fn [token] (py.- token pos_))
       (nlp text)))
  
(defn text->tokens-and-pos [text]
  (map (fn [token] [(py.- token text) (py.- token pos_)])
       (nlp text)))

(defn text->entities [text]
  (map (fn [entity] (py.- entity label_))
       (py.- (nlp text) ents)))

(defn qa
  "Use Transformer model for question answering"
  [question context-text]
  ;; prints to stdout and returns a map:
  (qa/answer question context-text))

(defn spacy-qa-demo [natural-language-query]
  (let [entity-map
        {"PERSON" "<http://dbpedia.org/ontology/Person>"
         "ORG"
         "<http://dbpedia.org/ontology/Organization>"
         "GPE"    "<http://dbpedia.org/ontology/Place>"}
        entities (text->entities natural-language-query)
        get-text-fn
        (fn [entity]
          (clojure.string/join
           " "
           (for [entity entities]
             (kgn/dbpedia-get-entity-text-by-name
              (first entity)
              (get entity-map (second entity))))))
        context-text
        (clojure.string/join
          " "
          (for [entity entities]
            (get-text-fn entity)))
        _ (println "* * context text:" context-text)
        answer (qa natural-language-query context-text)]
    answer))

(defn -main
  [& _]
  (println (text->entities test-text))
  (println (text->tokens-and-pos test-text))
  (println (text->pos test-text))
  (println (text->tokens test-text))
  (qa "where does Bill call home?"
      "Since last year, Bill lives in Seattle. He likes to skateboard.")
  (qa "what does Bill enjoy?"
      "Since last year, Bill lives in Seattle. He likes to skateboard.")
  (spacy-qa-demo "what is the population of Paris?")
  (spacy-qa-demo "where does Bill Gates Work?"))
~~~~~~~~

If you **lein run** to run the test **-main** function in lines 62-75 in the last listing, you will see the sample output that we saw earlier.

This example also shows how to load (see line 9 in the last listing) the local Python file **QA.py** and call a function defined in the file:

{lang="python",linenos=on}
~~~~~~~~
from transformers import pipeline

qa = pipeline(
    "question-answering",
    model="NeuML/bert-small-cord19-squad2",
    tokenizer="NeuML/bert-small-cord19qa"
)

def answer (query_text,context_text):
  answer = qa({
                "question": query_text,
                "context": context_text
               })
  print(answer)
  return answer
~~~~~~~~

Lines 5-6 specify names for pre-trained model files that we use. 

In the example repository, the file **INSTALL_MLW.txt** shows how I installed the dependencies for this example on a Google Cloud Platform VPS. While I sometimes use Docker for projects with custom dependencies that I don't want to install on my laptop, I often prefer using a VPS that I can start and stop when I need it.

Writing a Python wrapper that is called from your Clojure code is a good approach if, for example, you had existing Python code that uses TensorFlow or PyTorch, or there was a complete application written in Python that you wanted to use from Clojure. While it is possible to do everything in Clojure calling directly into Python libraries it is sometimes simpler to write Python wrappers that define top level functions that you need in your Clojure project.

The material in this chapter is of particular interest to me because I use both NLP and Knowledge Graph technologies in my work. With the ability to access the Python **spaCY** and Hugging Face Transformer models, as well as the Java Jena library for semantic web and Knowledge Graph applications (more on this topic later), Clojure is a nice language to use for my projects.