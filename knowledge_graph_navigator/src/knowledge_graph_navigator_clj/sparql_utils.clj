(ns knowledge-graph-navigator-clj.sparql-utils)

(defn sparql_template
  "open SPARQL template file and perform variable substitutions"
  [template-fpath substitution-map]
  (let [template-as-string (slurp template-fpath)]
    (clojure.string/replace
      template-as-string
      (re-pattern
        ; create a regex pattern of quoted replacements separated by |:
        ; code derived from a stackoverflow example by user bmillare
        (apply
          str
          (interpose
            "|"
            (map
              (fn [x] (java.util.regex.Pattern/quote x))
              (keys substitution-map)))))
      substitution-map)))
