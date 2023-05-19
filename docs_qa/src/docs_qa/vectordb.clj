(ns docs-qa.vectordb)

(defn string-to-floats [s]
  (map #(Float/parseFloat %) (clojure.string/split s #" ")))

(defn truncate-string [s max-length]
  (if (< (count s) max-length)
    s
    (subs s 0 max-length)))

(defn break-into-chunks [s chunk-size]
  (let [chunks (partition-all chunk-size s)]
    (map #(apply str %) chunks)))

(defn document-texts-from_dir [dir-path]
  (map #(slurp %) (rest (file-seq (clojure.java.io/file dir-path)))))

(defn document-texts-to-chunks [strings]
  (flatten
   (map #(break-into-chunks % 200) strings)))

(def directory-path "data")

(def doc-strings (document-texts-from_dir directory-path))

(def doc-chunks
  (filter #(> (count %) 40) (document-texts-to-chunks doc-strings)))

(def chunk-embeddings
  (map #(openai-api.core/embeddings %) doc-chunks))

(def embeddings-with-chunk-texts
  (map vector chunk-embeddings doc-chunks))

;;(clojure.pprint/pprint (first embeddings-with-chunk-texts))
